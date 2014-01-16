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

<div class="span12">
    <div class="logo-holder">
        <div class="ot3-logo"><img src="welcome_files/ot3-logo.png"/></div>
        <h4>a web foundation framework that can be used to quickly setup <br>a web application using <span class="text-info">Spring MVC</span> and <span class="text-info">JPA.</span></h4>
    </div>
</div>

<div class="row-fluid welcome">
	<!-- NAVIGATION -->
	<div class="span2">
		<div class="temp-nav">
			<span class="code-emphasize">Open-tides 3</span> is a web foundation framework that can be used to quickly setup a web application using <span class="code-emphasize">Spring MVC</span> and <span class="code-emphasize">JPA</span>.
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-flag"></i> This is a tutorial. Got it?</h1>
		<p class="lead">
			This is a tutorial. Expect this to be hard, tedious and lots of incomplete/inaccurate data. Nahh I'm just kidding,
			We tried our best to make this as user friendly as possible for you.
		</p>
		<p class="lead">
			In the following pages, you'll learn and understand the basic fundamentals of <span class="code-emphasize">Open-Tides 3</span>. 
			Ranging from creating CRUD pages into simply adding icons, we'll tackle them all!
		</p>
		<p class="lead">
			The tutorial will consist of 3 parts; <span class="code-emphasize">Setting up</span>, <span class="code-emphasize">Basic</span> and <span class="code-emphasize">Other topics</span>
			We'll also develop a simple application alongside each topics covered.
			This project will be your exercise and a playground to explore.
			
			Don't worry, sample codes will be provided along the way. Goodluck! <i class="icon-thumbs-up-alt"></i>
		</p>
		
		<ul class="pager">
			<li class="next">
				<a href="${home}/opentides">Next (Open-Tides 3) &rarr;</a>
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