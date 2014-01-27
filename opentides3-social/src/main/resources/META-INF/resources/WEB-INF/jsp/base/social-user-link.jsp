<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>

<app:header pageTitle="label.social-account" pageType="anonymous-page"/>

<div id="social-user-link-body">
	<div class="main container">
		<div class="row-fluid">
			<div id="social-user-link-body" class="pagination-centered span6 offset3">
				<div class="alert alert-success"><spring:message code="${status}"/></div>
			        <i class="icon-social-font icon-${socialType}-sign connected"></i>
				<div>
		        <p>
		        	<spring:message code="label.social.redirecting" />
		        </p>
		        <p>
		            <button id="btn-social-back" class="btn-link"><spring:message code="label.social.back" /></button>
		        </p>
		        </div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$("#btn-social-back").on("click", function(event) {
			event.preventDefault();
			window.location.href = "${refererURI}";
		});
		
		// NOTE : Automatically Redirect to previous page 
		setTimeout(function(){
			window.location.href = "${refererURI}";
		}, 5000);
	});
</script>

<tides:footer/>