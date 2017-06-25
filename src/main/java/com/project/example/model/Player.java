package com.project.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Player {

	@Id
	private String id;
	private float x;
	private float y;
	private float z;
	private float rotation;
	private int bomb;
	private int fire;
	private float speed;
	private boolean alive;
	
	public Player() {
		setX(0f);
		setY(0f);
		setZ(0f);
		setBomb(1);
		setFire(1);
		setSpeed(1);
		setAlive(true);
		setId("1");
		setRotation(0f);
	}
	public Player(String id) {
		setX(0f);
		setY(0f);
		setZ(0f);
		setBomb(1);
		setFire(1);
		setSpeed(1);
		setAlive(true);
		setId(id);
	}

	public float getRotation() {
		return rotation;
	}
	
	public void setRotation(float in) {
		rotation=in;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		System.out.println(x+" "+id+"player x "+"seted.");
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		System.out.println(y+" "+id+"player y "+"seted.");
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		System.out.println(z+" "+id+"player z "+"seted.");
		this.z = z;
	}

	public int getBomb() {
		return bomb;
	}

	public void setBomb(int bomb) {
		if(bomb <= 10) this.bomb = bomb;
	}
	
	public void addBomb() {
		if(bomb <= 10) this.bomb++;
	}

	public int getFire() {
		return fire;
	}

	public void setFire(int fire) {
		if(fire < 10) this.fire = fire;
	}
	
	public void addFire() {
		if(fire < 10) this.fire++;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		if(speed < 10) this.speed = speed;
	}
	
	public void addSpeed() {
		if(speed < 10) this.speed++;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
