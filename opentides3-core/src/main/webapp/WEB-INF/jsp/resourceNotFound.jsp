<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header pageTitle="label.error-page" />

	<div class="alert alert-error">
		${exception.customMsg}
	</div>
	
<app:footer/>