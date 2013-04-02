<%--
	- ninja-details.jsp
	- Displays complete Ninja details
	-
	- @author - ajalbaniel
--%>
<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="label.ninja" active="ninja"/>
	
<ul class="breadcrumb">
  <li><a href="${home}"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><a href="${home}/ninja"><spring:message code="label.ninja"/></a> <span class="divider">/</span></li>
  <li>${ninja.completeName}</li>
</ul>

<div class="row-fluid" style="margin-bottom: 20px;">
	<div class="span2">
		<img class="img-polaroid" src="${home}/ninja/photo?id=${ninja.id}"/>
	</div>
	<div class="span10">
		<h1>${ninja.completeName}</h1>	
	</div>
</div>

<table class="table table-striped">
	<tr>
		<td><spring:message code="label.ninja.firstName"/></td>
		<td>${ninja.firstName}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.lastName"/></td>
		<td>${ninja.lastName}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.email"/></td>
		<td><a href="mailto:#">${ninja.email}</a></td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.age"/></td>
		<td>${ninja.age}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.score"/></td>
		<td>${ninja.score}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.joinDate"/></td>
		<td><fmt:formatDate value="${record.joinDate}" pattern="MM/dd/yyyy"/></td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.gender"/></td>
		<td>${ninja.gender}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.status"/></td>
		<td>${ninja.status.value}</td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.skills"/></td>
		<td><c:forEach items="${ninja.skillSet}" var="skill">
			<span class="label label-info">${skill.value}</span>
		</c:forEach></td>
	</tr>
	<tr>
		<td><spring:message code="label.ninja.active"/></td>
		<td>
			<i class="${ninja.active ? 'icon-ok' : 'icon-remove'}"></i>
		</td>
	</tr>
</table>

<!-- Comments Area -->

<div>

	<h4>Comments</h4>

	<c:if test="${empty ninja.comments}">
		No comments
	</c:if>
	<c:forEach items="${ninja.comments}" var="comment">
	
		<div class="row-fluid">
			<div class="span1">
				<img class="img-circle" src="${home}/organization/users/photo?id=${comment.author.id}&size=s"/>
			</div>
			<div class="span11">
				<blockquote>
					<p>${comment.text}</p>
					<small>${comment.author.completeName}</small>
				</blockquote>
			</div>
		</div>
		
	</c:forEach>

</div>

<app:footer/>