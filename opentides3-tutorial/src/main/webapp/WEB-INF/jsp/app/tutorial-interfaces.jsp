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
</app:header>

<ul class="breadcrumb">
	ALTER BREADCRUMBS
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Interfaces</li>
				<li class="active"><a id="scroll-taggable">Taggable</a></li>
				<li><a id="scroll-messageProperties">Commentable</a></li>
				<li><a id="scroll-homepage">ImageUploadable</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> Opentides 3 Interfaces</h1>
		<p class="lead">
			Opentides 3 provides various interfaces depending on our needs. These interfaces guide the developers (us)
			in implementing various tasks.
		</p>
		
		<h3 id="taggable">Taggable</h3>
		<p>
			The interface <span class="code-emphasize">Taggable</span> is used when we want to add "tags" into our entities.
			This tags are commonly used for searching items inside a forum or a blog. However, this is not yet fully implemented in <span class="code-emphasize">Opentides 3</span>
		</p>
		<br/>
		<form:form modelAttribute="formCommand" cssClass="form-horizontal">
			<app:tokenizer label="label.tutorial.tags" path="tags" />
		</form:form>

		<h4>Usage</h4>
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
		
		<hr/>
		
		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/start">&larr; Previous (Change Me)</a>
			</li>
			<li class="next">
				<a href="${home}/entities-and-attributes">Next (And the Link) &rarr;</a>
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