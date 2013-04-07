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
		<li class="media">
			<a class="pull-left" style="margin-right: 20px;">
				<img class="img-circle" src="${home}/organization/users/photo?id={{authorId}}&size=s"/>
			</a>
			<div class="media-body">
				<h4 class="media-heading">{{author}}</h4>
				<blockquote>
					<p>{{text}}</p>
					<small>{{timestamp}}</small>
				</blockquote>
			</div>
		</li>
	</script>
	
	<c:forEach items="${commentList}" var="comment">
	
		<li class="media">
			<a class="pull-left" style="margin-right: 20px;">
				<img class="img-circle" src="${home}/organization/users/photo?id=${comment.author.id}&size=s"/>
			</a>
			<div class="media-body">
				<h4 class="media-heading">${comment.author.completeName}</h4>
				<blockquote>
					<p>${comment.text}</p>
					<small>${comment.prettyCreateDate}</small>
				</blockquote>
				<button class="btn btn-link btn-small"><spring:message code="label.remove-comment"/></button>
			</div>
		</li>
		
	</c:forEach>
	
	</ul>

	<form:form id="send-comment-form" commandName="command"
		enctype="multipart/form-data" method="POST"
		action="${action}?commentableId=${commentableId}">
		<div class="control-group">
			<textarea id="text" name="text" class="input-block-level"
				placeholder="<spring:message code="label.write-comment"/>"></textarea>
		</div>
		<div class="form-actions">
			<button class="btn btn-info pull-right">
				<spring:message code="label.send-comment" />
			</button>
		</div>
	</form:form>

</div>

<script type="text/javascript">
	$('#send-comment-form').ajaxForm(function(data) {
		var template = opentides3.template($('.media-list').find('script.template').html());
		$('.media-list').append(template(data));

		$('#text').val('');
	});
</script>