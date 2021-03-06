package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.helper.Helper;
import com.uos.mortaldestiny.world.MyMotionState;

public class GameObject extends ModelInstance implements Disposable {
	public btRigidBody body;
	public final MyMotionState motionState;
	public btRigidBody.btRigidBodyConstructionInfo constructionInfo;

	public GameObject(Model model, String node, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
		super(model, node);
		motionState = new MyMotionState();
		motionState.transform = transform;
		this.constructionInfo = constructionInfo;
		body = new btRigidBody(this.constructionInfo);
		body.setMotionState(motionState);
		body.userData = this;
	}
	
	public GameObject(Model model, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
		super(model);
		motionState = new MyMotionState();
		motionState.transform = transform;
		this.constructionInfo = constructionInfo;
		body = new btRigidBody(this.constructionInfo);
		body.setMotionState(motionState);
		body.userData = this;
	}

	@Override
	public void dispose() {
		GameClass.getInstance().physics.dynamicsWorld.removeCollisionObject(body);
		GameClass.instances.removeValue(this, true);
		body.dispose();
		motionState.dispose();
	}
	
	
	public void update(float delta){
		//here animations :P look in PlayerObject
	}

	public Vector3 myGetTranslation(){
		Matrix4 tr = body.getCenterOfMassTransform();	//Get Transform (Rotation and Translation)
		Vector3 trans = new Vector3();	//Create Vector to save Translation
		tr.getTranslation(trans);	//Get Translation
		return trans;
	}
	
	public void mySetTranslation(Vector3 pos){
		Matrix4 tr = body.getCenterOfMassTransform();
		tr.setTranslation(pos);	//Set Translation back from helper Vector
		body.setCenterOfMassTransform(tr);	//Apply Transform
	}
	
	/**
	 * Set the Yaw Pitch and Roll for a GameObject, without overriting its Translation
	 * @param yaw Yaw in Degree
	 * @param pitch Pitch in Degree
	 * @param roll Roll in Degree
	 */
	public void mySetYawPitchRoll(float yaw, float pitch, float roll){
		Matrix4 tr = body.getCenterOfMassTransform();	//Get Transform (Rotation and Translation)
		Vector3 trans = new Vector3();	//Create Vector to save Translation
		tr.getTranslation(trans);	//Get Translation
		tr.setFromEulerAngles(yaw, pitch, roll);	//Override Angle (with Translation as sideeffect)
		tr.setTranslation(trans);	//Set Translation back from helper Vector
		body.setCenterOfMassTransform(tr);	//Apply Transform
	}
	
	/**
	 * Set the Yaw for a GameObject, without overriting its Translation
	 * @param yaw Yaw in Degree
	 */
	public void mySetYaw(float yaw){
		mySetYawPitchRoll(yaw, myGetPitch(), myGetRoll());	//set Yaw and Save other Axes
	}
	
	public void mySetYaw(Vector3 lookDir){
		int yaw = (int) (Helper.getYawInDegree(lookDir)+.5f);
		mySetYaw(yaw);
	}
	
	/**
	 * Set the Pitch for a GameObject, without overriting its Translation
	 * @param pitch Pitch in Degree
	 */
	public void mySetPitch(float pitch){
		mySetYawPitchRoll(myGetYaw(), pitch, myGetRoll());	//set Pitch and Save other Axes
	}
	
	/**
	 * Set the Roll for a GameObject, without overriting its Translation
	 * @param roll Roll in Degree
	 */
	public void mySetRoll(float roll){
		mySetYawPitchRoll(myGetYaw(), myGetPitch(), roll);	//set Roll and Save other Axes
	}
	
	/**
	 * Get the Quaternion of a GameObject
	 * @return
	 */
	public Quaternion myGetQuaternion(){
		Quaternion q = new Quaternion();	//Create Quaternion
		body.getCenterOfMassTransform().getRotation(q, true); 	//Set Rotation by normalized Vectors 
		return q;
	}
	
	/**
	 * Get the Yaw of a GameObject by its Quaternion
	 * @return Yaw as float in Degree
	 */
	public float myGetYaw(){
		return myGetQuaternion().getYaw(); //Get Yaw from Quaternion
	}
	
	public Vector3 myGetYawVector(){
		Vector3 dir = new Vector3(1,0,0);
		dir.rotate(myGetYaw(), 0, 1, 0);
		return dir;
	}
	
	/**
	 * Get the Pitch of a GameObject by its Quaternion
	 * @return Pitch as float in Degree
	 */
	public float myGetPitch(){
		return myGetQuaternion().getPitch(); //Get Pitch from Quaternion
	}
	
	/**
	 * Get the Roll of a GameObject by its Quaternion
	 * @return Roll as float in Degree
	 */
	public float myGetRoll(){
		return myGetQuaternion().getRoll(); //Get Roll from Quaternion
	}
	
	/**
	 * Set the Scale of the GameObjct
	 * CollisionBody is scaled too
	 * @param scale
	 */
	public void mySetScale(float scale){
		for (Node n : nodes) {
			n.scale.set(scale,scale,scale);
		}
		
		//Update 27.01.16	Scaling the Collision shape now too
		btCollisionShape shape = this.body.getCollisionShape();
		shape.setLocalScaling(new Vector3(scale,scale,scale));
		this.body.setCollisionShape(shape);
		//End Update 27.01.16
		
		calculateTransforms();
	}
	
	public static class Constructor implements Disposable {
		public final Model model;
		public final String node;
		public final btCollisionShape shape;
		public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
		private static Vector3 localInertia = new Vector3();

		public Constructor(Model model, String node, btCollisionShape shape, float mass) {
			this.model = model;
			this.node = node;
			this.shape = shape;
			if (mass > 0f)
				shape.calculateLocalInertia(mass, localInertia);
			else
				localInertia.set(0, 0, 0);
			this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
		}
		
		public Constructor(Model model, btCollisionShape shape, float mass) {
			this.model = model;
			this.node = null;
			this.shape = shape;
			if (mass > 0f)
				shape.calculateLocalInertia(mass, localInertia);
			else
				localInertia.set(0, 0, 0);
			this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
		}

		public GameObject construct() {			
			if(node==null) return new GameObject(model,constructionInfo);
			return new GameObject(model, node, constructionInfo);
		}

		@Override
		public void dispose() {
			shape.dispose();
			constructionInfo.dispose();
		}
	}
}