<%--
	- input-file.tag
	- Generates input with type file element and handles file upload.
--%>
<%@ tag body-content="empty" dynamic-attributes="dAttrs" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="dropZone" required="false" type="java.lang.Boolean" %>
<%@ attribute name="dropZoneLabel" required="false" type="java.lang.String" %>
<%@ attribute name="id" required="true" type="java.lang.String" %>
<%@ attribute name="dataURL" required="false" type="java.lang.String" %>

<!-- Get all dynamic attributes of this tag. -->
<c:forEach items="${dAttrs}" var="attr">
	<c:set var="attrs" value='${attrs} ${attr.key}="${attr.value}"'/>
</c:forEach>

<!-- Initialize Upload URL. -->
<c:set var="uploadURL" value="${home}/image/upload"/>
<c:if test="${not empty dataURL}">
	<c:set var="uploadURL" value="${dataURL}" />
</c:if>

<!-- Drop Zone is enabled by default -->
<c:set var="hasDropZone" value="true" />
<c:if test="${not empty dropZone}">
	<c:set var="hasDropZone" value="${dropZone}" />
</c:if>

<div class="control-group ot-upload-file">
	
	<!-- Container of all image ID. -->
	<div id="imageIds" style="display: none;">
		<script id="imageIdsForUpload" type="text/template">
			<input class="ot-images" type="hidden" id="photos-{{imageId}}" name="photos" value="{{imageId}}"/>
		</script>
		<input class="ot-images" type="hidden" name="photos" value=""/>
	</div>
	
	<label class="control-label"><spring:message code="${label}" text="Images"/></label>
	<div class="controls">
		
		<div class="fileupload-buttonbar">
			<div class="fileupload-buttons">
				<span class="fileinput-button">
	            	<input id="${id}" ${attrs} type="file" name="attachments" multiple>
	            </span>
	            <span class="fileupload-loading"></span>
	        </div>
	        <div class="fileupload-progress fade" style="display:none">
	            <div class="progress" role="progressbar" aria-valuemin="0" aria-valuemax="100"></div>
	            <div class="progress-extended">&nbsp;</div>
	        </div>
		</div>
		
		<c:if test="${hasDropZone}">
			<!-- Drop Zone of files to be uploaded -->
        	<div id="dropzone"><spring:message code="${dropZoneLabel}" text="Drop files here"/></div>
        </c:if>
        
		<!-- Display Uploaded Files -->
        <table id="ot-image-list" class="table ot-image-list"><tbody class="files"></tbody></table>
        
        <script id="filesForUpload" class="template" type="text/template">
			<tr class="template-upload" >
        		<td style="width: 40%">
            		<p class="name">{{name}} ({{formattedFileSize}})</p>
        		</td>
        		<td style="width: 40%">
                	<div class="progress progress-striped active">
						<div class="progress-bar progress-bar-success" style="width:0%; height:20px; background: green"></div>
					</div>
        		</td>
				<td style="width: 20%">
					<button class="btn btn-warning cancel">
                    	<i class="icon-remove"></i>
                	</button>
				</td>
    		</tr>
		</script>
		<script id="filesForDownload" class="template" type="text/template">
			<tr class="template-download" id="template-download-{{imageId}}" >
        		<td style="width: 80%" colspan="2">
            		<img class="img-polaroid" src="${home}/image/{{imageId}}?c=x64"/>
        		</td>
				<td style="width: 20%">
					<button class="btn btn-warning cancel file-delete">
                    	<i class="icon-remove"></i>
                	</button>
				</td>
    		</tr>
		</script>
        
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		
		var formatFileSize = function (bytes) {
            if (typeof bytes !== 'number') {
                return '';
            }
            if (bytes >= 1000000000) {
                return (bytes / 1000000000).toFixed(2) + ' GB';
            }
            if (bytes >= 1000000) {
                return (bytes / 1000000).toFixed(2) + ' MB';
            }
            return (bytes / 1000).toFixed(2) + ' KB';
        }
		
		$('#${id}').fileupload({
			acceptFileTypes : /(\.|\/)(gif|jpe?g|png)$/i,
			dataType        : 'json',
			url             : '${uploadURL}',
			paramName       : 'attachment',
		    add: function (e, data) {
		    	var file = null;
	            $.each(data.files, function() {
	            	this.formattedFileSize = formatFileSize(this.size);
	            	file = opentides3.template($('script#filesForUpload').html(), this);
	            	$('.files').append(file);
	            });
	            data.context = $('.files tr:last');
	            data.submit();
	        },
	        progress: function (e, data) {
	        	var progress = Math.floor(data.loaded / data.total * 100);
	        	if (data.context) {
	        		data.context.find('.progress-bar').css('width', progress + '%');
	        		data.context.find('.cancel').click(function(e) {
        				e.preventDefault();
        			});
	        	}
	        },
	        done : function(e, data) {
	        	var imageId = $.trim(opentides3.template($('script#imageIdsForUpload').html(), data.result));
	        	$('#imageIds').append(imageId);
	        	if (data.context) {
	        		var newRow = opentides3.template($('script#filesForDownload').html(), data.result);
	        		data.context.replaceWith(newRow);
	        		data.context.find('.cancel').click(function(e) {
        				e.preventDefault();
        			});
	        	}
	        }
	        <c:if test="${hasDropZone}">
	        	,dropZone: $('#dropzone')
	        </c:if>
		});
		
	}).on('drop dragover', function (e) {
	    e.preventDefault();
	}).on('click', '.file-delete', function(e) {
		e.preventDefault();
		var id = $(this).closest(".template-download").attr("id").split("-")[2];
		$('input.ot-images[value="' + id + '"]').remove();
		$(this).closest(".template-download").hide();
	});;
	
</script>	