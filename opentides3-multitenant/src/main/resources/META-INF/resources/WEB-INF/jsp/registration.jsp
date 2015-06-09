<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.tenant.register" pageType="anonymous-page">
	<script type="text/javascript" src="<c:url value='/js/jquery-ui.min.js'/>"></script>
</app:header>

<div id="tenant-body" class="container">

	<spring:theme code="logo_large" var="logo" />
	
	<div class="wallpaper"></div>
	<div class="wallpaper-grid"></div>

	<div id="form-body" class="page">
		<div class="modal register-wrapper">
			<div class="modal-header">
				<h4 class="lead">We want to get to know you!</h4>
			</div>
			<form:form modelAttribute="formCommand" id="tenant-form">
				<div class="modal-body ">
					<div class="row-fluid">
						<div class="panel">
							<tides:input path="company" label="label.tenant.company"
								required="true" appendText="${subdomain }" cssClass="subdomain input-xlarge"/>
							<tides:select path="accountType" items="${accountTypeList}"
								label="label.tenant.account-type" itemLabel="name" itemValue="id"
								required="true" cssClass="input-block-level" />
							<tides:input path="owner.emailAddress" label="label.user.email"
								required="true" cssClass="input-block-level"  />
						</div>
						<div class="panel none">
							<tides:input path="owner.firstName" label="label.user.first-name"
								required="true" cssClass="input-block-level" />
							<tides:input path="owner.lastName" label="label.user.last-name"
								required="true" cssClass="input-block-level" />
							<tides:input path="owner.credential.username"
								label="label.user.username" required="true" cssClass="input-block-level" />
							<tides:input path="owner.credential.newPassword"
								label="label.user.password" type="password" required="true" cssClass="input-block-level" />
							<tides:input path="owner.credential.confirmPassword"
								label="label.user.confirm-password" type="password" required="true" cssClass="input-block-level" />
							<br />
						</div>
					</div>
				</div>
					<div class="modal-footer">
					<div>
						<button type="button" class="btn btn-primary btn-paging" id="nxt">
							<spring:message code="label.next" />
						</button>
					</div>
					<div class="none">
						<button type="button" class="btn btn-default btn-paging" id="back">
							<spring:message code="label.back" />
						</button>
						<input type="submit" class="btn btn-success" data-submit="save"
							value="<spring:message code="label.save" />" />
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>

<tides:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#tenant-body").RESTful();
		}).on('click', 'button.btn-paging', function(e){
			e.preventDefault();
			
			var $div = $('div.panel:visible');
			var $sibling = $div.siblings('div.panel:hidden');
			var $parent = $div.parents('div.row-fluid');
			
			$parent.animate({
				height: $sibling.outerHeight(),
				duration: 400
			});

			$div.toggle('slide', {
				duration : 400,
				complete : function() {
					$sibling.toggle('slide', {
						duration : 400
					});
				}
			});
			
			$('.modal-footer div').toggle();
		}).ajaxComplete(function() {
			var $hidden = $('div.panel:hidden');
			var $visible = $('div.panel:visible');
			
			if($hidden.find('.control-group.error').length > 0) {
				$('button#back').trigger('click');
			} else {
				var height = $visible.outerHeight();
				$visible.parents('div.row-fluid').animate({
					height: height,
					duration: 400
				});
			}
		});
	</script>
</tides:footer>
