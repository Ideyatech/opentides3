<%--
	- status.tag
	- Generates the status message for search results
	-
	- @param results - the results object, must be of type BaseResults
	- @param resultLabel - name referring to the results. Used as "Displaying X of Y <recordName>
	--%>
<%@ tag isELIgnored="false" body-content="empty" %>
<%@ attribute name="results" required="true" type="org.opentides.bean.SearchResults" %>
<%@ attribute name="pageParamName" required="false" %>
<%@ attribute name="searchFormId" required="false" %>
<%@ attribute name="displaySummary" required="false" type="java.lang.Boolean" %>
<%@ attribute name="displayPageLinks" required="false" type="java.lang.Boolean" %>
<%@ attribute name="resultLabel" required="false" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="tResultLabel" value="records" />
<c:if test="${not empty recordName}">
<c:set var="tResultLabel" value="${resultLabel}" />
</c:if>

<c:if test="${(results.totalResults == 0)}">
    <spring:message code="message.no-results-found"/>
</c:if>
<c:if test="${results.totalResults > 0}">
		<span class="records">
		    <spring:message code="message.displaying-x-of-y" 
	    				arguments="${results.startIndex+1},${results.endIndex+1},${results.totalResults},${tResultLabel}"/>
		</span>
	    <span class="searchTime"> (<spring:message code="message.result-seconds" arguments="${results.searchTime/1000}"/>)</span>
</c:if>
