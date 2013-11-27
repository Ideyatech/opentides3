<%--
	- auto_complete.tag
	- Generates form input element that generates bootstrap typeahead
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="pathValue" required="true" type="java.lang.String" %>
<%@ attribute name="source" required="false" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="placeholder" required="false" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>

<div class="control-group">

	<form:label path="${pathValue}" cssClass="control-label" cssErrorClass="highlight-error">
		<spring:message code="${label}"/>
		<c:if test="${required}">
			<span class="required"><spring:message code="label.required-field" /></span>
		</c:if>
	</form:label>
	
	<div class="controls">
	
		<div class="ot-typeahead-wrapper">
			<input type="text" 
				   class="ot-typeahead ${cssClass}"
				   id= "${pathValue}" 
				   name="${pathValue}"
				   placeholder="${placeholder}"
				   autocomplete="off"
				   />
			<form:hidden path="${path}"/>
		</div>
		
	</div>
	
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$('#${pathValue}').typeahead({
			remote : '${source}',
		}).on('typeahead:selected', function(e, datum) {
			$(this).parents(".ot-typeahead-wrapper:first").find('#${path}').val(datum.key);
		});
	});
</script>
