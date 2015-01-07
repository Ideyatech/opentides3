<%--
	- input.tag
	- Generates form input element
--%>
<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="modelAttribute" required="false" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="label" required="false" type="java.lang.String" %>

<c:if test="${not empty label}">
<div class="control-group">
<label class="control-label" for="${path}">
	<spring:message code="${label}"/>
</label>
<div class="controls">
</c:if>
<c:choose>
	<c:when test="${not empty modelAttribute}">
		<c:set var="cpath" value="${modelAttribute}.${path}"/>		
	</c:when>
	<c:otherwise>
		<c:set var="cpath" value="${path}"/>		
	</c:otherwise>
</c:choose>
	<spring:bind path="${cpath}">
	<span name="${status.expression}">
		<c:if test="${not empty cssClass}"> class="${cssClass}"> </c:if>
		<c:out value="${status.value} "/>
	</span>
	</spring:bind>
<c:if test="${not empty label}">
</div>
</div>
</c:if>