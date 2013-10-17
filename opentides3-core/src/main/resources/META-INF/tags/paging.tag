<%--
	- paging.tag
	- Generates paging hyperlinks for a given BaseResults object
	-
	- @param results - the results object, must be of type BaseResults
	--%>
<%@ tag dynamic-attributes="attributes" isELIgnored="false" body-content="empty" %>
<%@ attribute name="results" required="true" type="org.opentides.bean.SearchResults" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="pagination pagination-centered">
<c:if test="${results.totalResults > 0}">	
	<c:if test="${results.totalPages > 1}">
     	<ul>
<!-- First Page -->
     		<c:set var="clazz" value=""/>
			<c:if test="${results.startPage == 1}">
	     		<c:set var="clazz" value="disabled"/>
			</c:if>
	        <c:url var="linkFirst" value="${baseURL}">
	            <c:param name="p" value="1"/>
	        </c:url>
            <li class="ot3-firstPage ${clazz}"><a href='${linkFirst}' data-page='1'>&laquo;</a></li>			
            
<!-- Prev Page -->
            <c:set var="clazz" value=""/>
			<c:if test="${results.currPage <= 1}">
	     		<c:set var="clazz" value="disabled"/>
		    </c:if>
	        <c:url var="linkPrev" value="${baseURL}">
	            <c:param name="p" value="${results.currPage-1}"/>
	        </c:url>
            <li class="ot3-prevPage ${clazz}"><a href="${linkPrev}" data-page='${results.currPage-1}'>&lsaquo;</a></li>			
		        
<!-- Paging -->
	    <c:forEach begin="${results.startPage}" end="${results.endPage}" step="1" var="page">
	        <c:url var="link" value="${baseURL}">
	            <c:param name="p" value="${page}"/>
	        </c:url>
            <c:set var="clazz" value=""/>
	        <c:if test="${page == results.currPage}">
	     		<c:set var="clazz" value="active"/>
	        </c:if>
            <li class="ot3-page-${page} ${clazz}"><a href="${link}" data-page='${page}'>${page}</a></li>	        
	    </c:forEach>
	    
<!-- Next Page -->
	        <c:url var="linkNext" value="${baseURL}">
	            <c:param name="p" value="${results.currPage+1}"/>
	        </c:url>
	        <c:set var="clazz" value=""/>
		    <c:if test="${results.currPage == results.endPage}">
		     	<c:set var="clazz" value="disabled"/>
		    </c:if>
	        <li class="ot3-nextPage ${clazz}"><a href="${linkNext}" data-page='${results.currPage+1}'>&rsaquo;</a></li>	        
        
<!-- Last Page -->
	        <c:url var="linkLast" value="${baseURL}">
	            <c:param name="p" value="${results.totalPages}"/>
	        </c:url>
	        <c:set var="clazz" value=""/>
		    <c:if test="${results.endPage == results.totalPages}">
		     	<c:set var="clazz" value="disabled"/>
		    </c:if>
	        <li class="ot3-lastPage ${clazz}"><a href="${linkLast}" data-page='${results.totalPages}'>&raquo;</a></li>	        
        </ul>
	</c:if>
</c:if>
</div>
