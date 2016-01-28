package com.uos.mortaldestiny.Inputs;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.CameraController;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.player.Player;

public class ControllerHandler implements ControllerListener {

	public ControllerHandler(InputHandler inputHandler) {

	}

	public void updateInputLogic() {
		
		for (Controller controller : Controllers.getControllers()) {
			Player p = GameClass.getInstance().playerHandler.getPlayerByInput("controller:" + controller.hashCode());
			updateWalkDir(p,controller);
			updateLookDir(p,controller);
			updateABXY(p,controller);
			updateTrigger(p,controller);
		}
	}
	
	public void updateTrigger(Player p, Controller controller){
		float leftTrigger = controller.getAxis(XBox360Pad.AXIS_LEFT_TRIGGER);
		if(leftTrigger<2E-5){
			leftTrigger=0;
		}
		
		float rightTrigger = -controller.getAxis(XBox360Pad.AXIS_RIGHT_TRIGGER);
		if(rightTrigger<2E-5){
			rightTrigger=0;
		}
		
		float thresholdRightTrigger = 0.7f;
		p.shoot = (rightTrigger>thresholdRightTrigger);
		
//		System.out.println("Left: "+leftTrigger+" | Right: "+rightTrigger);
	}

	public void updateABXY(Player p, Controller controller) {
			p.jump = controller.getButton(XBox360Pad.BUTTON_A);
	}

	private float threshold = 0.4f; // spielraum, ab 20% wird Stick erst
	// gemessen

	public void updateWalkDir(Player p, Controller controller) {
			float ldy = controller.getAxis(XBox360Pad.AXIS_LEFT_Y);
			float ldx = controller.getAxis(XBox360Pad.AXIS_LEFT_X);

			Vector3 vec = new Vector3(ldx, 0, ldy);
			vec = CameraController.relativToCamera(vec);
			vec.clamp(0, 1);

			if (Math.abs(vec.len()) > threshold) {
				// System.out.println("ID: "+controller.hashCode()+"L:
				// "+Math.abs(vec.len()));
				
				p.stickLeft = vec.cpy();
			} else {

				p.stickLeft = new Vector3();
			}
			p.stickLeftDown = controller.getButton(XBox360Pad.BUTTON_L3);
	}

	public void updateLookDir(Player p, Controller controller) {

			float rdy = controller.getAxis(XBox360Pad.AXIS_RIGHT_Y);
			float rdx = controller.getAxis(XBox360Pad.AXIS_RIGHT_X);

			Vector3 vec = new Vector3(rdx, 0, rdy);
			vec = CameraController.relativToCamera(vec);
			vec.clamp(0, 1);
			
			if (Math.abs(vec.len()) > threshold) {
				// System.out.println("ID: "+controller.hashCode()+"L:
				// "+Math.abs(vec.len()));

				p.stickRight = vec.cpy();
			} else {
				// p.stickRight = new Vector3();
			}
	}

	@Override
	public void connected(Controller controller) {
		// TODO Auto-generated method stub
		GameClass.log(getClass(), "Controller connected");
	}

	@Override
	public void disconnected(Controller controller) {
		// TODO Auto-generated method stub
		GameClass.log(getClass(), "Controller disconnected");
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		// TODO Auto-generated method stub
		GameClass.log(getClass(), "ButtonDown: " + buttonCode);
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		// TODO Auto-generated method stub
		GameClass.log(getClass(), "ButtonUp: " + buttonCode);
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Auto-generated method stub
//		System.out.println("AxisMoved: "+axisCode+" value: "+value);
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		// TODO Auto-generated method stub
		GameClass.log(getClass(), "pov: " + povCode + " with " + value);
		
		Player p = GameClass.getInstance().playerHandler.getPlayerByInput("controller:" + controller.hashCode());

		if (value == XBox360Pad.BUTTON_DPAD_DOWN) {
			p.cameraController.distanceIncrease();
		}
		if (value == XBox360Pad.BUTTON_DPAD_UP) {
			p.cameraController.distanceDecrease();
		}

		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		// TODO Auto-generated method stub
		GameClass.log(getClass(), "xSlider: " + sliderCode + " with " + value);
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		// TODO Auto-generated method stub
		GameClass.log(getClass(), "ySlider: " + sliderCode + " with " + value);
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		// TODO Auto-generated method stub
		GameClass.log(getClass(), "accelerometer: " + accelerometerCode + " with " + value);
		return false;
	}

}
