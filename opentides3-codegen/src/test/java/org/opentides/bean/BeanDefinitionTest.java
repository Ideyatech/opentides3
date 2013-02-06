/**
 * 
 */
package org.opentides.bean;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author allantan
 *
 */
public class BeanDefinitionTest {
	private String className;
	
	private String modelName;
	
	private String formName;
	
	private String prefix;
	
	private String modelPackage;
	
	private String package_;
	
	@Test
	public void testConstructor1() {
		BeanDefinition def = new BeanDefinition("org.opentides.bean", "SystemCodes");
		assertEquals("SystemCodes", def.getClassName());
		assertEquals("systemCodes", def.getModelName());
		assertEquals("system-codes", def.getFormName());
		assertEquals("system-codes", def.getPrefix());
		assertEquals("org.opentides.bean", def.getModelPackage());
		assertEquals("org.opentides", def.getPackage_());
	}
	
	@Test
	public void testConstructor2() {
		BeanDefinition def = new BeanDefinition("bean", "Systems");
		assertEquals("Systems", def.getClassName());
		assertEquals("systems", def.getModelName());
		assertEquals("systems", def.getFormName());
		assertEquals("systems", def.getPrefix());
		assertEquals("bean", def.getModelPackage());
		assertEquals("", def.getPackage_());
	}
	
	@Test
	public void testConstructor3() {
		BeanDefinition def = new BeanDefinition("", "Systems");
		assertEquals("", def.getModelPackage());
		assertEquals("", def.getPackage_());
	}

}
