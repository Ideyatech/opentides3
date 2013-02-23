<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header pageTitle="label.error-page" />

	<div id="error-body" class="span12 well">	
		<h4>${exception.message}</h4>
		${exception}
	</div>

<app:footer/>