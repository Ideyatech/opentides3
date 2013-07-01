<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="label.usergroup" active="usergroups" >
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery.treeview.css'/>" />
	<script type="text/javascript" src="<c:url value='/js/jquery.treeview.js'/>"></script>
</app:header>

<div id="usergroup-body" class="container">

	<ul class="breadcrumb" style="display: block !important;">
	  <li><a href="${home}"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
	  <li><spring:message code="label.usergroup"/></li>
	</ul>

	<div id="search-body" class="${search}">
		
		<div id="results-panel">
			
			<div id="message-panel" class="row-fluid">
				<button id="usergroup-add" class="btn btn-info add-action">
					<i class="icon-plus-sign icon-white"></i>
					<spring:message code="label.usergroup.add" />
				</button>
				<div class="status" data-display-pagelinks="false" data-display-summary="true" data-summary-message='
					<spring:message code="message.displaying-x-of-y" arguments="#start,#end,#total,records"/>
				'>
					<app:status results="${results}" />
				</div>
			</div>
			
			<div class="clear"></div>
			
			<div class="table-wrapper-2 overflow-hidden">
			<div class="table-wrapper">
					<table id="usergroup-results" class="footable table-bordered table-striped table-hover table-condensed" data-page="${results.currPage}" >
						<thead>
							<tr class="table-header">
								<th data-class="expand" data-field-name="name"><spring:message code="label.usergroup.name"/></th>
			                	<th data-hide="phone" data-field-name="description"><spring:message code="label.usergroup.description"/></th>
			               		<th data-hide="phone,tablet" data-field-name="permissionCount"><spring:message code="label.usergroup.permissions"/></th>
			                	<th data-field-name="ot3-controls"></th>
							</tr>
						</thead>
						<tbody>
							<script type="text/template" class="template">
	                		<tr data-id="{{id}}">
								<!-- Define template here -->
								<td>{{name}}</td>
								<td>{{description}}</td>
								<td>{{permissionCount}}</td>
								<td>
									<i class='icon-pencil edit-action' data-id='{{id}}' data-title="<spring:message code="label.edit" />"></i>
									<i class='icon-trash remove-action' data-id='{{id}}' data-title="<spring:message code="label.delete" />"></i>
								</td>
							</tr>
						</script>
							<c:forEach items="${results.results}" var="record" varStatus="status">
								<tr data-id="${record.id}">
									<td><c:out value="${record.name}" /></td>
				                	<td><c:out value="${record.description}" /></td>
				                	<td><c:out value="${record.permissionCount}" /></td>
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
				<app:paging results="${results}"/>
			</div>
		</div>
		
	</div>
	
	<div id="form-body" class="page ${form}">
	
		<div id="form-panel" >
			<div class="component">
				<legend class="${add}"><spring:message code="label.usergroup.add" /></legend>
				<legend class="${update}"><spring:message code="label.usergroup.update" /></legend>
			</div>
	
			<form:form modelAttribute="formCommand" id="usergroup-form">
				<div class="message-container"></div>
				<div>
					<app:input path="name" label="label.usergroup.name" required="true"/>
					<app:input path="description" label="label.usergroup.description"/>
					<div class="control-group">
						<form:label path="authorityNames" cssClass="control-label"><spring:message code="label.usergroup.actions"/></form:label>
						<div class="controls">
		            		<c:set var="prevRole" value=""/>
		            		<ul id='usergroup-tree'>
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
				                	<form:checkbox path="authorityNames" value="${role.key}" class="check"/> 
				                		<c:out value="${fn:substring(role.value,11,-1)}" />
				                <c:set var="prevRole" value="${role.value}"/>
				    		</c:forEach>
		            		</ul>
						</div>
					</div>
				</div>
				<div class="form-actions">
					<div class="pull-right">
						<button type="button" class="btn btn-link" data-dismiss="page"><spring:message code="label.close" /></button>
						<input type="submit" class="btn btn-info  ${add}" data-form-display="add" data-submit="save-and-new" value="<spring:message code="label.save-and-new" />" />
						<input type="submit" class="btn btn-success" data-submit="save" value="<spring:message code="label.save" />" />
						<input type="hidden" name="id" />
					</div>
				</div>
			</form:form>
		</div>
		
	</div>

</div>

<app:footer>
	<script type="text/javascript">
		$(document).ready(function() {

			$('#usergroup-body').RESTful({form:'#form-body', search:'#search-body'});
		  		
	    	$("#usergroup-tree").treeview();

	    	$("input.check").click(function() {
	    	    if ($(this).is(":checked")) {
	    	        $(this).parents("li").children(".check").attr("checked","true");
	    	    } else {
	    	        var children = $(this).parent("li").find(".check");
	    	        if (children.size() > 1) {
	    				if (confirm('<spring:message code="msg.usergroup.this-will-deselect-all-child-permissions-are-you-sure" />')) {
	    					children.removeAttr("checked");    
	    				}
	    	        }
	    	    }
	    	});
	  		$('#copy-roles-${usergroup.id}').hide();

	  		$('#enable-copy-${usergroup.id}').click(function() {
	    		if($(this).attr('checked')) {
	    			$('#copy-roles-${usergroup.id}').show();
	    		} else {
	    			$('#copy-roles-${usergroup.id}').hide();
	    		}
	    	});
	  		
	    	$('#copy-roles-${usergroup.id}').change(function(){
	    		
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
			
			$('body').tooltip({selector: '.edit-action, .remove-action'});
			
		});
	</script>
</app:footer>