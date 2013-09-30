package org.opentides.bean.converter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opentides.bean.Tag;

public class StringArrayToListOfTagsConverterTest {

	@Test
	public void testConvert() {
		List<Tag> expected1 = new ArrayList<>();
		expected1.add(new Tag("tag1"));
		expected1.add(new Tag("tag2"));
		expected1.add(new Tag("tag3"));
		assertEquals(expected1, new StringArrayToListOfTagsConverter().convert(new String[] {"tag1,tag2,tag3"}));
		
		List<Tag> expected2 = new ArrayList<>();
		expected2.add(new Tag("tag1"));
		assertEquals(expected2, new StringArrayToListOfTagsConverter().convert(new String[] {"tag1"}));
		
		assertEquals(0, new StringArrayToListOfTagsConverter().convert(new String[] {""}).size());
	}

}
