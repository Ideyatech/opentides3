<%-- URL to Subscribe for notification --%>
<%@ attribute name="url" required="true" %>

<%-- HTML element where notification is displayed. 
This element will display the number of notifications. --%>
<%@ attribute name="notifyEl" required="true" %>

<%-- HTML element where user clicks to display the 
dropdown of notifications. --%>
<%@ attribute name="clickEl" required="true" %>

<%-- HTML element where content of notification is displayed. --%>
<%@ attribute name="contentEl" required="true" %>

<%-- Flag if notification will be displayed as desktop notification. --%>
<%@ attribute name="desktopNotify" required="false" type="java.lang.Boolean" %>

					
<script type="text/javascript">
    $(document).ready(function() {
        function callback(response) {
            if (response.transport != "polling" && response.state == "messageReceived") {
                if (response.status == 200) {
                    var data = JSON.parse(response.responseBody);
                    if (data) {
                    	if (data.notifyCount > 0) {
                            $("${notifyEl}").html(data.notifyCount).show();
                            if (data.notifyCount > 1) {
                            	desktopNotify('Notification', 'You have '+data.notifyCount+' new notifications.');
                            } else if (data.notifications[0].medium == 'POPUP') {
                                desktopNotify('Notification', data.notifications[0].message);
                            }
                            $("${contentEl}").html("");
                    	} 
                    	len = data.notifications.length;
						if (len==0) {
	                    	// no notifications
                   			$("${contentEl}").html("No notifications.");
                    		$("${notifyEl}").html("").hide();							
						} else {							
	                    	if (len > 10) len = 10;
	                        for (i=0;i<len;i++) {
	                        	$("${contentEl}").append("<li>"+data.notifications[i].message+"</li>");
	                        }
	                        if (data.notifications.length >= 10) {
	                            $("${contentEl}").append(
	                            		"<li><a href='${home}/your-notifications/page'>View All</a></li>");                            	
	                        }
						}
                    }
                }
            }
        }
        
        $( "${clickEl}" ).click(function() {
			if ( $( "${contentEl}" ).is( ":hidden" ) ) {
				if (window.Notification && Notification.permission !== "granted") {
					Notification.requestPermission(function (status) {
						if (Notification.permission !== status) {
					        Notification.permission = status;
					    }
					});
				}
				$( "${contentEl}" ).slideDown( "moderate" );
				if ($("${notifyEl}").is( ":visible" )) {
					$.get( "${home}/your-notifications/pop-up-clear/${currentUser.user.id}", function( data ) {
						$("${notifyEl}").hide();
					});								
				}
			} else {
				$( "${contentEl}" ).slideUp("moderate");
			}
		}); 
        
        $.atmosphere.subscribe(
                "${url}",
                callback,
                $.atmosphere.request = { transport:"websocket", requestCount: 0 });
        
        
		function desktopNotify(title, message) {
			if (window.Notification && Notification.permission !== "denied") {						
				Notification.requestPermission(function (status) {
			        if (Notification.permission !== status) {
			          Notification.permission = status;
			        }
			    });
			}
			if (window.Notification && Notification.permission === "granted") {
			      var n = new Notification(title, { icon: '${home}/img/icon_notify.png', body: message });
			}; 
		}
    });
</script>
