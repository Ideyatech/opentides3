package org.opentides.bean.converter;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ideyatech.bean.Ninja;

/**
 * Unit test for {@link BaseEntityToLongConverter}
 * 
 * @author gino
 *
 */
public class BaseEntityToLongConverterTest {
	
	private BaseEntityToLongConverter<Ninja> ninjaConverter;
	
	@Before
	public void init() {
		ninjaConverter = new BaseEntityToLongConverter<>();
	}
	
	@Test
	public void testConvert() {
		Ninja ninja = new Ninja();
		ninja.setId(100l);
		
		assertEquals(new Long(100l), ninjaConverter.convert(ninja));
		
		ninja.setId(null);
		assertEquals(null, ninjaConverter.convert(ninja));
	}

}
