var currentWholeUrl=window.location.href;
var webTemp3=currentWholeUrl.substring(7,currentWholeUrl.lastIndexOf("W")-1);
var PlayerNum;
var players = ["P1","P2","P3","P4"];
var myBGM;
var intervalTitle = null , intervalBackGrandDiv = null;
var arphaValue = 1.0 ;

var playerID = "1";
//var domainName = "http://localhost:8080";
var pusher = new Pusher('c0fe32e9371eeaf84578', {
  cluster: 'ap1',
  encrypted: true
});

var channel = pusher.subscribe('my-game');
channel.bind('start', function(data) {
  console.log(data);
  if(data == "start"){
    StartCountDown(5);
  }
});
var settings = {
		  "async": true,
		  "crossDomain": true,
		  "url": "http://"+webTemp3+"/game/data/player/getready",
		  "method": "GET"
		}
		var flag = 0;
		$.ajax(settings).done(function (response) {
			console.log(response);
			$("#status").html("");
			response = JSON.parse(response);
			console.log(response);
			for(i=0;i<4;i++){
				if(response[i]) flag++;
			}
			if(flag == 4){
				flag = 0;
				window.location = "error.html";
			}else{
				flag = 0;
			}
		});

function start()
{

}

function checkArpha()
{
	arphaValue -= 0.005 ;
	console.log(document.getElementById('background'));
    document.getElementById('background').setAttribute("style","opacity :" + arphaValue + ";");

}

function removeTitle()
{
	console.log(123);
	document.getElementById('background').remove();
	document.getElementById('title').remove();
	document.getElementById('bomb').remove();
	intervalBackGrandDiv = null ;
	intervalTitle = null ;
}
		
function SetUp()
{	intervalBackGrandDiv = setInterval( checkArpha , 25 ) ;
	intervalTitle = setInterval( removeTitle , 5001 ) ;
    myBGM = new Audio('sounds/Tea Time.mp3');
    myBGM.addEventListener('ended', function() {
      this.currentTime = 0;
      this.play();
    }, false);

    myBGM.play();
}
function SetPlayer(PNumber){
  PlayerNum = PNumber;
  console.log("PlayerNum = " + PlayerNum);
    $("#buttons").append('<button id="Rb" class="Ready_but"type="button" name="Ready" onclick="GetReady(1)"></button>');
  if(PlayerNum==0)
  {
    $("#buttons").append('<button id="Sb" class="Start_but"type="button" name="Start" onclick="GetStart()"></button>');
  }
  for(var i=0;i<4;i++)
  {
    document.getElementById(players[i]).setAttribute("onclick"," ");
  }
}

  function ready(id){
			var settings = {
		  "async": true,
		  "crossDomain": true,
		  "url": "http://"+webTemp3+"/game/data/player/ready?id="+id,
		  "method": "GET"
			};
      var myBool;
			$.ajax(settings).done(function (response) {
				if(response){
          myBool = true;
        }
			});
      console.log(myBool);
      return myBool;
		}

		function unready(id){
			var settings = {
		  "async": true,
		  "crossDomain": true,
		  "url": "http://"+webTemp3+"/game/data/player/notready?id="+id,
		  "method": "GET"
			}

			$.ajax(settings).done(function (response) {
				console.log(response);
			});
		}
    $( window ).on('unload',function() {
			unready(PlayerNum+1);
		});
  function status(){
			var settings = {
		  "async": true,
		  "crossDomain": true,
		  "url": "http://"+webTemp3+"/game/data/player/getready",
		  "method": "GET"
			}

			$.ajax(settings).done(function (response) {
				console.log(response);
				$("#status").html("");
				response = JSON.parse(response);
				console.log(response);
				for(var i=0;i<4;i++){
					//change
          SetState(i,response[i]);
				}
				
			});
		}
setInterval(function(){ status(); }, 1000);
function GetReady(isReady)
{
    console.log("PlayerNum in GR = " +PlayerNum);
    if(isReady)
    {
      var id = PlayerNum+1;
      var settings = {
		  "async": true,
		  "crossDomain": true,
		  "url": "http://"+webTemp3+"/game/data/player/ready?id="+id,
		  "method": "GET"
			};
      var myBool;
			$.ajax(settings).done(function (response) {
				if(response){
          console.log("success");
          document.getElementById ('select').play();
          document.getElementById(players[PlayerNum]).setAttribute("stroke","Red");
          $("#"+players[PlayerNum]+"Img").show();
          document.getElementById('Rb').setAttribute("class","Cancel_but");
          document.getElementById('Rb').setAttribute("onclick","GetReady(0)");
        }else{
          swal("這個位子有人，請重新整理");
        }
			});
      /*
      if(ready(PlayerNum+1)){
        console.log("success");
        document.getElementById ('select').play();
        document.getElementById(players[PlayerNum]).setAttribute("stroke","Red");
        $("#"+players[PlayerNum]+"Img").show();
        document.getElementById('Rb').setAttribute("class","Cancel_but");
        document.getElementById('Rb').setAttribute("onclick","GetReady(0)");
      }
      */
    }
    else
    {
      document.getElementById ('cancel').play();
      document.getElementById(players[PlayerNum]).setAttribute("stroke","Green");
	    $("#"+players[PlayerNum]+"Img").hide();
      document.getElementById('Rb').setAttribute("class","Ready_but");
      document.getElementById('Rb').setAttribute("onclick","GetReady(1)");
      unready(PlayerNum+1);
    }
}

  function SetState(PNum,PState)
  {
    //console.log("PlayerNum in GR = " +PlayerNum);
    if(PState)
    {
      document.getElementById(players[PNum]).setAttribute("stroke","Red");
      $("#"+players[PNum]+"Img").show();
    }
    else
    {
      document.getElementById(players[PNum]).setAttribute("stroke","Green");
      $("#"+players[PNum]+"Img").hide();
    }
  }



function GetStart()
{
  var settings = {
    "async": true,
    "crossDomain": true,
    "url": "http://"+webTemp3+"/game/data/player/start",
    "method": "GET"
    }
  $.ajax(settings).done(function (response) {
      if(response){
        var count = 5 ;
        document.getElementById ('getStart').play();
        StartCountDown(count);
      }else{
        swal("請等待大家到齊且準備好");
      }
    });
  
}
var hint = ["移動方法","放下武器","炸彈爆炸","火力增強","遊戲道具"];
var but_text = ["下一步","結束教學"];
function ShowHint(frame)
{
  var ConC = false;
  var but_no = 0;
  if(frame>=4)
  {
    ConC = true;
    but_no = 1;
  }
  swal({
      title: "<span class='Salert'>"+hint[frame]+"</span>",
      confirmButtonText:but_text[but_no],
      imageUrl: "Hint"+ (frame) +".png",
      imageSize: "480x270",
      closeOnConfirm: ConC,
      color:"#fff6db",
      html:true
      },
      function()
      {
        if(frame<4)
        {
          frame++;
          ShowHint(frame);
        }
      }
    );
}
function StartCountDown(count)
{
  swal({
      title: "準備好",
      text: "<span class='Salert'>遊戲於" + (count--) + "秒鐘後開始</span>",
      timer: 1000,
      showConfirmButton: false,
      html:true
    },
    function()
    {
        if(count>0)
        {
          StartCountDown(count);
        }
        else
        {
          //swal({title: "騙你DER"});
          window.location = "Player" + (PlayerNum+1) + ".html";
        }
    }
    );
}

addEventListener( "load" , start , false ) ;