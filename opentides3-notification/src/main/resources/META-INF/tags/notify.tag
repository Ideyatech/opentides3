<%@ attribute name="url" required="true" %>
<%@ attribute name="id" required="true" %>

<script type="text/javascript">
    $(document).ready(function() {
        function callback(response) {
            $.atmosphere.log("info", ["response.state: " + response.state]);
            $.atmosphere.log("info", ["response.transport: " + response.transport]);
            $.atmosphere.log("info", ["response.status: " + response.status]);
            if (response.transport != "polling" && response.state == "messageReceived") {
                $.atmosphere.log("info", ["response.responseBody: " + response.responseBody]);
                if (response.status == 200) {
                    var data = response.responseBody;
                    if (data) {
                        $("#${id}").text(data);
                    }
                }
            }
        }

        $.atmosphere.subscribe(
                "${url}",
                callback,
                $.atmosphere.request = { transport:"websocket", requestCount: 0 });
    });
</script>
