<%--
	- input.tag
	- Generates form input element
	- @param
--%>

<%@ tag dynamic-attributes="attributes" isELIgnored="false" body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>

<div class="control-group">
	<form:label path="${path}" cssClass="control-label"><spring:message code="${label}"/></form:label>
	<div class="controls">
		<form:password path="${path}" />
	</div>
<c:if test="${required}">
	<span class="required">*</span>
</c:if>
</div>
