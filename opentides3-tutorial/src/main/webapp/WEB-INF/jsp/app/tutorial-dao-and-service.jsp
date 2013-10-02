<%--
	- tutorial.jsp
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

<app:header pageTitle="label.tutorial" active="java-classes">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
	<style>
		code.prettyprint{
			background-color: #fff;
			border: 0;
			padding: 0;
		}
		
		code.prettyprint span.kwd{
			font-weight: bold;
		}
	</style>
</app:header>

<ul class="breadcrumb">
  <li>
  	<a href="${home}"><spring:message code="label.home"/></a> 
  	<span class="divider">/</span>
  </li>
  <li>
  	<spring:message code="label.tutorial"/>
  </li>
  
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Classes & Interface</li>
				<li class="active"><a id="scroll-baseEntity">BaseEntity</a></li>
				<li><a id="scroll-baseEntityDao">BaseEntityDao</a></li>
				<li><a id="scroll-baseEntityDaoJpaImpl">BaseEntityDaoJpaImpl</a></li>
				<li><a id="scroll-baseCrudService">BaseCrudService</a></li>
				<li><a id="scroll-baseCrudServiceImpl">BaseCrudServiceImpl</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> Guide in using Open Tides 3</h1>
		
		<h3 id="baseEntity">Base Entity (Class)</h3>
		<p>This is the base class for all entity objects (model) of open-tides. This class contains all the basic attributes such as
		id, createDate, updateDate etc. Every bean within Open Tides that would be persisted should extend this class.</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					@Entity<br/>
					@Table(name="NINJA")<br/>
					@Auditable<br/>
					public class Ninja extends BaseEntity{
					<br/><br/>
					}
				</code>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<ol>
					<li>Create a class and extend the class <code>BaseEntity</code> from <code>org.opentides.bean</code></li>
					<li>Annotate the whole class with <code>@Entity</code> from <code>javax.persistence</code> if you plan to save an instance of your model into the database</li>
					<li>Specify the name of the table by adding the <code>@Table(name="YOURNAME")</code> from <code>javax.persistence</code></li>
					<li>Make it auditable by adding <code>@Auditable</code> from <code>org.opentides.annotation</code> at the class level</li>
				</ol>
			</div>
		</div>
		
		<hr/>
		
		<h3 id="baseEntityDao">Base Entity Dao (Interface)</h3>
		<p>An interface that contains the basic CRUD functionalities. These functions are to be implemented by the implementing class.</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					public interface NinjaDao extends BaseEntityDao&lt;Ninja, Long&gt; {
					<br/><br/>
					}
				</code>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<ol>
					<li>Create a interface and extend the class <code>BaseEntityDaoJpaImpl</code> from <code>org.opentides.dao</code></li>
					<li>Inside the <code>&lt;BaseEntity, Serializable&gt;</code>, supply your bean from earlier for the BaseEntity and <code>Long</code> as the Serializeable value</li>
				</ol>
			</div>
		</div>
		
		<div class="alert alert-info">
			<strong>Heads up!</strong> CRUD stands for <strong>C</strong>reate, <strong>R</strong>ead, <strong>U</strong>pdate, <strong>D</strong>elete
		</div>
		
		<hr/>
		
		<h3 id="baseEntityDaoJpaImpl">Base Entity Dao JPA Implementation (Class)</h3>
		<p>A class that implements the BaseEntityDao interface.</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					@Repository(value="ninjaDao")<br/>
					public class NinjaDaoJpaImpl extends BaseEntityDaoJpaImpl&lt;Ninja, Long&gt; implements NinjaDao {
					<br/><br/>
					}
				</code>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<ol>
					<li>Create a class and extend <code>BaseEntityDaoJpaImpl</code> from <code>org.opentides.dao.impl</code></li>
					<li>Inside the <code>&lt;BaseEntity, Serializable&gt;</code>, supply your bean from earlier for the BaseEntity and <code>Long</code> as the Serializeable value</li>
					<li>Implement your earlier DAO interface</li>
					<li>Annotate the whole class with <code>@Repository</code> then add a <code>value</code> equivalent to the name of your earlier dao
				</ol>
			</div>
		</div>
		
		<hr/>
		
		<h3 id="baseCrudService">Base CRUD Service (Interface)</h3>
		<p>This is the interface for base class of all CRUD service modules.</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					public interface NinjaService extends BaseCrudService&lt;Ninja&gt; {
					<br/><br/>
					}
				</code>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<ol>
					<li>Create an interface and extend the <code>BaseCrudService</code> from <code>org.opentides.service</code></li>
					<li>Inside the <code>&ltBaseEntity&gt</code>, specify your earlier bean</li>
				</ol>
			</div>
		</div>
		
		<hr/>
		
		<h3 id="baseCrudServiceImpl">Base CRUD Service Implementation (Class)</h3>
		<p>This is a base implementation that supports CRUD operations.</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					@Service("ninjaService")<br/>
					@CrudSecure<br/>
					public class NinjaServiceImpl extends BaseCrudServiceImpl&lt;Ninja&gt; implements NinjaService {
					<br/><br/>
					}
				</code>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<ol>
					<li>Create a class and extend <code>BaseCrudServiceImpl</code> from <code>org.opentides.service.impl</code></li>
					<li>Inside the <code>&lt;BaseEntity&gt;</code>, specify your earlier bean</li>
					<li>Implement your earlier service interface</li>
					<li>Annotate the class with <code>@CrudSecure</code> from <code>org.opentides.annotation</code> and <code>@Service</code> from <code>org.springframework.stereotype</code>. Give <code>@Service</code> a value equivalent to the name of your interface</li>
				</ol>
			</div>
		</div>
		
		<hr/>
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
		
		$('.show-tooltip').tooltip();
	</script>
</app:footer>