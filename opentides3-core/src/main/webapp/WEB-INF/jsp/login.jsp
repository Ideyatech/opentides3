<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.login" pageType="anonymous-page" />

<spring:theme code="logo_large" var="logo" />

<div class="wallpaper"></div>
<div class="wallpaper-grid"></div>

<div class="form-signin modal" >
	<div class="modal-body">
		<div class="row-fluid">
			<form class="span6" method="POST" action="<c:url value='j_spring_security_check'/>">
				
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
				<div class="control-group">
					<input class="input-block-level" type="text" name="j_username" placeholder="Username" value="admin" autofocus="autofocus">
				</div>
				<div class="control-group">
					<input class="input-block-level" type="password" name="j_password" placeholder="Password" value="123123">
				</div>
				
				<div class="control-group">
					<label class="checkbox">
						<input type="checkbox" checked>
						<small><spring:message code="label.remember-me" /></small>
					</label>
				</div>
				
				<input type="submit" class="btn btn-info btn-block btn-large" value="<spring:message code="label.login" />"/>
				
			</form>
			<div class="span6 alternative-logins pagination-centered">
				<div class="control-group"><small>Login if your account is connected with Facebook, Twitter or Google</small></div>
				<div class="control-group"><button class="btn btn-primary btn-block">Login with Facebook</button></div>
				<div class="control-group"><button class="btn btn-info btn-block">Login with Twitter</button></div>
				<div class="control-group"><button class="btn btn-danger btn-block">Login with Google</button></div>
			</div>
		</div>
	</div>
	<div class="modal-footer pagination-centered">
		<a href="#"><small><spring:message code="message.forgot-your-password" /></small></a>
	</div>
</div>

<app:footer />
