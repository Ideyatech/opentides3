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
	<c:forEach var="message" items="${messages}">
		<h3>
			<spring:message code="${message}" />
		</h3>
	</c:forEach>

	<a href="${home}/"><spring:message code="label.home" /></a>
	<hr />

	<c:if test="${displayForm}">
		<c:choose>
			<c:when test="${action == 'request'}">
				<form:form commandName="passwordReset"
					action="${home}/request-password-reset/request">
					<div class="message-container">
						<spring:message code="msg.forgot-password-instructions" />
					</div>
					<tides:input path="emailAddress" label="label.email-address" />
					<div class="control-group">
						<div class="controls pagination-centered">
							<input type="submit" class="btn btn-info btn-large"
								value="<spring:message code="label.submit" />" />
						</div>
					</div>
				</form:form>
			</c:when>
			<c:when test="${action == 'change'}">
				<form:form commandName="passwordReset"
					action="${home}/change-password-reset/change">
					<form:hidden path="emailAddress" />
					<div class="message-container">
						<spring:message code="msg.change-password-instructions" />
					</div>
					<label for="emailAddress"><spring:message
							code="label.email-address" /></label> ${passwordReset.emailAddress}
						<label for="password"><spring:message
							code="label.password" /></label>
					<form:password path="password" size="40" maxlength="100"
						cssStyle="text" />
					<label for="confirmPassword"><spring:message
							code="label.confirm-password" /></label>
					<form:password path="confirmPassword" size="40" maxlength="100"
						cssStyle="text" />
					<label for="submit">&nbsp;</label>
					<input type="submit" value="Submit">
				</form:form>
			</c:when>
		</c:choose>
	</c:if>
</div>

<tides:footer />
