package org.opentides.processor;

import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * For unit testing of code generation. Copied from:
 * http://jcavallotti.blogspot.com/2013/05/how-to-unit-test-annotation-processor.html
 * 
 * @author allantan
 *
 */
public interface CompilerTestCase {
	/**
     * Retrieve the list of files whose compilation would be tested.
     * @return a list of files in relative or absolute position.
     */
    public String[] getClassesToCompile();
    
    /**
     * Perform the test.
     * 
     * @param diagnostics the compiler diagnostics for the evaluated files.
     * @param stdoutS  the output of the compiler.
     * @param result the result of the compilation. True if succeeded, false if not.
     */
    public void test(List<Diagnostic<? extends JavaFileObject>> diagnostics, String stdoutS, Boolean result);

}
