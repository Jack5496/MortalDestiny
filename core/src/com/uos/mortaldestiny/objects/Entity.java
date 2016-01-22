package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class Entity {

	/*
	 * Testing variables
	 */
	boolean calcExactly = false;

	/*
	 * Attributes
	 */
	private ModelInstance model;
	private float health;
	private String name;
	private float speedReset;
	private float speed;
	
	/*
	 * Variables
	 */
	btRigidBody body;
	MyMotionState motionState;
	
	/**
	 * Create a new Entity
	 * 
	 * @param model
	 *            ModelInstance for the Entiry
	 */
	public Entity(ModelInstance model) {
		this.model = model;
		
		translateTo(new Vector3(0, 1, 0));
		this.health = 100;
		this.name = "Entity";
		this.setResetSpeed(0.5f);
		this.setSpeed(getResetSpeed());
		
		motionState = new MyMotionState();
		motionState.transform = this.model.transform;
//		body = new btRigidBody();
//		body.setMotionState(motionState);
//		body.setCollisionFlags(MyContactListener.PLAYER_FLAG);
		
		btCollisionShape shape = Bullet.obtainStaticNodeShape(model.nodes);
	}
	
	static class MyMotionState extends btMotionState {
	    Matrix4 transform;
	    @Override
	    public void getWorldTransform (Matrix4 worldTrans) {
	        worldTrans.set(transform);
	    }
	    @Override
	    public void setWorldTransform (Matrix4 worldTrans) {
	        transform.set(worldTrans);
	    }
	}
	
	/**
	 * @return the ModelInstance of the Entity
	 */
	public ModelInstance getModelInstance() {
		return this.model;
	}

	/**
	 * @return the Position of the ModelInstance as Vector3
	 */
	public Vector3 getVector() {
		Vector3 vec = new Vector3();
		getModelInstance().transform.getTranslation(vec);
		return vec;
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
