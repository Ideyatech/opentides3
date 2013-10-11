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

<ul class="breadcrumb">
	<li><a href="${home}/overview">Overview</a><span class="divider">/</span></li>
	<li><a href="${home}/getting-started">Getting Started</a><span class="divider">/</span></li>
	<li><a href="${home}/creating-project">Creating Project</a><span class="divider">/</span></li>
	<li><a href="${home}/customize">Customization</a><span class="divider">/</span></li>
	<li><a href="${home}/entities-and-attributes">Entities & Attributes</a><span class="divider">/</span></li>
	<li><a href="${home}/dao-and-service">DAO's & Services</a><span class="divider">/</span></li>
	<li><a href="${home}/crud-controller">CRUD Controller</a><span class="divider">/</span></li>
	<li>CRUD View</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Navigation</li>
				<li class="active"><a id="scroll-create">Creating the JSP</a></li>
				<li><a id="scroll-dataSummary">Data Summary</a></li>
				<li><a id="scroll-dataTable">Data Table</a></li>
				<li><a id="scroll-searchPanel">Search Panel</a></li>
				<li><a id="scroll-newForm">New Form</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> CRUD Pages</h1>
		<p class="lead">
			In this chapter, we create and discuss the components of a CRUD page in JSP and all of its requirements.
		</p>
		
		
		<h3 id="create">Creating the JSP</h3>
		<p>We have provided a template page that will help you in creating your own CRUD pages.</p>
		<h4>Steps</h4>
		<ol>
			<li>Create the file <span class="code-emphasize">patient-crud.jsp</span> in <span class="code-emphasize">src->main->webapp->WEB-INF->jsp->app</span>.</li>
			<li>Search for the file <span class="code-emphasize">template-crud-page.jsp</span> in <span class="code-emphasize">src->main->webapp->WEB-INF->jsp->template</span> and copy all of its content into your <span class="code-emphasize">patient-crud.jsp</span></li>
		</ol>
		
		<hr/>
		
		<h3 id="dataSummary">Data Summary</h3>
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
		
		<h3 id="dataTable">Data Table</h3>
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
		
		<hr/>
		
		<h3 id="searchPanel">Search Panel</h3>
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
		
		<hr/>
		
		<h3 id="newForm">New Form</h3>
		<p>This is the form that is used for creating a new page. Initially it is hidden but then showed when needed.</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					&lt;tides:input label="label.patient.firstName" path="firstName" required="true"/&gt;<br/>
					&lt;tides:input label="label.patient.lastName" path="lastName" required="true"/&gt;
				</code>
			</div>
			<div class="highlight">
				<h4>Steps</h4>
				<ol>
					<li>Search for the div with the ID <span class="code-emphasize">form-body</span>.</li>
					<li>Change all the <span class="code-emphasize">%%%%</span> texts within the div with <code>patient</code>.</li>
					<li>Below the <code>&lt;!-- Define form fields here --&gt;</code> is where we would add our form fields with the use of <span class="code-emphasize">tides</span> tag.</li>
					<li>In the example above, i've shown how to create a field for the <span class="code-emphasize">first name</span> and the <span class="code-emphasize">last name</span>. Later on we'll add more fields to it.</li>
				</ol>
			</div>
			
		</div>
		<p>
			<i class="icon-ok"></i> Inside the form we can find a <code>&lt;div class="message-container"&gt;&lt;/div&gt;</code>. This 
			will hold error messages due to validation.
		</p>
		
		<hr/>
		
		<h2><i class="icon-bullhorn"></i> A few notes to remember</h2>
		<ul>
			<li>Be sure to change all the %%%% with the class being process; in our case it is <span class="code-emphasize">patient</span></li>
			<li>All labels in this jsp should be defined in the <span class="code-emphasize">tatiana.messages.properties</span> file that we have discussed earlier</li>
			<li>Remember to change the values of the header tag depending on your needs.</li>
		</ul>
		<p>If implemented properly, everything should look like this:</p>
		
		<img src="img/tides-patient-crud.png" class="img-rounded" width="800px"/>
		
		<p>In the next chapter, we will add validation to our forms and display error messages.</p>

		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/crud-controller">&larr; Previous (CRUD Controller)</a>
			</li>
			<li class="next">
				<a href="${home}/validation">Next (Form Validation) &rarr;</a>
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