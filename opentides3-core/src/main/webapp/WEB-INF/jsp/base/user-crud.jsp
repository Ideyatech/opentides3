<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header title_webpage="label.user" />
    <!--Content-->
	<div class="container-fluid">
        <div id="search-panel" class="row-fluid">
        	<form:form modelAttribute="searchCommand" id="user-search" cssClass="form-horizontal">
	        	<h3> <i class="icon-search"></i> <spring:message code="label.user.search" /> </h3> 
	        	<app:input path="firstName" label="label.user.name" />
				<app:search-control/>				
			</form:form>
        </div>
        
        <hr/>

        <div id="message-panel" class="row-fluid hide">
            <div class="pull-left ot3-paging" data-display-pagelinks="false" data-display-summary="true" 
            		data-summary-message='<spring:message code="message.displaying-x-of-y" 
            		arguments="#start,#end,#total,records"/>'>
            	<app:paging results="${results}" displayPageLinks="false" displaySummary="true"/>
            </div>
            <div class="pull-right">
                <button id="add-user" class="btn btn-success">
                	<i class="icon-plus-sign icon-white"></i> <spring:message code="label.user.add" />
                </button>
            </div>
	    </div>
                                
        <div class="clear"></div>                  

		<div id="results-panel" class="row-fluid">
        	<table id="user-results" class="table table-bordered table-striped table-hover table-condensed">
				<thead>
               	<tr class="table-header">
                   	<th class="col-1" data-class="expand" data-field-name="completeName"><spring:message code="label.user.name"/></th>
                	<th class="col-2" data-hide="phone" data-field-name="emailAddress"><spring:message code="label.user.email"/></th>
               		<th class="col-4" data-hide="phone,tablet" data-field-name="groups"><spring:message code="label.user.groups"/></th>
               		<th class="col-4" data-hide="phone,tablet" data-field-name="credential.enabled"><spring:message code="label.user.active"/></th>
                	<th class="col-5" data-field-name="ot3-controls"></th>
                </tr>
           		</thead>
            </table>
            <div class="ot3-paging" data-display-pagelinks="true" data-display-summary="false">
            </div>
            <app:paging results="${results}" displayPageLinks="true" displaySummary="false"/>
   		</div>
   		
   		<div id="form-panel" class="modal fade hide">
		  	<div class="modal-header">
		   		<button type="button" class="close" data-dismiss="modal">&times;</button>
		   	 	<h3><spring:message code="label.user.add" /></h3>		    
		  	</div>
			<form:form modelAttribute="formCommand" id="user-form">	
		  	<div class="modal-body">
				<app:input path="firstName" label="label.user.first-name" required="true"/>
				<app:input path="lastName" label="label.user.last-name" required="true"/>
				<app:input path="emailAddress" label="label.user.email" required="true"/>
				<app:input path="credential.newPassword" label="label.user.password" required="true"/>
				<app:input path="credential.confirmPassword" label="label.user.confirm-password" required="true"/>
		  	</div>
		 	<div class="modal-footer">
		    	<button type="button" class="btn btn-primary" data-submit="save"><spring:message code="label.save" /></button>
		    	<button type="button" class="btn btn-primary" data-submit="save-and-new"><spring:message code="label.save-and-new" /></button>
		    	<button type="button" class="btn" data-dismiss="modal"><spring:message code="label.close" /></button>
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
  		$('.table').footable();
  		$('#user-search').jsonSearch();  		
  		$('#add-user').jsonForm();
  	});
  </script>
</app:footer>
