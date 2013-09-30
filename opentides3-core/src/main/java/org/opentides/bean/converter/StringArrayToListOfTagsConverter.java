package org.opentides.bean.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.opentides.bean.Tag;
import org.opentides.util.StringUtil;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts a comma separated String to a list of {@link Tag} objects
 * @author gino
 *
 */
public class StringArrayToListOfTagsConverter implements Converter<String[], List<Tag>> {
	
	private static Logger log = Logger.getLogger(StringArrayToListOfTagsConverter.class);

	@Override
	public List<Tag> convert(String [] source) {
		if(source != null && source.length > 0) {
			List<Tag> tags = new ArrayList<>();
			String csv = source[0];
			if(log.isDebugEnabled()) {
				log.debug("Source string: " + csv);
			}
			String [] csvList = csv.split(",");
				for(String str : csvList) {
					if(!StringUtil.isEmpty(str)) {
						if(log.isDebugEnabled()) {
							log.debug("Converting string " + str + " to Tag object");
						}
						tags.add(new Tag(str));
					}
				}
			return tags;
		}
		return null;
	}

}
