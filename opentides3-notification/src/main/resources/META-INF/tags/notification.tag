<%@ attribute name="url" required="true" %>

<div class="btn-group pull-right hidden-tablet hidden-phone">
	<a class="btn dropdown-toggle btn-inverse" data-toggle="dropdown" href="#">
		<div id="n-alert"> </div>
	</a>
	<ul id="notification" class="dropdown-menu">
	</ul>
</div>


<script type="text/javascript">
    $(document).ready(function() {
        function callback(response) {
            if (response.transport != "polling" && response.state == "messageReceived") {
                if (response.status == 200) {
                    var data = JSON.parse(response.responseBody);
                    if (data) {
                    	if (data.length > 1) {
                            $("#n-alert").html(data[0]);
                        	$("#notification").html("<li>Notifications</li> <li class='divider'></li>");
                        	len = data.length;
                        	if (len > 10) len = 10;
                            for (i=1;i<len;i++) {
                            	$("#notification").append("<li>"+data[i]+"</li>");
                            }
                            if (data.length > 10) {
                                $("#notification").append("<li class='divider'></li>"+
                                		"<li><a href='${home}/my-notifications'>View All</a></li>");                            	
                            }
                    	} else {
                    		// no notifications
                    		$("#notification").html("No Notifications");
                    	}
                    }
                }
            }
        }
        $('#notify-count').click(function() {
        	$('#notification').show();
        });

        $.atmosphere.subscribe(
                "${url}",
                callback,
                $.atmosphere.request = { transport:"websocket", requestCount: 0 });
    });
</script>
