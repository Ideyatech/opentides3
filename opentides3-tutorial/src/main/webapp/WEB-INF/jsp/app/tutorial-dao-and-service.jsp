<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.dao-and-service" active="basics">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<ul class="breadcrumb">
	<li>Basics<span class="divider">/</span></li>
	<li>DAO & Services</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Navigation</li>
				<li class="active"><a id="scroll-baseEntityDao">BaseEntityDao</a></li>
				<li><a id="scroll-baseEntityDaoJpaImpl">BaseEntityDaoJpaImpl</a></li>
				<li><a id="scroll-baseCrudService">BaseCrudService</a></li>
				<li><a id="scroll-baseCrudServiceImpl">BaseCrudServiceImpl</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> DAO's and Services</h1>
		<p class="lead">
			DAO pattern is used to separate low level data accessing API or operations from high level business services
			while Service pattern aims to organize the services, within a service inventory, into a set of logical layers
		</p>
		
		<h3 id="baseEntityDao">Base Entity Dao (Interface)</h3>
		<p>An interface that contains the basic CRUD functionalities. These functions are to be implemented by the implementing class.</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					public interface PatientDao extends BaseEntityDao&lt;Patient, Long&gt; {
					<br/><br/>
					}
				</code>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<ol>
					<li>Create an interface under the package <span class="code-emphasize">org.tutorial.tatiana.dao</span> and extend the class <code>BaseEntityDaoJpaImpl</code> from <code>org.opentides.dao</code></li>
					<li>Inside the <code>&lt;BaseEntity, Serializable&gt;</code>, supply <span class="code-emphasize">Patient</span> for the BaseEntity and <span class="code-emphasize">Long</span> as the Serializeable value</li>
				</ol>
			</div>
		</div>
		
		<div class="alert alert-info">
			By convention, we name our DAO's as "YourClass"+"Dao". Example: PatientDao
		</div>
		
		<hr/>
		
		<h3 id="baseEntityDaoJpaImpl">Base Entity Dao JPA Implementation (Class)</h3>
		<p>A class that implements the BaseEntityDao interface.</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					@Repository(value="patientDao")<br/>
					public class PatientDaoJpaImpl extends BaseEntityDaoJpaImpl&lt;Patient, Long&gt; implements PatientDao {
					<br/><br/>
					}
				</code>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<ol>
					<li>Create a class under the package <span class="code-emphasize">org.tutorial.tatiana.dao.impl</span> and extend <code>BaseEntityDaoJpaImpl</code> from <code>org.opentides.dao.impl</code></li>
					<li>Inside the <code>&lt;BaseEntity, Serializable&gt;</code>, supply <span class="code-emphasize">Patient</span> as the BaseEntity and <span class="code-emphasize">Long</span> as the Serializeable value</li>
					<li>Implement <span class="code-emphasize">PatientDao</span></li>
					<li>Annotate the whole class with <code>@Repository</code> then add a value of <span class="code-emphasize">patientDao</span></li>
				</ol>
			</div>
		</div>
		<div class="alert alert-info">
			<span class="code-emphasize">@Repository</span> tells Spring that the annotated class should be treated as a DAO
		</div>
		
		<hr/>
		
		<h3 id="baseCrudService">Base CRUD Service (Interface)</h3>
		<p>This is the interface for base class of all CRUD service modules.</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					public interface PatientService extends BaseCrudService&lt;Patient&gt; {
					<br/><br/>
					}
				</code>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<ol>
					<li>Create an interface under the package <span class="code-emphasize">org.tutorial.tatiana.service</span> and extend the <code>BaseCrudService</code> from <code>org.opentides.service</code></li>
					<li>Inside <code>&ltBaseEntity&gt</code>, specify your <span class="code-emphasize">Patient</span> bean</li>
				</ol>
			</div>
		</div>
		
		<div class="alert alert-info">
			<strong>Heads up!</strong> CRUD stands for <strong>C</strong>reate, <strong>R</strong>ead, <strong>U</strong>pdate, <strong>D</strong>elete
		</div>
		
		<hr/>
		
		<h3 id="baseCrudServiceImpl">Base CRUD Service Implementation (Class)</h3>
		<p>This is a base implementation that supports CRUD operations.</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					@Service("patientService")<br/>
					public class PatientServiceImpl extends BaseCrudServiceImpl&lt;Patient&gt; implements PatientService {
					<br/><br/>
					}
				</code>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<ol>
					<li>Create a class under the package <span class="code-emphasize">org.tutorial.tatiana.service.impl</span> and extend <code>BaseCrudServiceImpl</code> from <code>org.opentides.service.impl</code></li>
					<li>Inside <code>&lt;BaseEntity&gt;</code>, specify <span class="code-emphasize">Patient</span></li>
					<li>Implement <span class="code-emphasize">PatientService</span></li>
					<li>Annotate the class with <code>@Service</code> from <code>org.springframework.stereotype</code>. Give <code>@Service</code> a value of <span class="code-emphasize">patientService</span></li>
				</ol>
			</div>
		</div>
		<div class="alert alert-info">
			<span class="code-emphasize">@Service</span> tells Spring that the annotated class should be treated on the Service layer.
		</div>
		
		<hr/>
		
		<h2><i class="icon-puzzle-piece"></i> Wait, what did just happen?</h2>
		<p>
			Basically, you've just implemented all the CRUD functionalities here in the back end. It includes the following: 
		</p>	
		<ul>
			<li>Creating a new Patient item</li>
			<li>Viewing a list of patients</li>
			<li>Updating a patient's information</li>
			<li>Deleting a patient from the list</li>
			<li>Searching for a patient</li>
		</ul>
			
		<p>
			All we need now is the front end side to make it work! 
			In the next chapter, we'll create the CRUD page for our Patient class and a controller that will handle all our request.
		</p>
		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/entities-and-attributes">&larr; Previous (Entities & Attributes)</a>
			</li>
			<li class="next">
				<a href="${home}/crud-controller">Next (CRUD Controller) &rarr;</a>
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
		
		$('.show-tooltip').tooltip();
	</script>
</app:footer>