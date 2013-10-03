<%--
	- tutorial-project-details.jsp
	- Displays a tutorial for using the tags of opentides 3
	-
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="label.tutorial" active="tags"/>

<ul class="breadcrumb">
  <li><a href="${home}"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><a href="${home}/start">Getting Started</a> <span class="divider">/</span></li>
  <li>Project Overview</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Getting to know</li>
				<li class="active"><a id="scroll-hello">Hello Tatiana!</a></li>
				<li><a id="scroll-functionalities">What can she do?</a></li>
				<li><a id="scroll-components">Meet the objects</a></li>
				<li><a id="scroll-diagram">Class Diagram</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> Get to know Tatiana</h1>
		<p>In this section, you will learn the basic specification of our project <span class="code-emphasize">Tatiana</span>.</p>
		<hr/>
		
		<h3 id="hello">Hello Tatiana!</h3>
		<p>
			Tatiana is a simple web app that will help small time dental clinics to manage all their records and keep track of their patient's upcoming appointments.
		</p>
		
		<hr/>
		
		<h3 id="functionalities">What can she do?</h3>
		<p>Tatiana will be simple, fast and clean. It can do the following: </p>
		<ul>
			<li>Provide a list of all existing patients.</li>
			<li>Create and manage a patient records</li>
			<li>Users are required to login to access the records</li>
			<li>Provide a list of all treatments that the clinic does</li>
			<li>Create and manage treatments</li>
			<li>Adding of an appointment record to a patient</li>
		</ul>
		
		<hr/>
		
		<h3 id="components">Meet the objects!</h3>
		<ul>
			<li>Dentist - He is the user of the whole application. He has his own login credentials.</li>
			<li>Patient - This object will be the primary star of our application.</li>
			<li>Treatment - This will represent the treatments present in the clinic such as tooth extraction, cleaning etc etc</li>
			<li>Appointment - This is the record that will be added to a patient. It would contain the treatment and other details</li>
		</ul>
		<hr/>
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/start">&larr; Previous (Getting Started)</a>
			</li>
			<li class="next">
				<a href="${home}/java-classes">Next (Basic classes) &rarr;</a>
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
			}, 1500);
		}
	</script>
</app:footer>