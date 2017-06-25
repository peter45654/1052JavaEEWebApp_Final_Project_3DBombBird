package com.project.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class MapObject {

	@Id
	private String id;
	private float x;
	private float z;
	private boolean exist;
	private int item;
	
	public MapObject(String id,float x,float z,Boolean exist,int item) {
		setId(id);
		setExist(exist);
		setItem(item);
		setX(x);
		setZ(z);
	}

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

}
