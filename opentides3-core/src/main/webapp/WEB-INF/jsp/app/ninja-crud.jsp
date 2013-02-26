<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header pageTitle="label.ninja" />
    <!--Content-->
	<div id="ninja-body" class="container-fluid container">
        <div id="search-body" class="${search}">
	        <div id="search-panel" class="row-fluid">
	        	<form:form modelAttribute="searchCommand" id="ninja-search" cssClass="form-horizontal">
		        	<h3> <i class="icon-search"></i> <spring:message code="label.ninja.search" /> </h3>  
					<div class="control-group">
						<form:label path="firstName" cssClass="control-label"><spring:message code="label.ninja.firstName"/></form:label>
						<div class="controls">
							<form:input path="firstName" maxlength="50"/>
						</div>
					</div>
					<div class="control-group">
						<form:label path="lastName" cssClass="control-label"><spring:message code="label.ninja.lastName"/></form:label>
						<div class="controls">
							<form:input path="lastName" maxlength="50"/>
						</div>
					</div>
					<div class="control-group">
						<form:label path="email" cssClass="control-label"><spring:message code="label.ninja.email" /></form:label>
						<div class="controls">
							<form:input path="email" maxlength="50" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">&nbsp;</label>
						<div class="controls">
							<button type="submit" class="btn btn-success" data-submit="search"><spring:message code="label.search"/></button>
							<button type="button" class="btn" data-submit="clear"><spring:message code="label.clear" /></button>
						</div>
					</div>					
				</form:form>
	        </div>
	        
	        <hr/>
	
	        <div id="message-panel" class="row-fluid">
	            <div class="pull-left status" 
	            		data-summary-message='<spring:message code="message.displaying-x-of-y" 
	            		arguments="#start,#end,#total,records"/>'>
	            	<app:status results="${results}"/>
	            </div>
	            <div class="pull-right">
	                <button id="add-ninja" class="btn btn-success add-action">
	                	<i class="icon-plus-sign icon-white"></i> <spring:message code="label.ninja.add" />
	                </button>
	            </div>
		    </div>
	                                
	        <div class="clear"></div>                  
	
			<div id="results-panel" class="row-fluid" style="overflow:hidden">
				<div class="span12" style="width:200%;">
	        	<table id="ninja-results" class="table table-bordered table-striped table-hover table-condensed" data-page="${results.currPage}" style="width:50%;">
					<thead>
	               	<tr class="table-header">
	                   	<th class="col-1" data-class="expand" data-field-name="completeName"><spring:message code="label.ninja.completeName"/></th>
	                   	<th class="col-2" data-hide="phone" data-field-name="email"><spring:message code="label.ninja.email"/></th>
	                	<th class="col-3" data-hide="phone,tablet" data-field-name="age"><spring:message code="label.ninja.age"/>
	                		<span class='template hide'>
	                			@#age years old
	                		</span>	                	
	                	</th>
	               		<th class="col-4" data-hide="phone,tablet" data-field-name="score"><spring:message code="label.ninja.score"/></th>
	               		<th class="col-4" data-hide="phone,tablet" data-field-name="joinDate"><spring:message code="label.ninja.joinDate"/></th>
	               		<th class="col-5" data-hide="phone,tablet" data-field-name="active"><spring:message code="label.ninja.active"/></th>
	                	<th class="col-6">
	                		<span class='template hide'>
			                	<i class='icon-edit edit-action' data-id='@#id'></i>
								<i class='icon-remove remove-action' data-id='@#id'></i>
	                		</span>
	                	</th>
	                </tr>
	           		</thead>
					<tbody>
	            	<c:forEach items="${results.results}" var="record" varStatus="status">
	            	<tr data-id="${record.id}">
	                	<td class="col-1"><c:out value="${record.completeName}" /></td>
	                	<td class="col-2"><c:out value="${record.email}" /></td>
	                	<td class="col-3"><c:out value="${record.age}" /></td>
	                	<td class="col-4"><c:out value="${record.score}" /></td>
	                	<td class="col-4"><c:out value="${record.joinDate}" /></td>
	                	<td class="col-5">
	                		<c:if test="${record.active}">Active</c:if>
	                		<c:if test="${!record.active}">Disabled</c:if>
	                	</td>
		                <td class="col-6">
		                	<i class='icon-edit edit-action' data-id='${record.id}'></i>	                	 
							<i class='icon-remove remove-action' data-id='${record.id}'></i>
		                </td>
	            	</tr>
	            	</c:forEach>
	            	</tbody>           		
	           		
	            </table>
	            </div>
	            <div class="paging clearfix" data-display-pagelinks="true" data-display-summary="false">
	            <app:paging results="${results}"/>
	            </div>
	   		</div>
   		</div> <!-- #search-body -->
   		
   		<div id="form-body" class="modal fade ${form}">
	   		<div id="form-panel">
			  	<div class="modal-header">
			   		<button type="button" class="close" data-dismiss="modal">&times;</button>
			   	 	<h3 class="${add}"><spring:message code="label.ninja.add" /></h3>		    
			   	 	<h3 class="${update}"><spring:message code="label.ninja.update" /></h3>		    
			  	</div>
			  	
				<form:form modelAttribute="formCommand" id="ninja-form" >	
			  	<div class="modal-body">
					<div class="pull-right">
				        <span class="required">*</span>
				        <span class="bold"><spring:message code="label.required-field"/></span>			
					</div>
					<div class="control-group">
						<form:label path="firstName" cssClass="control-label"><spring:message code="label.ninja.firstName"/></form:label>
						<div class="controls">
							<form:input path="firstName" maxlength="50"/>
						</div>
					</div>
					<div class="control-group">
						<form:label path="lastName" cssClass="control-label"><spring:message code="label.ninja.lastName"/></form:label>
						<div class="controls">
							<form:input path="lastName" maxlength="50"/>
						</div>
					</div>
					<div class="control-group">
						<form:label path="email" cssClass="control-label"><spring:message code="label.ninja.email" /></form:label>
						<div class="controls">
							<form:input path="email" maxlength="50" />
						</div>
					</div>
					<div class="control-group">
						<form:label path="age" cssClass="control-label"><spring:message code="label.ninja.age" /></form:label>
						<div class="controls">
							<form:input path="age" maxlength="50" />
						</div>
					</div>
					<div class="control-group">
						<form:label path="score" cssClass="control-label"><spring:message code="label.ninja.score" /></form:label>
						<div class="controls">
							<form:input path="score" maxlength="50" />
						</div>
					</div>
					<div class="control-group">
						<form:label path="joinDate" cssClass="control-label"><spring:message code="label.ninja.joinDate" /></form:label>
						<div class="controls">
							<form:input path="joinDate" maxlength="50" />
						</div>
					</div>
					<div class="control-group">
						<form:label path="active" cssClass="control-label"><spring:message code="label.ninja.active" /></form:label>
						<div class="controls">
							<form:checkbox path="active" maxlength="50" />
						</div>
					</div>
					<div class="control-group">
						<form:label path="active" cssClass="control-label"><spring:message code="label.ninja.gender" /></form:label>
						<div class="controls">
							<form:radiobuttons items="${genderList}" itemLabel="value" itemValue="key" path="gender"  />
						</div>
					</div>
					<div class="control-group">
						<form:label path="status" cssClass="control-label"><spring:message code="label.ninja.status" /></form:label>
						<div class="controls">
							<form:select path="status">
								<form:options items="${statusList}" itemLabel="value" itemValue="key"/>
							</form:select>
						</div>
					</div>
					<div class="control-group">
						<form:label path="skillSet" cssClass="control-label"><spring:message code="label.ninja.skills" /></form:label>
						<div class="controls">
							<form:select path="skillSet" multiple="true">
								<form:options items="${skillsList}" itemLabel="value" itemValue="key"/>
							</form:select>
						</div>
					</div>
					<div class="control-group">
						<form:label path="skillSet" cssClass="control-label"><spring:message code="label.ninja.skills" /></form:label>
						<div class="controls">
							<form:checkboxes items="${skillsList}" path="skillSet" itemLabel="value" itemValue="key"/>
						</div>
					</div>
			  	</div>
			 	<div class="modal-footer">
			    	<button type="submit" class="btn btn-primary" data-submit="save"><spring:message code="label.save" /></button>
			    	<button type="submit" class="btn btn-primary ${add}" data-submit="save-and-new"><spring:message code="label.save-and-new" /></button>
			    	<button type="button" class="btn" data-dismiss="modal"><spring:message code="label.close" /></button>
			    	<input type="hidden" name="id" />
			  	</div>
				</form:form>		  	
			</div> <!-- #form-panel -->
		</div> <!-- #form-body -->
	</div>
<app:footer>
  <script type="text/javascript">
  	$(document).ready(function() {
  		$("#ninja-body").RESTful();
  	});
  </script>
</app:footer>
