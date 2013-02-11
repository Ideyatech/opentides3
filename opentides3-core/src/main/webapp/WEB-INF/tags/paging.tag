<%--
	- paging.tag
	- Generates paging hyperlinks for a given BaseResults object
	-
	- @param results - the results object, must be of type BaseResults
	- @param baseURL - URL to use in hyperlinks
	- @param tPageParamName - page parameter to pass in baseURL 
	- @param displaySummary - boolean to indicate whether summary will be displayed. (e.g. Displaying X of Y records)
	- @param displayPageLinks - boolean to indicate whether page links will be displayed
	- @param resultLabel - name referring to the results. Used as "Displaying X of Y <recordName>
	--%>
<%@ tag dynamic-attributes="attributes" isELIgnored="false" body-content="empty" %>
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

<c:set var="tPageParamName" value="p" />
<c:if test="${not empty pageParamName}">
<c:set var="tPageParamName" value="${pageParamName}" />
</c:if>

<c:if test="${(results.totalResults == 0) && (displaySummary)}">
    <div class="alert alert-warning"> 
    <spring:message code="message.no-results-found"/>
    </div>
</c:if>
<c:if test="${results.totalResults > 0}">
	<c:if test="${displaySummary}">
		<span class="records">
		    <spring:message code="message.displaying-x-of-y" 
	    				arguments="${results.startIndex+1},${results.endIndex+1},${results.totalResults},${tResultLabel}"/>
		</span>
	    <span class="searchTime"> (<spring:message code="message.result-seconds" arguments="${results.searchTime/1000}"/>)</span>		
	</c:if>	
	
	<c:if test="${results.totalPages > 1 && displayPageLinks}">
	<div class="pagination pagination-centered">
     	<ul>
<!-- First Page -->
     		<c:set var="clazz" value=""/>
			<c:if test="${results.startPage == 1}">
	     		<c:set var="clazz" value="disabled"/>
			</c:if>
	        <c:url var="linkFirst" value="${baseURL}">
	            <c:param name="${tPageParamName}" value="1"/>
	        </c:url>
            <li class="ot3-firstPage ${clazz}"><a href='${linkFirst}' data-page='1'>&lt;&lt;</a></li>			
            
<!-- Prev Page -->
            <c:set var="clazz" value=""/>
			<c:if test="${results.currPage <= 1}">
	     		<c:set var="clazz" value="disabled"/>
		    </c:if>
	        <c:url var="linkPrev" value="${baseURL}">
	            <c:param name="${tPageParamName}" value="${results.currPage-1}"/>
	        </c:url>
            <li class="ot3-prevPage ${clazz}"><a href="${linkPrev}" data-page='${results.currPage-1}'>&lt;</a></li>			
		        
<!-- Paging -->
	    <c:forEach begin="${results.startPage}" end="${results.endPage}" step="1" var="page">
	        <c:url var="link" value="${baseURL}">
	            <c:param name="${tPageParamName}" value="${page}"/>
	        </c:url>
            <c:set var="clazz" value=""/>
	        <c:if test="${page == results.currPage}">
	     		<c:set var="clazz" value="active"/>
	        </c:if>
            <li class="ot3-page-${page} ${clazz}"><a href="${link}" data-page='${page}'>${page}</a></li>	        
	    </c:forEach>
	    
<!-- Next Page -->
	        <c:url var="linkNext" value="${baseURL}">
	            <c:param name="${tPageParamName}" value="${results.currPage+1}"/>
	        </c:url>
	        <c:set var="clazz" value=""/>
		    <c:if test="${results.currPage == results.endPage}">
		     	<c:set var="clazz" value="disabled"/>
		    </c:if>
	        <li class="ot3-nextPage ${clazz}"><a href="${linkNext}" data-page='${results.currPage+1}'>&gt;</a></li>	        
        
<!-- Last Page -->
	        <c:url var="linkLast" value="${baseURL}">
	            <c:param name="${tPageParamName}" value="${results.totalPages}"/>
	        </c:url>
	        <c:set var="clazz" value=""/>
		    <c:if test="${results.endPage == results.totalPages}">
		     	<c:set var="clazz" value="disabled"/>
		    </c:if>
	        <li class="ot3-lastPage ${clazz}"><a href="${linkLast}" data-page='${results.totalPages}'>&gt;&gt;</a></li>	        
        </ul>
	</div>
	</c:if>
</c:if>
