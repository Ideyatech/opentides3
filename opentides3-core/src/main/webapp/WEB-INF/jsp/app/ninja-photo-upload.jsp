<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<form:form commandName="command" enctype="multipart/form-data"
	method="POST" action="/ninja/photo/upload?id=${command.id}">
	<input type="file" id="photo" name="photo" />
	<input type="submit" value="Submit" />
	
</form:form>