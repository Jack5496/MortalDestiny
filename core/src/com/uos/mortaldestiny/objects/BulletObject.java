package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.MyMotionState;

public class BulletObject extends GameObject implements Disposable {
	
	int damage;

	public BulletObject(GameObject obj, int damage) {
		super(obj.model, obj.constructionInfo);
		init(damage);
	}
	
	private void init(int damage){
		this.damage = damage;
		
		body.setAngularFactor(new Vector3(0,1,0));	//wont let object rotate around given axe
	}

	public static boolean isBulletObject(GameObject obj) {
		return (obj instanceof BulletObject);
	}
}