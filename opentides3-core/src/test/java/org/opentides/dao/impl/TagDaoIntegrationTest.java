package org.opentides.dao.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.Tag;
import org.opentides.dao.TagDao;
import org.springframework.beans.factory.annotation.Autowired;

public class TagDaoIntegrationTest extends BaseDaoTest {
	
	@Autowired
	private TagDao tagDao;

	@SuppressWarnings("rawtypes")
	@Test
	public void testFindByTaggableClassAndId() {
		Class clazz = SystemCodes.class;
		Long tagId = 1l;
		List<Tag> actual = tagDao.findByTaggableClassAndId(clazz, tagId);
		
		assertNotNull(actual);
		List<Tag> expected = new ArrayList<>();
		Tag tag1 = new Tag();
		tag1.setTagText("Tag 1");
		tag1.setTaggableClass(SystemCodes.class);
		tag1.setTaggableId(1l);
		
		Tag tag2 = new Tag();
		tag2.setTagText("Tag 2");
		tag2.setTaggableClass(SystemCodes.class);
		tag2.setTaggableId(1l);
		
		expected.add(tag1);
		expected.add(tag2);
		
		assertEquals(expected, actual);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindByTaggableClassIdTagTexts() {
		Class clazz = SystemCodes.class;
		Long tagId = 1l;
		List<Tag> actual = tagDao.findByTaggableClassIdTagTexts(clazz, tagId, Arrays.asList("Tag 1"));
		
		assertNotNull(actual);
		List<Tag> expected = new ArrayList<>();
		Tag tag1 = new Tag();
		tag1.setTagText("Tag 1");
		tag1.setTaggableClass(SystemCodes.class);
		tag1.setTaggableId(1l);
		expected.add(tag1);
		assertEquals(expected, actual);
	}

}
