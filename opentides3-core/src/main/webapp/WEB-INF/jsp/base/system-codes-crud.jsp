<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header title_webpage="label.system-codes" />
    <!--Content-->
	<div class="container-fluid">
        <div id="search-panel" class="row-fluid">
        	<form:form modelAttribute="searchCommand" id="system-codes-search" cssClass="form-horizontal">
	        	<h3> <i class="icon-search"></i> <spring:message code="label.system-codes.search" /> </h3>  
				<div class="control-group">
					<form:label path="category" cssClass="control-label"><spring:message code="label.system-codes.category" /></form:label>
					<div class="controls">
			            <form:select path="category">
			            <form:option value=""><spring:message code="label.select-one" /></form:option>
			            <form:options items="${categories}" itemValue="category" itemLabel="category" />
			            </form:select>
					</div>
				</div>
				<div class="control-group">
					<form:label path="key" cssClass="control-label"><spring:message code="label.system-codes.key"/></form:label>
					<div class="controls">
						<form:input path="key" maxlength="50"/>
					</div>
				</div>
				<div class="control-group">
					<form:label path="value" cssClass="control-label"><spring:message code="label.system-codes.value" /></form:label>
					<div class="controls">
						<form:input path="value" maxlength="50" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">&nbsp;</label>
					<div class="controls">
						<button type="button" class="btn btn-success" data-submit="search"><spring:message code="label.search"/></button>
						<button type="button" class="btn" data-submit="clear"><spring:message code="label.clear" /></button>
					</div>
				</div>					
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
                <button id="add-system-codes" class="btn btn-success">
                	<i class="icon-plus-sign icon-white"></i> <spring:message code="label.system-codes.add" />
                </button>
            </div>
	    </div>
                                
        <div class="clear"></div>                  

		<div id="results-panel" class="row-fluid">
        	<table id="system-codes-results" class="table table-bordered table-striped table-hover table-condensed">
				<thead>
               	<tr class="table-header">
                   	<th class="col-1" data-class="expand" data-field-name="value"><spring:message code="label.system-codes.value"/></th>
                   	<th class="col-1" data-hide="phone" data-field-name="key"><spring:message code="label.system-codes.key"/></th>
                	<th class="col-3" data-hide="phone,tablet" data-field-name="category"><spring:message code="label.system-codes.category"/></th>
               		<th class="col-4" data-hide="phone,tablet" data-field-name="numberValue"><spring:message code="label.system-codes.number-value"/></th>
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
		   	 	<h3><spring:message code="label.system-codes.add" /></h3>		    
		  	</div>
			<form:form modelAttribute="formCommand" id="system-codes-form">	
		  	<div class="modal-body">
				<div class="pull-right">
			        <span class="required">*</span>
			        <span class="bold"><spring:message code="label.required-field"/></span>			
				</div>
				<div class="control-group">
					<form:label path="category" cssErrorClass="highlight-error" cssClass="control-label"><spring:message code="label.system-codes.category" /></form:label>
					<div class="controls">
						<form:input path="category" maxlength="120" />
					<!-- 
			    		<form:select id="categorySelect" path="category">
			    			<form:option value=""><spring:message code="label.system-codes.select-category"/></form:option>
			    			<form:option value="0"><spring:message code="label.system-codes.new-category"/></form:option>
			    			<form:options items="${categories}" itemLabel="category" itemValue="category"/>
			            </form:select>
			         -->
			            <span class="required">*</span>
		            </div>
				</div>
				<div class="control-group">
					<form:label path="key" cssErrorClass="highlight-error" cssClass="control-label"><spring:message code="label.system-codes.key"/></form:label>
					<div class="controls">
						<form:input path="key" maxlength="120" />
					</div>
				</div>
				<div class="control-group">
					<form:label path="value" cssErrorClass="highlight-error" cssClass="control-label"><spring:message code="label.system-codes.value" /></form:label>
					<div class="controls">
						<form:input path="value" maxlength="120"/>
					</div>
				</div>
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

  		$('#system-codes-search').jsonSearch();  		
  		$('#add-system-codes').jsonForm();
  	});
  	
  	function checkNewCategory(fromDropdown) {
 		var dropDownHTML  ='<label for="category"><spring:message code="label.system-codes.category" /></label><select name="category" onclick="javascript:checkNewCategory(true);"><option value="0">Select a Category</option><option value="0">-- New Category --</option><c:forEach items="${categories}" var="record" varStatus="status"><option>${record.category}</option></c:forEach></select>';
 		var textFieldHTML ='<label for="category"><spring:message code="label.system-codes.category" /></label><input type="text" name="category" id="categoryText"/> <a href="javascript:void(0)" onclick="javascript:checkNewCategory(false)">Cancel</a>';
 		var ni = document.getElementById('categorySelector');
  		if (fromDropdown) {
  			// check if "New Category" is selected
  			var select = document.getElementById('categorySelect');
	  		if (select.selectedIndex==1)
	  			ni.innerHTML = textFieldHTML;
  		} else
  			ni.innerHTML = dropDownHTML;
	}
  </script>
</app:footer>
