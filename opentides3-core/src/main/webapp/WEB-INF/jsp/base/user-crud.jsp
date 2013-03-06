<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.user" active="users"/>

<div id="user-body">
	
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
				<form:form modelAttribute="searchCommand" id="user-search">
				    <app:input path="firstName" label="label.user.first-name" class="input-block-level"/>
				    <app:input path="lastName" label="label.user.last-name" class="input-block-level"/>
				    <hr />
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
            	<spring:message code="message.displaying-x-of-y" arguments="#start,#end,#total,records"/>
            '>
            	<app:status results="${results}" />
            </div>
	    </div>
	    
	    <div class="clear"></div>
	    
	    <div style="overflow:hidden">
	    	<div style="width:200%;">
	        	<table id="user-results" class="footable table-bordered table-striped table-hover table-condensed" style="width:50%;" data-page="${results.currPage}" >
					<thead>
		               	<tr class="table-header">
		                   	<th data-class="expand" data-field-name="completeName"><spring:message code="label.user.name"/></th>
		                	<th data-field-name="emailAddress"><spring:message code="label.user.email"/></th>
		               		<th data-hide="phone" data-field-name="displayGroups"><spring:message code="label.user.groups"/></th>
		               		<th data-hide="phone" data-field-name="credential.enabled"><spring:message code="label.user.active"/></th>
		                	<th data-field-name="ot3-controls"></th>
		                </tr>
	           		</thead>
	           		<tbody>
		            	<c:forEach items="${results.results}" var="record" varStatus="status">
			            	<tr data-id="${record.id}">            
			                	<td><c:out value="${record.completeName}" /></td>
			                	<td><c:out value="${record.emailAddress}" /></td>
			                	<td><c:out value="${record.displayGroups}" /></td>
			                	<td><c:out value="${record.credential.enabled}" /></td>
				                <td>
									<i class='icon-pencil edit-action' data-id='${record.id}'></i>
									<i class='icon-trash remove-action' data-id='${record.id}'></i>
				                </td>
			            	</tr>
		            	</c:forEach>
	            	</tbody>
	            </table>
			</div>
		</div>
		
		<div class="paging clearfix">
			<app:paging results="${results}"/>
		</div>
	</div>

</div>
   		
<div id="form-body" class="modal fade hide">

	<div id="form-panel">
	
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="${add}"><spring:message code="label.user.add" /></h4>		    
			<h4 class="${update}"><spring:message code="label.user.update" /></h4>			    
		</div>
		
		<form:form modelAttribute="formCommand" id="user-form">	
			<div class="modal-body">
			
				<div class="control-group">
					<label>Profile Picture</label>
					<div class="controls">
						<input type="file" />
					</div>
				</div>
			
				<app:input path="firstName" label="label.user.first-name" required="true"/>
				<app:input path="lastName" label="label.user.last-name" required="true"/>
				<app:input path="emailAddress" label="label.user.email" required="true"/>
				<app:input path="credential.newPassword" label="label.user.password" type="password"/>
				<app:input path="credential.confirmPassword" label="label.user.confirm-password" type="password"/>
				<app:select label="label.user.groups" path="groups" multiple="true"
					items="${userGroupsList}" itemLabel="name" itemValue="id" select2="true"/>
				<app:checkbox label="label.user.active" path="credential.enabled"/>
				<br/>
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

</div>

<app:footer>
  <script type="text/javascript">
  	$(document).ready(function() {
		//$('.footable').footable();	
  		$("#user-body").RESTful();
	});
  </script>
</app:footer>
