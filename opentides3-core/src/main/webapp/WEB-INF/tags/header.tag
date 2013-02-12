<!DOCTYPE html>
<%@ attribute name="title_webpage" required="true" type="java.lang.String"%>
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
<spring:theme code="stylesheet" var="stylesheet" />
<spring:theme code="logo" var="logo" />
<spring:theme code="favicon" var="favicon" />

<html lang="${pageContext.response.locale}">

<head>

	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${client_name} - <spring:message code="${title_webpage}" /></title>
    
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value='${favicon}'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap.min.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-responsive.min.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/footable-0.1.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap-notify.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/opentides-3.0.1.css'/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='${stylesheet}'/>" /> 
    
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    
    <jsp:doBody />

</head>

<body>

	<div id="wrap">
	
		<div id="nav" class="navbar navbar-inverse navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container">
					<a class="btn btn-navbar" data-toggle="collapse"
						data-target=".nav-collapse"> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
					</a>
					<a class="brand" href="${home}">
						<img id="logo" src="<c:url value='${logo}'/>"/>
					</a>
					<div class="nav-collapse collapse">
						<ul class="nav">
							<li class="${active eq 'home' ? 'active' : ''}">
								<a href="${home}">Home</a>
							</li>
							<li class="${active eq 'system-codes' ? 'active' : ''}">
								<a href="${home}/system/system-codes/">System Codes</a>
							</li>
							<li class="${active eq 'system-codes' ? 'active' : ''}">
								<a href="${home}/organization/users/">Users</a>
							</li>
							<li class="${active eq 'system-codes' ? 'active' : ''}">
								<a href="${home}/organization/usergroups/">User Groups</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>

		<div class='notifications top-left'></div>	
	    	<div class='notifications top-right'></div>
	    <div class='notifications system-error'></div>
		<header class="jumbotron">
			<div class="container">
				<h2><spring:message code="${title_webpage}" /></h2>
			</div>
		</header>

		<div id="main" class="container">