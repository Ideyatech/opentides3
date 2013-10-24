<%--
	- @author - ONZ
--%>

<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>

<app:header pageTitle="title.interfaces" active="advanced">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div class="span2">
		<div class="alert alert-info">
			Images uploaded are saved locally in your machine. It can be configured inside
			the <strong>localhost.properties</strong> file
		</div>
	</div>
	
	<!-- CONTENT -->
	<div id="tutorial-content" class="span10">
		<ul class="breadcrumb">
			<li>Advanced <span class="divider">/</span></li>
			<li>Attaching Images</li>
		</ul>
		
		<h1><i class="icon-book"></i> Attaching images</h1>
		<p class="lead">
			Uploading images can be a very hard thing to implement. Luckily, <span class="code-emphasize">Opentides 3</span> provided
			us with a way to upload and attach images into our entities. It even supports multiple uploads too! Say hello to <span class="code-emphasize">ImageUploadable</span>
		</p>
		<form:form modelAttribute="formCommand" cssClass="form-horizontal">
			<tides:input-file label="Images" id="fileUpload"/>
		</form:form>
		<h3>Usage</h3>
		<p>In order to upload images, we will only need to change 2 files. Our jsp page to display our form and the entity which will have the image. Simple?</p>
		<ol>
			<li>Implement the interface <span class="code-emphasize">ImageUploadable</span> from <span class="code-emphasize">org.opentides.bean</span> and add the necessary functions</li>
			<li>
				Add the following attributes and create a getter and setter function for them:
				<div class="example">
					<code class="prettyprint">
						@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)<br/>
						@JoinTable(name = "PATIENT_PHOTO", <br/>
						&nbsp;&nbsp;&nbsp;joinColumns = { @JoinColumn(name = "PATIENT_ID", referencedColumnName = "ID")}, <br/>
						&nbsp;&nbsp;&nbsp;inverseJoinColumns = {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@JoinColumn(name = "PHOTO_ID")}<br/>
						)<br/>
						private List<ImageInfo> photos;<br/>
						<br/>
						private transient MultipartFile photo;<br/>
					</code>
				</div>
			</li>
			
			<li>
				Define the methods for the implemented functions:
				<div class="example">
					<code class="prettyprint">
						@Override<br/>
						public ImageInfo getPrimaryPhoto() {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;if(!CollectionUtils.isEmpty(this.photos)) {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for(ImageInfo imageInfo : this.photos) {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if(imageInfo.getIsPrimary()) {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return imageInfo;<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;return null;<br/>
						}<br/>
						<br/>
						public void addPhoto(ImageInfo photoInfo){<br/>
						synchronized (photoInfo) {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;if (photos == null){<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;photos = new ArrayList&lt;ImageInfo&gt;();<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;photos.add(photoInfo);<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
						}<br/>
					</code>
				</div>
			</li>
			<li>
				Import the following <span class="code-emphasize">CSS</span> and <span class="code-emphasize">JS</span> files into the page which will contain our form.
				<div class="example">
					<code class="prettyprint">
						&lt;link rel="stylesheet" type="text/css" href="&lt;c:url value='/css/jquery-jcrop.min.css'/&gt;" /&gt;<br/>
						&lt;script type="text/javascript" src="&lt;c:url value='/js/jquery-jcrop.min.js'/&gt;"&gt;&lt;/script&gt;<br/>
						&lt;script type="text/javascript" src="&lt;c:url value='/js/jquery-ui.min.js'/&gt;"&gt;&lt;/script&gt;<br/>
						&lt;script type="text/javascript" src="&lt;c:url value='/js/jquery.iframe-transport.js'/&gt;"&gt;&lt;/script&gt;<br/>
						&lt;script type="text/javascript" src="&lt;c:url value='/js/jquery.fileupload.js'/&gt;"&gt;&lt;/script&gt;<br/>
						&lt;script type="text/javascript" src="&lt;c:url value='/js/jquery.fileupload-process.js'/&gt;"&gt;&lt;/script&gt;<br/>
						&lt;script type="text/javascript" src="&lt;c:url value='/js/jquery.fileupload-image.js'/&gt;"&gt;&lt;/script&gt;<br/>
					</code>
				</div>
			</li>
			<li>
				Call the tag. Be sure that it is inside a <span class="code-emphasize">form</span> element.
				<div class="example">
					<code class="prettyprint">
						&lt;tides:input-file label="Images" id="fileUpload"/&gt;
					</code>
				</div>
			</li>
			<li>
				Append <code>.on("click", '.adjust-photo', opentides3.showAdjustPhoto).on("click", '.upload-photo', opentides3.showUploadPhoto);</code> into your 
				<span class="code-emphasize">jQuery</span> ready function.
				<div class="example">
					<code class="prettyprint">
						$(document).ready(function() {<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;//some codes here<br/>
						})<br/>
						.on("click", '.adjust-photo', opentides3.showAdjustPhoto)<br/>
						.on("click", '.upload-photo', opentides3.showUploadPhoto);<br/>
					</code>
				</div>
			</li>
		</ol>
		
		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/advanced-overview">&larr; Go Back</a>
			</li>
		</ul>
	</div>
</div>

<app:footer>
</app:footer>