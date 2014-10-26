/**
 * 
 */
package org.opentides.processor;

import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.junit.Assert;

/**
 * For unit testing of code generation. Copied from:
 * http://jcavallotti.blogspot.com/2013/05/how-to-unit-test-annotation-processor.html
 * 
 * @author allantan
 * 
 */
public class SimpleVerifierCase implements CompilerTestCase {

	@Override
	public String[] getClassesToCompile() {
		return new String[] { "src/test/java/org/opentides/bean/Ninja.java" };
	}

	@Override
	public void test(List<Diagnostic<? extends JavaFileObject>> diagnostics,
			String stdoutS, Boolean result) {

		// no mandatory warnings or compilation errors should be found.
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
			if (diagnostic.getKind() == Kind.MANDATORY_WARNING
					|| diagnostic.getKind() == Kind.ERROR) {
				Assert.fail("Failed with message: "
						+ diagnostic.toString());
			}
		}

		Assert.assertEquals("Files should have no compilation errors",
				Boolean.TRUE, result);
	}
}
