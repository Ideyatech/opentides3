<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<app:header title_webpage="label.system-codes" active="system-codes"/>

<div id="system-codes-body">

<!-- SEARCH PANEL -->
<div id="left-column" class="span3">
	<div id="search-panel" data-spy="affix" data-offset-top="80" style="top: 60px;">
		<div class="navbar">
			<div class="navbar-inner">
				<span class="brand"><i class="icon-search"></i><spring:message code="label.system-codes.search" /></span>
			</div>
		</div>
	
		<form:form modelAttribute="searchCommand" id="system-codes-search">
	
			<form:label path="category">
				<spring:message code="label.system-codes.category" />
			</form:label>
	
			<form:select path="category">
				<form:option value="">
					<spring:message code="label.select-one" />
				</form:option>
				<form:options items="${categories}" itemValue="category"
					itemLabel="category" />
			</form:select>
	
			<form:label path="key">
				<spring:message code="label.system-codes.key" />
			</form:label>
	
			<form:input path="key" maxlength="50" />
	
			<form:label path="value">
				<spring:message code="label.system-codes.value" />
			</form:label>
	
			<form:input path="value" maxlength="50" />
	
			<hr/>
			<button type="button" class="btn btn-info" data-submit="search">
				<spring:message code="label.search" />
			</button>
			<button type="button" class="btn btn-link" data-submit="clear">
				<spring:message code="label.clear" />
			</button>
	
		</form:form>
	</div>
</div>


<div id="right-column" class="span9">
	<div id="message-panel" class="row-fluid">
		<div class="pull-left status" data-display-pagelinks="false"
			data-display-summary="true"
			data-summary-message='<spring:message code="message.displaying-x-of-y" 
            		arguments="#start,#end,#total,records"/>'>
			<app:paging results="${results}" displayPageLinks="false"
				displaySummary="true" />
		</div>
		<div class="pull-right">
			<button id="add-system-codes" class="add-entry btn btn-info add-action">
				<i class="icon-plus-sign icon-white"></i>
				<spring:message code="label.system-codes.add" />
			</button>
		</div>
	</div>

	<div class="clear"></div>

		<div id="results-panel" class="row-fluid" style="overflow:hidden">
			<div class="span12" style="width:200%;">		
        	<table id="system-codes-results" class="footable table-bordered table-striped table-hover table-condensed" data-page="${results.currPage}" style="width:50%;">
			<thead>
				<tr class="table-header">
					<th class="col-1" data-class="expand" data-field-name="value"><spring:message
							code="label.system-codes.value" /></th>
					<th class="col-2" data-hide="phone" data-field-name="key"><spring:message
							code="label.system-codes.key" /></th>
					<th class="col-3" data-hide="phone,tablet"
						data-field-name="category"><spring:message
							code="label.system-codes.category" /></th>
					<th class="col-4" data-hide="phone,tablet"
						data-field-name="numberValue"><spring:message
							code="label.system-codes.number-value" /></th>
					<th class="col-5" data-field-name="ot3-controls"></th>
				</tr>
			</thead>
			<tbody id="system-codes-table-results">
				<c:forEach items="${results.results}" var="record"
					varStatus="status">
					<tr id="usergroup-row-${record.id}">
						<td class="col-1"><c:out value="${record.value}" /></td>
						<td class="col-2"><c:out value="${record.key}" /></td>
						<td class="col-3"><c:out value="${record.category}" /></td>
						<td class="col-4"><c:out value="${record.numberValue}" /></td>
						<td class="col-5"><i class='icon-pencil edit-action'
							data-id='${record.id}'></i> <i class='icon-trash remove-action'
							data-id='${record.id}'></i></td>
					</tr>
				</c:forEach>
			</tbody>

		</table>
            </div>
            <div class="status clearfix" data-display-pagelinks="true" data-display-summary="false">
			<app:paging results="${results}" displayPageLinks="true"
				displaySummary="false" />
		</div>
	</div>

	<div id="form-panel" class="modal fade hide">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 data-form-display="add">
				<spring:message code="label.system-codes.add" />
			</h4>
			<h4 data-form-display="update">
				<spring:message code="label.system-codes.update" />
			</h4>
		</div>

		<form:form modelAttribute="formCommand" id="system-codes-form">
			<div class="modal-body">
				<div class="pull-right">
					<span class="required">*</span> <span class="bold"><spring:message
							code="label.required-field" /></span>
				</div>
				<div class="control-group">
					<form:label path="category" cssErrorClass="highlight-error"
						cssClass="control-label">
						<spring:message code="label.system-codes.category" />
					</form:label>
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
					<form:label path="key" cssErrorClass="highlight-error"
						cssClass="control-label">
						<spring:message code="label.system-codes.key" />
					</form:label>
					<div class="controls">
						<form:input path="key" maxlength="120" />
					</div>
				</div>
				<div class="control-group">
					<form:label path="value" cssErrorClass="highlight-error"
						cssClass="control-label">
						<spring:message code="label.system-codes.value" />
					</form:label>
					<div class="controls">
						<form:input path="value" maxlength="120" />
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-success" data-submit="save">
					<spring:message code="label.save" />
				</button>
				<button type="button" class="btn"
					data-form-display="add" data-submit="save-and-new">
					<spring:message code="label.save-and-new" />
				</button>
				<button type="button" class="btn btn-link" data-dismiss="modal">
					<spring:message code="label.close" />
				</button>
				<input type="hidden" name="id" />
			</div>
		</form:form>
	</div>
</div>

</div>

<app:footer>
  <script type="text/javascript">
			$(document).ready(function() {
				// call footable first before hiding the table
				// there is an bug in footable that hides other table elements  		
				//$('.footable').footable();
				
				// for single page crud (modal)
				// for single page crud (inline)
				// for single page crud (new page)
				$("#system-codes-body").RESTful();
				// (1) look for class='add-icon', on click, display class='add-form'. 
				// (1.1) when displaying check if add form is modal, inline, on newpage.
				// (2) look for class='search-form', convert inner forms to jsonSearch.   		
				// (2.1) display search results to class='results-panel' (must contain table)
				// 

				//  		$("system-codes-form").RESTful();

				// for multiple page crud
				//  		$('#system-codes-search').jsonSearch();

				// convert the search form to ajax search
			});

			function checkNewCategory(fromDropdown) {
				var dropDownHTML = '<label for="category"><spring:message code="label.system-codes.category" /></label><select name="category" onclick="javascript:checkNewCategory(true);"><option value="0">Select a Category</option><option value="0">-- New Category --</option><c:forEach items="${categories}" var="record" varStatus="status"><option>${record.category}</option></c:forEach></select>';
				var textFieldHTML = '<label for="category"><spring:message code="label.system-codes.category" /></label><input type="text" name="category" id="categoryText"/> <a href="javascript:void(0)" onclick="javascript:checkNewCategory(false)">Cancel</a>';
				var ni = document.getElementById('categorySelector');
				if (fromDropdown) {
					// check if "New Category" is selected
					var select = document.getElementById('categorySelect');
					if (select.selectedIndex == 1)
						ni.innerHTML = textFieldHTML;
				} else
					ni.innerHTML = dropDownHTML;
			}
			
		</script>
</app:footer>
