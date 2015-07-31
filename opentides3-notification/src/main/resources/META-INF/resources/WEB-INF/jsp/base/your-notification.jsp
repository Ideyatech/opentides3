<%@ page contentType="text/html;utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="tides" uri="http://www.ideyatech.com/tides"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 
<app:header pageTitle="label.notification.your-notifications" active="system" />

<div id="bd" class="container">

	<!-- START OF CONTENT -->
    	<div style="float:right;">
 		<table border="0">
 			<tr>
 				<td>
 					<div class="date input-append">
					<input name="st" type=text" placeHolder="Start Date" id="startDate" value='<fmt:formatDate value="${startDate}" pattern="MM/dd/yyyy"/>' 
						class="input-small"/>
					<button id="start-button" type="button" class="btn date-button"><i class="icon-calendar"></i></button>
					</div>
				</td>
 				<td> &nbsp; to  &nbsp; </td>
 				<td>
					<div class="date input-append">
					<input name="st" type=text"  placeHolder="End Date" id="endDate" value='<fmt:formatDate value="${endDate}" pattern="MM/dd/yyyy"/>' 
						class="input-small" />
					<button id="end-button" type="button" class="btn date-button"><i class="icon-calendar"></i></button>
					</div>
 				</td> 
 				<td>
 					<div style="margin-top:-8px;">
					<a id="dateFilter" class="btn btn-primary" href="#">Go</a>			
 					</div>
 				</td>
			
 			</tr>		
 		</table>
 		</div>
    <h4><spring:message code="label.notification.your-notifications"/> </h4>
    <div class="notification-all">
    	<c:if test="${empty notifications}">
    	<div>    	
        	<p class="date"><spring:message code="message.notification.you-have-no-notifications"/></p>
        </div>
    	</c:if>
    	<c:if test="${not empty notifications}">
    		<jsp:useBean id="now" class="java.util.Date"/>
    		<jsp:useBean id="yest" class="java.util.Date"/>
    		<jsp:setProperty name="yest" property="time" value="${yest.time - 86400000}"/> <!-- 86400000 = 60*60*24*1000  -->
    		
    		<fmt:formatDate pattern="MMMM dd, yyyy" value="${notifications[0].createDate}" var="dateLoop" />
    		<fmt:formatDate pattern="MMMM dd, yyyy" value="${now}" var="today" />
    		<fmt:formatDate pattern="MMMM dd, yyyy" value="${yest}" var="yesterday" />
    		
	    	<div>    	
	        	<p class="date">
	        		<c:choose>
      					<c:when test="${dateLoop eq today}">Today</c:when>
      					<c:when test="${dateLoop eq yesterday}">Yesterday</c:when>
      					<c:otherwise>${dateLoop}</c:otherwise>
					</c:choose>
	        	</p>    		
    		<c:forEach items="${notifications}" var="notification">
    			<fmt:formatDate pattern="MMMM dd, yyyy" value="${notification.createDate}" var="dateCurr" />
    			<c:if test="${dateLoop ne dateCurr}">
    			<c:set var="dateLoop" value="${dateCurr}"/>
    		</div>
	    	<div>

		        	<p class="date">
	        		<c:choose>
      					<c:when test="${dateLoop eq today}">Today</c:when>
      					<c:when test="${dateLoop eq yesterday}">Yesterday</c:when>
      					<c:otherwise>${dateLoop}</c:otherwise>
					</c:choose>
					</p>        		
				</c:if>
	            <p class="item"><span class="time">
	            	<fmt:formatDate pattern="hh:mm a" value="${notification.createDate}" var="time" />
	            	${time}
	            	</span>${notification.message}
	            </p>
    		</c:forEach>
	    	</div>
	     </c:if>
    </div>
	<!-- END OF CONTENT -->
</div>

<div id="preloader" class="modal hide fade" role="dialog" data-backdrop="static" data-keyboard="false">
    <div class="modal-body" style="text-align:center;padding-bottom:15px;">
    
    </div>
    <div class="footer" style="text-align:center;padding-bottom:15px;">
    <button class="btn" data-dismiss="modal" aria-hidden="true"><spring:message code="label.close" /></button>
    </div>
</div>

<app:footer>
	<script type="text/javascript">
		$('#startDate').datepicker({
			format: "mm/dd/yyyy",
			todayHighlight: true,
			autoclose: true
		});
		$('#endDate').datepicker({
			format: "mm/dd/yyyy",
			todayHighlight: true,
			autoclose: true
		});
		
		$('#dateFilter').click(function() {
			var url = "${home}/your-notifications/page?sd="+$('#startDate').val()+"&ed="+$('#endDate').val();
			window.location = url;
		});	
		
		$(document).ready(function(){

		}).on('change','#startDate, #endDate',function(){
			validateDateRange();
		});
		
		var validateDateRange = function () {
			if($('#startDate').val() != "" && $('#endDate').val() != "" && $('#startDate').val()>$('#endDate').val()) {
				
				$('#preloader .modal-body').html("Start Date should not be greater than End Date");
			    $('#preloader .footer').removeClass("hide");
				$('#preloader').modal('show');
			}
		};
		
	</script>

</app:footer>
