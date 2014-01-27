<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.customize" active="basics">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div id="sideBar" class="span2">
		<div id="nav-list-wrapper" class="affix">
			<ul class="nav nav-list side-nav">
				<li><a href="#header">Header</a></li>
				<li><a href="#messages">Messages.properties</a></li>
				<li><a href="#homepage">Home Page</a></li>
				<li><a href="#css">CSS Styling</a></li>
				<li><a href="#icons">Icons</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Basics<span class="divider">/</span></li>
			<li>Customization</li>
		</ul>
		
		<h1><i class="icon-book"></i> Customizing the project</h1>
		<p class="lead">
			In the previous chapter, we have imported <span class="code-emphasize">Opentides 3</span> and ran it into our machine. In this chapter,
			we will customize it to make it our own project.
		</p>
		
		<section id="header">
			<h3>Header</h3>
			<p>
				Opentides 3 includes its own header, however, we must define our own for easier access and customization.
				In this section, we will create a simple header which will be based on Opentides 3's design.
			</p>
			<h4>Steps</h4>
			<ol>
				<li>
					Create a <span class="code-emphasize">header.tag</span> in the directory 
					<span class="code-emphasize">src->main->webapp->WEB-INF->tags</span>.
				</li>
				<li>
					Search for the <span class="code-emphasize">template-header.tag</span> in <span class="code-emphasize">src->main->webapp->WEB-INF->tags</span> and copy all of its content into our own <span class="code-emphasize">header.tag</span>
					Do not worry! We will only tackle the core part of the header tag to get our small application moving. The rest will be up to you!
				</li>
				<li>Look for the section where <code>&lt;div class="nav-collapse collapse"&gt;</code> is written. We will only focus on this part.</li>
				<li>
					Notice each <code>&lt;li&gt;</code> element, each one of this represents an item in our header tag.
					<div class="example">
						<code class="prettyprint">
							&lt;li class="&#36;{active eq 'tags' ? 'active' : ''}"&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&lt;a href="&#36;{home}/tags"&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;spring:message code="label.tutorial.tags"/&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&lt;/a&gt;<br/>
							&lt;/li&gt;
						</code>
					</div>
					The <code>'tags'</code> represent which item will be marked as active.
				</li>
				<li>
					Below the <span class="code-emphasize">usergroups</span> header item, create an entry for the <span class="code-emphasize">Patient</span> tab.
					Specify the URL as <span class="code-emphasize">&#36;{home}/patient</span>. It should look like this:
					<div class="example">
						<code class="prettyprint">
							&lt;li class="&#36;{active eq 'patient' ? 'active' : ''}"&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&lt;a href="&#36;{home}/patient"&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;spring:message code="label.tatiana.patient"/&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&lt;/a&gt;<br/>
							&lt;/li&gt;
						</code>
					</div>
				</li>
			</ol>
		</section>
		
		<section id="messages">
			<h3>Messages.properties</h3>
			<p>
				This file will be the default container for all the texts, messages and labels that will be found in your app.
				The purpose of this file is to handle internationalization when the need comes. This is also required in using Opentides 3.
			</p>
			<h4>Usage</h4>
			<ol>
				<li>Create a new file under <span class="code-emphasize">src/main/resources->app->languages</span>. Name it as <span class="code-emphasize">tatiana.messages.properties</span>.</li>
				<li>
					Add your key and values:
					<div class="example">
						<code class="prettyprint">
							label.tatiana.patient = Patient<br/>
							label.patient.add = Add a new Patient<br/>
							label.patient.firstName = First Name<br/>
						</code>
					</div>
				</li>
				<li>To use the values in your jsp page, write them as: <code>&lt;spring:message code="label.patient.firstName"/&gt;</code></li>
			</ol>
			<h4>Remember</h4>
			<ul>
				<li>Every time you see a "label.something.something" it means that the actual message is inside the properties file. This task is very tedious and requires proper discipline.</li>
				<li>Careful there, they are case sensitive.</li>
				<li>
					There is no definite way of writing in the properties file but to be sure, let's just follow a convention like this:
					<div class="example">
						<code class="prettyprint">
							#defining the patient class from the project tatiana<br/>
							label.tatiana.patient = Patient<br/>
							<br/>
							#defining the firstName attribute of the patient class<br/>
							label.patient.firstName = First Name<br/>
							<br/>
							#defining a random word<br/>
							label.potato = Potato<br/>
						</code>
					</div>
				</li>
			</ul>
			<i class="icon-location-arrow"></i> Learn more about spring messages <a href="http://docs.spring.io/spring/docs/1.1.5/taglib/tag/MessageTag.html" target="_blank">here</a>
		</section>
		
		<section id="homepage">
			<h3>Home Page</h3>
			<p>Let's create our own home page. Its easy!</p>
			<ol>
				<li>Create the file <span class="code-emphasize">home.jsp</span> in the <span class="code-emphasize">src->main->webapp->WEB-INF->jsp</span></li>
				<li>Search for the file <span class="code-emphasize">template-page.jsp</span> in <span class="code-emphasize">src->main->webapp->WEB-INF->jsp->template</span> and copy its content.</li>
				<li>Specify your <code>pageTitle</code> and the <code>active</code> tab in the <code>app:header</code></li>
				<li>It is highly recommended to use the <code>breadcrumb</code> to tell the user about his location.</li>
				<li>You may start writing your code under the <code>row-fluid</code> div.</li>
			</ol>
		</section>
		
		<section id="css">
			<h3>CSS Styling</h3>
			<p>Additional styling can easily be done in Opentides 3. We have provided a blank <span class="code-emphasize">style.css</span> file for your use.</p>
			<h4>Usage</h4>
			<ol>
				<li>Open the file <span class="code-emphasize">style.css</span> in <span class="code-emphasize">src->main->webapp->app->css</span>. If file is not found, create a new one.</li>
				<li>Write your css codes there.</li>
			</ol>
			<div class="alert alert-info">
				<strong>Heads up!</strong> Opentides 3 is built on top of Twitter bootstrap 2.3.2. To learn more about it, please refer to this <strong><a href="http://getbootstrap.com/2.3.2/" target="_blank">link</a></strong>.
			</div>
		</section>
		
		<section id="icons">
			<h3>Using Icons</h3>
			<p>Opentides 3 uses <span class="code-emphasize">Font Awesome 3.2.1</span> for its icons. Try them, it's really awesome. <i class="icon-thumbs-up-alt"></i>
			<div class="tutorial-body">
				<div class="example">
					<code class="prettyprint">
						&lt;i class="icon-thumbs-up-alt"&gt;&lt;/i&gt;
					</code>
				</div>
				<div class="highlight">
					<h4>Usage</h4>
					<ol>
						<li>Choose an icon from their <a href="http://fortawesome.github.io/Font-Awesome/icons/">website</a></li>
						<li>Create an <code>&lt;i&gt;&lt;/i&gt;</code> element and specify the class of the icon like in the example above.</li>
					</ol>
				</div>
			</div>
		</section>
		
		<hr/>
		
		<h2><i class="icon-beaker"></i> Run it!</h2>
		<p>Run your server and see if everything works well.</p> 
		<p>
			In the next chapter, we will discuss and create the entity needed for our project which is the 
			<span class="code-emphasize">Patient</span> and define it's attributes.
		</p>

		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/start">&larr; Previous (Getting Started)</a>
			</li>
			<li class="next">
				<a href="${home}/entities-and-attributes">Next (Entities & Attributes) &rarr;</a>
			</li>
		</ul>
	</div>
</div>

<app:footer>
</app:footer>