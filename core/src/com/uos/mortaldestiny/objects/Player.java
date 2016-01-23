package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;

public class Player{
	
	String name;
	GameObject obj;
	
	/**
	 * InputVariables
	 */
	public Vector3 stickLeft;
	public boolean stickLeftDown;

	public Vector3 stickRight;

	public float boostValue;
	
	public Player(String name){
		this.name = name;
		obj = GameClass.getInstance().physics.spawnPlayer();
		GameClass.getInstance().cameraController.setTrack(obj);
		resetInputVariables();
	}	
	
	public void resetInputVariables() {
		stickLeft = new Vector3();
		stickLeftDown = false;
		
		stickRight = new Vector3();
		
		boostValue = 0;
	}
	
	public void updateMyGameObjects(){
		
		Vector3 dir = stickLeft.cpy();
		if(stickLeftDown){
			dir.scl(1.7f);
		}
		
//		obj.body.translate(dir.scl(0.1f));
		obj.body.applyCentralImpulse(dir.scl(0.2f));
	}

}
