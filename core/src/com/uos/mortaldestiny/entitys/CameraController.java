package com.uos.mortaldestiny.entitys;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.Inputs.Helper;
import com.uos.mortaldestiny.Inputs.InputHandler;

public class CameraController {

	private PerspectiveCamera cam;

	private Vector3 distanceVector;

	private float yaw;

	private float yawHeight;

	private float distance;
	private float stepDistance = 5;
	private float minDistance = 10;
	private float maxDistance = 400;

	private Vector3 lookAt;
	private ModelInstance track;
	// public OrthographicCamera cam;

	public CameraController() {

		// cam = new OrthographicCamera(10, 10 * (Gdx.graphics.getHeight() /
		// (float)Gdx.graphics.getWidth()));
		// cam.position.set(10, 15, 10);
		// cam.direction.set(-1, -1, -1);
		// cam.near = 1;
		// cam.far = 100;
		// matrix.setToRotation(new Vector3(1, 0, 0), 90);

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		updateCameraAxis(45 - 90, 65, 20);
		updateDisVector();

		track = null;

		cam.near = 1f;
		cam.far = 300f;

		lookAt = new Vector3(0, 0, 0);

		update();
	}

	public void distanceIncrease() {
		distanceAdd(stepDistance);
	}

	public void distanceDecrease() {
		distanceAdd(-stepDistance);
	}

	public void distanceAdd(float distance) {
		updateCameraAxisOffset(0, 0, distance);
	}

	public void updateCameraAxisOffset(float yaw, float yawHeight, float distance) {
		this.yaw += yaw;
		this.yawHeight += yawHeight;
		this.distance += distance;
		updateCameraAxis(this.yaw, this.yawHeight, this.distance);
	}

	public void updateCameraAxis(float yaw, float yawHeight, float distance) {
		this.yaw = yaw;
		this.yawHeight = yawHeight;

		if (distance < minDistance) {
			distance = minDistance;
		}
		if (distance > maxDistance) {
			distance = maxDistance;
		}
		this.distance = distance;

		updateDisVector();
	}

	private void updateDisVector() {
		distanceVector = calcDisVector(yaw, yawHeight, distance);
	}

	private Vector3 calcDisVector(float yaw, float yawHeight, float distance) {
		Vector3 back = new Vector3(1, 0, 0);
		back.rotate(new Vector3(0, 0, 1), yawHeight);
		back.rotate(new Vector3(0, 1, 0), yaw);
		back.scl(distance);
		return back;
	}

	public void setTrack(ModelInstance track) {
		this.track = track;
	}

	public void update() {

		if (track == null) {
			Vector3 offsetPos = lookAt.cpy();
			offsetPos.add(distanceVector);

			cam.position.set(offsetPos);
			cam.lookAt(lookAt);
		}
		if (track != null) {
			track.transform.getTranslation(lookAt);

			Vector3 offsetPos = lookAt.cpy();
			offsetPos.add(distanceVector);

			cam.position.set(offsetPos);
			cam.lookAt(lookAt);
		}

		cam.update();
	}

	public Camera getCamera() {
		return cam;
	}

}
