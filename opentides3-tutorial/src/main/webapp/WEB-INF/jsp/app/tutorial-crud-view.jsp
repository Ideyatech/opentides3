<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.crud-view" active="basics">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div id="sideBar" class="span2">
		<div id="nav-list-wrapper" class="affix">
			<ul class="nav nav-list side-nav">
				<li><a href="#create">Creating the JSP</a></li>
				<li><a href="#dataSummary">Data Summary</a></li>
				<li><a href="#dataTable">Data Table</a></li>
				<li><a href="#searchPanel">Search Panel</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Basics<span class="divider">/</span></li>
			<li>CRUD View</li>
		</ul>
		
		<h1><i class="icon-book"></i> CRUD Pages</h1>
		<p class="lead">
			In this chapter, we create and discuss the components of a CRUD page in JSP and all of its requirements.
		</p>
		
		<section id="create">
			<h3>Creating the JSP</h3>
			<p>We have provided a template page that will help you in creating your own CRUD pages.</p>
			<h4>Steps</h4>
			<ol>
				<li>Create the file <span class="code-emphasize">patient-crud.jsp</span> in <span class="code-emphasize">src->main->webapp->WEB-INF->jsp->app</span>.</li>
				<li>Search for the file <span class="code-emphasize">template-crud-page.jsp</span> in <span class="code-emphasize">src->main->webapp->WEB-INF->jsp->template</span> and copy all of its content into your <span class="code-emphasize">patient-crud.jsp</span></li>
			</ol>
		</section>
		
		<section id="dataSummary">
			<h3>Data Summary</h3>
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
		</section>
		
		<section id="dataTable">
			<h3>Data Table</h3>
			<p>
				The data table displays the list of patients we currently have. As a developer, we program what data would be visible here. This table would also
				be responsible for displaying search results.
			</p>
			
			<h4>Steps</h4>
			<ol>
				<li>Search for the <span class="code-emphasize">div</span> with the class <span class="code-emphasize">table-wrapper</span></li>
				<li>Change all the <span class="code-emphasize">%%%%</span> texts within the div with <code>patient</code>.</li>
				<li>Below the <code>&lt;!-- Define headers here --&gt;</code>, we define our table headers.</li>
				<li>
					In the example below, i've shown how to create a header for the <span class="code-emphasize">first name</span> and the <span class="code-emphasize">last name</span>. Later on we'll add more.
					<div class="example">
						<code class="prettyprint">
							&lt;th data-field-name="firstName"&gt;&lt;spring:message code="label.patient.firstName"/&gt;&lt;/th&gt;<br/>
							&lt;th data-field-name="lastName"&gt;&lt;spring:message code="label.patient.lastName"/&gt;&lt;/th&gt;
						</code>
					</div>
				</li>
				
				
				<li>For each <span class="code-emphasize">th</span> we create, we define a <span class="code-emphasize">td</span> for its data.</li>
				<li>One way is through <span class="code-emphasize">Moustache</span>: <code>&lt;td&gt;{{firstName}}&lt;/td&gt;</code></li>
				<li>Another way is through the normal <span class="code-emphasize">expression language</span>: <code>&#36;{record.firstName}</code></li>
			</ol>
			
			<!-- EXPLAIN DATA FIELD NAME -->
			
			<div class="alert alert-info">
				<strong>Remember!</strong> The value for moustache and expression language should be written the same as how your attribute is defined.
			</div>
		</section>
		
		<section id="searchPanel">
			<h3>Search Panel</h3>
			<p>
				This part of the page provides us with a working search field depending on what parameters we specify. Results will be displayed on the <span class="code-emphasize">Data Table</span>.
			</p>
			<div class="tutorial-body">
				<div class="example">
					<code class="prettyprint">
						&lt;tides:input path="firstName" label="label.patient.firstName" cssClass="input-block-level"/&gt;<br/>
					    &lt;tides:input path="lastName" label="label.patient.lastName" cssClass="input-block-level"/&gt;
					</code>
				</div>
				<div class="highlight">
					<h4>Steps</h4>
					<ol>
						<li>Search for the div with the ID <span class="code-emphasize">search-panel</span>.</li>
						<li>Change all the <span class="code-emphasize">%%%%</span> texts within the div with <code>patient</code>.</li>
						<li>Take a look at <code>&lt;!-- Define search fields here --&gt;</code>. This is where you will specify your search parameters. In our case, we want to search using either the <span class="code-emphasize">First name</span> or <span class="code-emphasize">Last name</span>.</li>
					</ol>
				</div>
				<div class="alert alert-info">
					<strong>Hey!</strong> Opentides 3 is equipped with various input fields named under the <span class="code-emphasize">tides</span> tag. More on this on the following chapters so no worries!
				</div>
			</div>
		</section>
		
		<hr/>
		
		<h2><i class="icon-bullhorn"></i> A few notes to remember</h2>
		<ul>
			<li>Be sure to change all the <span class="code-emphasize">%%%%</span> with the class being process; in our case it is <span class="code-emphasize">patient</span></li>
			<li>All labels in this jsp should be defined in the <span class="code-emphasize">tatiana.messages.properties</span> file that we have discussed earlier</li>
			<li>Remember to change the values of the header tag depending on your needs.</li>
		</ul>
		<p>If implemented properly, everything should look like this:</p>
		
		<img src="img/tides-patient-crud.png" class="img-polaroid" width="80%"/>

		<p>In the next chapter, we will add forms into our page so that we can create new objects.</p>

		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/crud-controller">&larr; Previous (CRUD Controller)</a>
			</li>
			<li class="next">
				<a href="${home}/crud-form">Next (Adding Form) &rarr;</a>
			</li>
		</ul>
		
	</div>
</div>

<app:footer>
</app:footer>