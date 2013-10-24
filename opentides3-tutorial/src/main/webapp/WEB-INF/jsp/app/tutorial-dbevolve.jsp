<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.dbevolve" active="advanced">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<ul class="breadcrumb">
	<li>Advanced <span class="divider">/</span></li>
	<li>DB Evolve</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div class="alert alert-info">
			<strong>Heads up!</strong><br/>DB Evolves are not limited to a single file only. You can create as many as you want. Just make sure you made it in a sequential order.
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> DB Evolve</h1>
		<p class="lead">
			DB Evolves are special classes from Opentides 3 that helps developers to populate the database in advance with pre-defined contents.
			Most Common use of DB Evolves are countries, cities, and even genders.
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
		
		<hr/>
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/getting-started">&larr; Go Back</a>
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