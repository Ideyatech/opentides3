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
<%@ attribute name="itemValue" required="false" type="java.lang.String" %>
<%@ attribute name="items" required="false" type="java.util.Collection" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
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
		
		<form:select path="${path}" multiple="${multiple}" cssClass="${cssClass} ${select2 ? 'ot-select2':''}" style="${select2 ? 'width: 220px;':''}">
			<c:if test="${select2 and not multiple}">
				<option></option>
			</c:if>
			<c:if test="${not required and not select2 }">
				<option></option>
			</c:if>
			<c:if test="${not empty items}">
				<c:choose>
				<c:when test="${not empty itemLabel and not empty itemValue}">
				<form:options items="${items}" itemLabel="${itemLabel}" itemValue="${itemValue}"/>
				</c:when>
				<c:when test="${not empty itemValue}">
				<form:options items="${items}" itemValue="${itemValue}"/>
				</c:when>
				<c:when test="${not empty itemLabel}">
				<form:options items="${items}" itemLabel="${itemLabel}"/>
				</c:when>
				<c:otherwise>
				<form:options items="${items}" />
				</c:otherwise>
				</c:choose>
			</c:if>
		</form:select>
	</div>
	
</div>

<c:if test="${select2}">
	<script type="text/javascript">
		$(document).ready(function() {
			$('#${path}.ot-select2').select2({
				placeholder: '<spring:message code="label.select-one"/>'
			});
		});
	</script>
</c:if>