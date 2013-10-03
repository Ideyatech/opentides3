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
  <li><a href="${home}/start">Creating Entities</a> <span class="divider">/</span></li>
  <li>Defining Attributes</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Navigation</li>
				<li class="active"><a id="scroll-systemCodes">System Codes</a></li>
				<li><a id="scroll-definingAttributes">Defining Attributes</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> Defining Attributes</h1>
		<p>Defining attributes in Opentides 3 doesn't vary it all from our normal coding practice. However, there are a few steps to be followed.</p>
		
		<h3 id="systemCodes">SystemCodes</h3>
		<p>
			Before we proceed, we must first understand the SystemCodes class. SystemCodes are commonly the values of a drop down menu. The usual use for them would be for the male and female gender.
		</p>
		<h4>How to create a SystemCode</h4>
		<ol>
			<li>Access the dashboard for Opentides 3.</li>
			<li>In the navigation, select the tab named <span class="code-emphasize">System Codes</span>.</li>
			<li>Click on the <span class="code-emphasize">Add System Codes</span> button.</li>
			<li>A form will appear. Category refers to the group of <span class="code-emphasize">SystemCodes</span> you want to have them in. This can either be a new category or an existing category. Please take note that you may dynamically add new categories here.</li>
			<li>A key is used when searching for a specific <span class="code-emphasize">System Codes</span>.</li>
			<li>The value refers to the actual value of the <span class="code-emphasize">SystemCode</span>. This will be the one that will be displayed on the drop down menu.</li>
			<li>After specifying all the fields, <span class="code-emphasize">Save</span></li>
		</ol>
		
		<div class="alert alert-info">
			<strong>Heads up!</strong> By convention, we always use capital letters for the category field and key field
		</div>
		
		<h4>Usage</h4>
		<p>
			Defining a SystemCode into your class would be fairly easy.
		</p>
		<div class="example">
			<code class="prettyprint">
				@JoinColumn(name = "GENDER_ID")<br/>
				@JsonView(Views.SearchView.class)<br/>
				@ManyToOne(cascade = {CascadeType.ALL})<br/>
				private SystemCodes gender;<br/>
				<br/><br/>
				//getters and setters<br/>
				public SystemCodes getGender() {<br/>
				&nbsp;&nbsp;&nbsp;&nbsp;return gender;<br/>
				}<br/>
				public void setGender(SystemCodes gender) {<br/>
				&nbsp;&nbsp;&nbsp;&nbsp;this.gender = gender;<br/>
				}
			</code>
		</div>
		
		<hr/>
		
		<h3 id="definingAttributes">Defining class attributes</h3>
		<p>
			Now that we've learned how to use the <span class="code-emphasize">SystemCodes</span> class, its time to define our attributes!
		</p>
		<h4>Steps</h4>
		<ol>
			<li>Define our attributes normally as private like any other java classes.</li>
			<li>Create a public getter and setter method for each attribute.</li>
			<li>Annotate each attribute with <code>@Column(name = "")</code> and specify the name of the column that would appear in the database.</li>
			<li>We also annotate some of our attributes with <code>@JsonView(Views.SearchView.class)</code>. This would help us later on with our CRUD pages.</li>
			<li>Lastly, attributes which refers to another entities are given their <span class="code-emphasize">Hibernate</span> annotation.</li>
		</ol>
		<h4>Example</h4>
		<div class="example">
			<code class="prettyprint">
				@Column(name = "MIDDLE_NAME", nullable=false)<br/>
				@JsonView(Views.SearchView.class)<br/>
				private String lastName;<br/>
				<br/>
				@Column(name = "BIRTHDATE")<br/>
				@JsonView(Views.SearchView.class)<br/>
				private Date birthdate;<br/>
			</code>
		</div>
		
		<p><i class="icon-info-sign"></i> For more information about hibernate annotations, please refer to this <a href="http://tadtech.blogspot.com/2007/09/hibernate-association-mappings-in.html" target="_blank">link</a></p>

		<div class="alert alert-info">
			<strong>Heads up!</strong> We use <strong>java.util.date</strong> when working with dates.
		</div>
		
		<hr/>
		
		<h2><i class="icon-pushpin"></i> Exercise</h2>
		<p>
			Define attributes into our <span class="code-emphasize">Patient</span>. Some attributes may be the following:
			<ul>
				<li>First Name</li>
				<li>Last Name</li>
				<li>Middle Name</li>
				<li>Gender</li>
				<li>Birth Date</li>
				<li>Mobile no.</li>
				<li>Landline no.</li>
			</ul>
			In the next chapter, we will create our DAO's and services. They will be  magical so be sure to pay close attention!
		</p>
		
		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/creating-entities">&larr; Previous (Creating Entities)</a>
			</li>
			<li class="next">
				<a href="${home}/dao-and-service">Next (DAO's and Services) &rarr;</a>
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