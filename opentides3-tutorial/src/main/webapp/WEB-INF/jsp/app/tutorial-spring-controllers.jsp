<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.spring-controllers" active="advanced">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div class="alert alert-info">
			<strong>By convention</strong>, 
			we append the word "Controller" at the end for the name of our controller.
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
	
		<ul class="breadcrumb">
			<li>Advanced <span class="divider">/</span></li>
			<li>Spring Controllers</li>
		</ul>
		
		<h1><i class="icon-book"></i> Spring Controllers</h1>
		<p class="lead">
			Controllers in general interpret user input and transform it into a model that is represented to the user by the view.
			In this section we will only discuss how to create controllers and provide helpful links for better understanding of the topic.
		</p>
		
		<hr/>
		
		<h3 id="creation">Lets create one!</h3>
		<ol>
			<li>
				Create a new java file in the package <span class="code-emphasize">org.tutorial.tathiana.web.controller</span> under <span class="code-emphasize">src/main/java</span> and 
				Note that package may vary depending on your project name however, it should still be in the <span class="code-emphasize">*.web.controller</span>.
			</li>
			<li>
				Annotate the file with <code>@Controller</code> from <code>org.springframework.stereotype</code>
			</li>
		</ol>
		
		<hr/>
		
		<h3 id="links">Reading is good</h3>
		<p>
			Since the web contains a lot of resources and information about <span class="code-emphasize">Spring Controllers</span>, I'm gonna leave you alone with those (you can do it).
			Below are some helpful links to help you create and understand <span class="code-emphasize">Spring Controllers</span>
		</p>
		<ul>
			<li><a href="http://docs.spring.io/spring/docs/3.0.x/reference/mvc.html" target="_blank">Learn the MVC pattern which use controllers</a></li>
			<li><a href="http://www.mkyong.com/spring3/spring-3-mvc-hello-world-example/" target="_blank">Simple implementation of controllers by mkyong</a></li>
		</ul>
		
		<hr/>
		
		<h2 id="exercise"><i class="icon-stethoscope"></i> Exercise</h2>
		<p>
			Create a page that will serve as a patient's profile page. This can be done by adding a link into the 
			first name or last name of the patient in our crud page.
		</p>
		<img src="img/tides-controller-1.png" class="img-polaroid"/>
		<br/>
		<img src="img/tides-controller-2.png" class="img-polaroid"/>

		<ul class="pager">
			<li class="previous">
				<a href="${home}/advanced-overview">&larr; Go Back</a>
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