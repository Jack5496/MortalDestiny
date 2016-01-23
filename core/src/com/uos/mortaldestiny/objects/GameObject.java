package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
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