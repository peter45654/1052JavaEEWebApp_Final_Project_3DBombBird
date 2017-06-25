package com.project.example.model;

public class Bomb {
	private float x;
	private float y;
	private float z;
	private boolean checkp1;
	private boolean checkp2;
	private boolean checkp3;
	private boolean checkp4;
	private int fire;
	public Bomb(){
		setFire(0);
		setCheckp1(false);
		setCheckp2(false);
		setCheckp3(false);
		setCheckp4(false);
		setX(0f);
		setY(0f);
		setZ(0f);System.out.println("Bomb init in the wrong(default) way");
	}
	//for playerBomb function  to create a bomb
	public Bomb(float x ,float z,int fireIn){
		setFire( fireIn);
		setCheckp1(false);
		setCheckp2(false);
		setCheckp3(false);
		setCheckp4(false);
		setX(x);
		setY(2.5f);
		setZ(z);
	}
	public int getFire(){
		return fire;
	}
	public void setFire(int fireIn){
		this.fire=fireIn;
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
	public void setCheckp1(boolean n){
		checkp1=n;
	}
	public void setCheckp2(boolean n){
		checkp2=n;
	}
	public void setCheckp3(boolean n){
		checkp3=n;
	}
	public void setCheckp4(boolean n){
		checkp4=n;
	}
	public boolean getCheckp1(){
		return checkp1;
	}
	public boolean getCheckp2(){
		return checkp2;
	}
	public boolean getCheckp3(){
		return checkp3;
	}
	public boolean getCheckp4(){
		return checkp4;
	}
}
