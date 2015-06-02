<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.login" pageType="anonymous-page">
	<script type="text/javascript" src="<c:url value='/js/jquery-ui.min.js'/>"></script>
</app:header>

<spring:theme code="logo_large" var="logo" />

<div class="wallpaper"></div>
<div class="wallpaper-grid"></div>

<div class="modal form-login">
	<div class="modal-body ">
		<form:form modelAttribute="formCommand" id="tenant-form">
			<div class="overflow-hidden row-fluid">
			<h4>
				<spring:message code='label.register' />
			</h4>
			<hr />
			<div class="panel top">
				<tides:input path="company" label="label.tenant.company"
					required="true" appendText="${subdomain }" cssClass="subdomain"/>
				<tides:select path="accountType" items="${accountTypeList}"
					label="label.tenant.account-type" itemLabel="name" itemValue="id"
					required="true" />
				<tides:input path="owner.emailAddress" label="label.user.email"
					required="true" />
				<br />
				<button type="button" class="btn btn-link" id="nxt">
					<spring:message code="label.next" />
				</button>
			</div>
			<div class="panel bottom none">
				<tides:input path="owner.firstName" label="label.user.first-name"
					required="true" />
				<tides:input path="owner.lastName" label="label.user.last-name"
					required="true" />
				<tides:input path="owner.credential.username"
					label="label.user.username" required="true" />
				<tides:input path="owner.credential.newPassword"
					label="label.user.password" type="password" required="true" />
				<tides:input path="owner.credential.confirmPassword"
					label="label.user.confirm-password" type="password" required="true" />
				<br />
				<button type="button" class="btn btn-link" id="back">
					<spring:message code="label.back" />
				</button>
				<input type="submit" class="btn btn-success" data-submit="save"
					value="<spring:message code="label.save" />" />
			</div>
		</div>
		</form:form>
	</div>
</div>

<tides:footer>
	<script type="text/javascript">
		$(document).ready(function() {
		}).on('click', 'button.btn-link', function(e){
			e.preventDefault();
			var $div = $(this).parents('div.panel');
			var $parent = $div.parents('div.row-fluid');
			
			$parent.animate({
				height: $div.siblings('div.panel').outerHeight(),
				duration: 200
			});

			$parent.children('div.top').toggle('slide', {
				direction : 'down',
				duration : 200,
				complete : function() {
					$parent.children('div.bottom').toggle('slide', {
						direction : 'up',
						duration : 200,
					});
				}
			});
		});
	</script>
</tides:footer>
