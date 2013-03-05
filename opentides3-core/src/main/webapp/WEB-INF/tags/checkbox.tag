<%--
	- checkbox.tag
	- Generates form input element
--%>
<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>

<div class="control-group checkbox-wrapper">

	<spring:message code="${label}" var="checkboxLabel" />
	<form:checkbox path="${path}" label="${checkboxLabel}"/>

</div>