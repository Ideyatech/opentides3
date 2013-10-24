<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.dbevolve" active="advanced">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>



<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div class="alert alert-info">
			<strong>TIP</strong><br/>Don't forget to write the exact file extension for your images
		</div>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Advanced <span class="divider">/</span></li>
			<li>Adding Images</li>
		</ul>
		
		<h1><i class="icon-book"></i> Adding your Images</h1>
		<p class="lead">
			In this section, you will learn how to properly add and display your own images inside a <span class="code-emphasize">JSP</span> page.
		</p>

		<hr/>
		
		<h3>Using Images</h3>
		<ol>
			<li>
				Paste your images in the <span class="code-emphasize">src->main->webapp->app->img</span>.
				If directory does not exist, create one.
			</li>
			<li>
				In your jsp page, write the following code and change the name to match your file name.
				<div class="example">
					<code class="prettyprint">
						&lt;img src="img/yourimagename.jpg"/&gt;
					</code>
				</div>
			</li>
		</ol>
		
		<hr/>
		
		<h3>Styling</h3>
		<p>
			Styling your image border can easily be done by applying <span class="code-emphasize">Twitter Bootstrap's</span> custom classes.
			<a href="http://getbootstrap.com/2.3.2/base-css.html#images" target="_blank">Visit the link</a>
		</p>
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/advanced-overview">&larr; Go Back</a>
			</li>
		</ul>
		
		
	</div>
</div>

<app:footer>
</app:footer>