<%--
	- crud.jsp
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

<app:header pageTitle="label.tutorial" active="tags">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<ul class="breadcrumb">
  <li><a href="${home}"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><a href="${home}/start">Getting Started</a> <span class="divider">/</span></li>
  <li><a href="${home}/start">Getting Started</a> <span class="divider">/</span></li>
  <li>Creating Entities</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Navigation</li>
				<li class="active"><a id="scroll-patient">Patient.java</a></li>
				<li><a id="scroll-appointment">Appointment.java</a></li>
				<li><a id="scroll-treatment">Treatment</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> CRUD JSP Pages</h1>
		<p>In this chapter, we create and discuss the components of a CRUD page in JSP.</p>
		
		<h3 id="baseEntityDao">Messages.properties</h3>
		<p>
			This file will be the default container for all the texts, messages and labels that will be found in your app.
			The purpose of this file is to handle internationalization when the need comes. This is also required in using Opentides 3.
		</p>
		<h3>Step</h3>
		<ol>
			<li>Create a new file under <span class="code-emphasize">src/main/resources -> app -> languages</span>. Name it as <span class="code-emphasize">tatiana.messages.properties</span>.</li>
		</ol>
		
		<div class="alert alert-info">
			<strong>Remember!</strong> Every time you see a "label.something.something" it means that the actual message is inside the properties file.
		</div>
		
		<hr/>
		
		<h3 id="baseEntityDao">Data Summary</h3>
		<p>The data summary displays how fast it took for the system to fetch the data in the table. It also shows which page you are.</p>
		<div class="tutorial-body">
			<div class="example">
				<p>
					Displaying 1 to 2 of 2 records (0.025 seconds)
				</p>
			</div>
			<div class="highlight">
				<h4>Steps</h4>
				<ol>
					<li>Search for the <span class="code-emphasize">div</span> with the ID <code>message-panel</code></li>
					<li>Change all the <span class="code-emphasize">%%%%</span> texts within the div with <code>patient</code>.</li>
				</ol>
			</div>
		</div>
		
		<hr/>
		
		<h3 id="baseEntityDao">Data Table</h3>
		<p>
			The quick brown fox jumps over the lazy dog.
		</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					&lt;th data-field-name="landline"&gt;&lt;spring:message code="label.patient.landline"/&gt;&lt;/th&gt;
				</code>
			</div>
			<div class="highlight">
				<h4>Steps</h4>
				<ol>
					<li>Search for the <span class="code-emphasize">div</span> with the class <span class="code-emphasize">table-wrapper</span></li>
					<li>Change all the <span class="code-emphasize">%%%%</span> texts within the div with <code>patient</code>.</li>
					<li>Below the <code>&lt;!-- Define headers here --&gt;</code>, we define our table headers. In our case, it would be first name, last name, mobile, landline, birthdate</li>
					<li>For each <span class="code-emphasize">th</span> we create, we define a <span class="code-emphasize">td</span> for its data.</li>
					<li>One way is through <span class="code-emphasize">Moustache</span>. Example: <code>&lt;td&gt;{{mobile}}&lt;/td&gt;</code></li>
					<li>Another way is through the normal <span class="code-emphasize">foreach loop</span>. Example: <code>&lt;td&gt;&lt;c:out value="&#36;{record.mobile}"/>&lt;/td&gt;</code></li>
				</ol>
			</div>
		</div>
		
		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/dao-and-service">&larr; Previous (DAO's and Service's)</a>
			</li>
			<li class="next">
				<a href="${home}/dao-and-service">Next (DAO's and Services) &rarr;</a>
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