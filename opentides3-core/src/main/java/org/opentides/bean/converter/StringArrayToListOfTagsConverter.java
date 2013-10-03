package org.opentides.bean.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.opentides.bean.Tag;
import org.opentides.util.StringUtil;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

/**
 * Converts a comma separated String to a list of {@link Tag} objects.
 * 
 * @author gino
 *
 */
public class StringArrayToListOfTagsConverter implements ConditionalGenericConverter {
	
	private static Logger log = Logger.getLogger(StringArrayToListOfTagsConverter.class);

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> convertibleTypes = new HashSet<ConvertiblePair>();
		convertibleTypes.add(new ConvertiblePair(String[].class, new ArrayList<Tag>().getClass()));
		return convertibleTypes;
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType,
			TypeDescriptor targetType) {
		String [] sourceStr = (String[])source;
		if(sourceStr != null && sourceStr.length > 0) {
			List<Tag> tags = new ArrayList<>();
			String csv = sourceStr[0];
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

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		boolean sourceValid = false, targetValid = false;
		if(sourceType.isArray()) {
			TypeDescriptor sourceTypeDesc = sourceType.getElementTypeDescriptor();
			if(sourceTypeDesc != null) {
				if(String.class.isAssignableFrom(sourceTypeDesc.getObjectType())) {
					sourceValid = true;
				}
			}
		}
		if(targetType.isCollection()) {
			TypeDescriptor targetTypeDesc = targetType.getElementTypeDescriptor();
			if(targetTypeDesc != null) {
				if(Tag.class.isAssignableFrom(targetTypeDesc.getObjectType())) {
					targetValid = true;
				}
			}
		}
		return sourceValid && targetValid;
	}

}
