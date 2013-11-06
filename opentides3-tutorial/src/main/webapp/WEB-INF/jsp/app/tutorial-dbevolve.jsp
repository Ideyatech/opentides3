<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.dbevolve" active="basics">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div class="alert alert-info">
			<strong>Heads up!</strong><br/>DB Evolves are not limited to a single file only. You can create as many as you want. Just make sure you made it in a sequential order for consistency.
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Basics <span class="divider">/</span></li>
			<li>DB Evolve</li>
		</ul>
		
		<h1><i class="icon-book"></i> DB Evolve</h1>
		<p class="lead">
			DB Evolves are special classes from Opentides 3 that helps developers populate or alter the database in advance with pre-defined contents.
			They ensure that database schema and data is consistently maintained across several releases and multiple servers.
		</p>

		<hr/>
		
		<h3>Usage</h3>
		<ol>
			<li>Create the package <span class="code-emphasize">org.tutorial.tathiana.persistence.evolve</span> under <span class="code-emphasize">src/main/java</li>
			<li>Create the class <span class="code-emphasize">DBEvolve0001.java</span>.</li>
			<li>Extend the class <span class="code-emphasize">Evolver</span> from <span class="code-emphasize">org.opentides.persistence.evolve</span> and implement the interface <span class="code-emphasize">DBEvolve</span> from <span class="code-emphasize">org.opentides.persistence.evolve</span></li>
			<li>There will be 3 functions to be implemented. The first one is <code>getVersion()</code>. In here, we just simply specify the version number of our DBEvolve. In this case, it would be <span class="code-emphasize">1</span></li>
			<li>The second function would be <code>getDescription()</code>. In here, we just return a summary of what the DB Evolve is all about.</li>
			<li>
				The last function would be <code>execute()</code>. In here, we write codes that will be implemented by the DB Evolve. See the example below
				<div class="example">
					<code class="prettyprint">
						@Override<br/>
						@Transactional<br/>
						public void execute() {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;SystemCodes male = new SystemCodes("GENDER", "MALE", "Male");<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;SystemCodes female = new SystemCodes("GENDER", "FEMALE", "Female");<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;systemCodesService.save(male, true);<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;systemCodesService.save(female, true);<br/>
						}<br/>
					</code>
				</div>
			</li>
			<li>
				After creating the class file, we must declare our DB Evolve into the <span class="code-emphasize">applicationContext-evolve.xml</span>.
				This is located in <span class="code-emphasize">src/main/resources->app->spring</span>
			</li>
			<li>
				We define our DBEvolve inside the <code>List</code>. See example below:
				<div class="example">
					<code class="prettyprint">
						&lt;list&gt;<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&lt;bean class="org.tutorial.tathiana.persistence.evolve.DBEvolve0001"&gt;&lt;/bean&gt;<br/>
						&lt;/list&gt;<br/>
					</code>
				</div>
			</li>
		</ol>
		
		<div class="alert alert-info">
			<strong>Don't..</strong> Don't ever try to forget incrementing the version number when creating multiple DB Evolves. Something bad will happen (it won't work).
		</div>
		
		<hr/>
		
		<h2><i class="icon-cloud"></i> Jogging</h2>
		<p>Jogging is a form of body exercise. Exercise is good. Therefore its time for your coding exercise <i class="icon-code"></i></p>
		<p>Create a DB Evolve that would add the following Asian countries into your database as <span class="code-emphasize">System Codes</span></p>
		<ul>
			<li>Japan</li>
			<li>Korea</li>
			<li>Philippines</li>
			<li>Singapore</li>
			<li>Taiwan</li>
			<li>Thailand</li>
		</ul>
		<p>Check if you have done it properly by visiting the <span class="code-emphasize">System Codes</span> tab in your header.</p>
		<p>
			In the following chapter, we would talk about <span class="code-emphasize">User Management</span> which would help
			us understand <span class="code-emphasize">usergroups</span> and <span class="code-emphasize">authorities</span> to prevent other users from accessing valuable information.
		</p>
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/validation">&larr; Previous (Validation)</a>
			</li>
			<li class="next">
				<a href="${home}/user-management">Next (User Management)&rarr;</a>
			</li>
		</ul>
		
		
	</div>
</div>

<app:footer>
</app:footer>