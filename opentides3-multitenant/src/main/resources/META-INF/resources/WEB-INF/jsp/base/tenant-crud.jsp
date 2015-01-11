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

<app:header pageTitle="label.tenant" active="tenant">
	<script type="text/javascript" src="<c:url value='/js/typeahead.js'/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/typeahead.css'/>" />
</app:header>

<div id="tenant-body" class="container">

<ul class="breadcrumb">
  <li><a href="${home}/"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><spring:message code="label.tenant"/></li>
</ul>

<div class="page-wrapper-2 overflow-hidden">
<div class="page-wrapper">

<div id="search-body"  class="page main ${search}">

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
				<form:form modelAttribute="searchCommand" id="tenant-search">
					<tides:select path="accountType" items="${accountTypeList}" label="label.tenant.account-type" itemLabel="name" itemValue="id" />
				    <tides:input path="name" label="label.tenant.name" cssClass="input-block-level"/>
				    <hr/>
					<input type="submit" class="btn btn-info btn-block" data-submit="search" value="<spring:message code="label.search"/>">
					<button type="button" class="btn btn-link btn-block" data-submit="clear"><spring:message code="label.clear" /></button>		
				</form:form>
			</div>
		</div>
	</div>
	
	<div id="results-panel" class="span9">
		
		<div id="message-panel" class="row-fluid">
			<button id="tenant-add" class="btn btn-info add-action">
               	<i class="icon-plus-sign icon-white"></i>
               	<spring:message code="label.tenant.add" />
               </button>
            <div class="status" data-summary-message='
            	<spring:message code="message.displaying-x-of-y" arguments="#start,#end,#total,records"/>'>
            	<tides:status results="${results}" />
            </div>
	    </div>
	    
		<div class="clear"></div>
		
		<div class="table-wrapper-2 overflow-hidden">
			<div class="table-wrapper">
				<table id="tenant-results" class="footable table-bordered table-striped table-hover table-condensed" data-page="${results.currPage}" >
					<thead>
						<tr class="table-header">
							<!-- Define headers here -->
							<th data-field-name="name"><spring:message code="label.tenant.name" /></th>
							<th data-field-name="owner.fullName"><spring:message code="label.tenant.owner" /></th>
							<th data-field-name="accountType"><spring:message code="label.tenant.account-type" /></th>
							<th data-field-name="expireDate"><spring:message code="label.tenant.expiration-date" /></th>
							<th data-field-name="ot3-controls"></th>
						</tr>
					</thead>
					<tbody>
						<script type="text/template" class="template">
	                		<tr id="tenant-row-{{id}}" data-id="{{id}}">
								<td>{{company}}</td>
								<td>{{owner.fullName}}</td>
								<td>{{accountType.name}}</td>
								<td>{{expirationDate}}</td>
								<td>
									<i class='icon-pencil edit-action' data-id='{{id}}' data-title="<spring:message code="label.edit" />"></i>
									<i class='icon-trash remove-action' data-id='{{id}}' data-title="<spring:message code="label.delete" />"></i>
								</td>
							</tr>
						</script>
						<c:forEach items="${results.results}" var="record" varStatus="status">
							<tr id="tenant-row-${record.id}" data-id="${record.id}">
								<td>${record.company}</td>
								<td>${record.owner.fullName}</td>
								<td>${record.accountType.name}</td>
								<td>${record.expirationDate}</td>
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

<div id="form-body" class="page hide ${form}">

	<div id="form-panel">
	
		<div class="modal-header">
			<h4 class="${add}"><spring:message code="label.tenant.add" /></h4>		    
			<h4 class="${update}"><spring:message code="label.tenant.update" /></h4>			    
		</div>
		
		<form:form modelAttribute="formCommand" id="tenant-form" >
			<div class="span5">
				<div class="modal-body">
					<div class="message-container"></div>
					<tides:input    path="company" label="label.tenant.company" required="true"/>
					<tides:select   path="accountType" items="${accountTypeList}" label="label.tenant.account-type" itemLabel="name" itemValue="id" />
					<tides:date_picker path="expirationDate" label="label.tenant.expiration-date" autoClose="true" required="true"/>					
					<tides:input  path="schema" label="label.tenant.schema" readonly="true"/>
					<tides:input  path="dbVersion" label="label.tenant.db-version" readonly="true"/>
					<br/>
				</div>
			</div>
			<div class="span5">
				<div class="modal-body">
					<form:hidden path="owner.id"/>
					<tides:input path="owner.credential.username" label="label.user.username" required="true"/>
					<tides:input path="owner.firstName" label="label.user.first-name" required="true"/>
					<tides:input path="owner.lastName" label="label.user.last-name" required="true"/>
					<tides:input path="owner.emailAddress" label="label.user.email" required="true"/>
					<tides:input path="owner.credential.newPassword" label="label.user.password" type="password" />
					<tides:input path="owner.credential.confirmPassword" label="label.user.confirm-password" type="password" />
					<br/>
				</div>
			</div>
			<div class="clearfix"> </div>			
		 	<div class="modal-footer">
		    	<button type="button" class="btn btn-link" data-dismiss="page"><spring:message code="label.back" /></button>
				<input type="submit" class="btn btn-info  ${add}" data-form-display="add" data-submit="save-and-new" value="<spring:message code="label.save-and-new" />" />
				<input type="submit" class="btn btn-success" data-submit="save" value="<spring:message code="label.save" />" />
		    	<input type="hidden" name="id" />
		  	</div>
		</form:form>
		
	</div>			
</div>

</div> <!-- END OF page-wrapper-2 -->
</div> <!-- END OF page-wrapper -->

</div>

<tides:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			$('body').tooltip({selector: '.edit-action, .remove-action'});
			$("#tenant-body").RESTful();
		});
	</script>
</tides:footer>