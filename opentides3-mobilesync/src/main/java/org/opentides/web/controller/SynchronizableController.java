package org.opentides.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.opentides.bean.BaseEntity;
import org.opentides.bean.ChangeLog;
import org.opentides.bean.SyncEndpoint;
import org.opentides.bean.SyncResults;
import org.opentides.service.ChangeLogService;
import org.opentides.service.SyncEndpointService;
import org.opentides.util.DatabaseUtil;
import org.opentides.web.json.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.gson.Gson;

/**
 * 
 * @author allanctan
 *
 */
@Controller
@RequestMapping("/sync")
public class SynchronizableController {
	
	@Autowired
	private ChangeLogService changeLogService;

	@Autowired
	private SyncEndpointService syncEndpointService;

	public final String UPDATE_ENDPOINT = "[UPDATE ENDPOINT] ";

	public final String WEB_UPDATE = "[WEB_UPDATE] ";

	public final String SUCCESS = "[SUCCESS] ";

	public final String FAIL = "[FAIL] ";
	
	public final static String EXCEPTION = "[EXCEPTION] ";

	@Value("${log.sync.dir}")
	private String logDirectory;

	@Value("${log.sync.filename}")
	private String syncLogFileName;

	private static Logger _log = Logger
			.getLogger(SynchronizableController.class);

	public final static HttpHeaders httpHeaders = initializeAccessAllHeaders();

	public String clientCode;

	/**
	 * Returns the list of updates that should be applied from the given
	 * 
	 * version. The version is the latest version of the endpoint. Returns the
	 * list of updates after the version upto the most current update.
	 * 
	 * 
	 * @param name
	 * @return
	 */
	@JsonView(Views.FormView.class)
	@RequestMapping(value = "/{clientcode}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity getUpdates(
			@PathVariable("clientcode") String clientcode) {
		this.clientCode = clientcode;
		getLogger();
		
		SyncResults results = new SyncResults();
		SyncEndpoint endpoint = syncEndpointService.findSyncEndpointByClientCode(clientcode);


		if (endpoint != null) {

			List<ChangeLog> changes = changeLogService
					.findAfterVersion(endpoint.getSyncVersion());

			if (!changes.isEmpty()) {
				ChangeLog last = changes.get(changes.size() - 1);
				results.setLatestVersion(last.getId());
				results.setLogs(changes);
			} else {
				results.setLatestVersion(endpoint.getSyncVersion());
				//results.setLogs(new ArrayList<ChangeLog>());
			}

		    ChangeLog latestChange = this.changeLogService.findLatestChange();
		    if (latestChange != null){
		       Long targetVersion = latestChange.getId();
		       results.setTargetVersion(targetVersion.longValue());
		    }
		    
			// Log updates from WEB to POS
			_log.info("[" + clientcode + "] " + UPDATE_ENDPOINT
					+ " Send Change Log version " + endpoint.getSyncVersion()
					+ " to " + results.getLatestVersion());
		} else {
			_log.info("[" + clientcode + "] " + UPDATE_ENDPOINT + FAIL
					+ " End point is null");
		}

		Gson gSon = new Gson();

		return new ResponseEntity(gSon.toJson(results), httpHeaders,
				HttpStatus.OK);
	}

	/**
	 * 
	 * 
	 * Updates the version of the endpoint. Endpoint should invoke this method
	 * when
	 * 
	 * @return
	 */
	@RequestMapping(value = "/success/{clientcode}/{version}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity updateEndpoint(
			@PathVariable("clientcode") String clientcode,
			@PathVariable("version") Long version) {
		this.clientCode = clientcode;
		getLogger();
		
		SyncResults results = new SyncResults();
		SyncEndpoint endpoint = syncEndpointService.findSyncEndpointByClientCode(clientcode);

		if (endpoint != null) {
			endpoint.setSyncVersion(version);
			syncEndpointService.save(endpoint);

			// Update sync endpoint latest version to WEB
			_log.info("[" + clientcode + "] " + WEB_UPDATE + SUCCESS
					+ " Previous Endpoint sync success. Web "
					+ "will update endpoint sync version to "
					+ endpoint.getSyncVersion());

		} else {
			_log.info("[" + clientcode + "] " + WEB_UPDATE + FAIL
					+ " End point is null");
		}

		results.setLatestVersion(version);
		Gson gSon = new Gson();

		return new ResponseEntity(gSon.toJson(results), httpHeaders,
				HttpStatus.OK);
	}

	/**
	 * 
	 * 
	 * Updates the version of the endpoint. Endpoint should invoke this method
	 * when
	 * 
	 * @return
	 */
	@RequestMapping(value = "/upload/{clientcode}", method = {
			RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
	public ResponseEntity updateServer(
			@PathVariable("clientcode") String clientcode,
			HttpServletRequest request) {
		this.clientCode = clientcode;
		getLogger();
		
		SyncEndpoint endpoint = syncEndpointService.findSyncEndpointByClientCode(clientcode);

		StringBuilder appender = new StringBuilder();
		String result = SUCCESS;
		String errorMsg = "";

		SyncResults response = new SyncResults();

		String clientParam = "";
		try {
			clientParam = getBody(request);
			appender.append(clientParam);
			
		} catch (IOException e) {
			_log.error(EXCEPTION + e.getMessage(), e);
			result = FAIL;
			errorMsg = "Client parameters parsing error.";
			
			e.printStackTrace();
		}

		if (endpoint != null) {

			List<ChangeLog> changes = changeLogService
					.findAfterVersion(endpoint.getSyncVersion());

			long latestVersion = 0L;

			if (changes != null && changes.size() > 0) {
				latestVersion = changes.get(changes.size() - 1).getId();
				response.setLatestVersion(latestVersion);
			}
			response.setLogs(changes);

			Gson gSon = new Gson();
			SyncResults clientChanges = gSon.fromJson(clientParam,
					SyncResults.class);

			List<ChangeLog> logs = clientChanges.getLogs();
			if (logs != null && logs.size() > 0) {
				EntityManager em = DatabaseUtil.getEntityManager();
				try {
					Query query = null;
					em.getTransaction().begin();

					for (int i = 0; i < logs.size(); i++) {
						String sql = logs.get(i).getSqlCommand();

						query = em.createNativeQuery(sql);
						query.executeUpdate();
						int changeLogAction = ChangeLog.INSERT;
						if (sql.toLowerCase().contains("update")) {
							changeLogAction = ChangeLog.UPDATE;
						} else if (sql.toLowerCase().contains("delete")) {
							changeLogAction = ChangeLog.DELETE;
						}
						BaseEntity bse = (BaseEntity) logs.get(i);
						ChangeLog cl = new ChangeLog(0L, bse.getClass(),
								changeLogAction, "", sql);
						latestVersion++;
						em.persist(cl);

					}

					em.getTransaction().commit();

				} catch (Exception ex) {
					result = FAIL;
					errorMsg = "(Error: " + stackTraceToString(ex) + ")";

					_log.error(EXCEPTION + ex.getMessage(), ex);
					em.getTransaction().rollback();
				} finally {
					if (em != null && em.isOpen()) {
						em.close();
					}
				}

			}

			response.setLatestVersion(latestVersion);
		}

		Gson gSon = new Gson();

		_log.info("[" + clientcode + "] " + WEB_UPDATE + result
				+ appender.toString() + errorMsg);

		return new ResponseEntity(gSon.toJson(response), httpHeaders,
				HttpStatus.OK);
	}

	@SuppressWarnings("deprecation")
	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);

				}
			} else {
				stringBuilder.append("");

			}

			if (bufferedReader != null) {

				try {
					bufferedReader.close();

				} catch (IOException ex)

				{
					throw ex;

				}
			}
			body = stringBuilder.toString();

		} catch (IOException ex) {
			_log.error(EXCEPTION + ex.getMessage(), ex);
			
			throw ex;
		} finally {
			if (bufferedReader != null) {

				try {
					bufferedReader.close();
				} catch (IOException ex) {

					throw ex;
				}
			}
		}

		body = URLDecoder.decode(body);
		body = body.replaceAll("clientchanges", "");
		body = body.replaceAll("=", "");
		return body;
	}

	public static HttpHeaders initializeAccessAllHeaders() {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods",
				"POST, GET, OPTIONS, DELETE");
		headers.add("Access-Control-Max-Age", "3600");

		headers.add("Access-Control-Allow-Headers", "x-requested-with");

		return headers;

	}

	public void getLogger() {
		String logFolderName = logDirectory + clientCode;
		
		String logFilePath = logFolderName + "/" + syncLogFileName;

		FileAppender appender = (FileAppender) _log.getAppender("file");
		appender.setFile(logFilePath);
		appender.activateOptions();

	}
	
	public String stackTraceToString(Throwable e) {
	    StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    return sb.toString();
	}
}
