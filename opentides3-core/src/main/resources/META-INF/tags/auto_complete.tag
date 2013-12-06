<%--
	- auto_complete.tag
	- Generates form input element that generates bootstrap typeahead
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="pathValue" required="true" type="java.lang.String" %>
<%@ attribute name="url" required="false" type="java.lang.String" %>
<%@ attribute name="parameterName" required="false" type="java.lang.String" %>
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
				   data-provide="typeahead"
				   />
			<form:hidden path="${path}"/>
		</div>
		
	</div>
	
</div>

<script type="text/javascript">
	$(document).ready(function() {
		selectedId = "";
		mapId = {};
		var finished = true;
		$('#${pathValue}').typeahead({
		    source: function(query, process) {
		    	if(typeof(selectedId) != 'undefined' && selectedId != "") {
		    		if(query != "") {
		    			selectedId = "";
		    			$(this).closest(".ot-typeahead-wrapper").find('#${path}').val(selectedId);
		    		}
		    	}
		   		if(!finished) {
		            return;
		        }
		   	 	finished = false;
		   	 	values = [];
		      	$.ajax({
		        	url: "${url}?${parameterName}=" + $.trim(query),
		        	contentType : "application/json",
					dataType : "json",
					cache : false,
		        	success: function(data) { 
		        		$.each(data, function (i, obj) {
		        			mapId[obj.value] = obj.key;
		        	        values.push(obj.value);
		        	    });
		        		process(values);
	       				finished = true;
		    		}
		    	});
		    },
		    matcher: function (item) {
		        if (item.toLowerCase().indexOf($.trim(this.query).toLowerCase()) != -1) {
		            return true;
		        }
		    },
		    sorter: function (items) {
		        return items.sort();
		    },
		    highlighter: function(item) {
		        var regex = new RegExp( '(' + this.query + ')', 'gi' );
		        return item.replace( regex, "<strong>$1</strong>" );
		    }, 
		    updater: function(item) {
		    	selectedId = mapId[item];
		    	$('#${pathValue}').closest(".ot-typeahead-wrapper").find('#${path}').val(selectedId);
		    	return item;
		    }
		}).on("blur", function(){
		    $this = $(this);
		    var item = $(this).val();
		    if(mapId[item] != null) {
		        selectedId = mapId[item];
		        $this.closest(".ot-typeahead-wrapper").find('#${path}').val(selectedId);
		    }
		    $this.typeahead($this);
		});
	});
</script>
