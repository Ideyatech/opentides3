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
	<div id="sideBar" class="span2">
		<div id="nav-list-wrapper" class="affix">
			<ul class="nav nav-list side-nav">
				<li><a href="#imageuploadable">ImageUploadable</a></li>
				<li><a href="#input-file">Input file tag</a></li>
				<li><a href="#upload-primary">Uploading primary image</a></li>
				<li><a href="#display-primary">Displaying primary image</a></li>
			</ul>
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
		
		
		<section id="imageuploadable">
			<h3>ImageUploadable</h3>
			<p>An interface which allows us to upload images directly into the implementing class.</p>
			<h4>Usage</h4>
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
					These attributes will hold our uploaded images.
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
			</ol>
		</section>
		
		
		<section id="input-file">
			<h3>Input file tag</h3>
			<p>The <span class="code-emphasize">input_file</span> tag allows us to upload images to be attached into our model.</p>
			<div class="tutorial-body">
				<div class="example">
					<form:form modelAttribute="formCommand" cssClass="form-horizontal">
						<tides:input_file label="Images" id="fileUpload"/>
					</form:form>
				</div>
			</div>
			<h4>Usage</h4>
			<ol>
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
<!-- 				<li> -->
<%-- 					Append <code>.on("click", '.adjust-photo', opentides3.showAdjustPhoto).on("click", '.upload-photo', opentides3.showUploadPhoto);</code> into your  --%>
<!-- 					<span class="code-emphasize">jQuery</span> ready function. -->
<!-- 					<div class="example"> -->
<%-- 						<code class="prettyprint"> --%>
<!-- 							$(document).ready(function() {<br/> -->
<!-- 							&nbsp;&nbsp;&nbsp;&nbsp;//some codes here<br/> -->
<!-- 							})<br/> -->
<!-- 							.on("click", '.adjust-photo', opentides3.showAdjustPhoto)<br/> -->
<!-- 							.on("click", '.upload-photo', opentides3.showUploadPhoto);<br/> -->
<%-- 						</code> --%>
<!-- 					</div> -->
<!-- 				</li> -->
			</ol>
		</section>
		
		<section id="upload-primary">
			<h3>Uploading a primary image</h3>
			<p>The <span class="code-emphasize">primary image</span> would be the default image for that object.</p>
			<h4>Steps</h4>
			<ol>
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
					Create a link that would display our modal for uploading the image
					<div class="example">
						<code class="prettyprint">
							&lt;a data-url="&#36;{home}/image/upload?imageId=${patient.primaryPhoto.id}&className=Patient&classId=&#36;{record.id}" class="upload-photo"&gt;<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;Upload a primary photo<br/>
							&lt;/a&gt;<br/>
						</code>
					</div>
				</li>
				<li>Specify an <span class="code-emphasize">imageId</span> equal to the ID of the primary photo of your class, a <span class="code-emphasize">className</span> and a <span class="code-emphasize">classId</span> equal to the ID of your class.</li>
				<li>
					Create an empty div that will hold our modal for uploading and adjusting the photo
					<div class="example">
						<code class="prettyprint">
							&lt;div class="adjust-photo-modal modal hide fade" data-width="760" tabindex="-1"&gt;&lt;/div&gt;<br/>
							&lt;div class="upload-photo-modal modal hide fade" data-width="760" tabindex="-2"&gt;&lt;/div&gt;
						</code>
					</div>
					Ideally these elements are placed at the bottom of the page before the <span class="code-emphasize">footer</span> tag.
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
		</section>
		
		<section id="display-primary">
			<h3>Displaying the primary image</h3>
			<p>To display the primary image, copy, paste, and modify the following code:</p>
			<div class="example">
				<code class="prettyprint">
					&lt;img class="img-polaroid" src="${home}/image/&#36;{patient.primaryPhoto.id}?c=x64"/&gt;
				</code>
			</div>
			<h4>Sizing</h4>
			<table>
				<tr>
					<td class="wider">32</td>
					<td>Resizes the width to 32px and keeps aspect ratio</td>
				</tr>
				<tr>
					<td class="wider">x50</td>
					<td>Resizes the height to 50px and keep aspect ratio</td>
				</tr>
				<tr>
					<td class="wider">32x50</td>
					<td>Converts the image to max of 32px width or 50px height depending which produces the larger image</td>
				</tr>
				<tr>
					<td class="wider">32-c</td>
					<td>Crops the image to 32 x 32 pixels from the center</td>
				</tr>
				<tr>
					<td class="wider">32x50-c</td>
					<td>Crops the image to 32 x 50 pixels from the center</td>
				</tr>
				<tr>
					<td class="wider">32x50-c@100x100</td>
					<td>Crops the image to 32 x 50 pixels starting at location 100x100</td>
				</tr>
			</table>
		</section>
		
		<!-- pager -->
<!-- 		<ul class="pager"> -->
<!-- 			<li class="previous"> -->
<%-- 				<a href="${home}/advanced-overview">&larr; Go Back</a> --%>
<!-- 			</li> -->
<!-- 		</ul> -->
	</div>
</div>

<app:footer>
</app:footer>