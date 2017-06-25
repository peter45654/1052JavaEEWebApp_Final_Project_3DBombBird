package com.project.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.example.exception.PlayerNotFoundException;
import com.project.example.model.Player;
import com.project.example.repository.PlayerRepository;

@Component
public class PlayerService {

	@Autowired
	PlayerRepository playerRepository;
	
	public List<Player> getPlayer() {
		List<Player> all = playerRepository.findAll();
		return all;
	}
	
	public void addFire(String id){
		Player player = playerRepository.findOne(id);
		int temp=player.getFire();
		temp+=1;
		player.setFire(temp);
		playerRepository.save(player);
		}
	public void addSpeed(String id){
		Player player = playerRepository.findOne(id);
		float temp=player.getSpeed();
		temp+=0.005;
		player.setSpeed(temp);
		playerRepository.save(player);
	}
	
	public void addUser(String id, float x, float y, float z){
		Player player = new Player(id);
		player.setX(x);
		player.setY(y);
		player.setZ(z);
		player.setRotation(0f);
		playerRepository.save(player);
	}
	public void playerBeDeath(String id){
		Player player = playerRepository.findOne(id);
		player.setAlive(false);
		playerRepository.save(player);
	}
	public Player getSinglePlayer(String id){
		Player player = playerRepository.findOne(id);
		if(player == null) throw new PlayerNotFoundException();
		return  player;
	}
	public void update(String id,float x, float y, float z,int bomb,int fire, int speed, boolean alive){
		Player player = playerRepository.findOne(id);
		if(player == null) throw new PlayerNotFoundException();
		
		player.setX(x);
		player.setX(y);
		player.setZ(z);
		player.setBomb(bomb);
		player.setFire(fire);
		player.setSpeed(speed);
		player.setAlive(alive);
		playerRepository.save(player);
	}
	public void playerMoveFun(String action,String id,float position,float rotation){
		Player player = playerRepository.findOne(id);
		if(player == null) throw new PlayerNotFoundException();
		 System.out.println("playerMove action : "+action);
		 System.out.println("playerMove id : "+id);
		 System.out.println("playerMove position : "+position);
		 System.out.println("playerMove rotation : "+rotation);
		 float temp=0;
		switch(action){
		case"MoveUp":
			temp=player.getZ();
			temp+=position;
			player.setZ(temp);
			player.setRotation(rotation);
			break;
		case"MoveDown":
			temp=player.getZ();
			temp+=position;
			player.setZ(temp);
			player.setRotation(rotation);
			break;
		case"MoveLeft":
			temp=player.getX();
			temp+=position;
			player.setX(temp);
			player.setRotation(rotation);
			break;
		case"MoveRight":
			temp=player.getX();
			temp+=position;
			player.setX(temp);
			player.setRotation(rotation);
			break;
		}
		
		playerRepository.save(player);
	}
	public void deleteAll(){
		playerRepository.deleteAll();
	}

	public boolean checkExist(String id){
		Player player = playerRepository.findOne(id);
		if(player == null){
			return false;
		}else{
			return true;
		}
	}

}
