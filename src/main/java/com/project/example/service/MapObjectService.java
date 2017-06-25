package com.project.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.example.exception.MapObjectNotFoundException;
import com.project.example.model.MapObject;
import com.project.example.repository.MapObjectRepository;

@Component
public class MapObjectService {

	@Autowired
	MapObjectRepository mapObjectRepository;
	
	public List<MapObject> getMapObject(){
		List<MapObject> all = mapObjectRepository.findAll();
		return all;
	}
	
	public void addMapObject(String id,float x,float z,Boolean exist,int item){
		MapObject mapObject = new MapObject(id, x, z, exist, item);
		mapObjectRepository.save(mapObject);		
	}
	
	public void update(String id,boolean exist,int item){
		MapObject mapObject = mapObjectRepository.findOne(id);
		if(mapObject == null) throw new MapObjectNotFoundException();
		
		mapObject.setExist(exist);
		mapObject.setItem(item);
		mapObjectRepository.save(mapObject);
	}
	
	public void deleteAll(){
		mapObjectRepository.deleteAll();
	}

}
