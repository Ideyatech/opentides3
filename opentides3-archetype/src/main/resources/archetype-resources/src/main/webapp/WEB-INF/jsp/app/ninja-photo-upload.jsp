#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" uri="http://www.ideyatech.com/tides"%>

<form:form id="ninja-upload-photo" commandName="command" enctype="multipart/form-data"
		method="POST" action="/ninja/photo/upload?id=${symbol_dollar}{command.id}">
		
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
				<img class="img-polaroid current-image" src="${symbol_dollar}{home}/ninja/photo?id=${symbol_dollar}{command.id}&size=l"/>
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
			data-url="${symbol_dollar}{home}/ninja/photo/adjust?id=${symbol_dollar}{command.id}"
			value="<spring:message code="photo.edit-thumbnail" />" />
		<input type="button" id="start-upload" value="<spring:message code="photo.change-photo" />" class="btn btn-success" />
	</div>
	
</form:form>

<script type="text/javascript">
	${symbol_dollar}('${symbol_pound}browse-photo, ${symbol_pound}photo-path').on("click", function(){
		${symbol_dollar}('${symbol_pound}photo').click();
	});
	
	${symbol_dollar}('.switch-modal').on("click", opentides3.showAdjustPhoto);
	
	${symbol_dollar}('${symbol_pound}photo').on("change", function() {
	   ${symbol_dollar}('${symbol_pound}photo-path').val(${symbol_dollar}(this).val());
	});

	opentides3.jsonForm(${symbol_dollar}('${symbol_pound}ninja-upload-photo'), function(){
		${symbol_dollar}('${symbol_pound}ninja-upload-photo').find('.switch-modal').click();
	});
	
	${symbol_dollar}(document).ready().on('click', '${symbol_pound}start-upload', function(){
		${symbol_dollar}('${symbol_pound}ninja-upload-photo').modal('loading');
		${symbol_dollar}('${symbol_pound}ninja-upload-photo').submit();
	});
</script>