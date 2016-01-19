package com.uos.mortaldestiny.Inputs;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class KeyboardHandler implements InputProcessor {

	public boolean[] keys = new boolean[256];
	long[] keysTime = new long[256];

	private InputHandler inputHandler;

	public KeyboardHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}
	
	public boolean downLeft(){
		return keys[Keys.LEFT] || keys[Keys.A];
	}
	
	public boolean downRight(){
		return keys[Keys.DOWN] || keys[Keys.S];
	}
	
	public boolean upLeft(){
		return keys[Keys.UP] || keys[Keys.W];
	}
	
	public boolean upRight(){
		return keys[Keys.RIGHT] || keys[Keys.D];
	}
	
	

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
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
			System.out.println("Reset Speed");
		}
		if (keycode == Keys.SPACE) {
			System.out.println("Jump End");
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return true;
	}

}
