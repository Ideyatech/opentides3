<%--
	- radio.tag
	- Generates form radio element
--%>
<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="itemLabel" required="false" type="java.lang.String" %>
<%@ attribute name="itemValue" required="false" type="java.lang.String" %>
<%@ attribute name="items" required="false" type="java.util.Collection" %>

<div class="control-group radio-wrapper">

	<form:label path="${path}" cssClass="control-label" cssErrorClass="highlight-error">
		<spring:message code="${label}"/>
		<c:if test="${required}">
			<span class="required"><spring:message code="label.required-field" /></span>
		</c:if>
	</form:label>
	
	<div class="controls">
		<form:radiobuttons path="${path}" itemLabel="${itemLabel}" itemValue="${itemValue}" items="${items}"/>
	
	</div>
</div>