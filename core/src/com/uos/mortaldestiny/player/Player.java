package com.uos.mortaldestiny.player;

import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.CameraController;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.menu.MenuHandler;
import com.uos.mortaldestiny.objects.PlayerObject;

public class Player {

	public String name;
	public PlayerObject obj;

	/**
	 * InputVariables
	 */
	public Vector3 stickLeft;
	public boolean stickLeftDown;

	public Vector3 stickRight;	
	
	public Vector3 testPos;
	
	public float boostValue;
	public boolean jump;
	public boolean shoot;
	public boolean rightClick;
	public int health;
	public int healthCap;
	public int points;

	public CameraController cameraController;
	public MenuHandler menuHandler;

	public Player(String name) {
		this.name = name;
		menuHandler = new MenuHandler(this);
		obj = GameClass.getInstance().physics.spawnPlayer(this);
		resetInputVariables();
	}

	public void initCamera() {
		cameraController = new CameraController();
		cameraController.setTrack(obj);
	}

	public void addPoint() {
		points++;
		health += 10;
		if (health > 100)
			health = 100;
	}

	public Vector3 getObjPos() {
		return testPos;
	}

	public void dispose() {
		obj.dispose();
	}

	public void resetInputVariables() {
		stickLeft = new Vector3();
		stickLeftDown = false;
		shoot = false;
		rightClick = false;

		stickRight = new Vector3();
		health = 100;
		healthCap = 100;
		boostValue = 0;
		points = 0;
		
		testPos = obj.myGetTranslation();
	}

	boolean done = false;

	public boolean isStillAlive() {
		return (health > 0);
	}

	float lowLife = 0.5f;

	public boolean isHealthLow() {
		return isHealthUnderPercent(lowLife);
	}

	public boolean isHealthUnderPercent(float percent) {
		return health < healthCap * percent;
	}

	public boolean isHealthBetweenPercent(float min, float max) {
		return !(isHealthUnderPercent(min)) && isHealthUnderPercent(max);
	}

	public void updateMyGameObjects() {
		Vector3 dir = stickLeft.cpy();

		if (jump) {
			obj.jump();
		}
		if (shoot && !stickLeftDown) {
			obj.shootBall();
		}
		if (rightClick) {
			obj.spawnBox();
		}

		dir.scl(4);

		if (stickLeftDown) {
			dir.scl(4f);
		}

		obj.moveHorizontal(dir);
		obj.mySetYaw(stickRight);
		
		testPos = obj.myGetTranslation();
	}

}
