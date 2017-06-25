package com.project.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "map object not found")
public class MapObjectNotFoundException  extends RuntimeException{
	
}