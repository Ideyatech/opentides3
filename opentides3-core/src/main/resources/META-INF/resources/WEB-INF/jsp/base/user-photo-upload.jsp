<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" uri="http://www.ideyatech.com/tides"%>

<form:form id="user-upload-photo" commandName="command" enctype="multipart/form-data"
		method="POST" action="/user/photo/upload?id=${command.id}">
		
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">&times;</button>
		<h3><spring:message code="photo.change-photo" /></h3>
	</div>
	
	<div class="modal-body">

		<div class="message-container">
		</div>
		
		<div class="span2">
			<div class="current-image-wrapper">
				<img class="img-polaroid current-image" src="${home}/user/photo?id=${command.id}&size=l"/>
			</div>
		</div>
		<div class="span5">
			<br/>
			<input type="file" id="photo" name="photo" class="hide" accept="image/*"/>
			
			<div class="control-group">
				<div class="input-append">
				   <input id="photo-path" class="input-large" type="text" readonly>
				   <a class="btn" id="browse-photo"><spring:message code="photo.browse" /></a>
				</div>
				<small><spring:message code="photo.upload-instructions" /></small>
			</div>
		</div>
		
	</div>

	<div class="modal-footer">
		<input type="button" class="btn btn-link switch-modal"
			data-url="${home}/user/photo/adjust?id=${command.id}"
			value="<spring:message code="photo.edit-thumbnail" />" />
		<input type="button" id="start-upload" value="<spring:message code="photo.change-photo" />" class="btn btn-success" />
	</div>
	
</form:form>

<script type="text/javascript">
	$('#browse-photo, #photo-path').on("click", function(){
		$('#photo').click();
	});
	
	$('.switch-modal').on("click", opentides3.showAdjustPhoto);
	
	$('#photo').on("change", function() {
	   $('#photo-path').val($(this).val());
	});

	opentides3.jsonForm($('#user-upload-photo'), function(){
		$('#user-upload-photo').find('.switch-modal').click();
	});
	
	$(document).ready().on('click', '#start-upload', function(){
		$('#user-upload-photo').modal('loading');
		$('#user-upload-photo').submit();
	});
</script>