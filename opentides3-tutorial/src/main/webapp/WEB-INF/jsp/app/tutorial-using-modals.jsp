<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="title.using-modal" active="advanced">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div id="sideBar" class="span2" style="top: 55px;">
		<ul class="nav nav-list side-nav affix">
			<li><a href="#origin">Validator</a></li>
			<li><a href="#internal">Internal Modal</a></li>
			<li><a href="#external">External Modal</a></li>
		</ul>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Advanced <span class="divider">/</span></li>
			<li>Using Modals</li>
		</ul>
		
		<h1><i class="icon-book"></i> Using Modals</h1>
		<p class="lead">
			Modals are basically pop-up windows within a specific page. 
			Opentides 3 implemented a responsive bootstrap modal which can easily be used.
		</p>
		<p class="lead"><a id="modalOn" class="pointer">Let me show you</a></p>
		
		<div id="modalOnContainer" class="modal-container modal hide fade">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h3><i class="icon-magic"></i> Kazam! Just like that!</h3>
		</div>
		
		<div class="alert alert-warning">
			<strong>Hold on!</strong> This page requires you to know how to <a href="http://docs.spring.io/spring/docs/3.0.x/reference/mvc.html" target="_blank">create your own spring controller</a>.
		</div>

		<section id="origin">
			<h3>Where does the new content come from??</h3>
			<p>There are 2 ways in which the contents may originate:</p>
			<ul>
				<li>One is by having the new contents in the same page as the parent page. (Internal)</li>
				<li>Another is by calling a new <span class="code-emphasize">jsp</span> page through the use of a <span class="code-emphasize">URL</span> (External)</li>
			</ul>
			<hr/>
		</section>
		
		<section id="internal">
			<h3>Internal Modal</h3>
			<p>They are commonly used when displaying static contents. Internal modal are considered to be faster since contents are loaded at the same time the parent page is loaded.</p>
			<h4>Usage</h4>
			<ol>
				<li>
					Create a <span class="code-emphasize">div</span> with the classes <span class="code-emphasize">modal</span>, <span class="code-emphasize">hide</span>, <span class="code-emphasize">fade</span>. Give it an <span class="code-emphasize">id</span>
					<div class="example">
						<code class="prettyprint">
							&lt;div id="modal-container" class="modal hide fade"&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;//write modal contents here<br/>
							&lt;/div&gt;<br/>
						</code>
					</div>
				</li>
				<li>
					Create an HTML element that would have a javascript event. 
					<div class="example">
						<code class="prettyprint">
							&lt;a id="modal-trigger"&gt;Show modal&lt;/a&gt;<br/>
						</code>
					</div>	
				</li>
				<li>
					In that event, call the <span class="code-emphasize">modal()</span> function of your div
					<div class="example">
						<code class="prettyprint">
							$('#modal-trigger').click(function(){<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;$('#modal-container').modal("show");<br/>
							});<br/>
						</code>
					</div>	
				</li>
				<li>
					To close the modal, we add the following code:
					<div class="example">
						<code class="prettyprint">
							&lt;button type="button" class="close" data-dismiss="modal"&gt;&times;&lt;/button&gt;
						</code>
					</div>
				</li>
			</ol>
		</section>
		
		<section id="external">
			<h3>External Modal</h3>
			<p>External modal are used when we wanted to display dynamic or large contents. They are loaded only when triggered. </p>
			<h4>Usage</h4>
			<ol>
				<li>
					Create a <span class="code-emphasize">div</span> with the classes <span class="code-emphasize">modal</span>, <span class="code-emphasize">hide</span>, <span class="code-emphasize">fade</span>. Give it an <span class="code-emphasize">id</span>
					<div class="example">
						<code class="prettyprint">
							&lt;div id="external-modal-container" class="modal hide fade"&gt;&lt;/div&gt;
						</code>
					</div>
				</li>
				<li>
					Create an HTML element that would have a javascript event. 
					<div class="example">
						<code class="prettyprint">
							&lt;a id="external-modal-trigger"&gt;Show external modal&lt;/a&gt;<br/>
						</code>
					</div>	
				</li>
				<li>
					In that event, call the <span class="code-emphasize">load()</span> function of your div then at its success callback,
					call the <span class="code-emphasize">modal()</span> function.
					<div class="example">
						<code class="prettyprint">
							$('#external-modal-trigger').click(function(){<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;$('#external-modal-container').load("URLHERE", function(){<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$('#external-modal-container').modal("show");<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;});<br/>
							});<br/>
						</code>
					</div>	
				</li>
				<li>Now we create the external modal. Search for the file <span class="code-emphasize">template-modal-external.jsp</span> in <span class="code-emphasize">src->main->webapp->WEB-INF->jsp->template</span></li>
				<li>Copy all of its content and create your own jsp file. This jsp file would serve as our view for the controller.</li>
				<li>
					Create a controller or a function within a controller. Use that function's URL as the URL for the load method
					<div class="example">
						<code class="prettyprint">
							$('#external-modal-container').load("${home}/externalModal", function(){});
						</code>
					</div>
				</li>
			</ol>
			<p>
				<i class="icon-comment"></i> The <span class="code-emphasize">load()</span> function is the one responsible for getting contents from another jsp page through a controller into our blank <span class="code-emphasize">div</span>. 
				After the contents being loaded, we simply displayed our <span class="code-emphasize">div</span> using the modal() function.
			</p>
		</section>
		
		<ul class="pager">
			<li class="previous">
				<a href="${home}/advanced-overview">&larr; Go Back</a>
			</li>
		</ul>
		
	</div>
</div>

<app:footer>
	<script type="text/javascript">
		$(document).ready(function(){			
			$('#modalOn').click(function(){
				$('#modalOnContainer').modal("show");
			});
		});
		
		$('.show-tooltip').tooltip();
	</script>
</app:footer>