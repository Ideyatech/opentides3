package org.opentides.web.json;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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
		MappingJackson2HttpMessageConverter {
	
	private static final Logger _log = Logger.getLogger(ViewAwareJsonMessageConverter.class);

	public ViewAwareJsonMessageConverter() {
		super();
		ObjectMapper defaultMapper = new ObjectMapper();
		defaultMapper.configure(
				MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		setObjectMapper(defaultMapper);
	}

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
				.createJsonGenerator(outputMessage.getBody(), encoding);
		try {			
			ObjectWriter w = getObjectMapper().writerWithView(view.getView());
			w.writeValue(jsonGenerator, view.getData());
		} catch (IOException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: "
					+ ex.getMessage(), ex);
		}
	}
}
