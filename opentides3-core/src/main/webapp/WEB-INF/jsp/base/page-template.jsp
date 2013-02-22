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
	
<app:header pageTitle="label.empty" active=""/> <!-- Do not forget to define id of body -->

<div id="search-body">

	<div id="search-panel" class="span3">
		<app:search-panel formId="system-codes-search">
			<!-- Insert search fields here -->
		</app:search-panel>
	</div>
	
	
	<div id="results-panel" class="span9">
		
		<app:results-status />
		<app:add-entry buttonId="add-system-codes"/>
		
		<app:results-table tableId="system-codes-results">
			<thead>
				<!-- Declare table headers here -->
			</thead>
			<tbody>
				<!--  Iterate results here -->
			</tbody>
		</app:results-table>
		
		<app:pagination />

	</div>
	
</div>

<div id="form-body">

	<app:form-panel formId="system-codes-form">
		<!-- Insert form fields here -->
	</app:form-panel>
	
</div>

<app:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#system-codes-body").RESTful();
		});
	</script>
</app:footer>
