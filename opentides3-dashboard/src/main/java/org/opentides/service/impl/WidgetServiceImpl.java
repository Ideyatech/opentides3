package org.opentides.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.opentides.annotation.CrudSecure;
import org.opentides.bean.UrlResponseObject;
import org.opentides.bean.Widget;
import org.opentides.bean.user.BaseUser;
import org.opentides.dao.WidgetDao;
import org.opentides.service.WidgetService;
import org.opentides.util.SecurityUtil;
import org.opentides.util.StringUtil;
import org.opentides.util.UrlUtil;
import org.springframework.stereotype.Service;

@Service("widgetService")
@CrudSecure(value="WIDGETS")
public class WidgetServiceImpl extends BaseCrudServiceImpl<Widget> implements
		WidgetService {
	
	private static Logger _log = Logger.getLogger(WidgetServiceImpl.class);
	
	private String widgetColumn;
	
	private String IPAddress;
	
	private static Pattern pattern = Pattern.compile("<img\\s[^>]*src=\"?(.*?)[\" ]",
			Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);

	@Override
	public Widget findByName(String name) {
		Widget example = new Widget();
		example.setName(name);
		List<Widget> dashboardList = getDao().findByExample(example, true);
		if (dashboardList != null && dashboardList.size() > 0) {
			return dashboardList.get(0);
		}
		return null;
	}

	@Override
	public Widget findByUrl(String url) {
		Widget example = new Widget();
		example.setUrl(url);
		List<Widget> dashboardList = getDao().findByExample(example, true);
		if (dashboardList != null && dashboardList.size() > 0) {
			return dashboardList.get(0);
		}
		return null;
	}

	@Override
	public List<Widget> getCurrentUserWidgets() {
		Widget example = new Widget();
		example.setIsUserDefined(true);
		List<Widget> widgets = new ArrayList<Widget>();
		for (Widget widget:findByExample(example, true)) {
			if (StringUtil.isEmpty(widget.getAccessCode())) {
				widgets.add(widget);
			} else if (SecurityUtil.currentUserHasPermission(widget.getAccessCode())) { 
				widgets.add(widget);					
			}
		}
		return widgets;
	}

	@Override
	public Widget requestWidget(String widgetUrl, String name,
			HttpServletRequest req) {
		Widget widget = findByName(name);
		if (widget != null) {
			long now = System.currentTimeMillis();
			Date lastCacheDate = widget.getLastCacheUpdate();

			boolean useCache = false;
			// check if we should use cache or not
			if (lastCacheDate != null) {
				long expire = widget.getLastCacheUpdate().getTime()
						+ widget.getCacheDuration() * 1000;
				if (widget.getCacheType().startsWith(
						Widget.TYPE_IMAGE)
						|| now < expire)
					useCache = true;
			}
			if (useCache) {
				_log.debug("Reusing widget [" + widget.getName()
						+ "] from cache...");
				// retrieve from cache
				return widget;
			} else {
				// retrieve from url
				String url = widget.getUrl();
				if (!UrlUtil.hasProtocol(url)) {
					String slash = "/";
					if (!url.startsWith("/")) {
						url = slash + url;
					}
					url = req.getContextPath().toString() + url;
					if (req.getServerPort() != 80) {
						url = ":" + Integer.toString(req.getServerPort()) + url;
					}
					url = UrlUtil.ensureProtocol(req.getServerName() + url);
				}
				
				// set the IP Address as param
				Map<String, Object> param = new HashMap<String, Object>();				
				if (StringUtil.isEmpty(IPAddress))
					param.put("IPAddress", IPAddress);
				
				UrlResponseObject response = UrlUtil.getPage(url, req, param);
				if (response==null)
					return null;
				if (response.getResponseType()
						.startsWith(Widget.TYPE_IMAGE)) {
					_log.debug("Retrieving image [" + widget.getName()
							+ "] from url [" + widget.getUrl() + "]...");
					widget.setCacheType(response.getResponseType());
					widget.setCache(response.getResponseBody());
					widget.setLastCacheUpdate(new Date());
					save(widget);
					_log.debug("Saved image [" + widget.getName()
							+ "] to cache...");
					return widget;
				} else {
					_log.debug("Retrieving widget [" + widget.getName()
							+ "] from url [" + widget.getUrl() + "]...");
					widget.setCacheType(response.getResponseType());
					// check for image inside the html
					String html = new String(response.getResponseBody());
					String hostname = UrlUtil.getHostname(url);
					Matcher matcher = pattern.matcher(html);
					while (matcher.find()) {
						// add image link to cache
						String imageUrl = matcher.group(1);
						String cacheUrl = imageUrl;
						if (!UrlUtil.hasProtocol(cacheUrl)) {
							if (!imageUrl.startsWith("/")) { 
								cacheUrl = "http://" + hostname + "/" + imageUrl;
							} else {
								cacheUrl = "http://" + hostname  + imageUrl;
							}
						}
						String imageName = this.addCache(cacheUrl, req, widget);
						// replace html that reference to image with cached image
						String newUrl = widgetUrl+"?name="+imageName;
						html = html.replace(imageUrl, newUrl);
					}
					widget.setCache(html.getBytes());
					widget.setLastCacheUpdate(new Date());
					save(widget);
					_log.debug("Saved widget [" + widget.getName()
							+ "] to cache...");
					return widget;
				}
			}
		}
		return null; 
	}

	@Override
	public int getColumnConfig() {
		return StringUtil.convertToInt(getWidgetColumn(),2);
	}

	@Override
	public List<Widget> findDefaultWidget(BaseUser user) {
		return ((WidgetDao) getDao()).findDefaultWidget(user);
	}

	@Override
	public List<Widget> findWidgetWithAccessCode(List<String> accessCodes) {
		return ((WidgetDao) getDao()).findWidgetWithAccessCode(accessCodes);
	}
	
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	
	public String getWidgetColumn() {
		return widgetColumn;
	}
	
	public void setWidgetColumn(String widgetColumn) {
		this.widgetColumn = widgetColumn;
	}
	
	/**
	 * Private helper to save image cache.
	 * 
	 * @param imageUrl
	 * @return
	 */
	private String addCache(String url, HttpServletRequest req, Widget parentSettings) {
		Widget settings = this.findByUrl(url);
		if (settings == null)
			settings = new Widget(url, parentSettings);
		UrlResponseObject response = UrlUtil.getPage(url, req, null);
		if (response.getResponseType().startsWith(Widget.TYPE_IMAGE))
			settings.setCacheType(Widget.TYPE_IMAGE);
		else
			settings.setCacheType(Widget.TYPE_HTML);
		settings.setCache(response.getResponseBody());
		this.save(settings);
		settings.setName(""+settings.getId());
		this.save(settings);
		return settings.getName();
	}

}
