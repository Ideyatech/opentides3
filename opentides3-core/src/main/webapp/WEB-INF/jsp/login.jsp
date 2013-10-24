<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.login" pageType="anonymous-page" />

<spring:theme code="logo_large" var="logo" />

<div class="wallpaper"></div>
<div class="wallpaper-grid"></div>

<div class="form-login modal">
	<div class="modal-body">

		<div class="row-fluid">
			
			<form class="span6" method="POST" action="<c:url value='login'/>">
				
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
					<input class="input-block-level" type="text" name="j_username" placeholder="Username" autofocus="autofocus">
				</div>
				<div class="control-group">
					<input class="input-block-level" type="password" name="j_password" placeholder="Password">
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
				<div class="control-group"><small><spring:message code="message.alternative-login-help" /></small></div>
				<div class="control-group">
					<button class="btn btn-primary btn-block" onclick="window.location.href='${home}/facebook/connect'">
					<i class="icon-facebook-sign"></i><spring:message code="message.login-with-facebook" /></button>
				</div>
				<div class="control-group">
					<button class="btn btn-info btn-block" onclick="window.location.href='${home}/twitter/connect'">
					<i class="icon-twitter-sign"></i><spring:message code="message.login-with-twitter" /></button>
				</div>
				<div class="control-group">
					<button class="btn btn-danger btn-block" onclick="window.location.href='${home}/google/connect'">
					<i class="icon-google-plus-sign"></i><spring:message code="message.login-with-google" /></button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<small class="pull-left"><spring:message code="message.signup-help" /> <a href="#" data-url="${home}/register" class="show-signup"><spring:message code="message.signup-now" /></a></small>
		<small class="pull-right"><a href="#"><spring:message code="message.forgot-your-password" /></a></small>
	</div>
</div>

<div class="form-signup modal hide fade" data-width="460"></div>

<div class="verify-signup modal hide fade" data-width="660">
	<div class="modal-body">
		<h4><spring:message code="message.registration.heading" /></h4>
		<hr/>
		<p><spring:message code="message.registration.email-sent" /> <code class="signup-email"></code>.
		</p>
		<a href="#"><spring:message code="message.registration.resend-email" /></a>
	</div>
	<div class="modal-footer pagination-centered">
		<button class="btn btn-success btn-large" data-dismiss="modal"><spring:message code="message.login-now" /></button>
	</div>
</div>


<app:footer>

	<script type="text/javascript">
		$(document).ready(function() {
			
		}).on('click', 'a.show-signup', function(){

			var url = $(this).data('url');
			
			$('.form-signup').load(url, '', function(){
				$('.form-login').fadeOut();
				$('.form-signup').modal();
			});
			
		}).on('click', 'a.show-login', function () {
			$('.form-signup').modal('hide');
		}).on('hidden', '.form-signup', function () {
			$('.form-login').fadeIn();
		});
	</script>
	
</app:footer>
