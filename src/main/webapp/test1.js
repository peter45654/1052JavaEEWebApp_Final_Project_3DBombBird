var currentWholeUrl=window.location.href;
var testUrl=currentWholeUrl.substring(7,currentWholeUrl.lastIndexOf("P")-1);
var testUrlId=currentWholeUrl.charAt(currentWholeUrl.lastIndexOf(".")-1);
$(document).ready(function() {

});

var pusher = new Pusher('c0fe32e9371eeaf84578', {
      cluster: 'ap1',
      encrypted: true
    });


    var channel = pusher.subscribe('my-game');
    channel.bind('message', function(data) {
        var message = JSON.parse(data);
        console.log(message);
        $("#chatbox ul").append("<li class='red'><span>Player" + message.id +": </span><label id='chatLine1'>"+ message.message +"</label></li>");
        var lis = $("#chatbox ul li");
        if(lis.length > 10) {
            lis.eq(0).remove();
        }
    });
    
    
var id = testUrlId;
$(window).keydown(function(e){
	  if (e.keyCode == 13){
        if ($("#chatInput").is(":focus")) {
            var text = $('#chatInput').val();
            $('#chatInput').val("");
            console.log(text);
            sendMessage(id,text);
            $('#chatInput').blur();
        }else{
            
            $('#chatInput').focus();
        }
	  }
});

function sendMessage(id,message){
    var data = {
        "id":id,
        "message":message
    }
    str = JSON.stringify(data);
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": "http://"+testUrl+"/game/data/player/message",
        "method": "POST",
        "headers": {
            "content-type": "application/json",
            "cache-control": "no-cache",
            "postman-token": "ca971a82-0571-02f0-ae02-9cf414a6474f"
        },
        "processData": false,
        "data": str
    }

    $.ajax(settings).done(function (response) {
        console.log(response);
    });
}
    