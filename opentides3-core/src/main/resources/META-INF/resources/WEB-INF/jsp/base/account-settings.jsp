<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.account-settings" active="account-settings">
	<!-- Required for photo cropping -->
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery-jcrop.min.css'/>" />
	<script type="text/javascript" src="<c:url value='/js/jquery-jcrop.min.js'/>"></script>
</app:header>


<div id="account-settings-body">

	<ul class="breadcrumb">
	  <li><a href="${home}"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
	  <li><spring:message code="label.account-settings"/></li>
	</ul>

	<div class="row-fluid">
	
		<div id="basic-info" class="span4">
			<h3><spring:message code="label.account-settings.basic-info"/></h3>
			<form:form commandName="user" method="POST" enctype="multipart/form-data" action="/account-settings/basic-info">
				<div class="message-container"></div>
				
				<tides:input path="firstName" label="label.user.first-name" required="true"/>
				<tides:input path="lastName" label="label.user.last-name" required="true"/>
				<tides:input path="emailAddress" label="label.user.email" required="true"/>
				<br/>
		    	<input type="submit" class="btn btn-info" data-submit="save" value="<spring:message code="label.account-settings.save-basic-info" />" />
				
			</form:form>
		</div>
	
		<div id="change-password" class="span4">
			<h3><spring:message code="label.account-settings.change-password"/></h3>
			<form:form commandName="user" method="POST" enctype="multipart/form-data" action="/account-settings/change-password">
				<div class="message-container"></div>
				<tides:input path="credential.newPassword" label="label.user.password" type="password" required="true" />
				<tides:input path="credential.confirmPassword" label="label.user.confirm-password" type="password" required="true" />
				<br/>
		    	<input type="submit" class="btn btn-info" data-submit="save" value="<spring:message code="label.account-settings.change-password"/>" />
				
			</form:form>
		</div>
		
	</div>
	<hr/>
	<div id="profile-picture" class="row-fluid">
		<h3><spring:message code="label.account-settings.profile-picture"/></h3>
		<div class="media">
			<img class="img-polaroid pull-left" src="${home}/image/${user.primaryImage.id}" />
			<div class="media-body">
				<div class="control-group">
					<button class="btn btn-info upload-photo" data-url="${home}/image/upload?imageId=${user.primaryImage.id}&className=User&classId=${user.id}">
						<i class="icon-picture"></i> <spring:message code="photo.change-photo" />
					</button>
				</div>
				<div class="control-group">
					<button class="btn adjust-photo" data-url="${home}/image/adjust?imageId=${user.primaryImage.id}&className=User&classId=${user.id}">
						<i class="icon-move"></i> <spring:message code="photo.edit-thumbnail" />
					</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="adjust-photo-modal modal hide fade" data-width="760" tabindex="-1"></div>
	<div class="upload-photo-modal modal hide fade" data-width="760" tabindex="-2"></div>

</div>

<tides:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			opentides3.jsonForm($('#basic-info form'));
			opentides3.jsonForm($('#change-password form'));
		})
		.on("click", '.adjust-photo', opentides3.showAdjustPhoto)
		.on("click", '.upload-photo', opentides3.showUploadPhoto);;
	</script>
</tides:footer>