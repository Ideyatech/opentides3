<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="userNamePlaceHolder" required="false" type="java.lang.String" %>
<%@ attribute name="loginLabel" required="false" type="java.lang.String" %>
<%@ attribute name="rememberMeLabel" required="false" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>

<!-- Initialize Login Label. -->
<c:set var="btnLoginLabel" value="label.login"/>
<c:if test="${not empty loginLabel}">
	<c:set var="btnLoginLabel" value="${loginLabel}" />
</c:if>

<!-- Initialize Remember Me Label. -->
<c:set var="btnRememberMeLabel" value="label.remember-me"/>
<c:if test="${not empty rememberMeLabel}">
	<c:set var="btnRememberMeLabel" value="${rememberMeLabel}" />
</c:if>

<form class="${cssClass}" method="POST" action="<c:url value='j_spring_security_check'/>">
				
	<c:if test="${not empty param.login_error}">
		<div class="alert alert-error">
			<a class="close" data-dismiss="alert" href="#">&times;</a>
			<spring:message code='error.${param.login_error}' /> 
			<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
		</div>
	</c:if>

	<h4><spring:message code='label.login' /></h4>
	<hr/>
	
	<div class="control-group">
		<input class="input-block-level" type="text" name="j_username" placeholder='<spring:message code="${userNamePlaceHolder}" text="${userNamePlaceHolder}"/>' autofocus="autofocus">
	</div>
	<div class="control-group">
		<input class="input-block-level" type="password" name="j_password" placeholder="Password">
	</div>
	
	<div class="control-group">
		<label class="checkbox">
			<input type="checkbox" checked>
			<small><spring:message code="${btnRememberMeLabel}" /></small>
		</label>
	</div>
	
	<input type="submit" class="btn btn-info btn-block btn-large" value="<spring:message code="${btnLoginLabel}" />"/>
	
</form>
			