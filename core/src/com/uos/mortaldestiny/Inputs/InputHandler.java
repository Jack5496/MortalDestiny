package com.uos.mortaldestiny.Inputs;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class InputHandler implements InputProcessor, GestureListener {

	private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();
	public boolean[] keys = new boolean[256];
	long[] keysTime = new long[256];

	public InputHandler() {		
		for (int i = 0; i < 5; i++) {
			touches.put(i, new TouchInfo());
		}
	}


//	public void updateMovePadActiv() {
//		switch (Gdx.app.getType()) {
//		case Android:
//			movePadActiv = true;
//		case Applet:
//			movePadActiv = true;
//		case Desktop:
//			movePadActiv = useTouch;
//		case WebGL:
//			movePadActiv = true;
//		case iOS:
//			movePadActiv = true;
//		}
//	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (pointer < 5) {
			touches.get(pointer).startPos.x = screenX;
			touches.get(pointer).startPos.y = screenY;
			touches.get(pointer).lastPos.x = screenX;
			touches.get(pointer).lastPos.y = screenY;
			touches.get(pointer).touched = true;
		}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touches.get(pointer).lastPos.x = screenX;
		touches.get(pointer).lastPos.y = screenY;
		touches.get(pointer).touched = false;
	
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (pointer < 5) {
			touches.get(pointer).lastPos.x = screenX;
			touches.get(pointer).lastPos.y = screenY;
			touches.get(pointer).touched = true;
		}
		return true;
	}

	/**
	 * Updates every Key Input
	 */
	@Override
	public boolean keyDown(int keycode) {
		keys[keycode] = true;
		keysTime[keycode] = System.currentTimeMillis();
		System.out.println("Key: "+keycode+" at: "+keysTime[keycode]);
		
		

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

	public Map<Integer, TouchInfo> getTouches() {
		return touches;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		System.out.println("Tap");
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		System.out.println("Long Press");
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		System.out.println("Fling");
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		System.out.println("Pan");
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		System.out.println("PanStop");
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		System.out.println("Zoom");
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		System.out.println("Pinch");
		return false;
	}


	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

}
