package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class Entity extends GameObject{

	/*
	 * Testing variables
	 */
	boolean calcExactly = false;

	/*
	 * Attributes
	 */
	private float health;
	private String name;
	
	/**
	 * Create a new Entity
	 * 
	 * @param model
	 *            ModelInstance for the Entiry
	 */
	public Entity() {
		
	}
	

	
	
	/**
	 * Set Rotation of ModelInstance
	 * 
	 * @param degree
	 *            Degree of Rotation [0;359]
	 */
	public void setRotation(float degree) {
		Quaternion q = new Quaternion();
		getModelInstance().transform.getRotation(q);

		float diff;

		// May brings later better FPS if !calcExactly
		if (!calcExactly) {
			diff = degree-getRotation();
		}
		else{
			int dir = (int) degree;
			diff = dir - getRotation();			
		}
		
		q.setFromAxis(0, 1, 0, diff);
		getModelInstance().transform.rotate(q);
	}

	/**
	 * Add Rotation to ModelInstance
	 * 
	 * @param degree
	 *            Degree of Rotation difference [0;359]
	 */
	public void addRotation(float degree) {
		float total = getRotation() + degree;
		setRotation(total);
	}

	/**
	 * Return the Rotation of the ModelInstance of the Entity
	 * @return Rotation [0;359]
	 */
	public float getRotation() {
		Quaternion q = new Quaternion();
		getModelInstance().transform.getRotation(q, true); // get Rotation with
															// normalized Axis

		float yaw = q.getYaw();

		if (yaw < 0) { // add offset if needed so Output is safe
			yaw += 360;
		}

		// May brings later better FPS if !calcExactly
		if (calcExactly) {
			return yaw;
		} else {
			int p = (int) (yaw + 0.5f);
			return p;
		}
	}

	/**
	 * Set ModelInstance Position to 
	 * @param pos Vector3 Position be set
	 */
	public void translateTo(Vector3 pos) {
		Vector3 scale = new Vector3();
		model.transform.getScale(scale);
		model.transform.setToTranslationAndScaling(pos, scale);
	}

	/**
	 * Move ModelInstance in degree direction with Entity speed
	 * @param degree Degree of direction [0;359]
	 */
	public void move(float degree) {
		Vector3 v = new Vector3(0, 0, 1);
		v = v.rotate(new Vector3(0, 1, 0), degree); // not exactly calc somehow
		v.clamp(0, 1); // normalize Direction
		v.scl(getSpeed());

		getModelInstance().transform.trn(v.x, 0, v.z);
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void setResetSpeed(float speed){
		this.speedReset = speed;
	}
	
	public float getResetSpeed(){
		return this.speedReset;
	}

}
