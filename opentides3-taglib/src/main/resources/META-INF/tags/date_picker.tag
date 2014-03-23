<%--
	- date_picker.tag
	- Generates form input element with bootstrap date picker
--%>
<%@ tag body-content="empty" dynamic-attributes="dAttrs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="type" required="false" type="java.lang.String" %>
<%@ attribute name="placeholder" required="false" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="format" required="false" type="java.lang.String" %>
<%@ attribute name="autoClose" required="false" type="java.lang.String" %>

<c:forEach items="${dAttrs}" var="attr">
	<c:set var="attrs" value='${attrs} ${attr.key}="${attr.value}"'/>
</c:forEach>

<div class="control-group">

	<form:label path="${path}" cssClass="control-label" cssErrorClass="highlight-error">
		<spring:message code="${label}"/>
		<c:if test="${required}">
			<span class="required"><spring:message code="label.required-field" /></span>
		</c:if>
	</form:label>
	
	<div class="controls">
	
		<div id="ot-${path}" class="date input-append">
	
			<form:input path="${path}" 
				type="${empty type ? 'text': type }"
				placeholder="${placeholder}"
				cssClass="ot-datepicker ${cssClass}"
				/>
			
			<span class="add-on"><i class="icon-calendar"></i></span>
		</div>
		
	</div>
	
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$('*[id="ot-${path}"]').datepicker({
			format: "${empty format ? 'mm/dd/yyyy' : format}",
			autoclose : ${empty autoClose ? false : true},
			todayBtn : true
		});
		$('*[id="ot-${path}"]').siblings().on("click", function(){
			$('*[id="ot-${path}"]').focus();
		});
	});
</script>