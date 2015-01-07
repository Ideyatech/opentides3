	<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.user" active="users" />
	
<div id="user-body" class="container">


<ul class="breadcrumb">
  <li><a href="${home}/"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><spring:message code="label.user"/></li>
</ul>

<div class="page-wrapper-2 overflow-hidden">
<div class="page-wrapper">

<div id="search-body" class="page main ${search}">

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
				<form:form modelAttribute="searchCommand" id="user-search">
				    <tides:input path="firstName" label="label.user.first-name" cssClass="input-block-level"/>
				    <tides:input path="lastName" label="label.user.last-name" cssClass="input-block-level"/>
				    <hr/>
					<input type="submit" class="btn btn-info btn-block" data-submit="search" value="<spring:message code="label.search"/>">
					<button type="button" class="btn btn-link btn-block" data-submit="clear"><spring:message code="label.clear" /></button>		
				</form:form>
			</div>
		</div>
	</div>
	
	<div id="results-panel" class="span9">
	
		<div id="message-panel" class="row-fluid">
			<button id="user-add" class="btn btn-info add-action">
               	<i class="icon-plus-sign icon-white"></i>
               	<spring:message code="label.user.add" />
               </button>
            <div class="status" data-summary-message='
            	<spring:message code="message.displaying-x-of-y" arguments="#start,#end,#total,records"/>'>
            	<tides:status results="${results}" />
            </div>
	    </div>
	    
	    <div class="clear"></div>
	    
	    <div class="table-wrapper-2 overflow-hidden">
			<div class="table-wrapper">
	        	<table id="user-results" class="footable table-bordered table-striped table-hover table-condensed" data-page="${results.currPage}" >
					<thead>
		               	<tr class="table-header">
		                   	<th data-class="expand" data-field-name="completeName"><spring:message code="label.user.name"/></th>
		                	<th data-field-name="emailAddress"><spring:message code="label.user.email"/></th>
		               		<th data-hide="phone" data-field-name="displayGroups"><spring:message code="label.user.groups"/></th>
		               		<th data-hide="phone" data-field-name="credential.enabled"><spring:message code="label.user.active"/></th>
		                	<th data-field-name="ot3-controls">Controls</th>
		                </tr>
	           		</thead>
	           		<tbody>
	           			<script type="text/template" class="template">
	                		<tr id="user-row-{{id}}" data-id="{{id}}">
								<td>
									<a href="${home}/organization/users/view/{{id}}" class="view-action" 
											data-id="{{id}}" data-form="view-body">
									  	<img class="img-polaroid" src="${home}/image/{{primaryImage.id}}?c=32"/>
										{{completeName}}
									</a>
								</td>
								<td>{{emailAddress}}</td>
								<td>{{displayGroups}}</td>
								<td>{{credential.enabled}}</td>
								<td>
									<i class='icon-pencil edit-action' data-id='{{id}}' data-title="<spring:message code="label.edit" />"></i>
									<i class='icon-trash remove-action' data-id='{{id}}' data-title="<spring:message code="label.delete" />"></i>
								</td>
							</tr>
						</script>
		            	<c:forEach items="${results.results}" var="record" varStatus="status">
			            	<tr id="user-row-${record.id}" data-id="${record.id}">            
			                	<td>
			                		<a href="${home}/organization/users/view/${record.id}" class="view-action" data-id="${record.id}">
									  	<img class="img-polaroid" src="${home}/image/${record.primaryImage.id}?c=32"/>
										<c:out value="${record.completeName}" />
									</a>
								</td>
			                	<td><c:out value="${record.emailAddress}" /></td>
			                	<td><c:out value="${record.displayGroups}" /></td>
			                	<td><c:out value="${record.credential.enabled}" /></td>
				                <td>
									<i class='icon-pencil edit-action' data-id='${record.id}' data-title="<spring:message code="label.edit" />"></i>
									<i class='icon-trash remove-action' data-id='${record.id}' data-title="<spring:message code="label.delete" />"></i>
									<a data-url="${home}/image/upload?imageId=${record.primaryImage.id}&className=User&classId=${record.id}" class="upload-photo">
										<i class="icon-upload"></i>
									</a>
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
   		
<div id="form-body" class="page ${form}">

	<div id="form-panel">
	
		<div class="modal-header">
			<h4 class="${add}"><spring:message code="label.user.add" /></h4>		    
			<h4 class="${update}"><spring:message code="label.user.update" /></h4>			    
		</div>
		
		<form:form modelAttribute="formCommand" id="user-form" >
			<div class="modal-body">
				<div class="message-container"></div>
				<tides:input path="credential.username" label="label.user.username" required="true"/>
				<tides:input path="firstName" label="label.user.first-name" required="true"/>
				<tides:input path="lastName" label="label.user.last-name" required="true"/>
				<tides:input path="emailAddress" label="label.user.email" required="true"/>
				<c:if test="${formCommand.userClass eq 'MultitenantUser'}">
				<tides:auto_complete path="tenant" pathValue="tenantSearch" url="${home}/ajax/tenants-list" parameterName="q" label="label.user.tenant" required="true" />
				</c:if>
				<tides:input path="credential.newPassword" label="label.user.password" type="password" />
				<tides:input path="credential.confirmPassword" label="label.user.confirm-password" type="password" />
				<tides:select label="label.user.groups" path="groups" multiple="true"
					items="${userGroupsList}" itemLabel="name" itemValue="id" select2="true" required="true"/>
				<tides:checkbox label="label.user.active" path="credential.enabled"/>
					
				<br/>
			</div>
		 	<div class="modal-footer">
		    	<button type="button" class="btn btn-link" data-dismiss="page"><spring:message code="label.back" /></button>
				<input type="submit" class="btn btn-info  ${add}" data-form-display="add" data-submit="save-and-new" value="<spring:message code="label.save-and-new" />" />
				<input type="submit" class="btn btn-success" data-submit="save" value="<spring:message code="label.save" />" />
		    	<input type="hidden" name="id" />
		  	</div>
		</form:form>		  	
	</div>			
</div>

<div id="view-body" class="page ${view}">
	
	<button type="button" class="btn btn-link" data-dismiss="page"><spring:message code="label.back" /></button>
	
	<div class="row-fluid" style="margin-bottom: 20px;">
		<div class="span2">
			<img class="img-polaroid" src="${home}/image/${formCommand.primaryImage.id}"/>
		</div>
		<div class="span10">
			<h2>${formCommand.completeName}</h2>	
		</div>
	</div>
	
	<table class="table table-striped">
		<tr>
			<td><spring:message code="label.user.first-name"/></td>
			<td>${formCommand.firstName}</td>
		</tr>
		<tr>
			<td><spring:message code="label.user.last-name"/></td>
			<td>${formCommand.lastName}</td>
		</tr>
		<tr>
			<td><spring:message code="label.user.email"/></td>
			<td><a href="mailto:#">${formCommand.emailAddress}</a></td>
		</tr>
		<tr>
			<td><spring:message code="label.user.groups"/></td>
			<td>${formCommand.displayGroups}</td>
		</tr>
		<tr>
			<td><spring:message code="label.user.active"/></td>
			<td>
				<i class="${formCommand.credential.enabled ? 'icon-ok' : 'icon-remove'}"></i>
			</td>
		</tr>
	</table>
	
	<button type="button" class="btn btn-link" data-dismiss="page"><spring:message code="label.back" /></button>
	
</div>

</div> <!-- END OF page-wrapper-2 -->
</div> <!-- END OF page-wrapper -->

<div class="adjust-photo-modal modal hide fade" data-width="760" tabindex="-1"></div>
<div class="upload-photo-modal modal hide fade" data-width="760" tabindex="-2"></div>

</div>

<tides:footer>
  <script type="text/javascript">
  	$(document).ready(function() {
  		$("#user-body").RESTful();
		$('body').tooltip({selector: '.edit-action, .remove-action'});
	})
	.on("click", '.adjust-photo', opentides3.showAdjustPhoto)
	.on("click", '.upload-photo', opentides3.showUploadPhoto);
  </script>
  
</tides:footer>
