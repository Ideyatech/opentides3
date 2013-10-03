<%--
	- tutorial-creating-entities.jsp
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

<app:header pageTitle="label.tutorial" active="tags">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<ul class="breadcrumb">
  <li><a href="${home}"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><a href="${home}/start">Getting Started</a> <span class="divider">/</span></li>
  <li>Creating Entities</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Navigation</li>
				<li class="active"><a id="scroll-patient">Patient.java</a></li>
				<li><a id="scroll-appointment">Appointment.java</a></li>
				<li><a id="scroll-treatment">Treatment</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> Creating Entities</h1>
		<p>In this chapter, we create all the required classes for Tatiana.</p>
		
		<h3 id="patient">Patient</h3>
		<p>
			As discussed in the previous chapter, this object will be the main focus of our web app. Before we proceed
			in making the Patient class, let me introduce you to the <span class="code-emphasize">BaseEntity</span> class.
		</p>
		<h4 id="">Base Entity</h4>
		<p>
			This is the base class for all entity objects (model) within Opentides. This class contains all the basic attributes such as
			id, createDate, updateDate etc. Every bean within Open Tides that would be persisted should extend this class.
		</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					@Entity<br/>
					@Table(name="PATIENT")<br/>
					@Auditable<br/>
					public class Patient extends BaseEntity{
					<br/><br/>
					}
				</code>
			</div>
		</div>
		<p>Knowing this, we now create our <span class="code-emphasize">Patient</span> class</p>
		<h4>Steps</h4>
		<ol>
			<li>Create the <span class="code-emphasize">Patient</span> class under the package <span class="code-emphasize">org.tutorial.tatiana.bean</span> and extend <code>BaseEntity</code> from <code>org.opentides.bean</code>.</li>
			<li>Annotate the whole class with <code>@Entity</code> from <code>javax.persistence</code>. <code>@Entity</code> allows you to save an instance of your model into the database.</li>
			<li>Specify the name of the table by adding the <code>@Table(name="PATIENT")</code> from <code>javax.persistence</code>.</li>
			<li>Make it auditable by adding <code>@Auditable</code> from <code>org.opentides.annotation</code>.</li>
		</ol>
		
		<hr/>
		
		<h3 id="appointment">Appointment</h3>
		<p>
			This class is the link between the Patient and the type of treatment he has undergone. 
			As discussed earlier, we also implement <span class="code-emphasize">BaseEntity</span> for this class.
		</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					@Entity<br/>
					@Table(name="APPOINTMENT")<br/>
					@Auditable<br/>
					public class Appointment extends BaseEntity{
					<br/><br/>
					}
				</code>
			</div>
			<div class="highlight">
				<h4>Usage</h4>
				<ol>
					<li>Create the <span class="code-emphasize">Appointment</span> class under the package <span class="code-emphasize">org.tutorial.tatiana.bean</span> and extend <code>BaseEntity</code> from <code>org.opentides.bean</code>.</li>
					<li>Annotate the whole class with <code>@Entity</code> from <code>javax.persistence</code>.</li>
					<li>Specify the name of the table by adding the <code>@Table(name="APPOINTMENT")</code> from <code>javax.persistence</code>.</li>
					<li>Make it auditable by adding <code>@Auditable</code> from <code>org.opentides.annotation</code>.</li>
				</ol>
			</div>
		</div>
		
		<hr/>
		
		<h3 id="treatment">Treatment</h3>
		<p>
			We will not have a new class for the Treatment object since this is already handled by <span class="code-emphasize">SystemCodes</span>.
			They are predefined classes within Opentides that handles specific tasks and are easy to use. Anyway, you'll find out more about them in the following chapters.
		</p>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					@JoinColumn(name = "TREATMENT_ID")<br/>
					private SystemCodes treatment;
				</code>
			</div>
		</div>
		
		<hr/>
		
		<h2><i class="icon-thumbs-up"></i> Up next</h2>
		<p>
			In the next chapter, we will define all the attributes for our classes and learn about <span class="code-emphasize">SystemCodes</span>
		</p>
		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/start">&larr; Previous (Getting Started)</a>
			</li>
			<li class="next">
				<a href="${home}/defining-attributes">Next (Defining Attributes) &rarr;</a>
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
			}, 1500);
		}
	</script>
</app:footer>