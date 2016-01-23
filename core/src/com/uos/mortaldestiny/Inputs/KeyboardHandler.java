package com.uos.mortaldestiny.Inputs;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.objects.Player;

public class KeyboardHandler {

	public boolean[] keys = new boolean[256];
	long[] keysTime = new long[256];

	String inputHandlerName;

	public KeyboardHandler(InputHandler inputHandler) {
		inputHandlerName = "Keyboard";
	}

	public void updateInputLogic() {
		updateLeftStick();
	}

	// Vector3(-1, 0, 0)); //left
	// Vector3(1, 0, 0)); //right
	// Vector3(0, 0, -1)); //up
	// Vector3(0, 0, 1)); //down

	public void updateLeftStick() {
		Vector3 dir = new Vector3(0, 0, 0);
		boolean pushed = false;

		if (keys[Keys.A]) {
			dir.add(new Vector3(-1, 0, 0)); // left
			dir.add(new Vector3(0, 0, 1)); // down
			pushed = true;
		}
		if (keys[Keys.D]) {
			dir.add(new Vector3(1, 0, 0)); // right
			dir.add(new Vector3(0, 0, -1)); // up
			pushed = true;
		}
		if (keys[Keys.W]) {
			dir.add(new Vector3(0, 0, -1)); // up
			dir.add(new Vector3(-1, 0, 0)); // left
			pushed = true;
		}
		if (keys[Keys.S]) {
			dir.add(new Vector3(0, 0, 1)); // down
			dir.add(new Vector3(1, 0, 0)); // right
			pushed = true;
		}
		
		if(pushed){
		Player p = GameClass.getInstance().playerHandler.getPlayerByInput(inputHandlerName);
		p.stickLeftDown = keys[Keys.SHIFT_LEFT];
		p.stickLeft = dir;
		}
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	/**
	 * Updates every Key Input
	 */
	public boolean keyDown(int keycode) {
		keys[keycode] = true;
		keysTime[keycode] = System.currentTimeMillis();
		return true;
	}

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

	public boolean keyTyped(char character) {
		return false;
	}

	public float getYawInDegreeOfModelWithMouse(int screenX, int screenY, Vector3 track) {
		Vector3 mv = Helper.getMousePointAt(screenX, screenY);
		return Helper.getYawInDegree(mv, track);
	}

	// public void updateRightStick(int screenX, int screenY) {
	// p.setRotation(getYawInDegreeOfModelWithMouse(screenX, screenY,
	// p.getModelInstance()));
	// }

	public boolean mouseMoved(int screenX, int screenY) {
		Vector3 dir = new Vector3(1, 0, 0);
		float yaw = getYawInDegreeOfModelWithMouse(screenX, screenY, GameClass.getInstance().cameraController.lookAt);
		dir = dir.rotate(yaw, 0, 1, 0);
		
		Player p = GameClass.getInstance().playerHandler.getPlayerByInput(inputHandlerName);
		p.stickRight = dir;

		return true;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 dir = new Vector3(1, 0, 0);
		float yaw = getYawInDegreeOfModelWithMouse(screenX, screenY, GameClass.getInstance().cameraController.lookAt);
		dir = dir.rotate(yaw, 0, 1, 0);
		
		Player p = GameClass.getInstance().playerHandler.getPlayerByInput(inputHandlerName);
		p.stickRight = dir;

		return false;
	}

	public boolean scrolled(int amount) {
		GameClass.getInstance().cameraController.distanceAdd(amount);
		return true;
	}

}
