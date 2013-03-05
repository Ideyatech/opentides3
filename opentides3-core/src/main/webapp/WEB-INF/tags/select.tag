<%--
	- select.tag
	- Generates form select element
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="itemLabel" required="false" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="itemValue" required="false" type="java.lang.String" %>
<%@ attribute name="items" required="false" type="java.util.Collection" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="select2" required="false" type="java.lang.Boolean" %>
<%@ attribute name="multiple" required="false" type="java.lang.Boolean" %>

<div class="control-group">

	<form:label path="${path}" cssClass="control-label" cssErrorClass="highlight-error">
		<spring:message code="${label}"/>
		<c:if test="${required}">
			<span class="required"><spring:message code="label.required-field" /></span>
		</c:if>
	</form:label>
	
	<div class="controls">
		
		<form:select path="${path}" required="${required ? 'required' : ''}" multiple="${multiple}" cssClass="${cssClass}" style="${select2 ? 'width: 220px;':''}">
			<c:if test="${select2 and not multiple}">
				<option></option>
			</c:if>
			<c:if test="${not empty items}">
				<form:options items="${items}" itemLabel="${itemLabel}" itemValue="${itemValue}"/>
			</c:if>
		</form:select>
	</div>
	
</div>

<c:if test="${select2}">
	<script type="text/javascript">
		$(document).ready(function() {
			$('#${path}').select2({
				placeholder: '<spring:message code="label.select-one"/>'
			});
		});
	</script>
</c:if>