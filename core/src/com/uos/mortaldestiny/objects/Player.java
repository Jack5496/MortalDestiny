package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.Inputs.Helper;

public class Player {

	String name;
	GameObject obj;

	/**
	 * InputVariables
	 */
	public Vector3 stickLeft;
	public boolean stickLeftDown;

	public Vector3 stickRight;

	public float boostValue;
	
	public boolean jump;

	public AnimationController animationController;

	public Player(String name) {
		this.name = name;
		obj = GameClass.getInstance().physics.spawnPlayer();
		animationController = new AnimationController(obj);

		GameClass.getInstance().cameraController.setTrack(obj);
		resetInputVariables();
	}

	boolean ended = false;
	boolean running = false;

	public static final String WALK = "Armature|walk";

	public void walk() {
		if (ended) {
			animationController.setAnimation(null);
			ended = false;
		} else {
			if (!running) {
				running = true;
				String ani = obj.animations.get(0).id;
				animationController.setAnimation(ani, 0, 3.25f, 1, 10 * 0.5f, new AnimationListener() {

					@Override
					public void onEnd(AnimationDesc animation) {
						// this will be called when the current animation is
						// done.
						// queue up another animation called "balloon".
						// Passing a negative to loop count loops forever. 1f
						// for
						// speed is normal speed.
						// controller.setAnimation(null);
						running = false;
						ended = true;
					}

					@Override
					public void onLoop(AnimationDesc animation) {
						// TODO Auto-generated method stub

					}

				});
			}
		}

	}

	public void stop() {
		animationController.setAnimation(null);
		ended = false;
	}

	public void resetInputVariables() {
		stickLeft = new Vector3();
		stickLeftDown = false;

		stickRight = new Vector3();

		boostValue = 0;
	}
	
	boolean done = false;

	public void updateMyGameObjects() {

		Vector3 dir = stickLeft.cpy();
		

		// obj.body.translate(dir.scl(0.1f));
//		if (dir.len() > 0) {
//			walk();
//			obj.body.applyCentralImpulse(dir.scl(0.2f));
//			obj.body.applyCentralForce(dir);
//		}
//		else{
//			obj.body.applyCentralImpulse(new Vector3());
//			obj.body.applyCentralForce(dir);
//		}
		dir.scl(4);
		
		if (stickLeftDown) {
			dir.scl(1.7f);
		}
		
		Vector3 linV = obj.body.getLinearVelocity();
		dir.y = linV.y;
		
		obj.body.setLinearVelocity(dir);
		
		
		Vector3 lookDir = stickRight.cpy();
		int yaw = (int) (Helper.getYawInDegree(lookDir)+.5f);
		obj.mySetYaw(yaw);
		
//		obj.body.setLinearFactor(new Vector3(1,0,1));	//hold object in XZ-Layer
		obj.body.setAngularFactor(new Vector3(0,1,0));	//wont let object rotate around given axe
	}
	
	
	
	public void updateAnimation(float delta){
		animationController.update(delta);
	}

}
