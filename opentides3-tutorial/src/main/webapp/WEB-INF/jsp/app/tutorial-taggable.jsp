<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>

<app:header pageTitle="title.interfaces" active="advanced">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div class="alert alert-info">
			<strong>Remember</strong><br/>
			Tags field does not accept duplicate entries
		</div>
	</div>
	
	<!-- CONTENT -->
	<div id="tutorial-content" class="span10">
		<ul class="breadcrumb">
			<li>Advanced <span class="divider">/</span></li>
			<li>Taggable</li>
		</ul>
		
		<h1><i class="icon-book"></i> Adding Tags</h1>
		<p class="lead">
			The interface <span class="code-emphasize">Taggable</span> is used when we want to add "tags" into our entities.
			This tags are commonly used for searching items inside a forum or a blog. However, this is not yet fully implemented in <span class="code-emphasize">Opentides 3</span>
		</p>
		
			
		<form:form modelAttribute="formCommand" cssClass="form-horizontal">
			<app:tokenizer label="label.tutorial.tags" path="tags" />
		</form:form>

		<h3>Usage</h3>
		<ol>
			<li>Implement the interface <span class="code-emphasize">Taggable</span> from <span class="code-emphasize">org.opentides.bean</span>.</li>
			<li>
				Define a list of <span class="code-emphasize">Tags</span> as an attribute and add the necessary annotations and mappings as seen below:
				<div class="example">
					<code class="prettyprint">
						@OneToMany(cascade = CascadeType.REMOVE)<br/>
						@JoinTable(name="PATIENT_TAGS",<br/>
						joinColumns = {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;@JoinColumn(name="PATIENT_ID", referencedColumnName="ID")<br/>
						},<br/>
						inverseJoinColumns = {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;@JoinColumn(name="TAG_ID")<br/>
						})<br/>
						@JsonView(Views.FormView.class)<br/>
						private List<Tag> tags;<br/>
					</code>
				</div>
			</li>
			<li>
				Create a getter and setter for the <span class="code-emphasize">tags</span> attribute. Annotate the getter function with <code>@JsonSerialize(using = TagsSerializer.class)</code>.
				This will help later on for rendering our tags when updating the details.
			</li>
			<li>
				In our form (which is inside <span class="code-emphasize">patient-crud.jsp</span>), we add a new field:
				<div class="example">
					<code class="prettyprint">
						&lt;tides:tokenizer label="label.patient.tags" path="tags" /&gt;
					</code>
				</div>
			</li>
		</ol>
		
		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/advanced-overview">&larr; Go Back</a>
			</li>
		</ul>
	</div>
</div>

<app:footer>
</app:footer>