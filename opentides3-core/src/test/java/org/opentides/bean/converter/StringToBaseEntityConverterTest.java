package org.opentides.bean.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opentides.bean.Comment;
import org.opentides.bean.FileInfo;
import org.opentides.bean.ImageInfo;
import org.opentides.bean.Sequence;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.PasswordReset;
import org.opentides.bean.user.UserAuthority;
import org.opentides.bean.user.UserCredential;
import org.opentides.bean.user.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.ideyatech.bean.Clan;
import com.ideyatech.bean.Ninja;
import com.ideyatech.bean.Skills;
import com.ideyatech.bean.UserCriteria;
import com.ideyatech.service.NinjaService;

/**
 * Unit test for StringToBaseEntityConverter.
 * 
 * @author gino
 *
 */
@ContextConfiguration(locations = 
	{"classpath:org/opentides/bean/converter/applicationContext-converter-test.xml"})
public class StringToBaseEntityConverterTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private StringToBaseEntityConverter converter;
	
	@Autowired
	private NinjaService ninjaService;
	
	@Before
	public void init() {
		
	}
	
	@Test
	public void testGetConvertibleTypes() {
		
		//TODO Think of a better way to generate the expected value as this can easily break the test
		/*
		 * As of September 24, 2013 these are the only classes that is a subclass of BaseEntity
		 */
		Set<ConvertiblePair> expected = new HashSet<ConvertiblePair>();
		expected.add(new ConvertiblePair(String.class, FileInfo.class));
		expected.add(new ConvertiblePair(String.class, Comment.class));
		expected.add(new ConvertiblePair(String.class, Sequence.class));
		expected.add(new ConvertiblePair(String.class, BaseUser.class));
		expected.add(new ConvertiblePair(String.class, PasswordReset.class));
		expected.add(new ConvertiblePair(String.class, UserAuthority.class));
		expected.add(new ConvertiblePair(String.class, UserCredential.class));
		expected.add(new ConvertiblePair(String.class, ImageInfo.class));
		expected.add(new ConvertiblePair(String.class, UserGroup.class));
		expected.add(new ConvertiblePair(String.class, Ninja.class));
		expected.add(new ConvertiblePair(String.class, Skills.class));
		expected.add(new ConvertiblePair(String.class, Clan.class));
		expected.add(new ConvertiblePair(String.class, UserCriteria.class));
		
		Set<ConvertiblePair> actual = converter.getConvertibleTypes();
		
		assertEquals("Did you add a new class that is assignable to BaseEntity?", expected, actual);
		
	}
	
	@Test
	public void testConvert() {
		Ninja ninja = new Ninja();
		ninja.setId(1l);
		ninja.setFirstName("Master");
		ninja.setLastName("Roshi");
		
		String source = "1";
		Mockito.when(ninjaService.load(source)).thenReturn(ninja);
		
		TypeDescriptor targetType = TypeDescriptor.valueOf(Ninja.class);
		Ninja actual = (Ninja)converter.convert(source, null, targetType);
		
		assertNotNull(actual);
	}
	
	@Test
	public void testMatches() {
		TypeDescriptor target = TypeDescriptor.valueOf(Ninja.class);
		assertTrue(converter.matches(null, target));
	}

}
