<%--
	- tokenizer.tag
	- Generates form tokenizer element
	
	This is especially convenient in the tagging usecase
	where the user can quickly enter a number of tags by
	separating them with a comma or a space.
	
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>

<div class="control-group">

	<form:label path="${path}" cssClass="control-label" cssErrorClass="highlight-error">
		<spring:message code="${label}"/>
		<c:if test="${required}">
			<span class="required"><spring:message code="label.required-field" /></span>
		</c:if>
	</form:label>
	
	<div class="controls">
		<form:hidden path="${path}" cssClass="${cssClass} tokenizer" cssStyle="width: 220px;" />
		<p class="help-block"><small><spring:message code="message.tokenizer-help"/></small></p>
	</div>
	
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$('#${path}.tokenizer').select2({
            tags: [],
            tokenSeparators: [",", " "]
		});
	});
</script>