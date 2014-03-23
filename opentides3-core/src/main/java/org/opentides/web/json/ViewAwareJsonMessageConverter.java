package org.opentides.web.json;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Adds support for Jackson's JsonView on methods annotated with a
 * {@link ResponseView} annotation.
 * Updated for Jackson2.
 * 
 * @author martypitt
 * @author allantan
 * 
 */
public class ViewAwareJsonMessageConverter extends
		MappingJackson2HttpMessageConverter implements InitializingBean {
	
	private static final Logger _log = Logger.getLogger(ViewAwareJsonMessageConverter.class);
	
	private boolean prefixJson = false;
	
	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		try {
			if (object instanceof DataView && ((DataView) object).hasView()) {
				writeView((DataView) object, outputMessage);
			} else {
				super.writeInternal(object, outputMessage);
			}
		} catch (Exception e) {
			_log.error("Failed to convert object to json.", e);
			throw e;
		}
	}		

	protected void writeView(DataView view, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders()
				.getContentType());
		JsonGenerator jsonGenerator = getObjectMapper().getFactory()
				.createGenerator(outputMessage.getBody(), encoding);
		try {
			ObjectWriter w = getObjectMapper().writerWithView(view.getView());
			if (this.prefixJson) {
	            jsonGenerator.writeRaw("{} && ");
	        }
			w.writeValue(jsonGenerator, view.getData());
		} catch (IOException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: "
					+ ex.getMessage(), ex);
		}
	}

	/**
	 * @param prefixJson the prefixJson to set
	 */
	public final void setPrefixJson(boolean prefixJson) {
		this.prefixJson = prefixJson;
		super.setPrefixJson(prefixJson);
	}
	
	@Override
	public void afterPropertiesSet() {
		if (getObjectMapper()==null)
			setObjectMapper(new ObjectMapper());
		getObjectMapper().configure(
				MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}
}
