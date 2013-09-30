package org.opentides.bean.converter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opentides.bean.Tag;

public class ListOfTagsToCommaSeparatedStringConverterTest {

	@Test
	public void testConvert() {
		String expected = "Tag1,Tag2,Tag3";
		
		List<Tag> tags = new ArrayList<>();
		tags.add(new Tag("Tag1"));
		tags.add(new Tag("Tag2"));
		tags.add(new Tag("Tag3"));
		
		assertEquals(expected, new ListOfTagsToCommaSeparatedStringConverter().convert(tags));
		
		assertEquals(null, new ListOfTagsToCommaSeparatedStringConverter().convert(new ArrayList<Tag>()));
		assertEquals(null, new ListOfTagsToCommaSeparatedStringConverter().convert(null));
	}

}
