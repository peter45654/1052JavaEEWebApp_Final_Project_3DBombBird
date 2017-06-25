package com.project.example.controller;

import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.project.example.dto.MapObjectDTO;
import com.project.example.service.MapObjectService;
import com.pusher.rest.Pusher;

@RestController
@RequestMapping("game/data/map")
public class MapController {

	private static Logger log = LoggerFactory.getLogger(GameController.class);
	private MapObjectService mapObjectService;
	
	private Pusher pusher = new Pusher("347538", "c0fe32e9371eeaf84578", "11e9ac527d7294d7db28");
	
	@Autowired
	public MapController(MapObjectService mapObjectService) {
		this.mapObjectService = mapObjectService;
	}
	
	
	// 20*20 (*5) 
	// (0,0)      (20,0)
	//
	// 
	// (0,20)     (20,20)
	
	
	//channel name: my-game
	//event name: map
	
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String returnMapObject(){
		/*
		pusher.setCluster("ap1");
		pusher.setEncrypted(true);
		*/
        String json = new Gson().toJson(mapObjectService.getMapObject());
        //pusher.trigger("my-game", "map", json);
        return json;
	}
	
	@RequestMapping(method = RequestMethod.POST)
    public void newGame(){
		Random random = new Random();
		int i;
		float ranX, ranZ;
		int ranItem;
		boolean ranExist;
		for(i = 1;i <= 150; i++){
			
			//while();
			ranX = random.nextFloat()*19+1;
			ranZ = random.nextFloat()*19+1;
			ranExist = true;
			ranItem = random.nextInt(4);
			
			mapObjectService.addMapObject(Integer.toString(i), ranX, ranZ, ranExist, ranItem);
		}

		pusher.setCluster("ap1");
		pusher.setEncrypted(true);
		
        String json = new Gson().toJson(mapObjectService.getMapObject());
        pusher.trigger("my-game", "map", json);
	}
	
	
	@PutMapping
	public void updateMapObject(@RequestBody List<MapObjectDTO> mapObjectDTO){
		//log.info("{}",mapObjectDTO);
		for(MapObjectDTO mapObject: mapObjectDTO){
			mapObjectService.update(mapObject.getId(), mapObject.isExist(), mapObject.getItem());
		}
		pusher.setCluster("ap1");
		pusher.setEncrypted(true);
		
        String json = new Gson().toJson(mapObjectService.getMapObject());
        pusher.trigger("my-game", "map", json);
	}

	
	
	
	@DeleteMapping
	public void deleteMapObject(){
		mapObjectService.deleteAll();
	}
	
	@PostConstruct
    public void init(){
    	newGame();
    }
    
    @PreDestroy
    public void destroy(){
    	mapObjectService.deleteAll();
    }
}
