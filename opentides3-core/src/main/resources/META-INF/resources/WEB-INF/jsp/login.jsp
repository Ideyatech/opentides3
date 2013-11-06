<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.login" pageType="anonymous-page" />

<spring:theme code="logo_large" var="logo" />

<div class="wallpaper"></div>
<div class="wallpaper-grid"></div>

<div class="form-login modal">
	<div class="modal-body">
		<div class="row-fluid">
			<tides:login_form />
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


<tides:footer>

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
	
</tides:footer>
