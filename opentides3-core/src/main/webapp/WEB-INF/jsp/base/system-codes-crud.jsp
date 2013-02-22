<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.system-codes" bodyId="system-codes-body" active="system-codes"/>

<div id="search-body">

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
					<form:label path="category">
						<spring:message code="label.system-codes.category" />
					</form:label>
					<form:select path="category">
						<form:option value="">
							<spring:message code="label.select-one" />
						</form:option>
						<form:options items="${categories}" itemValue="category"
							itemLabel="category" />
					</form:select>
					<app:input path="key" label="label.system-codes.key"/>
					<app:input path="value" label="label.system-codes.value" />
					<hr/>
					<input type="submit" class="btn btn-info" data-submit="search" value="<spring:message code="label.search"/>">
					<button type="button" class="btn btn-link" data-submit="clear"><spring:message code="label.clear" /></button>
				</form:form>
			</div>
		</div>
	</div>
	
	<div id="results-panel" class="span9">
		
		<div id="message-panel" class="row-fluid">
		
			<button id="add-system-codes" class="add-entry btn btn-info add-action">
				<i class="icon-plus-sign icon-white"></i>
				<spring:message code="label.system-codes.add" />
			</button>
		
			<div class="status" data-display-pagelinks="false" data-display-summary="true" data-summary-message='
				<spring:message code="message.displaying-x-of-y" arguments="#start,#end,#total,records"/>
			'>
				<app:paging results="${results}" displayPageLinks="false" displaySummary="true" />
			</div>
		</div>
		
		<div class="clear"></div>
		
		<div style="overflow:hidden">
			<div style="width:200%;">
				<table id="system-codes-results" class="footable table-bordered table-striped table-hover table-condensed" data-page="${results.currPage}" style="width:50%;">
					<thead>
						<tr class="table-header">
							<th data-class="expand" data-field-name="value"><spring:message code="label.system-codes.value" /></th>
							<th data-hide="phone" data-field-name="key"><spring:message code="label.system-codes.key" /></th>
							<th data-field-name="category"><spring:message code="label.system-codes.category" /></th>
							<th data-hide="phone" data-field-name="numberValue"><spring:message code="label.system-codes.number-value" /></th>
							<th data-field-name="ot3-controls"></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${results.results}" var="record" varStatus="status">
							<tr id="system-codes-row-${record.id}">
								<td class="col-1"><c:out value="${record.value}" /></td>
								<td class="col-2"><c:out value="${record.key}" /></td>
								<td class="col-3"><c:out value="${record.category}" /></td>
								<td class="col-4"><c:out value="${record.numberValue}" /></td>
								<td class="col-5">
									<i class='icon-pencil edit-action' data-id='${record.id}'></i>
									<i class='icon-trash remove-action' data-id='${record.id}'></i>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>	
			</div>
		</div>

		<div class="status clearfix" data-display-pagelinks="true" data-display-summary="false">
			<app:paging results="${results}" displayPageLinks="true" displaySummary="false" />
		</div>
	</div>
	
</div>

<div id="form-body">

	<div id="form-panel" class="modal fade hide">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 data-form-display="add"><spring:message code="label.system-codes.add" /></h4>
			<h4 data-form-display="update"><spring:message code="label.system-codes.update" /></h4>
		</div>

		<form:form modelAttribute="formCommand" id="system-codes-form">
			<div class="modal-body">
				<div class="control-group">
					<form:label path="category" cssErrorClass="highlight-error" cssClass="control-label">
						<spring:message code="label.system-codes.category" />
						<span class="required"><spring:message code="label.required-field" /></span>
					</form:label>
					<div class="controls">
						<form:input path="category" maxlength="120" />
					</div>
				</div>
				<app:input path="key" label="label.system-codes.key" required="true"/>
				<app:input path="value" label="label.system-codes.value" required="true"/>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-link" data-dismiss="modal"><spring:message code="label.close" /></button>
				<input type="submit" class="btn btn-info" data-form-display="add" data-submit="save-and-new" value="<spring:message code="label.save-and-new" />" />
				<input type="submit" class="btn btn-success" data-submit="save" value="<spring:message code="label.save" />" />
				<input type="hidden" name="id" />
			</div>
		</form:form>
	</div>
	
</div>

<app:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.footable').footable();
			$("#system-codes-body").RESTful();
		});
	</script>
</app:footer>
