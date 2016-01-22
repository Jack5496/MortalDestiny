package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;

public class Player extends Entity {

	public AnimationController controller;
	
	public static final String WALK = "Armature|walk";

	public Player(ModelInstance model) {
		super(model);
		controller = new AnimationController(model);
	}

	public void move(float dir) {
		super.move(dir);
		walk();
	}

	public boolean ended = false;
	
	public void stop(){
		controller.setAnimation(null);
		ended = false;
	}
	
	public void walk() {
		if (ended) {
			controller.setAnimation(null);
			ended = false;
		} else {
			controller.setAnimation(WALK, 0, 3.25f, 1, 10*super.getSpeed(), new AnimationListener() {

				@Override
				public void onEnd(AnimationDesc animation) {
					// this will be called when the current animation is done.
					// queue up another animation called "balloon".
					// Passing a negative to loop count loops forever. 1f for
					// speed is normal speed.
					// controller.setAnimation(null);
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
