<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header title_webpage="label.usergroup" />
    <!--Content-->
	<div id="usergroup-body" class="container-fluid">
	
		<!--  No Search Panel -->
        <div id="message-panel" class="row-fluid">
            <div class="pull-left">
            	<app:paging results="${results}" displayPageLinks="false" displaySummary="true"/>
            </div>
            <div class="pull-right">
                <button id="usergroup-add" class="btn btn-success add-action">
                	<i class="icon-plus-sign icon-white"></i> <spring:message code="label.usergroup.add" />
                </button>
            </div>
	    </div>
                                
		<div id="results-panel" class="row-fluid">
        	<table id="usergroup-results" class="table table-bordered table-striped table-hover table-condensed">
				<thead>
               	<tr class="table-header">
                   	<th class="col-1" data-class="expand" data-field-name="name"><spring:message code="label.usergroup.name"/></th>
                	<th class="col-2" data-hide="phone" data-field-name="description"><spring:message code="label.usergroup.description"/></th>
               		<th class="col-3" data-hide="phone,tablet" data-field-name="permissionCount"><spring:message code="label.usergroup.permissions"/></th>
                	<th class="col-4" data-field-name="ot3-controls"></th>
                </tr>
           		</thead>
				<tbody id="usergroup-table-results">
            	<c:forEach items="${results.results}" var="record" varStatus="status">
            	<tr data-id="${record.id}">            
                	<td class="col-1"><c:out value="${record.name}" /></td>
                	<td class="col-2"><c:out value="${record.description}" /></td>
                	<td class="col-3"></td>
	                <td class="col-4">
	                	<i class='icon-edit edit-action' data-id='${record.id}'></i>	                	 
						<i class='icon-remove remove-action' data-id='${record.id}'></i>	                
	                </td>              
            	</tr>
            	</c:forEach>
            	</tbody>           		
            </table>
   		</div>
   		
   		<div id="form-panel" class="page hide">
		  	<div class="modal-header">
		   	 	<h3 data-form-display="add"><spring:message code="label.usergroup.add" /></h3>		    
		   	 	<h3 data-form-display="update"><spring:message code="label.usergroup.update" /></h3>			    
		  	</div>
			<form:form modelAttribute="formCommand" id="user-form">	
		  	<div class="modal-body">
				<app:input path="name" label="label.usergroup.name" required="true"/>
				<app:input path="description" label="label.usergroup.description" required="true"/>
		  	</div>
		 	<div class="modal-footer">
		    	<button type="button" class="btn btn-primary" data-submit="save"><spring:message code="label.save" /></button>
		    	<button type="button" class="btn btn-primary" data-form-display="add" data-submit="save-and-new"><spring:message code="label.save-and-new" /></button>
		    	<button type="button" class="btn" data-dismiss="page"><spring:message code="label.back" /></button>
		    	<input type="hidden" name="id" />
		  	</div>
			</form:form>		  	
		</div>
	</div>
<app:footer>
  <script type="text/javascript">
  	$(document).ready(function() {
  		// call footable first before hiding the table
  		// there is an bug in footable that hides other table elements  		
//  		$('.table').footable();
  		$('#usergroup-body').RESTful();
  	});
  </script>
</app:footer>
