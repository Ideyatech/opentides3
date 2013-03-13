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
		<input type="file" id="photo" name="photo" class="hide"/>
		<div class="input-append">
		   <input id="photo-path" class="input-large" type="text" readonly>
		   <a class="btn" id="browse-photo">Browse</a>
		</div>
		
		<hr/>
		<input type="submit" value="Change Photo" class="btn btn-success btn-large" />
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
