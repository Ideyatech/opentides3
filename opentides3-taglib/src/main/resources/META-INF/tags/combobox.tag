<%--
	- combobox.tag
	- Generates form combo box element using select2 plugin
	
	- Comboboxes are similar to selects but the user can add new entries. Awesome.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="selectValue" required="false" type="java.lang.String" %>
<%@ attribute name="selectLabel" required="false" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="items" required="false" type="java.util.Collection" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>

<div class="control-group">

	<form:label path="${path}" cssClass="control-label" cssErrorClass="highlight-error">
		<spring:message code="${label}"/>
		<c:if test="${required}">
			<span class="required"><spring:message code="label.required-field" /></span>
		</c:if>
	</form:label>
	
	<div class="controls">
		<form:hidden path="${path}" cssClass="${cssClass} combobox" cssStyle="width: 220px;" />
		<p class="help-block"><small><spring:message code="message.combobox-help"/></small></p>
	</div>
</div>
<script type="text/javascript">

	$(document).ready(function() {
		<c:choose>
		<c:when test="${not empty selectValue && not empty selectLabel}">
			$.extend(comboBoxTags, {'${path}' : [<c:forEach items="${items}" var="item">{id : "${item[selectValue]}", text : "${item[selectLabel]}" },</c:forEach>]});
		</c:when>
		<c:when test="${not empty selectValue}">
			$.extend(comboBoxTags, {'${path}' : [<c:forEach items="${items}" var="item">"${item[selectValue]}",</c:forEach>]});
		</c:when>
		<c:otherwise>
			$.extend(comboBoxTags, {'${path}' : [<c:forEach items="${items}" var="item">"${item}",</c:forEach>]});
		</c:otherwise>
		</c:choose>
		
		$('#${path}.combobox').select2({
			maximumSelectionSize: 1,
			tags: comboBoxTags['${path}']
		});
	});
</script>
