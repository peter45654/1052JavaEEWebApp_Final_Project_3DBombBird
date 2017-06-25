package com.project.example.dto;

public class MapObjectDTO {

	private String id;
	private float x;
	private float z;
	private boolean exist;
	private int item;
	
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
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public boolean isExist() {
		return exist;
	}
	public void setExist(boolean exist) {
		this.exist = exist;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	
	@Override
	public String toString() {
		return "MapObjectDTO [id=" + id + ", x=" + x + ", z=" + z + ", exist=" + exist + ", item=" + item + "]";
	}
	
	
}
