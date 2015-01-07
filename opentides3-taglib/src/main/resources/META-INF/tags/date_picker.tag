<%--
	- date_picker.tag
	- Generates form input element with bootstrap date picker
--%>
<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="required" required="false" type="java.lang.Boolean" %>
<%@ attribute name="type" required="false" type="java.lang.String" %>
<%@ attribute name="placeholder" required="false" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="format" required="false"  type="java.lang.String" %>
<%@ attribute name="autoClose" required="false" type="java.lang.String" %>

<c:if test="${empty format}">
	<c:set var="format" value="mm/dd/yyyy" />
</c:if>

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
		var $target = $('*[id="ot-${path}"]');
		$target.datepicker({
			format: "${format}",
			autoclose : ${empty autoClose ? false : true},
			todayBtn : true
		});
		$target.siblings().on("click", function(){
			$target.focus();
		});
		
		var dateValue = $target.find('[name="${path}"]').val();
		if(dateValue){
			$target.datepicker('setDate', new Date(dateValue));	
		}
	});
</script>