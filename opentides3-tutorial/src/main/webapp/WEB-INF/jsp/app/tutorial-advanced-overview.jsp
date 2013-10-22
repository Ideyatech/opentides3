<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.creating-project" active="setting-up">
</app:header>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div class="alert alert-info">
			I should write something useful and informative here so that the layout won't be ruined.
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Advanced Topics <span class="divider">/</span></li>
			<li>Overview</li>
		</ul>
		
		<h1><i class="icon-book"></i> Oh hi.</h1>
		<p class="lead">
			Welcome to the advanced section for this tutorial. Below are some topics that could
			guide you in developing your application using <span class="code-emphasize">Opentides 3</span>.
		</p>

		<ul>
			<li><a href="${home}/dbevolve">DB Evolve</a></li>
			<li><a href="${home}/attaching-images">Attaching Images</a></li>
			<li><a href="${home}/adding-tags">Adding Tags</a></li>
			<li><a href="${home}/using-modals">Using Modals</a></li>
			<li><a href="${home}/form-tags">Form Tags</a></li>
			<li><a href="${home}/spring-controllers">Spring Controller</a></li>
			<li><a href="${home}/adding-images">Adding custom Images</a></li>
		</ul>
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/review">&larr; Previous (Basics)</a>
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