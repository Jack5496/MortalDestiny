package com.uos.mortaldestiny.Inputs;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.uos.mortaldestiny.GameClass;

public class GestureHandler implements GestureListener {

	private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();

	public GestureHandler(InputHandler inputHandler) {
		for (int i = 0; i < 5; i++) {
			touches.put(i, new TouchInfo());
		}
	}

	public Map<Integer, TouchInfo> getTouches() {
		return touches;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		GameClass.log(getClass(), "Tap");
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		GameClass.log(getClass(), "Long Press");
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		GameClass.log(getClass(), "Fling");
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		GameClass.log(getClass(), "Pan");
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		GameClass.log(getClass(), "PanStop");
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		GameClass.log(getClass(), "Zoom");
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		GameClass.log(getClass(), "Pinch");
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		GameClass.log(getClass(), "touch");
		return false;
	}

}
