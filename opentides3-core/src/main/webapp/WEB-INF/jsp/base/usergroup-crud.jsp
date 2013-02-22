<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header pageTitle="label.usergroup">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery.treeview.css'/>" />
</app:header>
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
                	<td class="col-3"><c:out value="${record.permissionCount}" /></td>
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
				<div class="control-group">
					<form:label path="authorityNames" cssClass="control-label"><spring:message code="label.usergroup.actions"/></form:label>
					<div class="controls">
	            		<c:set var="prevRole" value=""/>	
	            		<ul id="roles-${usergroup.id}">
			       		<c:forEach items="${authoritiesList}" var="role">
				       		<c:set var="pStage" value="${fn:substring(prevRole,0,11)}"/>
							<c:set var="pList"  value="${fn:split(pStage, '.')}"/>
							<c:set var="cStage" value="${fn:substring(role.value,0,11)}"/>
							<c:set var="cList"  value="${fn:split(cStage, '.')}"/>
							<c:forEach begin="0" end="3" step="1" var="i">
							    <c:if test="${'00' eq cList[i] && not ('00' eq pList[i]) && not empty pList[i]}">
							        </ul></li>
							    </c:if>
							</c:forEach>
							<c:if test="${('00' eq cList[i]) || not ('00' eq pList[i] || empty pList[i]) }">
							    </li>
							</c:if>
							<c:forEach begin="0" end="3" step="1" var="i">
							    <c:if test="${not ('00' eq cList[i]) && ('00' eq pList[i] || empty pList[i]) }">
							        <ul>
							    </c:if>
							</c:forEach>
			                <li class="closed">
			                	<form:checkbox path="authorityNames" value="${role.key}" class="check ${role.key}"/> 
			                		<c:out value="${fn:substring(role.value,11,-1)}" />
			                <c:set var="prevRole" value="${role.value}"/>
			    		</c:forEach>
	            		</ul>					
					</div>
				</div>
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
  <script type="text/javascript" src="<c:url value='/js/jquery.treeview.js'/>"></script>
  <script type="text/javascript">
  	$(document).ready(function() {
  		// call footable first before hiding the table
  		// there is an bug in footable that hides other table elements  		
//  		$('.table').footable();
  		$('#usergroup-body').RESTful();
  		
  		$('#copy-roles-${usergroup.id}').hide();
    	$("#roles-${usergroup.id}").treeview();
    	$("input.check").click(function() {
    	    if ($(this).is(":checked")) {
    	        // if checked, check all parents
    	        $(this).parents("li").children(".check").attr("checked","true");
    	    } else {
    	        // if unchecked
    	        var children = $(this).parent("li").find(".check");
    	        if (children.size() > 1) {
    				if (confirm('<spring:message code="msg.usergroup.this-will-deselect-all-child-permissions-are-you-sure" />')) {
    					children.removeAttr("checked");    
    				}    	            
    	        }
    	    }
    	});
    	$('#enable-copy-${usergroup.id}').click(function() {
    		if($(this).attr('checked')) {
    			$('#copy-roles-${usergroup.id}').show();
    		} else {
    			$('#copy-roles-${usergroup.id}').hide();
    		}
    	});    	
    	$('#copy-roles-${usergroup.id}').change(function(){
    		// uncheck all checkboxes in this role
    		$('#roles-${usergroup.id} .check').removeAttr("checked");    
     		$.ajax({
				url : "roles-list.jspx?userGroupId="+$('#copy-roles-${usergroup.id}').val(),
				type : 'GET',
				dataType : "json",
				success : function(data) {
					$.each(data.roles, function(index, role) {
						$('#roles-${usergroup.id} .'+role).attr("checked","true");						
					});
				}
			});
     	});
  	});
  </script>
  
</app:footer>
