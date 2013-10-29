<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.review" active="basics"/>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div class="alert alert-info">
			Awesomeness!
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-trophy"></i> Congratulations!</h1>
		<p class="lead">You have finished the first part of the tutorial. Job well done! *clap clap*</p>
		
		<h3><i class="icon-thumbs-up-alt"></i> Accomplishments</h3>
		<p>Let's have a review of all the things that you have done and (hopefully) learned.</p>
		<ul class="icons-ul">
			<li><i class="icon-li icon-ok"></i>Cloning a copy of Opentides 3 from GitHub into your machine.</li>
			<li><i class="icon-li icon-ok"></i>Creating a new project on top of Opentides 3</li>
			<li><i class="icon-li icon-ok"></i>Creating your own home page and navigation header</li>
			<li><i class="icon-li icon-ok"></i>Creating entities and defining their attributes</li>
			<li><i class="icon-li icon-ok"></i>SystemCodes</li>
			<li><i class="icon-li icon-ok"></i>Implementing DAO's and Services for your entities</li>
			<li><i class="icon-li icon-ok"></i>Creating a controller and a CRUD page</li>
			<li><i class="icon-li icon-ok"></i>Creating and validating forms</li>
			<li><i class="icon-li icon-ok"></i>Creating a database evolve script</li>
			<li><i class="icon-li icon-ok"></i>User Management (authorities, usergroup, user)</li>
		</ul>
		
<!-- 		<hr/> -->
		
<!-- 		<h3><i class="icon-location-arrow"></i> We're not yet done!</h3> -->
<!-- 		<p> -->
<!-- 			In the following pages, I've also included other topics which are also essential in developing your project using -->
<!-- 			<span class="code-emphasize">Open-Tides 3</span>. Feel free to browse them. -->
<!-- 		</p> -->
		
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/user-management">&larr; Previous (User Management)</a>
			</li>
<!-- 			<li class="next"> -->
<%-- 				<a href="${home}/advanced-overview">Next (Advanced) &rarr;</a> --%>
<!-- 			</li> -->
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