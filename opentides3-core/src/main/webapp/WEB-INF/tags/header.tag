<!DOCTYPE html>
<%@ attribute name="pageTitle" required="true" type="java.lang.String"%>
<%@ attribute name="active" required="false" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1 
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0 
	response.setDateHeader("Expires", 0); //prevents caching 
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1   
%>
<c:set var="home" value="${pageContext.request.contextPath}" scope="application" />
<c:set var="ot_version" value="3.0" scope="application" />
<jsp:useBean id="currentUser" class="org.opentides.util.SecurityUtil" scope="request"/>
<spring:theme code="client_name" var="client_name" scope="application" />
<spring:theme code="logo" var="logo" />
<spring:theme code="favicon" var="favicon" />

<html lang="${pageContext.response.locale}">

<head>

	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${client_name} - <spring:message code="${pageTitle}" /></title>
    
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='${favicon}'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap.min.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-responsive.min.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/footable-0.1.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-notify.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/opentides-3.0.1.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/select2.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/datepicker.css'/>" />
    
	<script type="text/javascript" src="<c:url value='/js/jquery-1.9.0.min.js'/>"></script>	
	<script type="text/javascript" src="<c:url value='/js/jquery.deserialize.js'/>"></script>	
	<script type="text/javascript" src="<c:url value='/js/bootstrap.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/footable-0.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/bootstrap-notify.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/bootbox.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/bootstrap-datepicker.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/opentides-3.0.1.js'/>"></script>
    
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    
    <jsp:doBody />

</head>

<body>

    <div class='notifications center'></div>

	<div id="wrap">
	
		<div id="nav" class="navbar navbar-inverse navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container">
					<a class="btn btn-navbar" data-toggle="collapse"
						data-target=".nav-collapse"> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
					</a>
					<a class="brand" href="${home}/home">
						<img class="logo" src="<c:url value='${logo}'/>"/>
					</a>
					<div class="nav-collapse collapse">
						<ul class="nav">
							<li class="${active eq 'home' ? 'active' : ''}">
								<a href="${home}/home">
									<i class="icon-home icon-white"></i>
									Home
								</a>
							</li>
							<li class="${active eq 'users' ? 'active' : ''}">
								<a href="${home}/organization/users/">
									<i class="icon-user icon-white"></i>
									Users
								</a>
							</li>
							<li class="${active eq 'system-codes' ? 'active' : ''}">
								<a href="${home}/system/system-codes/">
									<i class="icon-barcode icon-white"></i>
									System Codes
								</a>
							</li>
							<li class="${active eq 'usergroups' ? 'active' : ''}">
								<a href="${home}/organization/usergroups/">
									<i class="icon-globe icon-white"></i>
									User Groups
								</a>
							</li>
							<li class="${active eq 'ninja' ? 'active' : ''}">
								<a href="${home}/ninja/">
									<i class="icon-leaf icon-white"></i>
									Ninja
								</a>
							</li>
							<li class="pull-right">
								<a href='<c:out value="${home}/j_spring_security_logout" />'>
									<spring:message code="label.logout"/>
								</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>

		<header class="jumbotron">
			<div class="container">
				<h2><spring:message code="${pageTitle}" /></h2>
			</div>
		</header>

		<div class="main container">
