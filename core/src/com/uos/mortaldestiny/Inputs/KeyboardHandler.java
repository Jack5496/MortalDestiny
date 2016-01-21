package com.uos.mortaldestiny.Inputs;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.entitys.Player;

public class KeyboardHandler implements InputProcessor {

	public boolean[] keys = new boolean[256];
	long[] keysTime = new long[256];
	Player p;

	private InputHandler inputHandler;

	public KeyboardHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
		this.p = GameClass.getInstance().player;
	}

	public void updateInputLogic() {
		updateWalkDir();
	}

	public void updateWalkDir() {
		Vector3 dir = new Vector3(0, 0, 0);

		if (keys[Keys.A]) {
			dir.add(new Vector3(1, 0, 0));	//links unten
			dir.add(new Vector3(0, 0, 1));	//links oben
		}
		if (keys[Keys.D]) {
			dir.add(new Vector3(-1, 0, 0));	//rechts oben
			dir.add(new Vector3(0, 0, -1));	//rechts unten
		}
		if (keys[Keys.W]) {
			dir.add(new Vector3(0, 0, 1));	//links oben
			dir.add(new Vector3(-1, 0, 0));	//rechts oben
		}
		if (keys[Keys.S]) {
			dir.add(new Vector3(0, 0, -1));	//rechts unten
			dir.add(new Vector3(1, 0, 0));	//links unten
		}
		
		

		if (dir.len() > 0) {	//Problem: if degree is 0° --> sin(0) will result a direction
			p.move(Helper.getYawInDegree(dir));
		}
		else{
			p.stop();
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	/**
	 * Updates every Key Input
	 */
	@Override
	public boolean keyDown(int keycode) {
		keys[keycode] = true;
		keysTime[keycode] = System.currentTimeMillis();
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keys[keycode] = false;

		if (keycode == Keys.SHIFT_LEFT) {
			System.out.println("Shift");
		}
		if (keycode == Keys.SPACE) {
			System.out.println("Space");
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	public float getYawInDegreeOfModelWithMouse(int screenX, int screenY, ModelInstance model) {
		Vector3 mv = Helper.getMousePointAt(screenX, screenY);
		Vector3 miv = Helper.getVectorFromModelInstance(model);
		return Helper.getYawInDegree(mv, miv);
	}

	public void rotatePlayer(int screenX, int screenY) {
		p.setRotation(getYawInDegreeOfModelWithMouse(screenX, screenY, p.getModelInstance()));
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		rotatePlayer(screenX, screenY);

		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		rotatePlayer(screenX, screenY);

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		GameClass.getInstance().cameraController.distanceAdd(amount);
		return true;
	}

}
