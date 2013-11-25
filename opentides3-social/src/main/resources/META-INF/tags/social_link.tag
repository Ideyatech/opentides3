<%--
	- social_link.tag
	- JSP Tag that handles linking of Social Media Account and displaying of Social Media button
	
	- @rabanes
--%>

<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- Need current user to check if has access to social linking. -->
<jsp:useBean id="currentUser" class="org.opentides.util.SecurityUtil" scope="request"/>

<%@ attribute name="socialType" required="true" type="java.lang.String" %>
<%@ attribute name="user" required="true" type="org.opentides.bean.user.BaseUser"%>

<!-- NOTE : toLowercase the socialType -->
<!-- Values must be : FACEBOOK, GOOGLE, and TWITTER -->
<c:set var="currentSocial" value="${fn:toLowerCase(socialType)}" /> 
<c:set var="social" value="" />

<c:catch var="exception">
	<c:forEach items="${user.socialCredentials}" var="socialCredential">
		<c:if test="${fn:toUpperCase(currentSocial) eq socialCredential.socialType}">
			<c:set var="social" value="${socialCredential.socialType}"/>
		</c:if>
	</c:forEach>
</c:catch>

<div class="clearfix">
    <i class="pull-left icon-${currentSocial}-sign icon-3x ${not empty social ? 'connected' : ''}"></i>
    <c:choose>
        <c:when test="${not empty social}">
            <i class="icon-ok"></i> <spring:message code="label.account-settings.connected"/><br/>
            <c:if test="${currentUser.user.id eq user.id}">
            	<small><a class="btn-unlink-${socialType}" href="${home}/${currentSocial}/unlink"><spring:message code="label.account-settings.disconnect-${currentSocial}"/></a></small>
            </c:if>
        </c:when>
        <c:otherwise>
            <small><spring:message code="label.account-settings.not-connected"/></small><br/>
            <c:if test="${currentUser.user.id eq user.id}">
            	<a href="${home}/${currentSocial}/link"><spring:message code="label.account-settings.connect-${currentSocial}"/></a>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>
		              
<script type="text/javascript">

	$(document).ready(function() {
		
		$(".btn-unlink-${socialType}").on("click", function(e) {
			e.preventDefault();
			var disconnectLink = $(this).attr("href");
			bootbox.dialog('<spring:message code="message.confirm-disconnection"/>',
			[{"label" : "Disconnect",
			  "class" : "btn-danger",
			  "callback" : function() {
			 		window.location.href = disconnectLink;
				}
			},
			{"label" : "Cancel",
			 "class" : "btn"
			}]);
		});
		
	});

</script>		                        