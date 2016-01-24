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
import com.uos.mortaldestiny.MyMotionState;

public class GameObject extends ModelInstance implements Disposable {
	public final btRigidBody body;
	public final MyMotionState motionState;
	public btRigidBody.btRigidBodyConstructionInfo constructionInfo;
	


	public GameObject(Model model, String node, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
		super(model, node);
		motionState = new MyMotionState();
		motionState.transform = transform;
		this.constructionInfo = constructionInfo;
		body = new btRigidBody(constructionInfo);
		body.setMotionState(motionState);
	}
	
	public GameObject(Model model, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
		super(model);
		motionState = new MyMotionState();
		motionState.transform = transform;
		this.constructionInfo = constructionInfo;
		body = new btRigidBody(constructionInfo);
		body.setMotionState(motionState);
	}

	@Override
	public void dispose() {
		body.dispose();
		motionState.dispose();
	}
	
	public void mySetYawPitchRoll(float yaw, float pitch, float roll){
		Matrix4 tr = body.getCenterOfMassTransform();	//Get Transform (Rotation and Translation)
		Vector3 trans = new Vector3();	//Create Vector to save Translation
		tr.getTranslation(trans);	//Get Translation
		tr.setFromEulerAngles(yaw, pitch, roll);	//Override Angle (with Translation as sideeffect)
		tr.setTranslation(trans);	//Set Translation back from helper Vector
		body.setCenterOfMassTransform(tr);	//Apply Transform
	}
	
	public void mySetYaw(float yaw){
		mySetYawPitchRoll(yaw, myGetPitch(), myGetRoll());	//set Yaw and Save other Axes
	}
	
	public void mySetPitch(float pitch){
		mySetYawPitchRoll(myGetYaw(), pitch, myGetRoll());	//set Pitch and Save other Axes
	}
	
	public void mySetRoll(float roll){
		mySetYawPitchRoll(myGetYaw(), myGetPitch(), roll);	//set Roll and Save other Axes
	}
	
	private Quaternion myGetQuaternion(){
		Quaternion q = new Quaternion();	//Create Quaternion
		body.getCenterOfMassTransform().getRotation(q, true); 	//Set Rotation by normalized Vectors 
		return q;
	}
	
	public float myGetYaw(){
		return myGetQuaternion().getYaw(); //Get Yaw from Quaternion
	}
	
	public float myGetPitch(){
		return myGetQuaternion().getPitch(); //Get Pitch from Quaternion
	}
	
	public float myGetRoll(){
		return myGetQuaternion().getRoll(); //Get Roll from Quaternion
	}
	
	public void mySetScale(float scale){
		for (Node n : nodes) {
			n.scale.set(scale,scale,scale);
		}
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