var currentWholeUrl=window.location.href;
var webTemp1=currentWholeUrl.substring(7,currentWholeUrl.lastIndexOf("P")-1);
var webTemp2="localhost:8080";
var cut=currentWholeUrl.substr(22);
var currentUrl=cut.substr(0,7);
//for java traditional severlet
var playerId=currentUrl.substr(6)-1;
//for spring playerId
var playerId2=currentWholeUrl.charAt(currentWholeUrl.length-6);
console.log("currentWholeUrl: "+currentWholeUrl);
console.log("cut: "+cut);
console.log("currentUrl: "+currentUrl);
console.log("playerId: "+playerId);
//function setPlayer(){
	//myPlayer=players[0];	
	//}
 function connect(actionIn,positionIn,rotationIn,putBomb,encounterItem){
	
	$.ajax({url: "GameControl",data:{ name:currentUrl,action:actionIn,position:positionIn,rotation:rotationIn,Bomb:putBomb,encounter:encounterItem},
		cache: false,type:"POST",success:function(text){
													//$("#viewBoard").append('<p>(Connect)'+text+'</p>');
													console.log("success");
													updateScene(text);
													}
	}
	);	
	
}
function updateScene(text)
{	
	var myJSONObject=JSON.parse(text); 
 //  $("#viewBoard").append('<p>(real_position)'+myPlayer.mesh.position.y+'</p>');
   var count = Object.keys(myJSONObject).length+1;

   //for(var c=0;c<count;c++){
   //player.mesh.rotation.y =myJSONObject.player[0].rotationY;
   //if(!player.detectPlayerCollision())
	for(var i=0;i<count;i++){
		try{
		players[i].mesh.position.set(myJSONObject.player[i].position[0],
									 myJSONObject.player[i].position[1],
									 myJSONObject.player[i].position[2]);
		//players[i].mesh.rotation=myJSONObject.player[i].rotation;
		}catch(e){
			
		}
   }
}
function refresh(){

	$.ajax({url: "GameControl",data:{action:"refresh"},
		cache: false,type:"POST",success:function(text){
															var myJSONObject2=JSON.parse(text);
															var count2 = Object.keys(myJSONObject2).length+1;
															   console.log("myJSONObject2 : "+myJSONObject2);
															for(var i=0;i<count2;i++){
																try{
																//修改位置
																players[i].mesh.position.set(myJSONObject2.player[i].position[0],
																							 myJSONObject2.player[i].position[1],
																							 myJSONObject2.player[i].position[2]);
																//修改rotation
																players[i].mesh.rotation.y=myJSONObject2.player[i].rotation;
																console.log("players[i].mesh.rotation : "+players[i].mesh.rotation);
																console.log("players[i].mesh.rotation : "+players[i].mesh.rotation);
																}catch(e){}
															}
													 }
		}
	);	
	
}
 function playerMove(actionIn,positionIn,rotationIn){
	  
		$.ajax({url: "http://"+webTemp1+"/game/data/player/playerMove",
			method: "POST",
			data:{"action":actionIn,"name":playerId2,"position":positionIn,"rotation":rotationIn},
			cache: false,
			success:function(){
				//$("#viewBoard").append('<p>(Connect)'+text+'</p>');
				console.log("playerMove success");
		
				}
	
			});
	 	 
 }
 function setBomb(positionXIn,positionZIn,fire){
	 console.log("setBooooooooomb :"+positionXIn+"  "+positionZIn);
		$.ajax({url: "http://"+webTemp1+"/game/data/player/playerBomb",
			method: "POST",
			data:{"bombPositionX":positionXIn,"bombPositionZ":positionZIn,"bombFire":fire},
			cache: false,
			success:function(text){
				//$("#viewBoard").append('<p>(Connect)'+text+'</p>');
				console.log("setBomb success");
				console.log(text);
				}
	
			});
	 
 }
 function playerItemFire(){
		$.ajax({url: "http://"+webTemp1+"/game/data/player/playerAddFire",
			method: "POST",
			data:{"playerId":playerId2},
			cache: false,
			success:function(num){
				var firetemp=num;
					players[playerId2-1].fire=firetemp;
					console.log("playerItemFire success");
				}
			});
 }
 function playerItemSpeed(){
		$.ajax({url: "http://"+webTemp1+"/game/data/player/playerAddFire",
			method: "POST",
			data:{"playerId":playerId2},
			cache: false,
			success:function(fnum){
				var temp=fnum;
					players[playerId2-1].speed=temp;
					console.log("playerItemSpeed success");
				}
			});
}
 function tellDeath(id){
	 
	 $.ajax({url: "http://"+webTemp1+"/game/data/player/playertoldDeath",
			method: "POST",
			data:{"playerId":id},
			cache: false,
			success:function(text){
					var deathData=JSON.parse(text);
					var deathCount = Object.keys(deathData).length;
					for(var c=0;c<deathCount;c++){
						players[playerId2-1].alive=deathData[c].alive;
						console.log("tellDeath : "+deathData[c].alive);
					}
					//players[playerId2-1].speed=temp;
					console.log("tellDeath success");
				}
			});
 }
 function bombRefresh(){
		$.ajax({url: "http://"+webTemp1+"/game/data/player/returnBombMethod",
			method: "GET",
			data:{"playerId":playerId2},
			cache: false,
			success:function(text){
				 var bombData=JSON.parse(text); 
				 var bombDataCount = Object.keys(bombData).length;
				 console.log("bombRefresh  : "+ bombDataCount);
				 for(var i=0;i<bombDataCount;i++){
					 var bomb=models.bomb.mesh.clone();
					 bomb.scale.set(2.7,2.7,2.7);
					 bomb.position.set(bombData[0].x,2.5,bombData[0].z);
					 bomb.fire=bombData[0].fire;
					 bombs.push(bomb);
					 bomb.alive = true;
					 setTimeout(function(){
						 players[playerId2-1].canAtk+=1;//可放的炸彈數量增加
							explode(bomb);
							bomb.alive = false;
							
						}, 1500);
					 scene.add(bomb);
				 	}
				}
			});
		
}
 function springRefresh(){
	  
		$.ajax({url: "http://"+webTemp1+"/game/data/player",
			method: "GET",
			cache: false,
			success:function(text){
				//$("#viewBoard").append('<p>(Connect)'+text+'</p>');
				//console.log("load refresh success");
				//console.log(text);
				springUpdateScene(text);
				}
	
		});
}
 function springUpdateScene(text){
	 var Data=JSON.parse(text); 
	 var dataCount = Object.keys(Data).length+1;
	 for(var i=0;i<dataCount;i++){
			try{
			//修改位置
			players[i].mesh.position.set(Data[i].x,
										Data[i].y,
										Data[i].z);
			//修改rotation
			players[i].mesh.rotation.y=Data[i].rotation;
			//console.log("players[i].mesh.rotation : "+players[i].mesh.rotation);
			//console.log("players[i].mesh.rotation : "+players[i].mesh.rotation);
			//修改死亡
			players[i].alive=Data[i].alive;
			if(!players[i].alive){
				scene.remove(players[i].mesh);
				players[i].fire=0;  // player的可爆炸範圍
				players[i].canAtk=0; // player的可放的炸彈數量
			}
			}catch(e){}
			
	 }
	 bombRefresh();
 }

 
 
 