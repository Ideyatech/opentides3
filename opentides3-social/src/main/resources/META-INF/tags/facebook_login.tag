<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="labelCode" required="false" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="iconClass" required="false" type="java.lang.String" %>

<!-- Initialize CSS. -->
<c:set var="btnClass" value="btn btn-primary btn-block"/>
<c:if test="${not empty cssClass}">
	<c:set var="btnClass" value="${cssClass}" />
</c:if>

<!-- Initialize Icon Class. -->
<c:set var="icon" value="icon-facebook-sign"/>
<c:if test="${not empty iconClass}">
	<c:set var="icon" value="${iconClass}" />
</c:if>

<!-- Initialize Label Code. -->
<c:set var="code" value="message.login-with-facebook"/>
<c:if test="${not empty labelCode}">
	<c:set var="code" value="${labelCode}" />
</c:if>

<div class="control-group">
	<button class="${btnClass}" onclick="window.location.href='${home}/facebook/connect'">
	<i class="${icon}"></i><spring:message code="${code}" text="${code}"/></button>
</div>