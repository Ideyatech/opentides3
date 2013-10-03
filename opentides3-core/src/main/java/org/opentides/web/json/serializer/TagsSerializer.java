package org.opentides.web.json.serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opentides.bean.Tag;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Serializer specifically made for List of Tags. This will convert the
 * list to comma separated strings.
 *  
 * @author gino
 *
 */
public class TagsSerializer extends StdSerializer<List<Tag>> {
	
	public TagsSerializer() {
		super(new ArrayList<Tag>().getClass(), false);
	}
	
	@Override
	public void serialize(List<Tag> value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonGenerationException {
		if(value != null && !value.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < value.size(); i++) {
				Tag tag = value.get(i);
				sb.append(tag.getTagText());
				if(i != value.size() - 1)
					sb.append(",");
			}
			jgen.writeString(sb.toString());
		}
	}
	
}
