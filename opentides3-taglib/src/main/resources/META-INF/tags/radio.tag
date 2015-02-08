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
		<c:if test="${not empty items}">
			<c:choose>
			<c:when test="${not empty itemLabel and not empty itemValue}">
			<form:radiobuttons path="${path}" itemLabel="${itemLabel}" itemValue="${itemValue}" items="${items}"/>
			</c:when>
			<c:when test="${not empty itemValue}">
			<form:radiobuttons path="${path}" items="${items}" itemValue="${itemValue}"/>
			</c:when>
			<c:when test="${not empty itemLabel}">
			<form:radiobuttons path="${path}" items="${items}" itemLabel="${itemLabel}"/>
			</c:when>
			<c:otherwise>
			<form:radiobuttons path="${path}" items="${items}"/>
			</c:otherwise>
			</c:choose>
		</c:if>
	</div>
</div>