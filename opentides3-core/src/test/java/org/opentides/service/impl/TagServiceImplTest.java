package org.opentides.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.Tag;
import org.opentides.bean.Taggable;
import org.opentides.dao.TagDao;

public class TagServiceImplTest {
	
	@InjectMocks
	private TagServiceImpl tagService = new TagServiceImpl();
	
	@Mock
	private TagDao tagDao;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSetTagDao() {
		tagService.setTagDao(tagDao);
		assertEquals(tagDao, tagService.getDao());
	}

	@Test
	public void testSaveAllTags() {
		List<Tag> tags = new ArrayList<>();
		Tag tag1 = new Tag();
		tag1.setTagText("Tag 1");
		tag1.setTaggableClass(SystemCodes.class);
		tag1.setTaggableId(1l);
		
		Tag tag2 = new Tag();
		tag2.setTagText("Tag 2");
		tag2.setTaggableClass(SystemCodes.class);
		tag2.setTaggableId(1l);
		
		tags.add(tag1);
		tags.add(tag2);
		
		tagService.saveAllTags(tags);
		verify(tagDao).saveAllEntityModel(tags);
	}

	@Test
	public void testFindByTaggableClassAndId() {
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
		
		when(tagDao.findByTaggableClassAndId(SystemCodes.class, 1l)).thenReturn(expected);
		
		List<Tag> actual = tagService.findByTaggableClassAndId(SystemCodes.class, 1l);
		verify(tagDao).findByTaggableClassAndId(SystemCodes.class, 1l);
		
		assertEquals(expected, actual);
	}

	@Test
	public void testRemoveExistingTags() {
		List<Tag> oldTags = new ArrayList<>();
		Tag tag1 = new Tag();
		tag1.setId(1l);
		tag1.setTagText("Tag 1");
		tag1.setTaggableClass(SampleClass.class);
		tag1.setTaggableId(1l);
		
		Tag tag3 = new Tag();
		tag3.setId(3l);
		tag3.setTagText("Tag 3");
		tag3.setTaggableClass(SampleClass.class);
		tag3.setTaggableId(1l);
		
		oldTags.add(tag1);
		oldTags.add(tag3);
		
		when(tagDao.findByTaggableClassIdTagTexts(SampleClass.class, 1l, Arrays.asList("Tag 1","Tag 3","Tag 4"))).thenReturn(oldTags);
		
		List<Tag> newTags = new ArrayList<>();
		Tag newTag1 = new Tag();
		newTag1.setTagText("Tag 1");
		newTag1.setTaggableClass(SampleClass.class);
		newTag1.setTaggableId(1l);
		
		Tag newTag2 = new Tag();
		newTag2.setTagText("Tag 3");
		newTag2.setTaggableClass(SampleClass.class);
		newTag2.setTaggableId(1l);
		
		Tag newTag3 = new Tag();
		newTag3.setTagText("Tag 4");
		newTag3.setTaggableClass(SampleClass.class);
		newTag3.setTaggableId(1l);
		
		newTags.add(newTag1);
		newTags.add(newTag2);
		newTags.add(newTag3);
		
		SampleClass sample = new SampleClass();
		sample.setTags(newTags);
		
		tagService.preProcessTags(sample, 1l, SampleClass.class);
		List<Tag> actual = sample.getTags();
		
		assertEquals(3, actual.size());
		for(Tag tag : actual) {
			if("Tag 1".equals(tag.getTagText())) {
				//Tag 1 should be old so there should be an ID
				assertNotNull("Tag 1 should be old so there should be an ID", tag.getId());
			} else if("Tag 2".equals(tag.getTagText())) {
				fail("Tag 2 was deleted so it should not be included.");
			} else if("Tag 3".equals(tag.getTagText())) {
				//Tag 3 should be added
				assertNotNull("Tag 3 should be retained", tag.getId());
			} else if("Tag 4".equals(tag.getTagText())) {
				assertNull("Tag 4 is new so it should not have an ID", tag.getId());
			}
		}
	}
	
	private class SampleClass implements Taggable {
		
		private List<Tag> tags;
		
		@Override
		public List<Tag> getTags() {
			return tags;
		}

		@Override
		public void setTags(List<Tag> tags) {
			this.tags = tags;
		}
		
	}

}
