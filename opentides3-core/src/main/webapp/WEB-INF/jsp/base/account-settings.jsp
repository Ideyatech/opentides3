<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.account-settings" active="account-settings">

	<!-- Required for photo cropping -->
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery-jcrop.min.css'/>" />
	<script type="text/javascript" src="<c:url value='/js/jquery-jcrop.min.js'/>"></script>
	
	<style type="text/css">
		#account-settings-body #my-accounts .col-1 {
			width: 40px;
		}
		
		#account-settings-body #my-accounts .icon-facebook-sign,
		#account-settings-body #my-accounts .icon-twitter-sign,
		#account-settings-body #my-accounts .icon-google-plus-sign {
			color: #aaa;
		}
		
		#account-settings-body #my-accounts .icon-facebook-sign.connected {
			color: #006dcc;
		}
		#account-settings-body #my-accounts .icon-twitter-sign.connected {
			color: #49afcd;
		}
		#account-settings-body #my-accounts .icon-google-plus-sign.connected {
			color: #da4f49;
		}
		
	</style>
	
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
				
				<app:input path="credential.username" label="label.user.username" required="true" />
				<app:input path="firstName" label="label.user.first-name" required="true"/>
				<app:input path="lastName" label="label.user.last-name" required="true"/>
				<app:input path="emailAddress" label="label.user.email" required="true"/>
				<br/>
		    	<input type="submit" class="btn btn-success" data-submit="save" value="<spring:message code="label.account-settings.save-basic-info" />" />
				
			</form:form>
		</div>
	
		<div id="change-password" class="span4">
			<h3><spring:message code="label.account-settings.change-password"/></h3>
			<form:form commandName="user" method="POST" enctype="multipart/form-data" action="/account-settings/change-password">
				<div class="message-container"></div>
				<app:input path="credential.newPassword" label="label.user.password" type="password" required="true" />
				<app:input path="credential.confirmPassword" label="label.user.confirm-password" type="password" required="true" />
				<br/>
		    	<input type="submit" class="btn btn-success" data-submit="save" value="<spring:message code="label.account-settings.change-password"/>" />
				
			</form:form>
		</div>
		
		<div id="my-accounts" class="span4">
			<h3><spring:message code="label.account-settings.my-accounts"/></h3>
			<table class="table">
				<tr>
					<td class="col-1"><i class="icon-facebook-sign icon-3x ${empty user.facebookId ? '':'connected'}"></i></td>
					<td>
						<c:choose>
							<c:when test="${empty user.facebookId}">
								<small><spring:message code="label.account-settings.not-connected"/></small><br/>
								<a href="${home}/facebook/link"><spring:message code="label.account-settings.connect-facebook"/></a>
							</c:when>
							<c:otherwise>
								<i class="icon-ok"></i> <spring:message code="label.account-settings.connected"/><br/>
								<small><a href="${home}/facebook/unlink"><spring:message code="label.account-settings.disconnect-facebook"/></a></small>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="col-1"><i class="icon-twitter-sign icon-3x ${empty user.twitterId ? '':'connected'}"></i></td>
					<td>
						<c:choose>
							<c:when test="${empty user.twitterId}">
								<small><spring:message code="label.account-settings.not-connected"/></small><br/>
								<a href="${home}/twitter/link"><spring:message code="label.account-settings.connect-twitter"/></a>
							</c:when>
							<c:otherwise>
								<i class="icon-ok"></i> <spring:message code="label.account-settings.connected"/><br/>
								<small><a href="${home}/twitter/unlink"><spring:message code="label.account-settings.disconnect-twitter"/></a></small>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="col-1"><i class="icon-google-plus-sign icon-3x ${empty user.googleId ? '':'connected'}"></i></td>
					<td>
						<c:choose>
							<c:when test="${empty user.googleId}">
								<small><spring:message code="label.account-settings.not-connected"/></small><br/>
								<a href="${home}/google/link"><spring:message code="label.account-settings.connect-google"/></a>
							</c:when>
							<c:otherwise>
								<i class="icon-ok"></i> <spring:message code="label.account-settings.connected"/><br/>
								<small><a href="${home}/google/unlink"><spring:message code="label.account-settings.disconnect-google"/></a></small>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
		</div>

	</div>
	<hr/>
	<div id="profile-picture" class="row-fluid">
		<h3><spring:message code="label.account-settings.profile-picture"/></h3>
		<div class="media">
			<img class="img-polaroid pull-left" src="${home}/user/photo?id=${user.id}&size=xs" />
			<img class="img-polaroid pull-left" src="${home}/user/photo?id=${user.id}&size=s" />
			<img class="img-polaroid pull-left" src="${home}/user/photo?id=${user.id}&size=m" />
			<img class="img-polaroid pull-left" src="${home}/user/photo?id=${user.id}&size=l" />
			<div class="media-body">
				<div class="control-group">
					<button class="btn btn-info upload-photo" data-url="${home}/user/photo/upload?id=${user.id}"><i class="icon-picture"></i> <spring:message code="photo.change-photo" /></button>
				</div>
				<div class="control-group">
					<button class="btn adjust-photo" data-url="${home}/user/photo/adjust?id=${user.id}"><i class="icon-move"></i> <spring:message code="photo.edit-thumbnail" /></button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="adjust-photo-modal modal hide fade" data-width="760" tabindex="-1"></div>
	<div class="upload-photo-modal modal hide fade" data-width="760" tabindex="-2"></div>

</div>

<app:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			opentides3.jsonForm($('#basic-info form'));
			opentides3.jsonForm($('#change-password form'));
		})
		.on("click", '.adjust-photo', opentides3.showAdjustPhoto)
		.on("click", '.upload-photo', opentides3.showUploadPhoto);;
	</script>
</app:footer>