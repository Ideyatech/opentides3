<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header pageTitle="label.error-page" />

	<div class="alert alert-error">
		<h4>${exception.message}</h4>
		${exception}
	</div>
	
<app:footer/>