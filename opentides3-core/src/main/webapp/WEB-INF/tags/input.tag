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
		<form:input path="${path}" />
	</div>
	<c:if test="${required}">
	<div class="pull-right">
		<span class="required">*</span>
		<span class="bold"><spring:message code="label.required-field"/></span>			
	</div>
	</c:if>
</div>
