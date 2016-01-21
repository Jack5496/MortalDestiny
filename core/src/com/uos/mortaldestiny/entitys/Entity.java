package com.uos.mortaldestiny.entitys;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.Inputs.Helper;

public class Entity{

	private ModelInstance model;
	private float health;
	private String name;
	private float speed;
		
	public Entity(ModelInstance model){
		this.model = model;
		moveTo(new Vector3(0, 1, 0));
		this.health = 100;
		this.name = "Entity";
		this.speed = 0.5f;
	}
	
	public ModelInstance getModelInstance(){
		return this.model;
	}

	public Vector3 getVector(){
		Vector3 vec = new Vector3();
		model.transform.getTranslation(vec);
		return vec;
	}
		
	public void setRotation(float lookDir){
		Quaternion q = new Quaternion();
		getModelInstance().transform.getRotation(q);
		int dir = (int) lookDir;
		float diff = dir-getRotation();
		q.setFromAxis(0, 1, 0, diff);
		model.transform.rotate(q);
	}
	
	/**
	 * Return the Rotation of the ModelInstance of the Entity
	 * @return Rotation [0;359]
	 */
	public float getRotation(){
		Quaternion q = new Quaternion();		
		model.transform.getRotation(q, true);
		
		float yaw = q.getYaw();
		
		if(yaw<0){
			yaw+=360;
		}
		
		int p = (int)(yaw+0.5f);
		
		return p;
	}
	
	public void moveTo(Vector3 pos){
		Vector3 scale = new Vector3();
		model.transform.getScale(scale);
		model.transform.setToTranslationAndScaling(pos, scale);
	}
	
	public void move(float dir) {
		Vector3 v = new Vector3(0,0,1);
		v = v.rotate(new Vector3(0,1,0), dir);	//not exactly calc somehow
		v.clamp(0, 1);	//normalize Direction
		v.scl(speed);	
		
		getModelInstance().transform.trn(v.x, 0, v.z);
	}
	
}
