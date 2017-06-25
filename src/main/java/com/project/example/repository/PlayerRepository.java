package com.project.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.example.model.Player;


public interface PlayerRepository extends MongoRepository<Player, String>{
	List<Player> findAllById(String id);
}
