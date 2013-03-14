<%@ tag dynamic-attributes="attributes" isELIgnored="false" body-content="scriptless" %>
<%@ attribute name="pageType" required="false" type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<c:if test="${pageType != 'modal-page'}">
		</div>
		
		<div id="push"></div>
		
	</div>
	
	<div id="footer">
      <div class="container">
        <p class="muted credit">
        	<small>
        		&copy; <spring:theme code="system_name"/> &nbsp;|&nbsp; <spring:message code="label.all-rights-reserved"/>
        	</small>
        </p>
      </div>
    </div>
</c:if>

<jsp:doBody /> 

</body>

</html>