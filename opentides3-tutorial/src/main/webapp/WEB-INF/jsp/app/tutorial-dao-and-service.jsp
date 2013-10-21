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



<div class="row-fluid">
	<!-- NAVIGATION -->
	<div id="sideBar" class="span2" style="top: 55px;">
		<ul class="nav nav-list side-nav affix">
			<li><a href="#baseEntityDao">BaseEntityDao</a></li>
			<li><a href="#baseEntityDaoJpaImpl">BaseEntityDaoJpaImpl</a></li>
			<li><a href="#baseCrudService">BaseCrudService</a></li>
			<li><a href="#baseCrudServiceImpl">BaseCrudServiceImpl</a></li>
		</ul>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Basics<span class="divider">/</span></li>
			<li>DAO & Services</li>
		</ul>
		
		<h1><i class="icon-book"></i> DAO's and Services</h1>
		<p class="lead">
			DAO pattern is used to separate low level data accessing API or operations from high level business services
			while Service pattern aims to organize the services, within a service inventory, into a set of logical layers
		</p>
		
		<section id="baseEntityDao">
			<h3>Base Entity Dao (Interface)</h3>
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
		</section>
		
		<section id="baseEntityDaoJpaImpl">
			<h3>Base Entity Dao JPA Implementation (Class)</h3>
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
		</section>
		
		<section id="baseCrudService">
			<h3>Base CRUD Service (Interface)</h3>
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
		</section>
		
		<section id="baseCrudServiceImpl">
			<h3>Base CRUD Service Implementation (Class)</h3>
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
		</section>
		
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
</app:footer>