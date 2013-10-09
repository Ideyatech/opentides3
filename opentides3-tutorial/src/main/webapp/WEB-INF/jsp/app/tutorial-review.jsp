<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="label.tutorial" active="tags"/>

<ul class="breadcrumb">
	ALTER ME
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span1">
		
	</div>
	
	<!-- CONTENT -->
	<div class="span11">
		<h1><i class="icon-trophy"></i> Congratulations!</h1>
		<p class="lead">You have finished the tutorial. Job well done! *clap clap*</p>
		<p>The quick brown fox jumps over the lazy dog.</p>
		
		
		<h3><i class="icon-thumbs-up-alt"></i> Accomplishments</h3>
		<p>Let's have a review of all the things that you have done and (hopefully) learned.</p>
		<ul>
			<li>Cloning a copy of Opentides 3 from GitHub into your machine.</li>
			<li>Creating a new project on top of Opentides 3</li>
			<li>Creating your own home page and navigation header</li>
			<li>Creating entities and defining their attributes</li>
			<li>SystemCodes</li>
			<li>Implementing DAO's and Services for your entities</li>
			<li>Creating a controller and a crud page</li>
			<li>Validating forms</li>
		</ul>
		
		<hr/>
		
		<h3><i class="icon-file"></i> We're not yet done!</h3>
		<p>
			Now that you have learned the basics of Opentides 3, it's time for the more advanced topics! Ready? Go!
		</p>
		
		
		<ul class="pager">
			<li class="next">
				<a href="${home}/getting-started">Next (Getting Started) &rarr;</a>
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