<%--
	- input.tag
	- Generates form input element
	- @param
--%>
<%@ tag body-content="empty" dynamic-attributes="dAttrs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="type" required="false" type="java.lang.String" %>
<%@ attribute name="appendSign" required="false" type="java.lang.String" %>
<%@ attribute name="prependSign" required="false" type="java.lang.String" %>



<c:forEach items="${dAttrs}" var="attr">
	<c:set var="attrs" value='${attrs} ${attr.key}="${attr.value}"'/>
</c:forEach>

<div class="control-group">

	<form:label path="${path}" cssClass="control-label" cssErrorClass="highlight-error">
		<spring:message code="${label}"/>
		<c:if test="${required}">
			<span class="required"><spring:message code="label.required-field" /></span>
		</c:if>
	</form:label>
	
	<div class="controls">
	
		<div class="${not empty prependSign ? 'input-prepend': ''} 
					${not empty appendSign ? 'input-append': ''}">
			<c:if test="${not empty prependSign}">
				<span class="add-on">${prependSign}</span>
			</c:if>
		
			<input name="${path}" id="${path}"
			
			type="${empty type ?'text': type }"
			
			${required ? 'required="required"' : ''}
			
			${attrs} />
			
			<c:if test="${not empty appendSign}">
				<span class="add-on">${appendSign}</span>
			</c:if>
		</div>
		
	</div>
	
</div>
