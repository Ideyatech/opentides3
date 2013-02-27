<%--
	- select.tag
	- Generates form select element
--%>
<%@ tag body-content="tagdependent" dynamic-attributes="dAttrs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="defaultToNull" required="false" type="java.lang.Boolean" %>

<c:forEach items="${dAttrs}" var="attr">
	<c:set var="attrs" value='${attrs} ${attr.key}="${attr.value}"'/>
</c:forEach>

<div class="control-group">

	<form:label path="${path}" cssClass="control-label" cssErrorClass="highlight-error">
		<spring:message code="${label}"/>
		<c:if test="${required}">
		</c:if>
	</form:label>
	
	<div class="controls">
		
		<select name="${path}" id="${path}"
		${required ? 'required="required"' : ''}
		
		${attrs}>
			<c:if test="${defaultToNull}">
				<option value=""><spring:message code="label.select-one" /></option>
			</c:if>
			
			<jsp:doBody/>
			
		</select>
		
	</div>
	
</div>
