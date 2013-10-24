<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.overview" active="setting-up"/>

<ul class="breadcrumb">
	<li>Setting Up<span class="divider">/</span></li>
	<li>Overview</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Project Overview</li>
				<li class="active"><a id="scroll-functionalities">What can she do?</a></li>
				<li><a id="scroll-components">Meet the objects</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> Get to know Tatiana</h1>
		<p class="lead">
			Tatiana is a simple web app that we will create while learning how to use <span class="code-emphasize">Opentides 3.</span> 
			By the end of this tutorial we would have her up and running in our local machine.
		</p>
		
		<hr/>
		
		<h3 id="functionalities">What can she do?</h3>
		<p>Tatiana will be simple, fast and clean. It can do the following: </p>
		<ul>
			<li>Provide a list of all existing patients.</li>
			<li>Create and manage a patient record</li>
			<li>Login</li>
		</ul>
		<p>Basically, <span class="code-emphasize">Tatiana</span> will demonstrate the core features of <span class="code-emphasize">Opentides 3</span> which is CRUD.</p>

		<hr/>
		
		<h3 id="components">Meet the object!</h3>
		<ul>
			<li>Patient</li>
		</ul>
		<p>We will only cover the CRUD page for the patient class. However, the project may be continued into something bigger such as adding treatments to a patient and appointments as well.</p>
		<hr/>
		
		<h2><i class="icon-lightbulb"></i> Up next!</h2>
		<p>In the next chapter, we will create a new project under <span class="code-emphasize">Opentides 3</span>. We will also configure our server and database and then run the project!</p>
		
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}">&larr; Previous (Welcome)</a>
			</li>
			<li class="next">
				<a href="${home}/getting-started">Next (Getting Started) &rarr;</a>
			</li>
		</ul>
	</div>
</div>

<app:footer>

	<script type="text/javascript">
		$(document).ready(function(){
			$('ul.nav li a').click(function(){
				$('ul.nav li').removeClass('active');
				$(this).parent('li').addClass('active');
				var id = $(this).attr("id").split("-")[1];
				scrollToView(id); 
			});
		});
		
		function scrollToView(destinationID){
			$('html, body').animate({
				scrollTop: $("#"+destinationID).offset().top-45
			}, 600);
		}
	</script>
</app:footer>