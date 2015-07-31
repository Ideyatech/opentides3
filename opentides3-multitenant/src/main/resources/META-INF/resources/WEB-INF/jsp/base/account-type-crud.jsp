<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.account-type" active="account-type"/>

<div id="account-type-body">

<ul class="breadcrumb">
  <li><a href="${home}/"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><spring:message code="label.account-type"/></li>
</ul>

<div id="search-body" class="${search}">
	
	<div id="results-panel" class="span9">
		
		<div id="message-panel" class="row-fluid">
			<button id="account-type-add" class="btn btn-info add-action">
				<i class="icon-plus-sign icon-white"></i>
				<spring:message code="label.account-type.add" />
			</button>
			<div class="status" data-display-pagelinks="false" data-display-summary="true" data-summary-message='
				<spring:message code="message.displaying-x-of-y" arguments="#start,#end,#total,records"/>
			'>
				<tides:status results="${results}" />
			</div>
		</div>
		
		<div class="clear"></div>
		
		<div class="table-wrapper-2 overflow-hidden">
			<div class="table-wrapper">
				<table id="account-type-results" class="footable table-bordered table-striped table-hover table-condensed" data-page="${results.currPage}" >
					<thead>
						<tr class="table-header">
							<th data-class="expand" data-field-name="name"><spring:message code="label.account-type.name" /></th>
							<th data-hide="phone" data-field-name="description"><spring:message code="label.account-type.description" /></th>
							<th data-field-name="subscription"><spring:message code="label.account-type.subscription" /></th>
							<th data-field-name="active"><spring:message code="label.account-type.active" /></th>
							<th data-field-name="ot3-controls"></th>
						</tr>
					</thead>
					<tbody>
						<script type="text/template" class="template">
	                		<tr data-id="{{id}}">
								<td>
									<a href="${home}/system/account-type/view/{{id}}">
										{{name}}
									</a>
								</td>
								<td>{{description}}</td>
								<td>{{subscription}}</td>
								<td>{{activeDisplay}}</td>
								<td>
									<i class='icon-pencil edit-action' data-id='{{id}}' data-title="<spring:message code="label.edit" />"></i>
									<i class='icon-trash remove-action' data-id='{{id}}' data-title="<spring:message code="label.delete" />"></i>
								</td>
							</tr>
						</script>
						<c:forEach items="${results.results}" var="record" varStatus="status">
							<tr data-id='${record.id}'>
								<td>
									<a href="${home}/system/account-type/view/${record.id}">
										<c:out value="${record.name}" />
									</a>
								</td>
								<td><c:out value="${record.description}" /></td>
								<td><c:out value="${record.subscription}" /></td>
								<td><c:out value="${record.activeDisplay}" /></td>
								<td>
									<i class='icon-pencil edit-action' data-id='${record.id}' data-title="<spring:message code="label.edit" />"></i>
									<i class='icon-trash remove-action' data-id='${record.id}' data-title="<spring:message code="label.delete" />"></i>
								</td>
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

<div id="form-body" class="modal fade ${form}">

	<div id="form-panel" >
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="${add}"><spring:message code="label.account-type.add" /></h4>
			<h4 class="${update}"><spring:message code="label.account-type.update" /></h4>
		</div>

		<form:form modelAttribute="formCommand" id="account-type-form">
			<div class="modal-body">
				<div class="message-container"></div>
				<tides:input path="name" label="label.account-type.name" required="true"/>
				<tides:input textarea="true" path="description" label="label.account-type.description" />
				<tides:input path="amount" label="label.account-type.amount" />
				<tides:select path="period" label="label.account-type.period" items="${periodList}" required="true"/>
				<tides:checkbox path="active" label="label.account-type.active"/>
				
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-link" data-dismiss="modal"><spring:message code="label.close" /></button>
				<input type="submit" class="btn btn-info  ${add}" data-form-display="add" data-submit="save-and-new" value="<spring:message code="label.save-and-new" />" />
				<input type="submit" class="btn btn-success" data-submit="save" value="<spring:message code="label.save" />" />
				<input type="hidden" name="id" />
			</div>
		</form:form>
	</div>
	
</div>

<div id="view-body" class="page ${view}">
	
	<div class="row-fluid" style="margin-bottom: 20px;">
		<div class="span10">
			<h2>${formCommand.name}</h2>	
		</div>
	</div>
	
	<table class="table table-striped">
		<tr>
			<td><spring:message code="label.account-type.name"/></td>
			<td>${formCommand.name}</td>
		</tr>
		<tr>
			<td><spring:message code="label.account-type.description"/></td>
			<td>${formCommand.description}</td>
		</tr>
		<tr>
			<td><spring:message code="label.account-type.amount"/></td>
			<td>${formCommand.amount}</td>
		</tr>
		<tr>
			<td><spring:message code="label.account-type.period"/></td>
			<td>${formCommand.period}</td>
		</tr>
		<tr>
			<td><spring:message code="label.account-type.active"/></td>
			<td>${formCommand.activeDisplay}</td>
		</tr>
	</table>
</div>

</div>

<tides:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#account-type-body").RESTful();
			$('body').tooltip({selector: '.edit-action, .remove-action'});
		});
	</script>
</tides:footer>
