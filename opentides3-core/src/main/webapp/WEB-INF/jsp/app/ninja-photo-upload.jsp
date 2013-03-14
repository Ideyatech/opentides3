<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header pageType="modal-page" />

<form:form commandName="command" enctype="multipart/form-data"
	method="POST" action="/ninja/photo/upload?id=${command.id}">
	
	<div class="span2">
		<img class="img-polaroid" src="${home}/ninja/photo?id=${command.id}&size=l"/>
	</div>
	<div class="span4">
		<input type="file" id="photo" name="photo" class="hide" accept="image/*"/>
		
		<div class="control-group">
			<div class="input-append">
			   <input id="photo-path" class="input-large" type="text" readonly>
			   <a class="btn" id="browse-photo"><spring:message code="photo.browse" /></a>
			</div>
			<small><spring:message code="photo.upload-instructions" /></small>
		</div>
		<div class="control-group">
			<input type="submit" value="<spring:message code="photo.change-photo" />" class="btn btn-success" />
		</div>
		<hr/>
		<div class="control-group">
			<a href="${home}/ninja/photo/adjust?id=${command.id}"><spring:message code="photo.adjust-current-photo" /></a>
		</div>
	</div>
	
</form:form>

<app:footer pageType="modal-page">
	<script type="text/javascript">
		$('#browse-photo, #photo-path').on("click", function(){
			$('#photo').click();
		});
		$('#photo').on("change", function() {
		   $('#photo-path').val($(this).val());
		});
	</script>
</app:footer>
