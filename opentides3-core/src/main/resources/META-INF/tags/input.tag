<%--
	- input.tag
	- Generates form input element
--%>
<%@ tag body-content="empty" dynamic-attributes="dAttrs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="textarea" required="false" type="java.lang.Boolean" %>
<%@ attribute name="type" required="false" type="java.lang.String" %>
<%@ attribute name="placeholder" required="false" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="appendText" required="false" type="java.lang.String" %>
<%@ attribute name="prependText" required="false" type="java.lang.String" %>
<%@ attribute name="appendIcon" required="false" type="java.lang.String" %>
<%@ attribute name="prependIcon" required="false" type="java.lang.String" %>

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
	
		<div class="
			${not empty prependText or not empty prependIcon ? 'input-prepend': ''}
			${not empty appendText or not empty appendIcon ? 'input-append': ''}">
			
			<c:if test="${not empty prependText}">
				<span class="add-on">${prependText}</span>
			</c:if>
			<c:if test="${not empty prependIcon}">
				<span class="add-on"><i class="${prependIcon}"></i></span>
			</c:if>
		
			<c:choose>
				<c:when test="${textarea}">
					<form:textarea path="${path}"
				
						type="${empty type ? 'text': type }"
						placeholder="${placeholder}"
						cssClass="${cssClass}"
						
						/>
				</c:when>
				<c:otherwise>
					<form:input path="${path}"
				
						type="${empty type ? 'text': type }"
						placeholder="${placeholder}"
						cssClass="${cssClass}"
						/>
				</c:otherwise>
			</c:choose>
			
			<c:if test="${not empty appendText}">
				<span class="add-on">${appendText}</span>
			</c:if>
			<c:if test="${not empty appendIcon}">
				<span class="add-on"><i class="${appendIcon}"></i></span>
			</c:if>
		</div>
		
	</div>
	
</div>
