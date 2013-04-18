<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="modal-body">

	<h4><spring:message code="label.user-registration" /></h4>
	<hr/>

	<form:form commandName="baseUser" action="${home}/register" method="POST" cssClass="form-horizontal" >
		<div class="message-container"></div>
	
		<app:input path="credential.username" label="label.user.username"/>
		<app:input path="firstName" label="label.user.first-name"/>
		<app:input path="lastName" label="label.user.last-name"/>
		<app:input path="emailAddress" label="label.user.email"/>
		<app:input path="credential.newPassword" label="label.user.password" type="password"/>
		<app:input path="credential.confirmPassword" label="label.user.confirm-password" type="password"/>
		<div class="control-group">
			<div class="controls">
				<input type="submit" class="btn btn-info btn-large" value="Create account"/>
			</div>
		</div>
		
	</form:form>
</div>
	
<div class="modal-footer">
	<small class="pull-left"><spring:message code="message.login-help" /> <a href="#" data-url="${home}/register" class="show-login"><spring:message code="message.login-now" /></a></small>
</div>

<script type="text/javascript">

	opentides3.jsonForm($('.form-signup form'), function(data){
		$('.signup-email').text(data.email);
		$('.form-signup').modal('hide');
		$('.verify-signup').modal();
	});
</script>
