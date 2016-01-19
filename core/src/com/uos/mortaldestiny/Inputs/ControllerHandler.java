package com.uos.mortaldestiny.Inputs;

import java.util.HashMap;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;

public class ControllerHandler implements ControllerListener {

	private InputHandler inputHandler;

	public ControllerHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

	public void updateInputLogic() {
		updateWalkDir();
		updateLookDir();
	}

	private float threshold = 0.5f; // spielraum, ab 20% wird Stick erst
	// gemessen

	public void updateWalkDir(){
		for(Controller controller : Controllers.getControllers()){
			float ldy = controller.getAxis(XBox360Pad.AXIS_LEFT_Y);
			float ldx = controller.getAxis(XBox360Pad.AXIS_LEFT_X);
			Vector3 vec = new Vector3(-ldx,0,-ldy);
			vec.rotate(new Vector3(0,1,0), -45);
			
			if (Math.abs(vec.len()) > threshold) {
				GameClass.getInstance().player.move(Helper.getYawInDegree(vec));
			}
		}
	}
	
	public void updateLookDir(){
		for(Controller controller : Controllers.getControllers()){
			float rdy = controller.getAxis(XBox360Pad.AXIS_RIGHT_Y);
			float rdx = controller.getAxis(XBox360Pad.AXIS_RIGHT_X);
			Vector3 vec = new Vector3(rdx,0,rdy);
			vec.rotate(new Vector3(0,1,0), 45);
			
			if (Math.abs(vec.len()) > threshold) {
				GameClass.getInstance().player.setDirection(Helper.getYawInDegree(vec));
			}
		}
	}

	@Override
	public void connected(Controller controller) {
		// TODO Auto-generated method stub
		System.out.println("Controller connected");
	}

	@Override
	public void disconnected(Controller controller) {
		// TODO Auto-generated method stub
		System.out.println("Controller disconnected");
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		// TODO Auto-generated method stub
		System.out.println("ButtonDown: " + buttonCode);
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		// TODO Auto-generated method stub
		System.out.println("ButtonUp: " + buttonCode);
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		// TODO Auto-generated method stub
		System.out.println("pov: " + povCode + " with " + value);
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		// TODO Auto-generated method stub
		System.out.println("xSlider: " + sliderCode + " with " + value);
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		// TODO Auto-generated method stub
		System.out.println("ySlider: " + sliderCode + " with " + value);
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		// TODO Auto-generated method stub
		System.out.println("accelerometer: " + accelerometerCode + " with " + value);
		return false;
	}

}
