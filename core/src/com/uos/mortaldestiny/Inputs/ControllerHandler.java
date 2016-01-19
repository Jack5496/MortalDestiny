package com.uos.mortaldestiny.Inputs;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ControllerHandler implements ControllerListener {

	private InputHandler inputHandler;

	public ControllerHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
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

	private float threshold = 0.5f; // spielraum, ab 20% wird Stick erst
									// gemessen

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Auto-generated method stub
		if (Math.abs(value) > threshold) {
			System.out.println("axis: " + axisCode + " with " + value);
			if (axisCode == 0) { // Stick Left Y
				//-1 Up	-	1 Down
			}
			if (axisCode == 1) { // Stick Left X
				//-1 Left	-	1 Right
			}
			if (axisCode == 2) { // Stick Right Y
				//-1 Up	-	1 Down
			}
			if (axisCode == 3) { // Stick Right X
				//-1 Left	-	1 Right
			}

		}
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
