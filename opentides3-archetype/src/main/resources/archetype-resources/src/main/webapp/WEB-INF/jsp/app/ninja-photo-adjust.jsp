#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" uri="http://www.ideyatech.com/tides"%>

<form:form id="ninja-adjust-photo" commandName="command" enctype="multipart/form-data"
	method="POST" action="/ninja/photo/adjust?id=${symbol_dollar}{command.id}">
	
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">&times;</button>
		<h3><spring:message code="photo.adjust-photo" /></h3>
	</div>
	
	<div class="modal-body">
		<div class="original-image-wrapper" style="">
			<img id="original-image" src="${symbol_dollar}{home}/ninja/photo?id=${symbol_dollar}{command.id}&size=original"/>
		</div>
		<input type="hidden" id="x" name="x" value="0"/>
		<input type="hidden" id="y" name="y" value="0"/>
		<input type="hidden" id="x2" name="x2" value="200"/>
		<input type="hidden" id="y2" name="y2" value="200"/>
		<input type="hidden" id="rw" name="rw" value="0"/>
	</div>
	
	<div class="modal-footer">
		<input type="button" class="btn btn-link switch-modal"
			data-url="${symbol_dollar}{home}/ninja/photo/upload?id=${symbol_dollar}{command.id}"
			value="<spring:message code="photo.upload-different-photo" />"
		/>
		<input id="adjust-photo-submit" type="submit" value="<spring:message code="photo.save-changes" />" class="btn btn-success" />
	</div>
	
</form:form>

<script type="text/javascript">
	${symbol_dollar}('${symbol_pound}adjust-photo-submit').on("click", function(e){
		e.preventDefault();
		${symbol_dollar}('${symbol_pound}rw').val(${symbol_dollar}('${symbol_pound}original-image').width());
		${symbol_dollar}(this).closest('form').submit();
	});
	
	${symbol_dollar}('.switch-modal').on("click", opentides3.showUploadPhoto);
	
	opentides3.jsonForm(${symbol_dollar}('${symbol_pound}ninja-adjust-photo'), function() {
		${symbol_dollar}('${symbol_pound}ninja-adjust-photo').find('.switch-modal').click();
	});
</script>