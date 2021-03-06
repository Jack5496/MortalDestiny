package com.uos.mortaldestiny;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class CameraController {

	public PerspectiveCamera camera;

	// Distance Vector which camera will use
	private Vector3 distanceVector;

	// Parameters for calculating distanceVector
	private float yaw;
	private float yawHeight;
	private float distance;

	// Parameters for distance changing
	private float stepDistance = 5;
	private float minDistance = 10;
	private float maxDistance = 400;

	// Variables for Camera look direction
	public Vector3 lookAt; // Vector to look at if no track ModelInstance
	private ModelInstance track = null; // track ModelInstance
//
//	public CameraInputController camController;
	
	/**
	 * CameraController which creates a Camera, with , which can be getted
	 */
	
	public static float offsetYaw = 45;
	public static float normalYaw = 90;
	
	public static Vector3 relativToCamera(Vector3 v){
		return v.rotate(new Vector3(0,1,0), normalYaw+offsetYaw+90);
	}
	
	public void updateViewPort(int width, int height){
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}
	
	public CameraController() {
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		updateCameraAxis(normalYaw+offsetYaw, 65, distance);
		updateDisVector();

		camera.near = 1f;
		camera.far = 300f;

		lookAt = new Vector3(0, 0, 0);

		update();	
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
		if (track != null) {
			track.transform.getTranslation(lookAt);
		}

		Vector3 offsetPos = lookAt.cpy();
		offsetPos.add(distanceVector);

		camera.position.set(offsetPos);
		camera.lookAt(lookAt);

		camera.update();
	}
	
	public void translateTo(Vector3 pos){
		this.lookAt = pos;
	}
	
	public void translateAdd(Vector3 offset){
		this.lookAt.add(offset);
	}

	/**
	 * get the Camera
	 * 
	 * @return Camera
	 */
	public Camera getCamera() {
		return camera;
	}

}
