package org.opentides.web.validator;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opentides.bean.Comment;
import org.opentides.bean.SystemCodes;
import org.springframework.validation.BindException;

public class CommentValidatorTest {

	@Test
	public void testSupports() {
		assertTrue(new CommentValidator().supports(Comment.class));
		assertFalse(new CommentValidator().supports(SystemCodes.class));
	}

	@Test
	public void testValidate() {
		Comment obj = new Comment();
		obj.setText("My Comment");
		BindException errors = new BindException(obj, "comment");
		
		new CommentValidator().validate(obj, errors);
		assertTrue(errors.getAllErrors().size() == 0);
		
		obj = new Comment();
		errors = new BindException(obj, "comment");
		new CommentValidator().validate(obj, errors);
		assertTrue(errors.getAllErrors().size() == 1);
		assertNotNull(errors.getFieldError("text"));
		
	}

}
