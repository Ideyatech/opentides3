#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%--
	- ninja-details.jsp
	- Displays complete Ninja details
	-
	- @author - AJ
--%>
<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="label.ninja" active="ninja"/>
	
<ul class="breadcrumb">
  <li><a href="${symbol_dollar}{home}"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><a href="${symbol_dollar}{home}/ninja"><spring:message code="label.ninja"/></a> <span class="divider">/</span></li>
  <li>${symbol_dollar}{ninja.completeName}</li>
</ul>

<div class="row-fluid" style="margin-bottom: 20px;">
	<div class="span2">
		<img class="img-polaroid" src="${symbol_dollar}{home}/ninja/photo?id=${symbol_dollar}{ninja.id}"/>
	</div>
	<div class="span10">
		<h2>${symbol_dollar}{ninja.completeName}</h2>	
	</div>
</div>

<table class="table table-striped">
	<tr>
		<td><spring:message code="label.ninja.firstName"/></td>
		<td>${symbol_dollar}{ninja.firstName}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.lastName"/></td>
		<td>${symbol_dollar}{ninja.lastName}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.email"/></td>
		<td><a href="mailto:${symbol_pound}">${symbol_dollar}{ninja.email}</a></td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.age"/></td>
		<td>${symbol_dollar}{ninja.age}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.score"/></td>
		<td>${symbol_dollar}{ninja.score}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.tags"/></td>
		<td><c:forEach items="${symbol_dollar}{ninja.tags}" var="tag">
			<span class="label label-info">${symbol_dollar}{tag.tagText}</span>
		</c:forEach></td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.joinDate"/></td>
		<td><fmt:formatDate value="${symbol_dollar}{ninja.joinDate}" pattern="MM/dd/yyyy"/></td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.gender"/></td>
		<td>${symbol_dollar}{ninja.gender}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.status"/></td>
		<td>${symbol_dollar}{ninja.status.value}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.skills"/></td>
		<td><c:forEach items="${symbol_dollar}{ninja.skillSet}" var="skill">
			<span class="label label-info">${symbol_dollar}{skill.value}</span>
		</c:forEach></td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.active"/></td>
		<td>
			<i class="${symbol_dollar}{ninja.active ? 'icon-ok' : 'icon-remove'}"></i>
		</td>
	</tr>
</table>

<!-- Comments Area -->

<app:comments commentList="${symbol_dollar}{ninja.comments}" action="/ninja/comment" commentableId="${symbol_dollar}{ninja.id}"/>

<app:footer/>