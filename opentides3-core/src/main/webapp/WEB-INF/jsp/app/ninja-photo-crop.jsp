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
	method="POST" action="/ninja/photo/crop?id=${command.id}">
	
	<img id="original-image" src="${home}/ninja/photo?id=${command.id}&size=original"/>

	<input type="text" id="x" name="x" value="0"/>
	<input type="text" id="x2" name="x2" value="0"/>
	<input type="text" id="y" name="y" value="200"/>
	<input type="text" id="y2" name="y2" value="200"/>
	<input type="submit" value="Save Changes" class="btn btn-success btn-large" />
	
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