package org.opentides.bean.formatter;

import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

/**
 * 
 * @author gino
 *
 */
public class OpentideFormatterRegistrar implements FormatterRegistrar {

	@Override
	public void registerFormatters(FormatterRegistry registry) {
		registry.addFormatter(new TagsFormatter());
	}

}
