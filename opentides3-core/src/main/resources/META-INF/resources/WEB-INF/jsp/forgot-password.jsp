<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>

<app:header pageType="anonymous-page" />

<spring:theme code="logo_large" var="logo" />

<div class="wallpaper"></div>
<div class="wallpaper-grid"></div>

<div class="pagination-centered">

	<h2><spring:message code="label.forgot-password" /></h2>
	
	<c:forEach var="message" items="${messages}">
		<div class="alert alert-info">
			<spring:message code="${message}" text="${message}" />
		</div>
	</c:forEach>

	<c:if test="${displayForm}">
		<c:choose>
			<c:when test="${action == 'request'}">
				<form:form commandName="passwordReset"
					action="${home}/request-password-reset/request">

					<input type="hidden" name="cipher" value="${passwd.cipher}"/>

					<div class="${showForm}">
						<small>
							<spring:message code="msg.forgot-password-instructions" />
						</small>
						<hr/>
						<tides:input path="emailAddress" label="label.email-address" />

						<div class="control-group">
							<div class="controls pagination-centered">
								<input type="submit" class="btn btn-info"
									value="<spring:message code="label.submit" />" />
							</div>
						</div>
					</div>
				</form:form>
			</c:when>
			<c:when test="${action == 'change'}">
				<form:form commandName="passwordReset"
					action="${home}/change-password-reset/change">
					<form:hidden path="cipher" />
					<form:hidden path="emailAddress" />
					
					<small><spring:message code="msg.change-password-instructions" /></small>
					<hr/>
					<div class="control-group">
						<label for="emailAddress"><spring:message code="label.email-address" /></label>
						<div class="controls">
							<input type="text" disabled="disabled" value="${passwordReset.emailAddress}" />
						</div>
					</div>
					<div class="control-group">
						<label for="password"><spring:message code="label.password" /></label>
						<div class="controls">
							<form:password path="password" size="40" maxlength="100" cssStyle="text" />
						</div>
					</div>
					<div class="control-group">
						<label for="confirmPassword"><spring:message code="label.confirm-password" /></label>
						<div class="controls">
							<form:password path="confirmPassword" size="40" maxlength="100" cssStyle="text" />
						</div>
					</div>
							
					<input class="btn btn-info" type="submit" value="Submit">
				</form:form>
			</c:when>
		</c:choose>
	</c:if>
	
	<a href="${home}/"><spring:message code="label.home" /></a>
	
</div>

<app:footer />
