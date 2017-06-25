package com.project.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.project.example.model.MapObject;

public interface MapObjectRepository extends MongoRepository<MapObject, String> {
	List<MapObject> findAllById(String id);
}
