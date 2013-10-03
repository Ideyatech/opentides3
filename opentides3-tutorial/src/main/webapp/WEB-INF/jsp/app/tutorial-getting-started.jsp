<%--
	- tutorial-getting-started.jsp
	- Displays a tutorial for creating a new project with Open Tides 3
	-
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="label.tutorial" active="getting-started">
	<style>
		code.prettyprint{
			background-color: #fff;
			border: 0;
			padding: 0;
		}
		
		code.prettyprint span.kwd{
			font-weight: bold;
		}
	</style>
</app:header>

<ul class="breadcrumb">
  <li><a href="${home}"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><spring:message code="label.tutorial.gettingStarted"/></li>
</ul>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div data-spy="affix" data-offset-top="60" class="affix-top" style="top: 55px;">
			<ul class="nav nav-list side-nav">
				<li class="nav-header">Getting Started</li>
				<li class="active"><a id="scroll-mavenArchetype">Installing Archetype</a></li>
				<li><a id="scroll-newProject">Creating a New Project</a></li>
				<li><a id="scroll-runningOnServer">Running on Server</a></li>
				<li><a id="scroll-dbConfig">Database Configuration</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<h1><i class="icon-book"></i> Creating your new project</h1>
		
		<h3 id="mavenArchetype">Installing the maven archetype for Opentides 3</h3>
		
		<p>
			Before we begin with our project, we must first install the maven archetype for Opentides 3 into your local repository.
			This maven archetype will later on be used for future creation of a project.
		</p>
		<h4>Steps</h4>
		<ol>
			<li>Open the <span class="code-emphasize">opentides3</span> project from your <span class="code-emphasize">Package Explorer</span>. Then right click on <span class="code-emphasize">opentides3-archetype</span></li>
			<li>Select <span class="code-emphasize">Run As</span> then <span class="code-emphasize	">Run Configurations</span></li>
			<li>Under the <span class="code-emphasize">Maven Build</span>, create a new configuration</li>
			<li>Add a value to the <span class="code-emphasize">Base Directory</span> by pressing the <span class="code-emphasize">Browse Workspace</span> button</li>
			<li>Locate the <span class="code-emphasize">Opentides3</span> project and open the drop menu. Select <span class="code-emphasize">opentides3-archetype</span>. Press <span class="code-emphasize">Ok</span></li>
			<li>Specify the <span class="code-emphasize">Goals</span> as <span class="code-emphasize">install</span></li>
			<li><span class="code-emphasize">Apply</span> then <span class="code-emphasize">Run</span></li>
		</ol>
		<div class="alert alert-info">
			<strong>Heads up!</strong> Installing the maven archetype is only a one time process. You don't need to repeat this step for every project you will make.
		</div>
		
		<hr/>
		
		<h3 id="newProject">Creating a new project</h3>
		<p>To create our sample project, simply import the existing maven archetype that have been previously installed into your local machine.</p>
		<h4>Steps</h4>
		<ol>
			<li>Press <span class="code-emphasize">alt+shift+n</span> then select <span class="code-emphasize">Maven Project</span> on the following window.</li>
			<li>Make sure that the <span class="code-emphasize">Catalog</span> is set into <span class="code-emphasize">Default Local</span></li>
			<li>Press <span class="code-emphasize">next</span>. On the following, tick on the checkbox that states <span class="code-emphasize">include snapshot</span></li>
			<li>Select the project with a <span class="code-emphasize">Group Id</span> of <span class="code-emphasize">org.opentides</span>. Press <span class="code-emphasize">next</span></li>
			<li>Enter <span class="code-emphasize">org.tutorial</span> on the <span class="code-emphasize">Group Id</span> and <span class="code-emphasize">tatiana</span> on the <span class="code-emphasize">Artifact Id</span>.
			<li>As for the <span class="code-emphasize">Package</span>, specify <span class="code-emphasize">org.tutorial.tatiana</span> then press <span class="code-emphasize">Finish</span></li>
		</ol>
		
		<hr/>
		
		<h3 id="runningOnServer">Adding the project on a Tomcat server</h3>
		<p>We have to add Tatiana to your local server first!</p>
		<h4>Steps</h4>
		<ol>
			<li>Right click on the <span class="code-emphasize">Servers</span> section of your IDE and select <span class="code-emphasize">New</span> then choose <span class="code-emphasize">Server</span></li>
			<li>On the following window, select a version which you have currently installed</li>
			<li>Specify your <span class="code-emphasize">Server Name</span> then press <span class="code-emphasize">Next</span></li>
			<li>Select <span class="code-emphasize">tatiana</span> then press <span class="code-emphasize">Add</span>. Then <span class="code-emphasize">Finish</span></li>
			<li>
				Double click your newly defined server. Under the <span class="code-emphasize">Timeouts</span> section, change the <span class="code-emphasize">Start (in seconds)</span> into <span class="code-emphasize">3000</span> 
				and the <span class="code-emphasize">Stop (in seconds)</span> into <span class="code-emphasize">600</span> Then save.
			</li>
			<li>Open the <span class="code-emphasize">server.xml</span> file. This is found inside your newly defined server in the <span class="code-emphasize">Servers</span> folder on the <span class="code-emphasize">Package Explorer</span></li>
			<li>Scroll down to the bottom, you will see something like this <code>&lt;Context docBase="tatiana" path="/opentides" reloadable="true" source="org.eclipse.jst.jee.server:tatiana"/&gt;</code></li>
			<li>Replace the <code>path</code> with your specified root directory. In our case it would be <span class="code-emphasize">tatiana</span>. This will appear on the URL as <span class="code-emphasize">localhost:8080/tatiana</span></li>
		
		</ol>
		
		<hr/>
		
		<h3 id="dbConfig">Database Configuration</h3>
		<p>In this section, we configure all the files that are related for a database connection.</p>
		<div class="alert alert-info">
			<strong>Wait!</strong> This section assumes that you already have a schema and a user in your database. Your user should be given all the privileges
		</div>
		<ol>
			<li>Open the file <span class="code-emphasize">web.xml</span> within your project. It is located at <span class="code-emphasize">src->main->webapp->WEB-INF</span></li>
			<li>Scroll down until you see the <code>&lt;res-ref-name&gt;</code>. Change it's value into <code>jdbc/yourschema</code></li>
			<li>Open the file <span class="code-emphasize">localhost.properties</span>. This is located at <span class="code-emphasize">src/main/resources</span> in the <span class="code-emphasize">Package Explorer</span> of our project</li>
			<li>Scroll down until you see the <code>#For Database connection</code></li>
			<li>Change the necessary values for the properties <code>database.url</code>, <code>database.username</code>, <code>database.password</code> and <code>database.jndi</code></li>
			<li>Lastly, open another file called <span class="code-emphasize">context.xml</span>. This is located at <span class="code-emphasize">Servers->YourDefinedServerName</span> in the <span class="code-emphasize">Package Explorer</span></li>
			<li>
				Scroll down until the bottom. Before the <code>&lt;/Context&gt;</code> tag, paste the following code:<br/>
				<code class="prettyprint">
					&lt;Resource auth="Container" driverClassName="com.mysql.jdbc.Driver"<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;maxActive="100" maxIdle="30000" maxWait="10000" name="jdbc/opentides3"<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;password="password" type="javax.sql.DataSource"<br/>
	  					&nbsp;&nbsp;&nbsp;&nbsp;url="jdbc:mysql://localhost:3306/opentides3?autoReconnect=true" <br/>
						&nbsp;&nbsp;&nbsp;&nbsp;username="Opentides"/&gt;
				</code>
			</li>
			<li>Change the necessary values for the properties <code>name</code>, <code>password</code>, <code>url</code> and <code>username</code></li>
		</ol>
		
		<hr/>
		
		<h3 id="dbConfig">Lets do this!</h3>
		<p>
			If everything went according to plan, just start your server and access 
			<span class="code-emphasize">http://localhost:8080/tatiana</span> in your browser. Login with 
			<span class="code-emphasize">admin</span> as username and <span class="code-emphasize">ideyatech</span> as password.
		</p>
		<ul class="pager">
			<li class="next">
				<a href="${home}/creating-entities">Next (Creating Entities) &rarr;</a>
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
			}, 1500);
		}
	</script>
</app:footer>