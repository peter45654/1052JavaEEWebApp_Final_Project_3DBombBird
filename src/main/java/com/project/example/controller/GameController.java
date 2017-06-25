package com.project.example.controller;

import com.project.example.model.Bomb;
import com.project.example.model.Player;
import com.project.example.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import com.project.example.dto.MessageDTO;
import com.project.example.dto.PlayerDTO;
import com.project.example.exception.PlayerOutOfBoundException;
import com.project.example.service.PlayerService;
import com.pusher.rest.Pusher;

@CrossOrigin

@RestController
@RequestMapping("game/data/player")
public class GameController {
	
	List<Bomb> bombList=new ArrayList<Bomb>(); 
    private static Logger log = LoggerFactory.getLogger(GameController.class);
    private PlayerService playerService = new PlayerService();
    private Pusher pusher = new Pusher("347538", "c0fe32e9371eeaf84578", "11e9ac527d7294d7db28");
    private boolean ready[]=new boolean[4];  
    //
    @Autowired
    public GameController(PlayerService playerService){
    	this.playerService = playerService;
    }
    //網頁*4 ->start
//	@RequestMapping("/playerMove")
//	public void finishGame(){
//	pusher.setCluster("ap1");
//	pusher.setEncrypted(true);
//	pusher.trigger("my-game", "finish", "finish");
//	}
    //channel name: my-game
  	//event name: player
    
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String returnPlayer(){
    	/*
    	pusher.setCluster("ap1");
    	pusher.setEncrypted(true);
        */
    	String json = new Gson().toJson(playerService.getPlayer());
        //pusher.trigger("my-channel", "player", json);
    	//System.out.println("test");
    	System.out.println("returnPlayer : "+json);
        return json;
    }
  
    @RequestMapping(value="returnBombMethod",method = RequestMethod.GET)
    public @ResponseBody String returnBomb( @RequestParam int playerId,HttpServletRequest request, HttpServletResponse responsel){
       String OutJson= new Gson().toJson(bombList);
       for(int c=0;c<bombList.size();c++){
    	   switch(playerId){
    	   case 1:bombList.get(c).setCheckp1(true);break;
    	   case 2:bombList.get(c).setCheckp2(true);break;
    	   case 3:bombList.get(c).setCheckp3(true);break;
    	   case 4:bombList.get(c).setCheckp4(true);break;
    	   }
    	   //只要兩位玩家拿取就刪掉炸彈buffer裡的炸彈
    	   if(bombList.get(c).getCheckp1()&&
     		  bombList.get(c).getCheckp2()
     		// &&bombList.get(c).getCheckp3()&&
     		 // bombList.get(c).getCheckp4()
     		  ){
    		   								bombList.remove(c); 
    	   	  								}
           }
	   System.out.println("bombList : "+playerId);
        return OutJson;
    }
    
    @RequestMapping("/playerMove")    
    @ResponseBody
    public void playerAction(@RequestParam String action,@RequestParam String name,
    						 @RequestParam float position,@RequestParam float rotation,
    						HttpServletRequest request, HttpServletResponse responsel) {
     System.out.println("from ajax action command: "+action);
     System.out.println("from ajax name : "+name);
     System.out.println("from ajax position : "+position);
     System.out.println("from ajax rotation : "+rotation);
    		    		
     playerService.playerMoveFun(action,name,position,rotation);
    }
    @RequestMapping("/playerBomb")    
    @ResponseBody
    public void playerSetBomb(@RequestParam float bombPositionX,@RequestParam float bombPositionZ,@RequestParam int bombFire,
    						HttpServletRequest request, HttpServletResponse responsel) {
     //bomb.position bomb.fire
     System.out.println("from ajax bomb.positionX : "+bombPositionX);
     System.out.println("from ajax bomb.positionZ : "+bombPositionZ);
     System.out.println("from ajax bomb.fire : "+bombFire);

     Bomb  insert=new Bomb(bombPositionX,bombPositionZ,bombFire);
     bombList.add(insert);
       
    }
    
    @RequestMapping("/playerAddSpeed")    
    @ResponseBody
    public float playerSpeedItem(@RequestParam String playerId,HttpServletRequest request ,HttpServletResponse responsel) {
   	Player pl = playerService.getSinglePlayer(playerId);
    playerService.addSpeed(playerId);
     return pl.getSpeed();
    }
    @RequestMapping("/playerAddFire")    
    @ResponseBody
    public int playerFireItem(@RequestParam String playerId,
    						HttpServletRequest request, HttpServletResponse responsel) {
     
     playerService.addFire(playerId);
     Player pl = playerService.getSinglePlayer(playerId);
     return pl.getFire();
    }
    @RequestMapping("/playertoldDeath")    
   //玩家登記死亡
    public  @ResponseBody String playerDeath(@RequestParam String playerId,
    						HttpServletRequest request, HttpServletResponse responsel) {
     playerService.playerBeDeath(playerId);
     String json = new Gson().toJson(playerService.getPlayer());
     return json;
    }
    
//    @RequestMapping("/gameover")
//    public void finishGame(){
//    	pusher.setCluster("ap1");
//    	pusher.setEncrypted(true);
//    	pusher.trigger("my-game", "finish", "finish");
//    }
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public String chat(@RequestBody MessageDTO messageDTO){
    	pusher.setCluster("ap1");
    	pusher.setEncrypted(true);
    	String json = new Gson().toJson(messageDTO);
    	System.out.println(json);
    	pusher.trigger("my-game", "message", json);
    	return messageDTO.getId() + messageDTO.getMessage();
    }

    @PostConstruct
    public void init(){
    	//init 4 ready個變數
    	for(int c=0;c<ready.length;c++){
    		ready[c]=false;
    	}
    	playerService.addUser("1", 22.5f, 2.5f, 22.5f);
    	playerService.addUser("2", 30.5f, 2.5f, 30.5f);
		playerService.addUser("3", 97.5f, 2.5f, 2.5f);
		playerService.addUser("4", 97.5f,2.5f, 97.5f);
    }
    
//    @PreDestroy
//    public void destroy(){
//    	playerService.deleteAll();
//    }
  //準備房間
  //-------------------------------------------------------------------------
    @RequestMapping("/start")
    public boolean startGame(){
    	
    	
    	int flag = 0;
    	for(int i = 0;i < 4;i++){
    		if(ready[i]) flag++;
    	}
    	if(flag == 4) {
        	pusher.setCluster("ap1");
        	pusher.setEncrypted(true);
        	pusher.trigger("my-game", "start", "start");
        	System.out.print("send");
    		return true;
    	}else
    		return false;
    }
    
    
    @RequestMapping(value = "/getready")
    public String gameGetReady(){
    	String json = new Gson().toJson(ready);
    	return json;
    }

    @RequestMapping(value = "/ready")
    public boolean gameReady(@RequestParam("id") String id){
    	/*
    	pusher.setCluster("ap1");
    	pusher.setEncrypted(true);
    	pusher.trigger("my-game", "start", "start");
    	*/
    	int player = Integer.parseInt(id);
    	player--;
    	if(!ready[player]){
    		ready[player] = true;
    		return true;
    	}else{
    		return false;
    	}
    }

    @RequestMapping(value = "/notready")
    public void gameNotReady(@RequestParam("id") String id){
    	/*
    	pusher.setCluster("ap1");
    	pusher.setEncrypted(true);
    	pusher.trigger("my-game", "start", "start");
    	*/
    	int player = Integer.parseInt(id);
    	player--;
    	ready[player] = false;
    }
    
    @RequestMapping("/gameover")
    public void finishGame(){
    	for(int i = 0;i < 4;i++){
    		ready[i] = false;
    	}
    	pusher.setCluster("ap1");
    	pusher.setEncrypted(true);
    	pusher.trigger("my-game", "finish", "finish");
    }
    //-------------------------------------------------------------------------
    
    
}
