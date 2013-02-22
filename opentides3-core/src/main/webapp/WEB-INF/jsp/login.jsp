<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<html lang="${pageContext.response.locale}">

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${client_name} - <spring:message code="label.login" /></title>

<c:set var="home" value="${pageContext.request.contextPath}"
	scope="application" />
<c:set var="ot_version" value="3.0" scope="application" />

<jsp:useBean id="currentUser" class="org.opentides.util.SecurityUtil"
	scope="request" />

<spring:theme code="client_name" var="client_name" scope="application" />
<spring:theme code="stylesheet" var="stylesheet" />
<spring:theme code="logo" var="logo" />
<spring:theme code="favicon" var="favicon" />



<link rel="shortcut icon" type="image/x-icon"
	href="<c:url value='${favicon}'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/bootstrap.min.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/bootstrap-responsive.min.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/bootstrap-notify.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/opentides-3.0.1.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='${stylesheet}'/>" />

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

</head>
<body>

	<!--Header-->
	<h2>Welcome to Opentides 3.0!</h2>

	<!--Content/Login-->

	<div id="login-body" class="container span12 pagination-centered">
		<div class="span6 offset3">
		<form class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="inputEmail">Email</label>
				<div class="controls input-prepend">
					<span class="add-on"><i class="icon-user"></i></span> 
					<input type="text" id="inputEmail" placeholder="Username">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="inputPassword">Password</label>
				<div class="controls input-prepend">
					<span class="add-on"><i class="icon-lock"></i></span>
					<input type="password" id="inputPassword" placeholder="Password">
				</div>
			</div>
			<div class="control-group">
				<div class="controls input-prepend">
					<label class="checkbox"> <input type="checkbox">
						Remember me
					</label>
					<span class="add-on"><i class="icon-ok"></i></span>
					<button type="submit" class="btn"> Sign in</button>
				</div>
			</div>		
		</form>
		</div>
		<div class="forgot-password">
			<a href="#">Forgot your password?</a>
		</div>
	</div>

	<!--Footer-->
	<div id="footer">
		<div class="container">
			<p class="muted credit">
				<small> &copy; <spring:theme code="system_name" />
					&nbsp;|&nbsp; <spring:message code="label.all-rights-reserved" />
				</small>
			</p>
		</div>
	</div>

	<script type="text/javascript"
		src="<c:url value='/js/jquery-1.9.0.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/js/jquery.deserialize.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/js/bootstrap.min.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/js/bootstrap-notify.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/js/opentides-3.0.1.js'/>"></script>
	<script type="text/javascript">
		function repositionFooter() {
			var docHeight = $(document).height(), footerHeight = $(".footer")
					.height();
			$(".footer").css('position', 'absolute');
			$(".footer").css("top", (docHeight - footerHeight - 10) + "px"); //this will change the top position of footer
		}
		//change "#footer" to corresponding ID
		$(document).ready(repositionFooter);
	</script>
</body>
</html>