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
		<br/>
		&nbsp;
	</c:if>
	<c:forEach items="${commentList}" var="comment">
	
		<div class="row-fluid">
			<div class="span1">
				<img class="img-circle" src="${home}/organization/users/photo?id=${comment.author.id}&size=s"/>
			</div>
			<div class="span11">
				<blockquote>
					<h4>${comment.author.completeName}</h4>
					<p>${comment.text}</p>
					<small>${comment.prettyCreateDate}</small>
				</blockquote>
			</div>
		</div>
		
	</c:forEach>
	
	<form:form id="ninja-upload-photo" commandName="command" enctype="multipart/form-data"
		method="POST" action="${action}?commentableId=${commentableId}">
		<div class="controls controls-row">
		  <textarea id="text" name="text"  class="span10" placeholder="<spring:message code="label.write-comment"/>"></textarea>
		  <button class="btn btn-info span2"><spring:message code="label.send-comment"/></button>
		</div>
	</form:form>

</div>