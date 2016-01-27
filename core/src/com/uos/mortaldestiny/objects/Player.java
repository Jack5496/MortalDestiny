package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.Inputs.Helper;

public class Player {

	String name;
	PlayerObject obj;

	/**
	 * InputVariables
	 */
	public Vector3 stickLeft;
	public boolean stickLeftDown;

	public Vector3 stickRight;

	public float boostValue;	
	public boolean jump;
	public boolean shoot;
	public int health;

	public Player(String name) {
		this.name = name;
		obj = GameClass.getInstance().physics.spawnPlayer(this);
		
		GameClass.getInstance().cameraController.setTrack(obj);
		resetInputVariables();
	}
	
	public Vector3 getObjPos(){
		return obj.myGetTranslation();
	}

	public void resetInputVariables() {
		stickLeft = new Vector3();
		stickLeftDown = false;
		shoot = false;

		stickRight = new Vector3();
		health = 100;
		boostValue = 0;
	}
	
	boolean done = false;

	public void updateMyGameObjects() {
		Vector3 dir = stickLeft.cpy();

		if(jump){
			obj.jump();
		}
		if(shoot){
			obj.shoot();
		}
	
		dir.scl(4);
		
		if (stickLeftDown) {
			dir.scl(1.7f);
		}
		
		obj.moveHorizontal(dir);		
		obj.mySetYaw(stickRight);
	}

}
