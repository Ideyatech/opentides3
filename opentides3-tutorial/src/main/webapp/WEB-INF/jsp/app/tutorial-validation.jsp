<%--
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
	<li><a href="${home}/overview">Overview</a><span class="divider">/</span></li>
	<li><a href="${home}/getting-started">Getting Started</a><span class="divider">/</span></li>
	<li><a href="${home}/creating-project">Creating Project</a><span class="divider">/</span></li>
	<li><a href="${home}/customize">Customization</a><span class="divider">/</span></li>
	<li><a href="${home}/entities-and-attributes">Entities & Attributes</a><span class="divider">/</span></li>
	<li><a href="${home}/dao-and-service">DAO's & Services</a><span class="divider">/</span></li>
	<li><a href="${home}/crud-controller">CRUD Controller</a><span class="divider">/</span></li>
	<li><a href="${home}/crud-view">CRUD View</a><span class="divider">/</span></li>
	<li>Form Validation</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Navigation</li>
				<li class="active"><a id="scroll-validator">Validator</a></li>
				<li><a id="scroll-settingValidator">Setting Validator</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> Implementing Validations</h1>
		<p>In this chapter, we discuss and create form validations and how to implement them. Implementation will be based on Spring 3's validation.</p>
		<h3 id="validator">Validator</h3>
		<p>We use Spring 3's validation feature for our forms.</p>
		<h4>Usage</h4>
		<ol>
			<li>
				Create the <span class="code-emphasize">PatientValidator</span> class under the package <span class="code-emphasize">org.tutorial.tatiana.web.validator</span> 
				and implement the interface <span class="code-emphasize">Validator</span> from <span class="code-emphasize">org.springframework.validation</span>
			</li>
			<li>Annotate the whole class with <code>@Component</code> from <code>org.springframework.stereotype</code>.</li>
			<li>
				Modify the contents of the <code>supports()</code> function with the following:
				<div class="example">
					<code class="prettyprint">
						return Patient.class.isAssignableFrom(clazz);
					</code>
				</div>
			</li>
			<li>
				Add your validation rules inside the <code>validate()</code> function. In the code below we add validations for the attributes 
				<span class="code-emphasize">First name</span> and <span class="code-emphasize">Last name</span>
				<div class="example">
					<code class="prettyprint">
						@Override
						public void validate(Object target, Errors errors) {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName",<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"error.required", new Object[]{"First Name"},"First Name is required.");<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", <br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"error.required", new Object[]{"Last Name"},"Last Name is required.");<br/>
						}
					</code>
				</div>
			</li>
		</ol>
		<p>
			<i class="icon-thumbs-up-alt"></i> For more information about Spring 3 validation, please refer to this 
			<a href="http://docs.spring.io/spring/docs/3.2.x/spring-framework-reference/html/validation.html" target="_blank">link</a>
		</p>
		
		<hr/>
		
		<h3 id="settingValidator">Setting the validator</h3>
		<div class="tutorial-body">
			<div class="example">
				<code class="prettyprint">
					@Autowired<br/>
					public void setValidator(PatientValidator patientValidator) {<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;this.formValidator = patientValidator;<br/>
					}
				</code>
			</div>
			<div class="highlight">
				<h4>Steps</h4>
				<ol>
					<li>
						We create a function called <span class="code-emphasize">setValidator</span> 
						in our <span class="code-emphasize">PatientCrudController</span>
					</li>
					<li>Annotate the function with <code>@Autowired</code>.</li>
					<li>Inside the function, we set the <span class="code-emphasize">formValidator</span> with our <span class="code-emphasize">patientValidator</span>.</li>
				</ol>
			</div>
		</div>
		
		<hr/>
		
		<h2><i class="icon-rocket"></i> We're all set!</h2>
		<p>Let's try out our new validation!</p>
		<img src="img/tides-login.png" width="800px" class="img-rounded"/>
		<p>In the next chapter, we will discuss the all the form tags and other tags under Opentides 3.</p>		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/crud-view">&larr; Previous (CRUD Pages)</a>
			</li>
			<li class="next">
				<a href="${home}/form-tags">Next (Form Tags) &rarr;</a>
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