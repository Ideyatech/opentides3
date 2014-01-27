<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.crud-form" active="basics">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div id="sideBar" class="span2">
		<div id="nav-list-wrapper" class="affix">
			<ul class="nav nav-list side-nav">
				<li><a href="#modal">Modal Form</a></li>
				<li><a href="#page">New Page Form</a></li>
				<li><a href="#fields">Defining Fields</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Basics<span class="divider">/</span></li>
			<li>CRUD Form</li>
		</ul>
		
		<h1><i class="icon-book"></i> CRUD Forms</h1>
		<p class="lead">
			Forms are used to pass data to a server. <span class="code-emphasize">Open-Tides 3</span> supports two types of forms, modal form and new page forms. 
		</p>
		
		<section id="modal">
			<h3>Modal Form</h3>
			<p>They are forms that pop out of the existing page when triggered. By default, the <code>crud-page-template.jsp</code> supports this one.</p>
			<div class="tutorial-body">
				<div class="example">
					<code class="prettyprint">
						&lt;div id="form-body" class="modal fade hide"&gt;<br/>
						&lt;!-- write form contents here --&gt;<br/>
						&lt;/div&gt;
					</code>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<ol>
						<li>Search for the div with the ID <span class="code-emphasize">form-body</span>.</li>
						<li>Change all the <span class="code-emphasize">%%%%</span> texts within the div with <code>patient</code>.</li>
						<li>Below the <code>&lt;!-- Define form fields here --&gt;</code> is where we would add our form fields with the use of <span class="code-emphasize">tides</span> tag. Later in the chapter we'll discuss how.</li>
					</ol>
				</div>
			</div>
		</section>
		
		<section id="page">
			<h3>New Page Form</h3>
			<p>In some cases, you would want to have your form inside a new page rather than a modal, for that we use <span class="code-emphasize">new page form</span></p>
			<h4>Usage</h4>
			<ol>
				<li>Search for the div with the ID <span class="code-emphasize">form-body</span>.</li>
				<li>
					Change the value of its <span class="code-emphasize">class</span> into <span class="code-emphasize">page &#36;{form}</span>
					<div class="example">
						<code class="prettyprint">
							&lt;div id="form-body" class="page &#36;{form}"&gt;
						</code>
					</div>
				</li>
				<li>Change all the <span class="code-emphasize">%%%%</span> texts within the div <span class="code-empahsize">form-body</span> with <code>patient</code>.</li>
				<li>
					Remove the close button. This close button is only useful when working with modal
					<div class="example">
						<code class="prettyprint">
							&lt;button type="button" class="close" data-dismiss="modal"&gt;&times;&lt;/button&gt; <-- REMOVE THIS LINE<br/>
							&lt;h4 class="${add}"&gt;&lt;spring:message code="label.patient.add" /&gt;&lt;/h4&gt;<br/>
							&lt;h4 class="${update}"&gt;&lt;spring:message code="label.patient.update" /&gt;&lt;/h4&gt;<br/>
						</code>
					</div>
				</li>
				<li>Search for the <span class="code-emphasize">Close Button</span> under <span class="code-emphasize">Form Actions</span>, set the value for <span class="code-emphasize">data-dismiss</span> to <span class="code-emphasize">data-dismiss</span>page</li>
			</ol>
		</section>
		
		<section id="fields">
			<h3>Defining Form Fields</h3>
			<p>Now that we have our form, its time to define our fields!</p>
			<h4>Steps</h4>
			<ol>
				<li>Search for <code>&lt;!-- Define form fields here --&gt;</code>. We will add our fields here</li>
				<li>
					Open-Tides 3 provided us with the <span class="code-emphasize">tides</span> library which contains several form fields. 
					For the purpose of this tutorial we'll create a field for the <span class="code-emphasize">first name</span> and <span class="code-emphasize">last name</span> of our patient.
					<div class="example">
						<code class="prettyprint">
							&lt;tides:input label="label.patient.firstName" path="firstName" required="true"/&gt;<br/>
							&lt;tides:input label="label.patient.lastName" path="lastName" required="true"/&gt;
						</code>
					</div>
				</li>
			</ol>
		</section>
		
		<div class="alert alert-info">
			<i class="icon-ok"></i> Inside the form we can find a <strong>&lt;div class="message-container"&gt;&lt;/div&gt;</strong>.
			This will hold error messages due to validation.
		</div>
		
		<hr/>
		
		<h2><i class="icon-trophy"></i> There you go!</h2>
		<p>Our forms are weak by themselves, in the next chapter, we would add validation to filter out unwanted inputs.</p>
		
		
		<img src="img/form-modal.PNG" class="img-polaroid" width="70%"/>
		<br/><br/>
		<img src="img/form-page.PNG" class="img-polaroid" width="70%"/>



		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/crud-view">&larr; Previous (CRUD View)</a>
			</li>
			<li class="next">
				<a href="${home}/validation">Next (Form Validation) &rarr;</a>
			</li>
		</ul>
		
	</div>
</div>

<app:footer>
</app:footer>