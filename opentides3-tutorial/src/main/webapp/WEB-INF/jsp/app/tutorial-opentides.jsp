<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.overview" active="opentides"/>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2"">
		<div class="alert alert-info">
			<strong>Open-Tides 3</strong> is used by more than 10,000 users worldwide.
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Opentides 3</li>
		</ul>
		
		<h1><i class="icon-book"></i> Open-Tides 3</h1>
		<p class="lead">
			Open-Tides 3 is a web foundation framework that can be used to quickly setup a web application using Spring MVC and JPA.
			It is inspired by several frameworks such as Ruby on Rails, AppFuse, Grails, Hibernate and Spring. 
			It follows the Web Foundation Framework which promotes reusability of codes and code generation through scaffolding.
		</p>	

		<section id="functionalities">
			<h3>What can it do?</h3>
			<p>Open-Tides 3 provides a lot of features that could quickly help you in setting up your web application.</p>
			<ul>
				<li>User Management</li>
				<li>Built-In Security</li>
				<li>CRUD Support</li>
				<li>Search Support</li>
				<li>Row Level Access Security</li>
				<li>Audit Logging</li>
				<li>System Codes</li>
				<li>Database Evolve Support</li>
			</ul>
		</section>
		
		<hr/>
		
		<h2><i class="icon-female"></i> Coming up next</h2>
		<p>In the next chapter i'll introduce you to <span class="code-emphasize">Tatiana</span>. Who is she? Find out!</p>
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}">&larr; Previous (Welcome)</a>
			</li>
			<li class="next">
				<a href="${home}/overview">Next (Tatiana) &rarr;</a>
			</li>
		</ul>
	</div>
</div>

<app:footer>
</app:footer>