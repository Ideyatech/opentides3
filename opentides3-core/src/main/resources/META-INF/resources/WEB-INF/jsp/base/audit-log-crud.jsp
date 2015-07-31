<%--
	- page-template.jsp
	- A template page for your reference
	-
	- @author - Your name here
--%>
<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.audit-log" active="audit-log">
	<script type="text/javascript" src="<c:url value='/js/typeahead.js'/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/typeahead.css'/>" />
</app:header>

<div id="audit-log-body">

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
				<form:form modelAttribute="searchCommand" id="audit-log-search" >
					<!-- Define search fields here -->
					<div class="control-group">
						<form:label path="userId" cssClass="control-label" cssErrorClass="highlight-error">
							<spring:message code="label.audit-log.name"/>
						</form:label>
						<input type="text" id="memberName"/>
						<form:hidden path="userId"/>
						<script type="text/javascript">
						(function() {
							$('#memberName').typeahead({
								remote : '${home}/ajax/users-list?q=%QUERY',
							}).on('typeahead:selected', function(e, datum) {
								$('#userId').val(datum.key);
							});
						})();
						</script>
					</div>
					<tides:date_picker label="label.audit-log.date-from" path="startDate" placeholder="Start Date" />
					<tides:date_picker label="label.audit-log.date-to" path="endDate" placeholder="End Date" />
					<div class="control-group">
						<form:label path="logAction" cssClass="control-label" cssErrorClass="highlight-error">
							<spring:message code="label.audit-log.action"/>
						</form:label>
						
						<div class="controls">
							<form:select path="logAction" >
								<form:option value="">Select Action</form:option>
								<form:option value="logged-in">Logged In</form:option>
								<form:option value="logged-out">Logged Out</form:option>
								<form:option value="added">Added Record</form:option>
								<form:option value="changed">Updated Record</form:option>
								<form:option value="deleted">Deleted Record</form:option>
							</form:select>
						</div>
						
					</div>
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
				<table id="audit-log-results" class="footable table-bordered table-striped table-hover table-condensed" data-page="${results.currPage}" >
					<thead>
						<tr class="table-header">
							<!-- Define headers here -->
							<th data-field-name="ot3-controls"><spring:message code="label.audit-log" /></th>
						</tr>
					</thead>
					<tbody>
						<script type="text/template" class="template">
	                		<tr id="audit-log-row-{{id}}" data-id="{{id}}">
								<td>
									<div>
										{{&message}}
										<div>
											<span>{{userNameDisplay}}</span>
										</div>
										<div>
											<span>{{createDateForDisplay}}</span>
										</div>
									</div>
									
								</td>
							</tr>
						</script>
						<c:forEach items="${results.results}" var="record" varStatus="status">
							<tr id="audit-log-${record.id }" data-id="${record.id}">
								<td>
									<div>
										${record.message}
									</div>
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

</div>

<tides:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			$('body').tooltip({selector: '.edit-action, .remove-action'});
			$("#audit-log-body").RESTful();
		});
	</script>
</tides:footer>