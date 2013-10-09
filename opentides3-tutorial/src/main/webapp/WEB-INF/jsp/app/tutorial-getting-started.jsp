<%--
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

<app:header pageTitle="label.tutorial" active="tags"/>

<ul class="breadcrumb">
	<li><a href="${home}/overview">Overview</a><span class="divider">/</span></li>
	<li>Getting Started</li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Project Overview</li>
				<li class="active"><a id="scroll-hello">Hello Tatiana!</a></li>
				<li><a id="scroll-functionalities">What can she do?</a></li>
				<li><a id="scroll-components">Meet the objects</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> Getting Started</h1>
		<p>
			So, you decided to use <span class="code-emphasize">Opentides 3</span>. Good for you! Don't worry, installing it into your machine would be a piece of cake. *wink*
			
		</p>
		
		<h3 id="hello">Requirements</h3>
		<p>
			With great power comes great responsibility. Of course, there are some requirements that you should have before obtaining the <span class="code-emphasize">"power"</span> <i class="icon-bolt"></i>
		</p>
		<ul>
			<li><a href="https://jdk7.java.net/" target="_blank">Java 7</a></li>
			<li><a href="http://tomcat.apache.org/download-70.cgi" target="_blank">Tomcat 7</a></li>
			<li><a href="http://dev.mysql.com/downloads/mysql/5.5.html" target="_blank">MySql 5.5</a></li>
			<li>IDE - We highly prefer using <a href="http://spring.io/tools/sts/all" target="_blank">Spring Tool Suite</a></li>
			<li><a href="https://github.com/" target="_blank">GIT</a> account</li>
		</ul>
		
		<hr/>
		
		<h3>Installation</h3>
		<p>
			Actually, we're really not installing <span class="code-emphasize">Opentides 3</span> into your machine, we're just cloning the original project
			from <span class="code-emphasize">GitHub <i class="icon-github"></i>.</span> Sorry <i class="icon-meh"></i>
		</p>
		<h4>Steps</h4>
		<ol>
			<li>Change your perspective into <span class="code-emphasize">Git Repository Exploring</span>.</li>
			<li>Choose <span class="code-emphasize">Clone a Git Repository</span></li>
			<li>Select <span class='code-emphasize'>URI</span> in the pop-up window.</li>
			<li>Specify <span class="code-emphasize">https://github.com/Ideyatech/opentides3.git</span> as the URI. Host would be <span class="code-emphasize">github.com</span> and repository path would be <span class="code-emphasize">/Ideyatech/opentides3.git</span>.</li>
			<li>In the authentication section, input the username and password of your <span class="code-emphasize">git</span> account. Then tick the checkbox <span class="code-emphasize">Store in secure store</span>. Then click next</li>
			<li>In the next window, check the box besides <span class="code-emphasize">master</span> then press next.</li>
			<li>Specify the destination in which <span class="code-emphasize">Opentides 3</span> will be cloned. Check the box besides <span class="code-emphasize">Import all Existing Project</li> then finish.</li>
		</ol>
		
		<hr/>
		
		<h3>imageutil.lib</h3>
		<p>Opentides 3 depends on imageutil.lib which is still not on any public maven repository. So we need to install it first in our local repository.</p>
		<h4>Steps</h4>
		<ol>
			<li>Right click the <span class="code-emphasize">Opentides 3</span> project in your package explorer.</li>
			<li>Select <span class="code-emphasize">Run As..</span> then <span class="code-emphasize">Run Configuration</span></li>
			<li>In the following window, create a new <span class="code-emphasize">Maven</span> configuration.
			<li>Specify the <span class="code-emphasize">Base Directory</span> by pressing <span class="code-emphasize">Browse Workspace</span> and selecting <span class="code-emphasize">Opentides 3</span>.</li>
			<li>
				Set the goal as: <span class="code-emphasize">install:install-file -Dfile=<em>ImageUtilLocation</em> -DgroupId=imageutil -DartifactId=imageutil -Dversion=1.0 -Dpackaging=jar</span>. Change the <em>ImageUtilLocation</em> with the location of your imageutil.lib.
				This is found in the directory where you installed <span class="code-emphasize">Opentides 3</span>. <span class="code-emphasize">opentides3->opentides3-core->src->main->webapp->WEB-INF->lib</span> 
			</li>
			<li><span class="code-emphasize">Apply</span> then <span class="code-emphasize">Run</span></li>
		</ol>
		
		<hr/>
		
		<h3>Maven Install</h3>
		<h4>Steps</h4>
		<ol>
			<li>Right click on the project <span class="code-emphasize">Opentides 3</span> in the package explorer.</li>
			<li>Select <span class="code-emphasize">Run As..</span> then <span class="code-emphasize">Run Configuration</span></li>
			<li>In the following window, create a new <span class="code-emphasize">Maven</span> configuration.
			<li>Specify the <span class="code-emphasize">Base Directory</span> by pressing <span class="code-emphasize">Browse Workspace</span> and selecting <span class="code-emphasize">Opentides 3</span>.</li>
			<li>Set the goal as <span class="code-emphasize">clean install</span>.</li>
			<li><span class="code-emphasize">Apply</span> then <span class="code-emphasize">Run</span></li>
		</ol>
		
		<hr/>
		
		<h2><i class="icon-trophy"></i> There you go!</h2>
		<p>
			If everything went according to plan, you should have successfully cloned <span class="code-emphasize">Opentides 3</span> from Git into your local machine. 
			In the next chapter, we will create a sample project out of <span class="code-emphasize">Opentides 3</span> and run it. See ya!
		</p>
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/overview">&larr; Previous (Overview)</a>
			</li>
			<li class="next">
				<a href="${home}/creating-project">Next (Creating a new project) &rarr;</a>
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