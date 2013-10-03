<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<form:form id="upload-photo" commandName="command" enctype="multipart/form-data"
		method="POST" action="${home}/image/upload/${className}/${classId}">
	
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
				<img class="img-polaroid current-image" src="${home}/image/${imageId}"/>
			</div>
		</div>
		<div class="span5">
			<input type="file" id="photo" name="attachment" style="visibility: hidden;" accept="image/*"/>
			<input type="hidden" name="isPrimary" value="true"/>
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
		<input type="button" class="btn btn-link switch-modal" id="switchToAdjust"
			data-url="${home}/image/adjust?imageId=${imageId}&className=${className}&classId=${classId}"
			value="<spring:message code="photo.edit-thumbnail" />" />
		<input type="button" id="start-upload" value="<spring:message code="photo.change-photo" />" class="btn btn-success" />
	</div>
	
	<script type="text/template" class="adjust-data-url">
		${home}/image/adjust?imageId={{imageId}}&className=${className}&classId=${classId}
	</script>
	
</form:form>

<script type="text/javascript">
	$('#browse-photo, #photo-path').on("click", function(){
		$('#photo').click();
	});
	
	$('.switch-modal').on("click", opentides3.showAdjustPhoto);
	
	$('#photo').on("change", function() {
	   $('#photo-path').val($(this).val());
	});

	opentides3.jsonForm($('#upload-photo'), function(data){
		//$('#upload-photo').find('.switch-modal').click();
		var newDataUrl = opentides3.template($('script.adjust-data-url').html(), data);
		$('#switchToAdjust').data('url', newDataUrl);
		$('#upload-photo').find('.switch-modal').click();
	});
	
	$(document).ready().on('click', '#start-upload', function(){
		$('#upload-photo').modal('loading');
		$('#upload-photo').submit();
	});
</script>