<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="n" uri="http://www.ideyatech.com/n"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.notification" active="notification"/>
<jsp:useBean id="currentUser" class="org.opentides.util.SecurityUtil" scope="request"/>

<div id="notification-body">

<ul class="breadcrumb">
  <li><a href="${home}/"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><spring:message code="label.notification.my-notifications"/></li>
</ul>

<div id="search-body" class="${search}">

	<div id="results-panel" class="span9">
		
		<div class="clear"></div>
		
		<div class="table-wrapper-2 overflow-hidden">
			<div class="table-wrapper">
				<table id="notification-results" class="footable table-bordered table-striped table-hover table-condensed" data-page="${results.currPage}" >
					<thead>
						<tr class="table-header">
							<!-- Define headers here -->
							<th data-field-name="date"><spring:message code="label.notification.date" /></th>
							<th data-field-name="recipient"><spring:message code="label.notification.recipient" /></th>							
							<th data-field-name="message"><spring:message code="label.notification.message" /></th>		
							<th data-field-name="status"><spring:message code="label.notification.status" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${notifications}" var="record" varStatus="status">
							<tr id="notification-${record.id}" data-id="${record.id}">
								<td>
								<fmt:formatDate value="${record.createDate}" pattern="MM/dd/yyyy hh:mm" var="createDate"/>
								<fmt:formatDate value="${record.updateDate}" pattern="MM/dd/yyyy hh:mm" var="updateDate"/>
								${createDate}
								</td>
								<td>${record.recipientDisplay}</td>
								<td>${record.messageDisplay}</td>
								<td>${record.status} since ${updateDate}. ${record.remarks}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>	
			</div>
		</div>
	</div>
	
</div>

</div>

<tides:footer/>
