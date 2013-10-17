<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.welcome" active="welcome"/>

<ul class="breadcrumb">
	<li>Welcome</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<img src="img/tacocat.png"/>
		<p class="tip">Taco cat spelled backwards is still Taco cat</p>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-flag"></i> This is a tutorial. Got it?</h1>
		<p class="lead">
			This is a tutorial. Expect this to be hard, tedious and lots of incomplete/inaccurate data. Nahh I'm just kidding,
			We tried our best to make this as user friendly as possible for you.
		</p>
		<p class="lead">
			In the following pages, you'll learn and understand the basic and advanced fundamentals of <span class="code-emphasize">Opentides 3</span>. 
			Ranging from creating CRUD pages into simply adding images, we'll tackle them all!
		</p>
		<p class="lead">
			The tutorial will consist of 3 parts; <span class="code-emphasize">Setting up</span>, <span class="code-emphasize">Basic</span> and <span class="code-emphasize">Advanced topics</span>
			We'll also develop a simple application alongside each topics covered.
			This project will be your exercise and a playground to explore.
			
			Don't worry, sample codes will be provided along the way. Goodluck!
		</p>
		
		<ul class="pager">
			<li class="next">
				<a href="${home}/overview">Next (Tatiana) &rarr;</a>
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