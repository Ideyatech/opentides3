<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="label.ninja" active="ninja">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery-jcrop.min.css'/>" />
	<script type="text/javascript" src="<c:url value='/js/jquery-jcrop.min.js'/>"></script>
</app:header>

<div id="ninja-body">

<div id="search-body">

	<div id="search-panel" class="span3">

		<div id="search-panel-inner" data-spy="affix" data-offset-top="60">
			<div class="navbar">
				<div class="navbar-inner">
					<span class="brand"><i class="icon-search"></i><spring:message code="label.search" /></span>
					<a class="show-search-form btn collapsed pull-right hidden-desktop hidden-tablet" data-toggle="collapse" data-target=".search-form">
						<i class="icon-chevron-up"></i>
						<i class="icon-chevron-down"></i>
					</a>
				</div>
			</div>
			<div class="search-form collapse">
				<form:form modelAttribute="searchCommand" id="ninja-search" >
					<div class="control-group">
						<form:label path="firstName" cssClass="control-label"><spring:message code="label.ninja.firstName"/></form:label>
						<div class="controls">
							<form:input path="firstName" maxlength="50" cssClass="input-block-level"/>
						</div>
					</div>
					<div class="control-group">
						<form:label path="lastName" cssClass="control-label"><spring:message code="label.ninja.lastName"/></form:label>
						<div class="controls">
							<form:input path="lastName" maxlength="50" cssClass="input-block-level"/>
						</div>
					</div>
					<div class="control-group">
						<form:label path="email" cssClass="control-label"><spring:message code="label.ninja.email" /></form:label>
						<div class="controls">
							<form:input path="email" maxlength="50" cssClass="input-block-level"/>
						</div>
					</div>
					<hr/>
					<input type="submit" class="btn btn-info btn-block" data-submit="search" value="<spring:message code="label.search"/>">
					<button type="button" class="btn btn-link btn-block" data-submit="clear"><spring:message code="label.clear" /></button>
				</form:form>
			</div>
		</div>
	</div>
	
	<div id="results-panel" class="span9">
		
		<div id="message-panel" class="row-fluid">
			<button id="ninja-add" class="btn btn-info add-action">
				<i class="icon-plus-sign icon-white"></i>
				<spring:message code="label.ninja.add" />
			</button>
			<div class="status" data-display-pagelinks="false" data-display-summary="true" data-summary-message='
				<spring:message code="message.displaying-x-of-y" arguments="#start,#end,#total,records"/>
			'>
				<app:status results="${results}" />
			</div>
		</div>
		
		<div class="clear"></div>
		
		<div style="overflow:hidden">
			<div style="width:200%;">
				<table id="ninja-results" class="footable table-bordered table-striped table-hover table-condensed" style="width:50%;" data-page="${results.currPage}" >
					<thead>
						<tr class="table-header">
							<th data-class="expand" data-field-name="completeName"><spring:message code="label.ninja.photo"/></th>
							<th data-class="expand" data-field-name="completeName"><spring:message code="label.ninja.completeName"/></th>
		                   	<th data-hide="phone" data-field-name="email"><spring:message code="label.ninja.email"/></th>
		                	<th data-hide="phone,tablet" data-field-name="age"><spring:message code="label.ninja.age"/></th>
		               		<th data-hide="phone,tablet" data-field-name="score"><spring:message code="label.ninja.score"/></th>
		               		<th data-hide="phone,tablet" data-field-name="joinDate"><spring:message code="label.ninja.joinDate"/></th>
		               		<th data-hide="phone,tablet" data-field-name="active"><spring:message code="label.ninja.active"/></th>
							<th data-field-name="ot3-controls"></th>
						</tr>
					</thead>
					<tbody>
						<script type="text/template" class="template">
	                		<tr id="ninja-row-{{id}}" data-id="{{id}}">
								<td>
									<img class="img-polaroid" src="${home}/ninja/photo?id={{id}}&size=xs"/>
									<i class='icon-upload upload-photo'></i>
									<i class='icon-edit adjust-photo'></i>
								</td>
								<td>{{completeName}}</td>
	                			<td>{{email}}</td>
	                			<td>{{age}}</td>
	                			<td>{{score}}</td>
	                			<td>{{joinDate}}</td>
								<td>{{active}}</td>
								<td>
									<i class='icon-pencil edit-action' data-id='{{id}}'></i>
									<i class='icon-trash remove-action' data-id='{{id}}'></i>
								</td>
							</tr>
						</script>
						<c:forEach items="${results.results}" var="record" varStatus="status">
							<tr id="ninja-row-${record.id}" data-id="${record.id}">
								<td>
									<img class="img-polaroid" src="${home}/ninja/photo?id=${record.id}&size=xs"/>
									<i class='icon-upload upload-photo'></i>
									<i class='icon-edit adjust-photo'></i>
								</td>
								<td><c:out value="${record.completeName}" /></td>
			                	<td><c:out value="${record.email}" /></td>
			                	<td><c:out value="${record.age}" /></td>
			                	<td><c:out value="${record.score}" /></td>
			                	<td><fmt:formatDate value="${record.joinDate}" pattern="MM/dd/yyyy"/></td>
			                	<td><c:out value="${record.active}" /></td>
								<td>
									<i class='icon-pencil edit-action' data-id='${record.id}'></i>
									<i class='icon-trash remove-action' data-id='${record.id}'></i>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>	
			</div>
		</div>

		<div class="paging clearfix">
			<app:paging results="${results}"/>
		</div>
	</div>
	
</div>

<div id="form-body" class="modal fade hide">

	<div id="form-panel" >
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="${add}"><spring:message code="label.ninja.add" /></h4>
			<h4 class="${update}"><spring:message code="label.ninja.update" /></h4>
		</div>

		<form:form modelAttribute="formCommand" id="ninja-form" cssClass="form-horizontal">
			<div class="modal-body">
				<app:input label="label.ninja.firstName" path="firstName" required="true"/>
				<app:input label="label.ninja.lastName" path="lastName" required="true" />
				<app:input label="label.ninja.email" path="email" type="email" />
				<app:input label="label.ninja.age" path="age" />
				<app:input label="label.ninja.score" path="score" />
				<app:input label="label.ninja.joinDate" path="joinDate" datepicker="true" appendIcon="icon-calendar"/>
				<app:radio label="label.ninja.gender" path="gender" items="${genderList}" itemLabel="value" itemValue="key" required="true"/>
				<app:select label="label.ninja.status" path="status" items="${statusList}" itemLabel="value" itemValue="key" select2="true" />
				<app:select label="label.ninja.skills" path="skillSet" items="${skillsList}" itemLabel="value" itemValue="key" multiple="true" select2="true" />
				<app:checkbox label="label.ninja.active" path="active"/>
				<br/>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-link" data-dismiss="modal"><spring:message code="label.close" /></button>
				<input type="submit" class="btn btn-info  ${add}" data-form-display="add" data-submit="save-and-new" value="<spring:message code="label.save-and-new" />" />
				<input type="submit" class="btn btn-success" data-submit="save" value="<spring:message code="label.save" />" />
				<input type="hidden" name="id" />
			</div>
		</form:form>
	</div>
	
</div>

	
<div class="adjust-photo-modal modal hide fade" data-width="760" tabindex="-1"></div>
<div class="upload-photo-modal modal hide fade" data-width="760" tabindex="-2"></div>
	
</div>

<app:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			//$('.footable').footable();
			$("#ninja-body").RESTful();
			
		}).on("click", '.adjust-photo', showAdjustPhoto).on("click", '.upload-photo', showUploadPhoto);
		
		function showUploadPhoto(){
			var id = $(this).closest('tr').data('id');
			var url = '${home}/ninja/photo/upload?id=' + id;
			
			$('.upload-photo-modal').load(url, function(){
				$('.upload-photo-modal').modal();
			});
		}
		
		function showAdjustPhoto(){
			var id = $(this).closest('tr').data('id');
			var url = '${home}/ninja/photo/adjust?id=' + id;
			
			$('.adjust-photo-modal').load(url, function(){
				$('.adjust-photo-modal').modal().on('shown', function(){
					$('#original-image').Jcrop({
						setSelect: [0, 0, 200, 200],
						allowSelect: false,
						minSize: [ 200, 200 ],
						aspectRatio: 1,
						onChange: opentides3.updateThumbnailCoordinates
					});
				});
			});
		}
		
	</script>
</app:footer>