<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.entities-and-attributes" active="basics">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>



<div class="row-fluid">
	<!-- NAVIGATION -->
	<div id="sideBar" class="span2">
		<div id="nav-list-wrapper" class="affix">
			<ul class="nav nav-list side-nav">
				<li><a href="#patient">Patient</a></li>
				<li><a href="#systemcodes">SystemCodes</a></li>
				<li><a href="#attributes">Attributes</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Basics<span class="divider">/</span></li>
			<li>Entities & Attributes</li>
		</ul>
		
		<h1><i class="icon-book"></i> Creating Entities & Defining Attributes</h1>
		<p class="lead">In this chapter, we create all the required classes for Tatiana and define it's basic attributes. We'll also tackle about SystemCodes; a very useful class in <span class="code-emphasize">Open-Tides 3</span></p>
		
		<section id="patient">
			<h3>Patient</h3>
			<p>
				As discussed in the previous chapter, this object will be the main focus of our web app. Before we proceed
				in making the Patient class, let me introduce you to the <span class="code-emphasize">BaseEntity</span> class.
			</p>
			<h4 id="">Base Entity</h4>
			<p>
				This is the base class for all entity objects (model) within Opentides. This class contains all the basic attributes such as
				id, createDate, updateDate etc. Every bean within Opentides that would be persisted should extend this class.
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
		</section>
		
		<section id="systemcodes">
			<h3>SystemCodes</h3>
			<p>
				Before we start defining our attributes, we must first understand the SystemCodes class. 
				SystemCodes are commonly the values of a drop down menu. The common use for them would be for the male and female gender.
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
				Defining a SystemCode into your class would be a piece of cake <i class="icon-thumbs-up-alt"></i>
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
		</section>
		
		<section id="attributes">
			<h3>Defining Patient Attributes</h3>
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
					@Column(name = "LAST_NAME")<br/>
					@JsonView(Views.SearchView.class)<br/>
					private String lastName;<br/>
					<br/>
					@Column(name = "BIRTHDATE")<br/>
					@JsonView(Views.SearchView.class)<br/>
					private Date birthdate;<br/>
				</code>
			</div>
			
			<p><i class="icon-rocket"></i> For more information about Hibernate annotations, please refer to this <a href="http://tadtech.blogspot.com/2007/09/hibernate-association-mappings-in.html" target="_blank">link</a></p>
	
			<div class="alert alert-info">
				<strong>Heads up!</strong> We use <strong>java.Util.date</strong> when working with dates.
			</div>
		</section>
		
		<hr/>
		
		<h2><i class="icon-code"></i> Exercise</h2>
		<p>
			Define attributes into our <span class="code-emphasize">Patient</span>. Some attributes may be the following:
			<ul>
				<li>First Name</li>
				<li>Last Name</li>
				<li>Middle Name</li>
				<li>Gender - SystemCodes</li>
				<li>Birth Date</li>
				<li>Mobile no.</li>
				<li>Landline no.</li>
			</ul>
			
			As an additional task, create a system code for the <span class="code-emphasize">GENDER</span> category for both male and female.<br/><br/>
			In the next chapter, we will create our DAO's and services. They will be  magical so be sure to pay close attention!
		</p>
		
		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/customize">&larr; Previous (Customization)</a>
			</li>
			<li class="next">
				<a href="${home}/dao-and-service">Next (DAO's and Services) &rarr;</a>
			</li>
		</ul>
	</div>
</div>

<app:footer>
</app:footer>