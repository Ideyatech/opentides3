<%--
	- social_login.tag
	- JSP Tag that handles logging in and displaying of Social Media button
	
	- @rabanes
--%>

<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ attribute name="socialType" required="true" type="java.lang.String" %>
<%@ attribute name="labelCode" required="false" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="iconClass" required="false" type="java.lang.String" %>

<!-- NOTE : toLowercase the socialType -->
<!-- Values must be : FACEBOOK, GOOGLE, and TWITTER -->
<c:set var="currentSocial" value="${fn:toLowerCase(socialType)}" />

<div class="control-group">
	<button class="${not empty cssClass ? cssClass : 'btn btn-block btn-primary'}" onclick="window.location.href='${home}/${currentSocial}/connect'">
	
	<c:set var="btnIcon" value="icon-${currentSocial}-sign"/>
	<c:set var="label" value="message.login-with-${currentSocial}"/>
	<i class="${not empty iconClass ? iconClass : btnIcon}"></i><spring:message code="${not empty labelCode ? labelCode : label}" /></button>
</div>