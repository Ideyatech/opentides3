<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header pageType="modal-page">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery-jcrop.min.css'/>" />
	<script type="text/javascript" src="<c:url value='/js/jquery-jcrop.min.js'/>"></script>
</app:header>

<form:form commandName="command" enctype="multipart/form-data"
	method="POST" action="/ninja/photo/adjust?id=${command.id}">
	
	
	<div class="span6 original-image-wrapper">
		<img id="original-image" src="${home}/ninja/photo?id=${command.id}&size=original"/>
	</div>
	<div class="span4">
		<input type="hidden" id="x" name="x" value="0"/>
		<input type="hidden" id="y" name="y" value="0"/>
		<input type="hidden" id="x2" name="x2" value="200"/>
		<input type="hidden" id="y2" name="y2" value="200"/>
		<div class="control-group">
			<input type="submit" value="<spring:message code="photo.save-changes" />" class="btn btn-success" />
		</div>
		<hr/>
		<div class="control-group">
			<a href="${home}/ninja/photo/upload?id=${command.id}"><spring:message code="photo.upload-different-photo" /></a>
		</div>
	</div>
</form:form>

<app:footer pageType="modal-page">
	<script type="text/javascript">
		jQuery(function($){
			$('#original-image').Jcrop({
				setSelect: [0, 0, 200, 200],
				allowSelect: false,
				minSize: [ 200, 200 ],
				aspectRatio: 1,
				onChange: updateCoordinates
			});
			
			function updateCoordinates(c) {
				$('#x').val(c.x);
			    $('#y').val(c.y);
			    $('#x2').val(c.x2);
			    $('#y2').val(c.y2);
			};
			
		});
	</script>
</app:footer>