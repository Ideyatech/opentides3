<%@ tag dynamic-attributes="attributes" isELIgnored="false" body-content="scriptless" %>
<%@ attribute name="commentList" required="true" type="java.util.Collection"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ attribute name="commentableId" required="true" type="java.lang.Long"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="comments-panel">

	<h3><spring:message code="label.comments"/></h3>

	<c:if test="${empty commentList}">
		<spring:message code="label.no-comments"/>
	</c:if>
	
	<ul class="media-list">
	
	<script type="text/template" class="template">
		<li class="media" data-id="{{id}}">
			<a class="pull-left" style="margin-right: 20px;">
				<img class="img-circle" src="${home}/user/photo?id={{authorId}}&size=s"/>
			</a>
			<div class="media-body">
				<h4 class="media-heading">{{author}}</h4>
				<blockquote>
					<p>{{text}}</p>
					<small>{{timestamp}}</small>
				</blockquote>
				<button class="btn btn-link btn-small remove-comment"><spring:message code="label.remove-comment"/></button>
			</div>
		</li>
	</script>
	
	<c:forEach items="${commentList}" var="comment">
	
		<li class="media comment" data-id="${comment.id}">
			<a class="pull-left" style="margin-right: 20px;">
				<img class="img-circle" src="${home}/user/photo?id=${comment.author.id}&size=s"/>
			</a>
			<div class="media-body">
				<h4 class="media-heading">${comment.author.completeName}</h4>
				<blockquote>
					<p>${comment.text}</p>
					<small>${comment.prettyCreateDate}</small>
				</blockquote>
				
				<c:forEach items="${comment.files}" var="file">
					<button data-download-path="${file.fullPath}" class="btn btn-link btn-small download">Download attachment</button>
				</c:forEach>
				
				<button class="btn btn-link btn-small remove-comment"><spring:message code="label.remove-comment"/></button>
			</div>
		</li>
		
	</c:forEach>
	
	</ul>

	<hr/>

	<form:form id="send-comment-form" commandName="command"
		enctype="multipart/form-data" method="POST"
		action="${action}?commentableId=${commentableId}">
		<div class="control-group">
			<textarea id="text" name="text" class="input-block-level"
				placeholder="<spring:message code="label.write-comment"/>" required></textarea>
		</div>
		<div class="form-actions">
		
			<input type="file" id="file" name="file" class="hide" />
		
			<div class="input-append pull-left">
				<input id="file-path" type="text" readonly>
				<a class="btn" id="browse-file"><spring:message code="label.comment.attach-file" /></a>
			</div>
		
			<button class="btn btn-info pull-right">
				<spring:message code="label.send-comment" />
			</button>
		</div>
	</form:form>

</div>

<script type="text/javascript">

	$(document).ready(function() {
		$('#send-comment-form').ajaxForm(function(data) {
			if(data.sent){
				var template = opentides3.template($('.media-list').find('script.template').html());
				$('.media-list').append(template(data));
		
				$('#text').val('');
				$('#file').val('');
				$('#file-path').val('');
				$('#browse-file').text('<spring:message code="label.comment.attach-file" />');
			}
		});
		
		$('#browse-file').on("click", function(){
			$('#file').click();
		});
		
		$('#file').on("change", function() {
		   $('#file-path').val($(this).val());
		   $('#browse-file').text('<spring:message code="label.comment.change-attachment" />');
		});
		
	}).on("click", '.remove-comment', function(){

		var commentId = $(this).closest('.comment').data('id');
		var url = '${home}${action}/delete?commentId=' + commentId; 
			
		bootbox.dialog('<spring:message code="label.comment.confirm-remove" />',
				[{"label" : '<spring:message code="label.remove-comment"/>',
				  "class" : "btn-danger",
				  "callback" : function() {

						$.ajax({
							url : url,
							success : function(data) {
								if(data.deleted) {
									$('.comment[data-id="' + commentId + '"]').remove();
								}
							},
							dataType : 'json'
						});
					}
				},
				{"label" : "Cancel",
				 "class" : "btn"
				}]);
	});
	
</script>