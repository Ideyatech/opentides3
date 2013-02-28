<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header-anonymous pageTitle="label.login" />

<spring:theme code="logo_large" var="logo" />

	<div class="form-signin" >
		<form method="POST" action="<c:url value='j_spring_security_check'/>">
			
			<h4><spring:message code='label.login' /></h4>
			<hr/>
			<c:if test="${not empty param.login_error}">
				<div class="alert alert-error">
					<a class="close" data-dismiss="alert" href="#">&times;</a>
					<spring:message code='error.${param.login_error}' />
					<br />
					<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
				</div>
			</c:if>
			
			<input class="input-block-level" type="text" name="j_username" placeholder="Username">
			<input class="input-block-level" type="password" name="j_password" placeholder="Password">
			
			<label class="checkbox">
				<input type="checkbox">
				<small><spring:message code="label.remember-me" /></small>
			</label>
			
			<input type="submit" class="btn btn-info btn-block btn-large" value="<spring:message code="label.login" />"/>
			
		</form>
		
		<div class="forgot-password pagination-centered">
			<a href="#"><small><spring:message code="message.forgot-your-password" /></small></a>
		</div>
	</div>

<app:footer />