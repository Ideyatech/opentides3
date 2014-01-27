<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.system-codes" active="system-codes"/>

<div id="system-codes-body">

<ul class="breadcrumb">
  <li><a href="${home}"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><spring:message code="label.system-codes"/></li>
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
				<form:form modelAttribute="searchCommand" id="system-codes-search" >
					<tides:select path="category" items="${categoryList}" label="label.system-codes.category" cssClass="input-block-level" />
					<tides:input path="key" label="label.system-codes.key" cssClass="input-block-level"/>
					<tides:input path="value" label="label.system-codes.value" cssClass="input-block-level"/>
					<hr/>
					<input type="submit" class="btn btn-info btn-block" data-submit="search" value="<spring:message code="label.search"/>">
					<button type="button" class="btn btn-link btn-block" data-submit="clear"><spring:message code="label.clear" /></button>
				</form:form>
			</div>
		</div>
	</div>
	
	<div id="results-panel" class="span9">
		
		<div id="message-panel" class="row-fluid">
			<button id="system-codes-add" class="btn btn-info add-action">
				<i class="icon-plus-sign icon-white"></i>
				<spring:message code="label.system-codes.add" />
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
				<table id="system-codes-results" class="footable table-bordered table-striped table-hover table-condensed" data-page="${results.currPage}" >
					<thead>
						<tr class="table-header">
							<th data-class="expand" data-field-name="value"><spring:message code="label.system-codes.value" /></th>
							<th data-hide="phone" data-field-name="key"><spring:message code="label.system-codes.key" /></th>
							<th data-field-name="category"><spring:message code="label.system-codes.category" /></th>
							<th data-field-name="ot3-controls"></th>
						</tr>
					</thead>
					<tbody>
						<script type="text/template" class="template">
	                		<tr data-id="{{id}}">
								<td>
									<a href="${home}/system/system-codes/view/{{id}}">
										{{value}}
									</a>
								</td>
								<td>{{key}}</td>
								<td>{{category}}</td>
								<td>
									<i class='icon-pencil edit-action' data-id='{{id}}' data-title="<spring:message code="label.edit" />"></i>
									<i class='icon-trash remove-action' data-id='{{id}}' data-title="<spring:message code="label.delete" />"></i>
								</td>
							</tr>
						</script>
						<c:forEach items="${results.results}" var="record" varStatus="status">
							<tr id="system-codes-row-${record.id}">
								<td>
									<a href="${home}/system/system-codes/view/${record.id}">
										<c:out value="${record.value}" />
									</a>
								</td>
								<td><c:out value="${record.key}" /></td>
								<td><c:out value="${record.category}" /></td>
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
			<h4 class="${add}"><spring:message code="label.system-codes.add" /></h4>
			<h4 class="${update}"><spring:message code="label.system-codes.update" /></h4>
		</div>

		<form:form modelAttribute="formCommand" id="system-codes-form">
			<div class="modal-body">
				<div class="message-container"></div>
				<tides:combobox path="category" label="label.system-codes.category" items="${categoryList}"/>
				<tides:input path="key" label="label.system-codes.key" />
				<tides:input path="value" label="label.system-codes.value" />
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
			<h2>${formCommand.category}</h2>	
		</div>
	</div>
	
	<table class="table table-striped">
		<tr>
			<td><spring:message code="label.system-codes.key"/></td>
			<td>${formCommand.key}</td>
		</tr>
		<tr>
			<td><spring:message code="label.system-codes.value"/></td>
			<td>${formCommand.value}</td>
		</tr>
		<tr>
			<td><spring:message code="label.system-codes.number-value"/></td>
			<td><a href="mailto:#">${formCommand.numberValue}</a></td>
		</tr>
	</table>
</div>

</div>

<tides:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#system-codes-body").RESTful();
			$('body').tooltip({selector: '.edit-action, .remove-action'});
		});
	</script>
</tides:footer>
