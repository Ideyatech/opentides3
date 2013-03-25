<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.form-builder" active="form-builder"/>

<div class="span6" style="margin-left: 0;">
	<div style="padding: 18px 18px 0 18px; border: 1px solid #ccc;">
		<div class="component">
			<legend>Form Name</legend>
		</div>
		<div style="min-height: 100px;">
			<!-- Form fields here -->
		</div>
		<div class="form-actions">
			<div class="pull-right">
				<button type="button" class="btn btn-link"><spring:message code="label.close" /></button>
				<input type="submit" class="btn btn-info" value="<spring:message code="label.save-and-new" />" />
				<input type="submit" class="btn btn-success" value="<spring:message code="label.save" />" />
			</div>
		</div>
	</div>
</div>

<div class="span6">
	Drag and drop components from here
</div>

<app:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
</app:footer>