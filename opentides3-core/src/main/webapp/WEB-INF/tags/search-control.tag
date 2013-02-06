<%--
	- search-control.tag
	- Generates search submit controls
	-
--%>
<%@ tag dynamic-attributes="attributes" isELIgnored="false" body-content="empty" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="control-group">
	<label class="control-label">&nbsp;</label>
	<div class="controls">
		<button type="button" class="btn btn-success" data-submit="search"><spring:message code="label.search"/></button>
		<button type="button" class="btn" data-submit="clear"><spring:message code="label.clear" /></button>
	</div>
</div>					

