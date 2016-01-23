package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;

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

	public AnimationController controller;

	public Player(String name) {
		this.name = name;
		obj = GameClass.getInstance().physics.spawnPlayer();
		controller = new AnimationController(obj);

		GameClass.getInstance().cameraController.setTrack(obj);
		resetInputVariables();
	}

	boolean ended = false;
	boolean running = false;

	public static final String WALK = "Armature|walk";

	public void walk() {
		if (ended) {
			controller.setAnimation(null);
			ended = false;
		} else {
			if (!running) {
				running = true;
				String ani = obj.animations.get(0).id;
				System.out.println("Ok lets start: " + ani);
				controller.setAnimation(ani, 0, 3.25f, 1, 10 * 0.5f, new AnimationListener() {

					@Override
					public void onEnd(AnimationDesc animation) {
						// this will be called when the current animation is
						// done.
						// queue up another animation called "balloon".
						// Passing a negative to loop count loops forever. 1f
						// for
						// speed is normal speed.
						// controller.setAnimation(null);
						System.out.println("Finished");
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
		controller.setAnimation(null);
		ended = false;
	}

	public void resetInputVariables() {
		stickLeft = new Vector3();
		stickLeftDown = false;

		stickRight = new Vector3();

		boostValue = 0;
	}

	public void updateMyGameObjects() {

		Vector3 dir = stickLeft.cpy();
		if (stickLeftDown) {
			dir.scl(1.7f);
		}

		// obj.body.translate(dir.scl(0.1f));
		if (dir.len() > 0) {
			walk();
		}
		obj.body.applyCentralImpulse(dir.scl(0.2f));
	}
	
	public void updateAnimation(float delta){
		controller.update(delta);
	}

}
