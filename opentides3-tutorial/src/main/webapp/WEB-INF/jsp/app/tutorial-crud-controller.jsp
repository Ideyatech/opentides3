<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.crud-controller" active="basics">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>


<div class="row-fluid">
	<!-- NAVIGATION -->
	<div id="sideBar" class="span2">
		<div id="nav-list-wrapper" class="affix">
			<ul class="nav nav-list side-nav">
				<li><a href="#basecrudcontroller">Base Crud Controller</a></li>
				<li><a href="#systemCodesService">SystemCodes Service</a></li>
				<li><a href="#onLoadSearch">onLoadSearch()</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Basics<span class="divider">/</span></li>
			<li>CRUD Controller</li>
		</ul>
		<h1><i class="icon-book"></i> CRUD Controller</h1>
		
		<section id="basecrudcontroller">
			<h3>BaseCrudController</h3>
			<p>This controller is responsible for all the CRUD functionalities therefore less coding for us.</p>
			<div class="tutorial-body">
				<div class="example">
					<code class="prettyprint">
						@Controller<br/>
						@RequestMapping("/patient")<br/>
						public class PatientCrudController extends BaseCrudController&lt;Patient&gt;{<br/>
						<br/>
						}
					</code>
				</div>
				<div class="highlight">
					<h4>Steps</h4>
					<ol>
						<li>Create a class under the package <span class="code-emphasize">org.tutorial.tatiana.web.controller</span> and extend the class <code>BaseCrudController</code> from <code>org.opentides.web.controller</code>.</li> 
						<li>Supply <span class="code-emphasize">Patient</span> as the BaseEntity.</li>
						<li>Annotate the whole class with <code>@Controller</code> from <span class="code-emphasize">org.springframework.stereotype</span>.</li>
						<li>Also, annotate the whole class with <code>@RequestMapping("/patient")</code>. This will be the URL that this controller could be called upon. <span class="code-emphasize">http://localhost:8080/tatiana/patient</span></li>
					</ol>
				</div>
			</div>
		</section>
		<section id="systemCodesService">
			<h3>SystemCodesService</h3>
			<p>Earlier, we have discussed the class <span class="code-emphasize">SystemCodes</span> and their usage. Now, we will create an instance of it's service.</p>
			<div class="tutorial-body">
				<div class="example">
					<code class="prettyprint">
						@Autowired<br/>
						private SystemCodesService systemCodesService;<br/>
						<br/>
						@ModelAttribute("genderList")<br/>
						public List&lt;SystemCodes&gt; genderList(){<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;List&lt;SystemCodes&gt; genderList = new ArrayList&lt;SystemCodes&gt;();<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;genderList = systemCodesService.findSystemCodesByCategory("GENDER");<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;return genderList;<br/>
						}
					</code>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<ol>
						<li>First, we must define a <code>SystemCodesService</code> inside our controller. This service will be used when we are dealing with <span class="code-emphasize">SystemCodes</span>.</li>
						<li>By adding the <code>@Autowired</code> annotation, we have injected our instance of that service into our controller. This is also known as <span class="code-emphasize">Dependency Injection</span> in Spring.</li> 
						<li>Create a function that would return a <span class="code-emphasize">List of SystemCodes</span>. Annotate it with <code>@ModelAttribute("genderList")</code>. This annotation allows us to access the data in the form of expression language by calling <span class="code-emphasize">&#36;{genderList}</span> in our JSP</li>	
						<li>Call the function <code>findSystemCodesByCategory()</code> from the <code>systemCodesService</code>. This function will return a list of all the <span class="code-emphasize">SystemCodes</span> that we have created under the <span class="code-emphasize">GENDER</span> category.</li>
						<li>Return the result.</li>
					</ol>
				</div>
			</div>
		</section>
		
		<section id="onLoadSearch">
			<h3>onLoadSearch()</h3>
			<p>This function is used to initially perform a search on page load. This search will display all the list of items under a specific class.</p>
			<div class="tutorial-body">
				<div class="example">
					<code class="prettyprint">
						@Override<br/>
						protected void onLoadSearch(Patient command, BindingResult bindingResult,<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Model uiModel, HttpServletRequest request,<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;HttpServletResponse response) {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;uiModel.addAttribute("results", search(command, request));<br/>
						}
					</code>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<ol>
						<li><span class="code-emphasize">Override</span> the function onLoadSearch from <span class="code-emphasize">BaseCrudController</span></li>
						<li>Replace the code inside the function with <code>uiModel.addAttribute("results", search(command, request));</code></li>
					</ol>
				</div>
			</div>
		</section>
		
		<hr/>
		
		<h2><i class="icon-coffee"></i> Up next!</h2>
		<p>
			In the next chapter, we will create the supporting JSP page that will complete our whole CRUD process.
		</p>
		
		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/dao-and-service">&larr; Previous (DAO's and Service's)</a>
			</li>
			<li class="next">
				<a href="${home}/crud-view">Next (CRUD View) &rarr;</a>
			</li>
		</ul>
		
	</div>
</div>

<app:footer>
</app:footer>