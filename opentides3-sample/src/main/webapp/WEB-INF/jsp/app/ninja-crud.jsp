<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<app:header pageTitle="label.ninja" active="ninja">
	
	<!-- Required for photo cropping -->
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery-jcrop.min.css'/>" />
	<script type="text/javascript" src="<c:url value='/js/jquery-jcrop.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery-ui.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.iframe-transport.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.fileupload.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.fileupload-process.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.fileupload-image.js'/>"></script>
</app:header>

<div id="ninja-body">

<ul class="breadcrumb">
  <li><a href="${home}"><spring:message code="label.home"/></a> <span class="divider">/</span></li>
  <li><spring:message code="label.ninja"/></li>
</ul>

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
				    <tides:input path="firstName" label="label.ninja.firstName" cssClass="input-block-level"/>
				    <tides:input path="lastName" label="label.ninja.lastName" cssClass="input-block-level"/>
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
				<tides:status results="${results}" />
			</div>
		</div>
		
		<div class="clear"></div>
		
		<div class="table-wrapper-2 overflow-hidden">
			<div class="table-wrapper">	
				<table id="ninja-results" class="footable table-bordered table-striped table-hover table-condensed" data-page="${results.currPage}" >
					<thead>
						<tr class="table-header">
							<th data-class="expand" data-field-name="completeName"><spring:message code="label.ninja.completeName"/></th>
							<th data-class="expand" data-field-name="completeName"><spring:message code="label.ninja.photo"/></th>
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
									<a href="${home}/ninja/profile/{{id}}">
										{{completeName}}
									</a>
								</td>
								<td>
									<div class="btn-group">
									  <img class="img-polaroid" src="${home}/image/{{primaryPhoto.id}}?c=32"/>
									</div>
								</td>
	                			<td>{{email}}</td>
	                			<td>{{age}}</td>
	                			<td>{{score}}</td>
	                			<td>{{joinDate}}</td>
								<td>{{active}}</td>
								<td>
									<i class='icon-pencil edit-action' data-id='{{id}}' data-title="<spring:message code="label.edit" />"></i>
									<i class='icon-trash remove-action' data-id='{{id}}' data-title="<spring:message code="label.delete" />"></i>
									<a data-url="${home}/image/upload?imageId={{primaryPhoto.id}}&className=Ninja&classId={{id}}" class="upload-photo">
										<i class="icon-upload"></i> <spring:message code="photo.change-photo" />
									</a>
								</td>
							</tr>
						</script>
						<c:forEach items="${results.results}" var="record" varStatus="status">
							<tr id="ninja-row-${record.id}" data-id="${record.id}">
								<td>
									<a href="${home}/ninja/profile/${record.id}">
										<c:out value="${record.completeName}" />
									</a>
								</td>
								<td>
								  <img class="img-polaroid" src="${home}/image/${record.primaryPhoto.id}?c=32"/>
								</td>
			                	<td><c:out value="${record.email}" /></td>
			                	<td><c:out value="${record.age}" /></td>
			                	<td><c:out value="${record.score}" /></td>
			                	<td><fmt:formatDate value="${record.joinDate}" pattern="MM/dd/yyyy"/></td>
			                	<td><c:out value="${record.active}" /></td>
								<td>
									<i class='icon-pencil edit-action' data-id='${record.id}' data-title="<spring:message code="label.edit" />"></i>
									<i class='icon-trash remove-action' data-id='${record.id}' data-title="<spring:message code="label.delete" />"></i>
									<a data-url="${home}/image/upload?imageId=${record.primaryPhoto.id}&className=Ninja&classId=${record.id}" class="upload-photo">
										<i class="icon-upload"></i>
									</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>	
			</div>
		</div>

		<div class="paging clearfix">
			<tides:paging results="${results}"/>
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
			<div class="modal-body" style="overflow-x: hidden !important">
				<div class="message-container"></div>
				<tides:input label="label.ninja.firstName" path="firstName" required="true"/>
				<tides:input label="label.ninja.lastName" path="lastName" required="true" />
				<tides:input label="label.ninja.email" path="email" />
				<tides:input label="label.ninja.age" path="age" />
				<tides:input label="label.ninja.score" path="score" />
				<tides:tokenizer label="label.ninja.tags" path="tags" />
				<tides:input label="label.ninja.joinDate" path="joinDate" datepicker="true" appendIcon="icon-calendar"/>
				<tides:radio label="label.ninja.gender" path="gender" items="${genderList}" itemLabel="value" itemValue="key" required="true"/>
				<tides:select label="label.ninja.status" path="status" items="${statusList}" itemLabel="value" itemValue="key" select2="true" />
				<tides:select label="label.ninja.skills" path="skillSet" items="${skillsList}" itemLabel="value" itemValue="key" multiple="true" select2="true" />
				<tides:checkbox label="label.ninja.active" path="active"/>
				<tides:input-file label="Images" id="fileUpload"/>
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

<tides:footer>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#ninja-body").RESTful();
			$('body').tooltip({selector: '.edit-action, .remove-action'});
		})
		.on("click", '.adjust-photo', opentides3.showAdjustPhoto)
		.on("click", '.upload-photo', opentides3.showUploadPhoto);
		
	</script>
</tides:footer>