<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header title_webpage="label.error-page" /> 
<div class="container-fluid">
<h3>${exception.message}</h3>
${exception}
</div>
<app:footer/>