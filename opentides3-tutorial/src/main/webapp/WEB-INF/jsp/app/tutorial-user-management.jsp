<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.user-management" active="opentides"/>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div id="sideBar" class="span2">
		<div id="nav-list-wrapper" class="affix">
			<ul class="nav nav-list side-nav">
				<li><a href="#authorities">Authorities</a></li>
				<li><a href="#usergroup">Usergroup</a></li>
				<li><a href="#user">User</a></li>
			</ul>
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Basics<span class="divider">/</span></li>
			<li>User Management</li>
		</ul>
		
		<h1><i class="icon-book"></i> User Management</h1>
		<p class="lead">
			<span class="code-emphasize">Open-Tides 3</span> has a built in <span class="code-emphasize">user management</span> feature that would help developers quickly restrict or allow certain groups of people to access specific roles. There will be 3 key terms in this section; <span class="code-emphasize">authorities</span>, <span class="code-emphasize">usergroup</span> and <span class="code-emphasize">user</span>.
		</p>	

		<div class="alert alert-warning">
			For the purpose of this chapter, we would assume that we are adding an authorization for Patients tab
		</div>


		<section id="authorities">
			<h3>Authorities</h3>
			<p>Authorities are roles or tasks that are assigned to specific group of users which is also known as a <span class="code-emphasize">usergroup</span>. Each function or module of your application can be considered as an <span class="code-emphasize">authority</span>. How big the scope of the authority will be up to you.</p>
			<h4>Creating new authorities</h4>
			<ol>
				<li>Search for the file <span class="code-emphasize">applicationContext-security.xml</span>. This is located at <span class="code-emphasize">src/main/resources->app->spring</span></li>
				<li>Scroll down til you reach the <code>bean</code> with the ID <code>authorities</code>. Inside that bean you will see all the authorities pre-defined by <span class="code-emphasize">Open-Tides 3</span>. We'll also create ours here.</li>
				<li>
					Paste the following code below the <code>entry</code> with the key <span class="code-emphasize">ACCESS_DASHBOARD_TAB</span>
					<div class="example">
						<code class="prettyprint">
							&lt;!-- PATIENT TAB --&gt;<br/>
							&lt;entry key="ACCESS_PATIENT_TAB"&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;value&gt;03.00.00.00 Access Patient Tab&lt;/value&gt;<br/>
							&lt;/entry&gt;<br/>
						</code>
					</div>
					<p>
						The <span class="code-emphasize">03.00.00.00</span> corresponds to the positioning/order in which the authority would appear. 
						Having another authority with <span class="code-emphasize">03.01.00.00</span> will be positioned inside the node of <span class="code-emphasize">ACCESS_PATIENT_TAB</span> and so on.
					</p>
				</li>
				<li>Go to your <span class="code-emphasize">header.tag</span>. It is located in <span class="code-emphasize">src->main->webapp->WEB-INF->tags</span></li>
				<li>
					Remember our <span class="code-emphasize">Patient tab</span> in the header? We'll add security to it by wrapping it around the <code>&lt;sec:authorize&gt;</code>
					<div class="example">
						<code class="prettyprint">
							&lt;sec:authorize ifAllGranted="ACCESS_PATIENT_TAB"&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&lt;li class="${active eq 'patient' ? 'active' : ''}"&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;a href="${home}/patient"&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;spring:message code="label.tatiana.patient"/&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/a&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&lt;/li&gt;<br/>
							&lt;/sec:authorize&gt;<br/>
						</code>
					</div>
					Notice the <span class="code-emphasize">ACCESS_PATIENT_TAB</span>, this corresponds to our <code>entry key</code> earlier.
				</li>
			</ol>
			<p><i class="icon-ok"></i> For more information about spring security such as <span class="code-emphasize">ifAllGranted</span>, visit their documentation <a href="http://docs.spring.io/spring-security/site/docs/3.0.x/reference/taglibs.html" target="_blank">here</a></p>
		</section>

		<section id="usergroup">
			<h3>Usergroup</h3>
			<p><span class="code-emphasize">Usergroup</span> are group of users that are given specific set of authorities. For example, a user whose job is to encode data should not be given administrative privileges such as creating new users.</p>
			<h4>Creating new usergroup</h4>
			<ol>
				<li>Select <span class="code-emphasize">User Group</span> from your header</li>
				<li>
					Here you will see a pre-defined group which is the <span class="code-emphasize">Administrator</span>.
					<span class="code-emphasize">Open-Tides 3</span> provided us with the user <span class="code-emphasize">admin</span>(which we use to initially login) which was added to the group <span class="code-emphasize">Administrator</span> Get it?.
				</li>
				<li>Specify a name and description for your new group. Lets try <span class="code-emphasize">Encoder</span> as the group name</li>
				<li>The actions below are <span class="code-emphasize">authorities</span> which your user group could access. In our case, we wanted to create an encoder group which only has access to Patients tab and possibly the Dashboard Tab(the home page).</li>
				<li>Check the checkbox for the <span class="code-emphasize">Patient Tab</span> and <span class="code-emphasize">Dashboard Tab</span> then press <span class="code-emphasize">Save</span>.</li>
			</ol>
		</section>
		
		<section id="user">
			<h3>User</h3>
			<h4>Creating a new User</h4>
			<ol>
				<li>Select <span class="code-emphasize">Users</span> from your header</li>
				<li>
					Here you will see the pre-defined user for <span class="code-emphasize">Open-Tides 3</span> which is <span class="code-emphasize">Administrator</span>.
					This user is included in the Administrator <span class="code-emphasize">Usergroup</span>.
				</li>
				<li>Create a new user by pressing the button <button class="btn btn-info btn-small"><i class="icon-plus-sign icon-white"></i> Add User</button></li>
				<li>Fill up the necessary details. Then on the <span class="code-emphasize">Group/s</span> section, specify <span class="code-emphasize">Encoder</span>(its the one we created earlier)</li>
				<li>Don't forget to activate the user by ticking the <span class="code-emphasize">Active</span> checkbox.</li>
			</ol>
			
			<div class="alert alert-info">
				<strong>Heads up!</strong> Users may have multiple usergroups.
			</div>
		</section>
		
		<hr/>
		
		<h2><i class="icon-puzzle-piece"></i> Disclaimer</h2>
		<p>
			Naturally, if you log-in the new user you have created under the group <span class="code-emphasize">Encoder</span>,
			you would still see all the tabs on the menu bar. Why? because we have only limited the <span class="code-emphasize">Patients Tab</span> by adding the 
			<code>&lt;sec:authorize&gt;</code>. Other contents of the header are not yet given access limitation.
		</p>
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/dbevolve">&larr; Previous (DB Evolve)</a>
			</li>
			<li class="next">
				<a href="${home}/review">Next &rarr;</a>
			</li>
		</ul>
	</div>
</div>

<app:footer>
</app:footer>