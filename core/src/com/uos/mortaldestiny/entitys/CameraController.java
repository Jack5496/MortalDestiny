package com.uos.mortaldestiny.entitys;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.Inputs.Helper;
import com.uos.mortaldestiny.Inputs.InputHandler;

public class CameraController {

	private PerspectiveCamera camPers;
	private OrthographicCamera camOrtho;

	private Vector3 distanceVector;

	private float yaw;

	private float yawHeight;

	private float distance;
	private float stepDistance = 5;
	private float minDistance = 10;
	private float maxDistance = 400;

	private Vector3 lookAt;
	private ModelInstance track = null;

	public boolean ortho = false;

	/**
	 * CameraController which creates a Camera, with , which can be getted
	 */
	public CameraController() {

		// float pers = 10;
		// float pers = 23;
		// float pers = 60;
//		float pers = 20;

		// float far = 13;
		// float far = 70;
		// float far = 183;
//		float far = getOrthoDis(pers);

		initCamera();
	}
	
	public void switchPerspective(){
		initCamera();
	}
	
	public void initCamera(){
		// cam.position.set(10, 15, 10);
				// cam.direction.set(-1, -1, -1);
				// cam.near = 1;
				// cam.far = 100;
				// matrix.setToRotation(new Vector3(1, 0, 0), 90);

				float far = getOrthoDis(distance);
				camOrtho = new OrthographicCamera(far, far * Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
				camPers = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

				// cam = new OrthographicCamera(10, 10 * (Gdx.graphics.getHeight() /
				// (float) Gdx.graphics.getWidth()));

				updateCameraAxis(45 - 90, 65, distance);
				updateDisVector();

				camOrtho.near = 1f;
				camOrtho.far = 300f;

				camPers.near = 1f;
				camPers.far = 300f;

				lookAt = new Vector3(0, 0, 0);

				update();
	}

	// public float getOrthoDist(float dis){
	// return (float) (7.47329f*Math.pow(Math.E, 1.07332f*dis));
	// }
	//
	// public float getOrthoDistFloat(float dis){
	// return (float) ((Math.log(dis/3.65078f))/0.932511f);
	// }

	public float getOrthoDis(float persDis) {
		return (float) (1.68347f * Math.pow(persDis, 1.151f));
	}

	/**
	 * Increases the distance by defined offset
	 */
	public void distanceIncrease() {
		distanceAdd(stepDistance);
	}

	/**
	 * Decreases the distance by defined offset
	 */
	public void distanceDecrease() {
		distanceAdd(-stepDistance);
	}

	/**
	 * Set Distance to given Parameter offset
	 * 
	 * @param distance
	 *            Distance offset to add
	 */
	public void distanceAdd(float distance) {
		updateCameraAxisOffset(0, 0, distance);
	}

	/**
	 * Set CameraAxis to given Parameter as Offset
	 * 
	 * @param yaw
	 *            Yaw Offset in Degree [0;359]
	 * @param yawHeight
	 *            YawHeight Offset in Degree [0;359]
	 * @param distance
	 *            Distance Offset to lookAt point
	 */
	public void updateCameraAxisOffset(float yaw, float yawHeight, float distance) {
		this.yaw += yaw;
		this.yawHeight += yawHeight;
		this.distance += distance;
		updateCameraAxis(this.yaw, this.yawHeight, this.distance);
	}

	/**
	 * Set CameraAxis to given Parameter
	 * 
	 * @param yaw
	 *            Yaw in Degree [0;359]
	 * @param yawHeight
	 *            YawHeight in Degree [0;359]
	 * @param distance
	 *            Distance to lookAt point
	 */
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

	/**
	 * Updated the Distance Vector with the Class Variable (yaw, yawHeight,
	 * distance)
	 */
	private void updateDisVector() {
		distanceVector = calcDisVector(yaw, yawHeight, distance);
	}

	/**
	 * Get a DistanceVector from given Parameter
	 * 
	 * @param yaw
	 *            Yaw in Degree [0;359]
	 * @param yawHeight
	 *            YawHeight in Degree [0;359]
	 * @param distance
	 *            Distance as length
	 * @return Vector3 Distance Vector
	 */
	private Vector3 calcDisVector(float yaw, float yawHeight, float distance) {
		Vector3 back = new Vector3(1, 0, 0);
		back.rotate(new Vector3(0, 0, 1), yawHeight);
		back.rotate(new Vector3(0, 1, 0), yaw);
		back.scl(distance);
		return back;
	}

	/**
	 * Set a ModelInstance to be tracked, Set null if untrack
	 * 
	 * @param track
	 */
	public void setTrack(ModelInstance track) {
		this.track = track;
	}

	/**
	 * Updates the Position of the Camera to the Class Variables
	 */
	public void update() {

		if (track == null) {
			Vector3 offsetPos = lookAt.cpy();
			offsetPos.add(distanceVector);

			camOrtho.position.set(offsetPos);
			camOrtho.lookAt(lookAt);

			camPers.position.set(offsetPos);
			camPers.lookAt(lookAt);
		}
		if (track != null) {
			track.transform.getTranslation(lookAt);

			Vector3 offsetPos = lookAt.cpy();
			offsetPos.add(distanceVector);

			camOrtho.position.set(offsetPos);
			camOrtho.lookAt(lookAt);

			camPers.position.set(offsetPos);
			camPers.lookAt(lookAt);
		}

		camOrtho.update();
		camPers.update();
	}

	/**
	 * get the Camera
	 * 
	 * @return Camera
	 */
	public Camera getCamera() {
		if (ortho) {
			return camOrtho;
		} else {
			return camPers;
		}
	}

}
