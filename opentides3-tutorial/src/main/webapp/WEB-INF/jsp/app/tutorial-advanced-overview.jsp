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

<ul class="breadcrumb">
	<li>Setting Up <span class="divider">/</span></li>
	<li>Creating Project</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div class="alert alert-info">
			
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> Oh hi.</h1>
		<p class="lead">
			Welcome to the advanced section for this tutorial. Below are some topics that could
			guide you in developing your application using <span class="code-emphasize">Opentides 3</span>.
		</p>
		
		
		<ul>
			<li><a href="">DB Evolve</a></li>
			<li><a href="">Interfaces</a></li>
			<li><a href="">Using Modals</a></li>
			<li><a href="">Form Tags</a></li>
			<li><a href="">Spring Controller</a></li>
			<li><a href="">Adding custom Images</a></li>
		</ul>
		
		
		
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/getting-started">&larr; Previous (Getting Started)</a>
			</li>
			<li class="next">
				<a href="${home}/customize">Next (Customization) &rarr;</a>
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