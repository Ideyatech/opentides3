<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<app:header pageTitle="label.404-page" />

	<div class="alert alert-error">
		<spring:message code="label.404-note" />
	</div>
	
<app:footer/>