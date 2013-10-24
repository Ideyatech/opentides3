package org.opentides.bean.converter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opentides.bean.Tag;
import org.opentides.bean.user.UserAuthority;
import org.springframework.core.convert.TypeDescriptor;

public class StringArrayToListOfTagsConverterTest {

	@Test
	public void testConvert() {
		List<Tag> expected1 = new ArrayList<>();
		expected1.add(new Tag("tag1"));
		expected1.add(new Tag("tag2"));
		expected1.add(new Tag("tag3"));
		assertEquals(expected1, new StringArrayToListOfTagsConverter().convert(new String[] {"tag1,tag2,tag3"}, null, null));
		
		List<Tag> expected2 = new ArrayList<>();
		expected2.add(new Tag("tag1"));
		assertEquals(expected2, new StringArrayToListOfTagsConverter().convert(new String[] {"tag1"}, null, null));
		
		
	}
	
	@Test
	public void testMatches() {
		TypeDescriptor source = TypeDescriptor.valueOf(String[].class);
		TypeDescriptor target = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(UserAuthority.class));
		assertFalse(new StringArrayToListOfTagsConverter().matches(source, target));
		
		source = TypeDescriptor.valueOf(String.class);
		target = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Tag.class));
		assertFalse(new StringArrayToListOfTagsConverter().matches(source, target));
		
		source = TypeDescriptor.valueOf(String[].class);
		target = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Tag.class));
		assertTrue(new StringArrayToListOfTagsConverter().matches(source, target));
	}

}
