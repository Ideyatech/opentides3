<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<form:form id="ninja-upload-photo" commandName="command" enctype="multipart/form-data"
		method="POST" action="/ninja/photo/upload?id=${command.id}">
		
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">&times;</button>
		<h3><spring:message code="photo.change-photo" /></h3>
	</div>
	
	<div class="modal-body">

		<spring:bind path="command.*">
			<c:if test="${status.error}">
				<div class="alert alert-error">
					<form:errors path="*" />
				</div>
			</c:if>
		</spring:bind>
		
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
				   <a class="btn" id="browse-photo"><spring:message code="photo.browse" /></a>
				</div>
				<small><spring:message code="photo.upload-instructions" /></small>
			</div>
		</div>
		
	</div>

	<div class="modal-footer">
		<button class="btn btn-link" onclick="window.location.href='${home}/ninja/photo/adjust?id=${command.id}'">
			<spring:message code="photo.edit-thumbnail" />
		</button>
		<input type="submit" value="<spring:message code="photo.change-photo" />" class="btn btn-success" />
	</div>
	
</form:form>

<script type="text/javascript">
	$('#browse-photo, #photo-path').on("click", function(){
		$('#photo').click();
	});
	$('#photo').on("change", function() {
	   $('#photo-path').val($(this).val());
	});
	
	$('#ninja-upload-photo').ajaxForm(function() { 
        alert("You have uploaded the photo via AJAX! Nice!"); 
    }); 
	
</script>