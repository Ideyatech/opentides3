<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="app.spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<form:form id="ninja-upload-photo" commandName="command" enctype="multipart/form-data"
		method="POST" action="/ninja/photo/upload?id=${command.id}">
		
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">&times;</button>
		<h3><app.spring:message code="photo.change-photo" /></h3>
	</div>
	
	<div class="modal-body">

		<div class="message-container">
		</div>
		
		<div class="span2">
			<div class="current-image-wrapper">
				<img class="img-polaroid current-image" src="${home}/ninja/photo?id=${command.id}&size=l"/>
			</div>
		</div>
		<div class="span5">
			<br/>
			<input type="file" id="photo" name="photo" class="hide" accept="image/*"/>
			
			<div class="control-group">
				<div class="input-append">
				   <input id="photo-path" class="input-large" type="text" readonly>
				   <a class="btn" id="browse-photo"><app.spring:message code="photo.browse" /></a>
				</div>
				<small><app.spring:message code="photo.upload-instructions" /></small>
			</div>
		</div>
		
	</div>

	<div class="modal-footer">
		<input type="button" class="btn btn-link switch-modal"
			data-url="${home}/ninja/photo/adjust?id=${command.id}"
			value="<app.spring:message code="photo.edit-thumbnail" />" />
		<input type="button" id="start-upload" value="<app.spring:message code="photo.change-photo" />" class="btn btn-success" />
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

	opentides3.jsonForm($('#ninja-upload-photo'), function(){
		$('#ninja-upload-photo').find('.switch-modal').click();
	});
	
	$(document).ready().on('click', '#start-upload', function(){
		$('#ninja-upload-photo').modal('loading');
		$('#ninja-upload-photo').submit();
	});
</script>