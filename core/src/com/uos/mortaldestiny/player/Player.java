package com.uos.mortaldestiny.player;

import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.CameraController;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.objects.PlayerObject;

public class Player {

	public String name;
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
	public boolean rightClick;
	public int health;
	
	public CameraController cameraController;

	public Player(String name) {
		this.name = name;
		obj = GameClass.getInstance().physics.spawnPlayer(this);
		resetInputVariables();
	}
	
	public void initCamera() {
		cameraController = new CameraController();
		cameraController.setTrack(obj);
	}
	
	public Vector3 getObjPos(){
		return obj.myGetTranslation();
	}

	public void resetInputVariables() {
		stickLeft = new Vector3();
		stickLeftDown = false;
		shoot = false;
		rightClick = false;

		stickRight = new Vector3();
		health = 100;
		boostValue = 0;
	}
	
	boolean done = false;
	
	public boolean isStillAlive(){
		if(health<=0){
			obj.myDelete();
			return false;
		}
		return true;
	}

	public void updateMyGameObjects() {
		Vector3 dir = stickLeft.cpy();

		if(jump){
			obj.jump();
		}
		if(shoot){
			obj.shootBall();
		}
		if(rightClick){
			obj.spawnBox();
		}
	
		dir.scl(4);
		
		if (stickLeftDown) {
			dir.scl(1.7f);
		}
		
		obj.moveHorizontal(dir);		
		obj.mySetYaw(stickRight);
	}

}
