<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.notification-log" active="notification-log"/>
<jsp:useBean id="currentUser" class="org.opentides.util.SecurityUtil" scope="request"/>

<div id="notification-body">

<ul class="breadcrumb">
  <li><a href="${home}/"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><spring:message code="label.notification-log"/></li>
</ul>

<div id="search-body" class="${search}">

	<div id="search-panel" class="span3">

		<div id="search-panel-inner" data-spy="affix" data-offset-top="60">
			<div class="navbar">
				<div class="navbar-inner">
					<span class="brand"><i class="icon-search"></i><spring:message code="label.search" /></span>
					<a class="show-search-form btn collapsed pull-right hidden-desktop hidden-tablet" data-toggle="collapse" data-target=".search-form">
						<i class="icon-chevron-up"></i>
						<i class="icon-chevron-down"></i>
					</a>
				</div>
			</div>
			<div class="search-form collapse">
				<form:form modelAttribute="searchCommand" id="notification-search" >
					<tides:input label="label.notification.date-from" path="startDate" datepicker="true" appendIcon="icon-calendar"/>
					<tides:input label="label.notification.date-to" path="endDate" datepicker="true" appendIcon="icon-calendar"/>	
					<tides:select label="label.notification.medium" path="medium" items="${mediumList}" itemLabel="value" itemValue="key" select2="true" />
					<tides:select label="label.notification.status" path="status" items="${statusList}" itemLabel="value" itemValue="key" select2="true" />
					<hr/>
					<input type="submit" class="btn btn-info btn-block" data-submit="search" value="<spring:message code="label.search"/>">
					<button type="button" class="btn btn-link btn-block" data-submit="clear"><spring:message code="label.clear" /></button>
				</form:form>
			</div>
		</div>
	</div>
	
	<div id="results-panel" class="span9">
	
		<div id="message-panel" class="row-fluid">
			<div class="status" data-display-pagelinks="false" data-display-summary="true" data-summary-message='
				<spring:message code="message.displaying-x-of-y" arguments="#start,#end,#total,records"/>
			'>
				<tides:status results="${results}" />
			</div>
		</div>
		
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
						<script type="text/template" class="template">
	                		<tr id="notification-row-{{id}}" data-id="{{id}}">
								<td>{{notifyDate}}</td>
								<td>{{recipientDisplay}}</td>
								<td>{{&messageDisplay}}</td>
								<td>{{status}} {{remarks}}</td>
							</tr>
						</script>
						<c:forEach items="${results.results}" var="record" varStatus="status">
							<tr id="notification-${record.id}" data-id="${record.id}">
								<td>
								<fmt:formatDate value="${record.notifyDate}" pattern="MM/dd/yyyy hh:mm" var="notifyDate"/>
								${notifyDate}
								</td>
								<td>${record.recipientDisplay}</td>
								<td>${record.messageDisplay}</td>
								<td>${record.status}. ${record.remarks}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>	
			</div>
		</div>

		<div class="paging clearfix">
			<tides:paging results="${results}"/>
		</div>
	</div>
	
</div>

</div>

<tides:footer>
	<script type="text/javascript">
		$(document).ready(function() {
//			$('body').tooltip({selector: '.edit-action, .remove-action'});
			$("#notification-body").RESTful();
		});
	</script>
</tides:footer>
