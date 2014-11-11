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
                    	if (data.length > 1) {
                            $("${notifyEl}").html(data[0]).show();
                            desktopNotify('Notification', data[1]);
                            $("${contentEl}").html("");
                        	len = data.length;
                        	if (len > 10) len = 10;
                            for (i=1;i<len;i++) {
                            	$("${contentEl}").append("<li>"+data[i]+"</li>");
                            }
                            if (data.length > 10) {
                                $("${contentEl}").append("<li class='divider'></li>"+
                                		"<li><a href='${home}/your-notifications/page'>View All</a></li>");                            	
                            }
                    	} else {
                    		// no notifications
                    		$("${contentEl}").html("No notifications");
                    		$("${notifyEl}").html("").hide();
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
			      var n = new Notification(title, { icon: '${home}/base/img/icon_notify.png', body: message });
			}; 
		}
    });
</script>
