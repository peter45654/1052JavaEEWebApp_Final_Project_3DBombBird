package com.project.example.dto;


public class PlayerDTO {

	private String id;
	private float x;
	private float y;
	private float z;
	private int bomb;
	private int fire;
	private int speed;
	private boolean alive;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public int getBomb() {
		return bomb;
	}
	public void setBomb(int bomb) {
		this.bomb = bomb;
	}
	public int getFire() {
		return fire;
	}
	public void setFire(int fire) {
		this.fire = fire;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	@Override
	public String toString() {
		return "PlayerDTO [id=" + id + ", x=" + x + ", z=" + z + ", bomb=" + bomb + ", fire=" + fire + ", speed="
				+ speed + ", alive=" + alive + "]";
	}
	
	
}
