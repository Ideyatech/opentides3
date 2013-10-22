<%--
	- tutorial.jsp
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
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>

<app:header pageTitle="title.interfaces" active="advanced">
	<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
</app:header>

<div class="row-fluid">
	<!-- NAVIGATION -->
	<div id="sideBar" class="span2" style="top: 55px;">
		<ul class="nav nav-list side-nav affix">
			<li><a href="#taggable">Taggable</a></li>
			<li><a href="#imageuploadable">ImageUploadable</a></li>

		</ul>
	</div>
	
	<!-- CONTENT -->
	<div class="span10">
		<ul class="breadcrumb">
			<li>Advanced <span class="divider">/</span></li>
			<li>Interfaces</li>
		</ul>
		
		<h1><i class="icon-book"></i> Opentides 3 Interfaces</h1>
		<p class="lead">
			Opentides 3 provides various interfaces depending on our needs. These interfaces guide the developers (us)
			in implementing various tasks.
		</p>
		
		<section id="taggable">
			<h3>Taggable</h3>
			<p>
				The interface <span class="code-emphasize">Taggable</span> is used when we want to add "tags" into our entities.
				This tags are commonly used for searching items inside a forum or a blog. However, this is not yet fully implemented in <span class="code-emphasize">Opentides 3</span>
			</p>
			<br/>
			<form:form modelAttribute="formCommand" cssClass="form-horizontal">
				<app:tokenizer label="label.tutorial.tags" path="tags" />
			</form:form>
	
			<h4>Usage</h4>
			<ol>
				<li>Implement the interface <span class="code-emphasize">Taggable</span> from <span class="code-emphasize">org.opentides.bean</span>.</li>
				<li>
					Define a list of <span class="code-emphasize">Tags</span> as an attribute and add the necessary annotations and mappings as seen below:
					<div class="example">
						<code class="prettyprint">
							@OneToMany(cascade = CascadeType.REMOVE)<br/>
							@JoinTable(name="PATIENT_TAGS",<br/>
							joinColumns = {<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;@JoinColumn(name="PATIENT_ID", referencedColumnName="ID")<br/>
							},<br/>
							inverseJoinColumns = {<br/>
							&nbsp;&nbsp;&nbsp;&nbsp;@JoinColumn(name="TAG_ID")<br/>
							})<br/>
							@JsonView(Views.FormView.class)<br/>
							private List<Tag> tags;<br/>
						</code>
					</div>
				</li>
				<li>
					Create a getter and setter for the <span class="code-emphasize">tags</span> attribute. Annotate the getter function with <code>@JsonSerialize(using = TagsSerializer.class)</code>.
					This will help later on for rendering our tags when updating the details.
				</li>
				<li>
					In our form (which is inside <span class="code-emphasize">patient-crud.jsp</span>), we add a new field:
					<div class="example">
						<code class="prettyprint">
							&lt;tides:tokenizer label="label.patient.tags" path="tags" /&gt;
						</code>
					</div>
				</li>
			</ol>
		</section>
		
		
		<section id="imageuploadable">
			<h3>ImageUploadable</h3>
			<p>
				Uploading of images can be a very hard task to implement; luckily <span class="code-emphasize">Opentides 3</span> provided us with 
				an interface that would help us in accomplishing this task with ease. <span class="code-emphasize">ImageUploadable</span> lets you upload multiple images at the same time and specify a primary image among them. 
			</p>
			<br/>
			<form:form modelAttribute="formCommand" cssClass="form-horizontal">
				<tides:input-file label="Images" id="fileUpload"/>
			</form:form>
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
		</section>
		
		
		<!-- pager -->
		<ul class="pager">
			<li class="previous">
				<a href="${home}/start">&larr; Previous (Change Me)</a>
			</li>
		</ul>
	</div>
</div>

<app:footer>
</app:footer>