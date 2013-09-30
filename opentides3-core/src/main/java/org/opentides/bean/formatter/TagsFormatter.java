package org.opentides.bean.formatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.opentides.bean.Tag;
import org.opentides.util.StringUtil;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component(value = "tagsFormatter")
public class TagsFormatter implements Formatter<List<Tag>> {
	
	private static Logger log = Logger.getLogger(TagsFormatter.class);

	@Override
	public String print(List<Tag> object, Locale locale) {
		if(object != null && !object.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < object.size(); i++) {
				Tag tag = object.get(i);
				sb.append(tag.getTagText());
				if(i != object.size() - 1)
					sb.append(",");
			}
			return sb.toString();
		}
		return "";
	}

	@Override
	public List<Tag> parse(String text, Locale locale) throws ParseException {
		if(!StringUtil.isEmpty(text)) {
			List<Tag> tags = new ArrayList<>();
			String [] csvList = text.split(",");
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
