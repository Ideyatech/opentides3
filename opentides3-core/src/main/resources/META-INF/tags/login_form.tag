<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="supportRememberMe" required="false" type="java.lang.Boolean"%>
<%@ attribute name="cssClass" required="false" type="java.lang.String"%>

<form class="${cssClass}" method="POST"
	action="<c:url value='j_spring_security_check'/>">

	<c:if test="${not empty param.login_error}">
		<div class="alert alert-error">
			<a class="close" data-dismiss="alert" href="#">&times;</a>
			<spring:message code='error.${param.login_error}' />
			${SPRING_SECURITY_LAST_EXCEPTION.message}
		</div>
	</c:if>

	<h4>
		<spring:message code='label.login' />
	</h4>
	<hr />

	<div class="control-group">
		<div class="input-prepend">
			<span class="add-on"><i class="icon-user"></i></span> <input
				class="input-block-level" type="text" name="j_username" 
				placeholder="<spring:message code='label.username' />"
				autofocus="autofocus">
		</div>
	</div>
	<div class="control-group">
		<div class="input-prepend">
			<span class="add-on"><i class="icon-lock"></i></span> <input
				class="input-block-level" type="password" name="j_password" placeholder="Password">
		</div>
	</div>

<c:if test="${supportRememberMe}">
	<div class="control-group">
		<label class="checkbox"> <input type="checkbox" checked>
			<small><spring:message code="label.remember-me" /></small>
		</label>
	</div>
</c:if>

	<input type="submit" class="btn btn-primary btn-block btn-large"
		value="<spring:message code="label.login-action" />" />

</form>
