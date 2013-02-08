<%@ tag dynamic-attributes="attributes" isELIgnored="false" body-content="scriptless" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

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

<script type="text/javascript" src="<c:url value='/js/jquery-1.9.0.min.js'/>"></script>	
<script type="text/javascript" src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/footable-0.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/bootstrap-notify.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/opentides-3.0.1.js'/>"></script>

<jsp:doBody /> 

</body>

</html>