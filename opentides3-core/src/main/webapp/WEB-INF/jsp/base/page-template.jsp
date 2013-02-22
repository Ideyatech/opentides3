<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

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
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageTitle="%%%%%" active="%%%%%"/>

<div id="%%%%%-body">
	
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
				<form:form modelAttribute="searchCommand" id="%%%%%-search">
				    <!-- Add fields for search form here -->
				    <hr />
					<input type="submit" class="btn btn-info" data-submit="search" value="<spring:message code="label.search"/>">
					<button type="button" class="btn btn-link" data-submit="clear"><spring:message code="label.clear" /></button>		
				</form:form>
			</div>
		</div>
	</div>
	
	<div id="results-panel" class="span9">
	
		<div id="message-panel" class="row-fluid">
			<button id="%%%%%-add" class="btn btn-info add-action">
               	<i class="icon-plus-sign icon-white"></i>
               	<spring:message code="label.%%%%%.add" />
               </button>
            <div class="status" data-summary-message='
            	<spring:message code="message.displaying-x-of-y" arguments="#start,#end,#total,records"/>
            '>
            	<app:paging results="${results}" />
            </div>
	    </div>
	    
	    <div class="clear"></div>
	    
	    <div style="overflow:hidden">
	    	<div style="width:200%;">
	        	<table id="%%%%%-results" class="footable table-bordered table-striped table-hover table-condensed" style="width:50%;">
					<thead>
		               	<tr class="table-header">
		                   	<!-- Add table header here -->
		                </tr>
	           		</thead>
	           		<tbody>
		            	<c:forEach items="${results.results}" var="record" varStatus="status">
			            	<tr data-id="${record.id}">            
			                	<!-- Add fields to be displayed here -->
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
			<h4 class="${add}"><spring:message code="label.%%%%%.add" /></h4>		    
			<h4 class="${update}"><spring:message code="label.%%%%%.update" /></h4>			    
		</div>
		
		<form:form modelAttribute="formCommand" id="%%%%%-form">	
			<div class="modal-body">
				<!-- Add fields here -->
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
		$('.footable').footable();	
  		$("%%%%%-body").RESTful();
	});
  </script>
</app:footer>