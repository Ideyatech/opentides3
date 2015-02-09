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
<%@ attribute name="isFile" required="false" type="java.lang.Boolean" %>
<%@ attribute name="fieldName" required="false" type="java.lang.String" %>
<%@ attribute name="isMultiple" required="false" type="java.lang.String" %>
<%@ attribute name="imageOnly" required="false" type="java.lang.String" %>

<c:if test="${empty isMultiple}" >
	<c:set var="isMultiple" value="false" />
</c:if>

<c:if test="${empty imageOnly}">
	<c:set var="imageOnly" value="true" />
</c:if>

<c:set var="fName" value="files"></c:set>
<c:if test="${not empty fieldName }">
	<c:set var="fName" value="${fieldName}"></c:set>
</c:if>

<!-- Get all dynamic attributes of this tag. -->
<c:forEach items="${dAttrs}" var="attr">
	<c:set var="attrs" value='${attrs} ${attr.key}="${attr.value}"'/>
</c:forEach>

<!-- Initialize Upload URL. -->
<c:set var="uploadURL" value="${home}/image/upload"/>

<!-- Determine if file upload -->
<c:if test="${isFile}">
	<c:set var="uploadURL" value="${home}/file-template/upload" />
</c:if>

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
	<div id="${id}-attachmentIds" style="display: none;">
		
	</div>
	<script id="attachmentIdsForUpload" type="text/template">
		<input id="hidden-attachment-{{attachmentId}}" class="ot-files" type="hidden" name="${fName}" value="{{attachmentId}}"/>
	</script>
	
	<input id="" class="ot-files" type="hidden" name="files" value=""/>
	
	<label class="control-label"><spring:message code="${label}" text="Files"/></label>
	<div class="controls">
		
		<div class="fileupload-buttonbar">
			<div class="fileupload-buttons">
				<span class="fileinput-button">
					<c:set var="control" value="" />
					<c:if test="${not empty imageOnly}">
						<c:set var="control" value="accepts=\"image/*\""/>
					</c:if>
					<input type="button" value='Browse...' class="browse-button-dummy"/>
	            	<input id="${id}" ${attrs} type="file" name="attachments" ${control} multiple style="display: none;">
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
        <table id="${id}-ot-attachment-list" class="table ot-attachment-list"><tbody class="files"></tbody></table>
        
        <script id="filesForUpload" class="template" type="text/template">
			<tr class="template-upload" >
        		<td style="width: 40%">
            		<strong><p class="name">{{name}} ({{formattedFileSize}})</p></strong>
        		</td>
        		<td style="width: 40%">
                	<div class="progress progress-striped active">
						<div class="progress-bar progress-bar-success" style="width:0%; height:20px; background: green"></div>
					</div>
        		</td>
				<td style="width: 20%">
                	<button class="btn btn-small btn-danger cancel"><i class="icon-remove"></i></button>
				</td>
    		</tr>
		</script>
		<script id="filesForDownload" class="template" type="text/template">
			<tr class="template-download" >
        		<td style="width: 80%" colspan="2">
            		<strong>{{attachmentName}}</strong>
        		</td>
				<td style="width: 20%">
					<input type="hidden" id="fileUploadId" class="files" value="{{attachmentId}}"/>
					<input type="hidden" class="fileList" value="{{attachmentId}}"/>
                	<a id="remove-file-id-{{attachmentId}}" class="btn btn-small btn-danger remove-attachment"><i class="icon-remove"></i></a>
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
	            	$('table#${id}-ot-attachment-list tbody.files').append(file);
	            });
	            data.context = $('table#${id}-ot-attachment-list tbody.files tr:last');
	            data.submit();
	        },
	        progress: function (e, data) {
	        	var progress = Math.floor(data.loaded / data.total * 100);
	        	if (data.context) {
	        		data.context.find('.progress-bar').css('width', progress + '%');
	        	}
	        },
	        done : function(e, data) {
	        	if(data.result.attachmentId){
	        		<c:if test="${not isMultiple}">
				    $('div#${id}-attachmentIds').empty();
		        	</c:if>
				    
		        	var attachmentId = $.trim(opentides3.template($('script#attachmentIdsForUpload').html(), data.result));

		        	$('div#${id}-attachmentIds').append(attachmentId);
		        	
		        	if (data.context) {
		        		var newRow = opentides3.template($('script#filesForDownload').html(), data.result);
		        		data.context.replaceWith(newRow);
		        	}
		        	
		        	var $input = $('#${id}');
		        	if($input.attr('id') != 'fileUploadCheck'){
		        		$input.prop('disabled', true);
		        	}
		        	$input.trigger('change');
		        	<c:if test="${not isMultiple}">
		        		$('.browse-button-dummy').prop('disabled', true);
		        	</c:if>
	        	} else {
	        		if(data.result.messages[0].code === 'photo.invalid-file-type'){
	        			alert(data.result.messages[0].message);
	        			data.context.remove();
	        		}
	        	}
	        }
	        <c:if test="${hasDropZone}">
	        	,dropZone: $('#dropzone')
	        </c:if>
		});
		
		//for removing files that are uploaded
		$('#${id}-ot-attachment-list').off().on('click', '.remove-attachment', function() {
		    var attachmentId = $(this).attr('id').split('-')[3];
		    $("#${id}-attachmentIds #hidden-attachment-"+attachmentId).val('');
		    
		    $(this).parent().parent().fadeOut(); 
		    $('.notifications').notify({ message: "Attachment successfully removed", type: 'success'}).show();
		    $('.browse-button-dummy').prop('disabled', false);
		   	$('#${id}').prop('disabled', false);
			$('#${id}').trigger('change');
		});
		
	}).on('drop dragover', function (e) {
	    e.preventDefault();
	}).on('click', '.browse-button-dummy', function() {
		$(this).next('input[type="file"]').click();
	});
</script>	