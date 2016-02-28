package com.uos.mortaldestiny.Inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.CameraController;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.helper.Helper;
import com.uos.mortaldestiny.player.Player;
import com.uos.mortaldestiny.rendering.Renderer;

public class KeyboardHandler {

	public boolean[] keys = new boolean[256];
	long[] keysTime = new long[256];

	public boolean mouseLeft = false;
	public boolean mouseRight = false;

	public static String inputHandlerName;

	public KeyboardHandler(InputHandler inputHandler) {
		inputHandlerName = "Keyboard";
	}

	public void updateInputLogic() {
		updateLeftStick();
		updateABXY();
		updateMouseInputs();
	}

	public void updateMouseInputs() {
		Player p = GameClass.getInstance().playerHandler.getPlayerByInput(inputHandlerName);
		p.shoot = mouseLeft;
		p.rightClick = mouseRight;
	}

	public void updateABXY() {
		Player p = GameClass.getInstance().playerHandler.getPlayerByInput(inputHandlerName);
		p.jump = keys[Keys.SPACE];
		
		if(keys[Keys.Z]){
			for(int i=0;i<100; i++){
				GameClass.getInstance().aiHandler.createAI("AI."+i);
			}
		}
	}

	// Vector3(-1, 0, 0)); //left
	// Vector3(1, 0, 0)); //right
	// Vector3(0, 0, -1)); //up
	// Vector3(0, 0, 1)); //down

	public void updateLeftStick() {
		Vector3 dir = new Vector3(0, 0, 0);

		if (keys[Keys.A]) {
			dir.add(new Vector3(-1, 0, 0)); // left
		}
		if (keys[Keys.D]) {
			dir.add(new Vector3(1, 0, 0)); // right
		}
		if (keys[Keys.W]) {
			dir.add(new Vector3(0, 0, -1)); // up
		}
		if (keys[Keys.S]) {
			dir.add(new Vector3(0, 0, 1)); // down
		}

		dir.clamp(0, 1);
		Player p = GameClass.getInstance().playerHandler.getPlayerByInput(inputHandlerName);
		p.stickLeftDown = keys[Keys.SHIFT_LEFT];
		p.stickLeft = CameraController.relativToCamera(dir);
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
			mouseLeft = false;
		}
		if (button == Input.Buttons.RIGHT) {
			mouseRight = false;
		}
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
		return false;
	}

	public boolean keyTyped(char character) {
		if(keys[Keys.P]){
			Renderer.renderOutlines = !Renderer.renderOutlines;
		}
		if(keys[Keys.O]){
			Renderer.renderNormal = !Renderer.renderNormal;
			int width = GameClass.getInstance().getWidth();
			int height = GameClass.getInstance().getHeight();
			if(Renderer.renderNormal){
				Renderer.frameBuffer2 = new FrameBuffer(Format.RGBA8888, width, height, true);
			}
			else{
				Renderer.frameBuffer2 = new FrameBuffer(Format.RGB888, width, height, true);
			}
		}
		if(keys[Keys.I]){
			Renderer.renderHUD = !Renderer.renderHUD;
		}
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
		Player p = GameClass.getInstance().playerHandler.getPlayerByInput(inputHandlerName);

		float yaw = getYawInDegreeOfModelWithMouse(screenX, screenY, p.getObjPos());
		dir = dir.rotate(yaw, 0, 1, 0);

		p.stickRight = dir;

		return true;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// GameClass.log(getClass(), "mouse Down");
		
		if (button == Input.Buttons.LEFT) {
			mouseLeft = true;
		}
		if (button == Input.Buttons.RIGHT) {
			mouseRight = true;
		}
		
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 dir = new Vector3(1, 0, 0);
		Player p = GameClass.getInstance().playerHandler.getPlayerByInput(inputHandlerName);

		float yaw = getYawInDegreeOfModelWithMouse(screenX, screenY, p.getObjPos());
		dir = dir.rotate(yaw, 0, 1, 0);

		p.stickRight = dir;

		return false;
	}

	public boolean scrolled(int amount) {
		Player p = GameClass.getInstance().playerHandler.getPlayerByInput(inputHandlerName);
		p.cameraController.distanceAdd(amount);
		return true;
	}

}
