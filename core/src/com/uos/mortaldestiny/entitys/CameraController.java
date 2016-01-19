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

	private Vector3 distance;
	private Vector3 lookAt;
	private ModelInstance track;
	// public OrthographicCamera cam;

	public CameraController() {
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		distance = new Vector3(10f, 10f, 10f);
		track = null;

		cam.near = 1f;
		cam.far = 300f;

		lookAt = new Vector3(0, 0, 0);

		update();
	}
	
	public void setTrack(ModelInstance track){
		this.track = track;
	}

	public void update() {
		
		if(track==null){
			Vector3 offsetPos = lookAt.cpy();
			offsetPos.add(distance);
			
			cam.position.set(offsetPos);
			cam.lookAt(lookAt);
		}
		if (track != null) {
			track.transform.getTranslation(lookAt);
			
			Vector3 offsetPos = lookAt.cpy();
			offsetPos.add(distance);
			
			cam.position.set(offsetPos);
			cam.lookAt(lookAt);
		}
		

		

		cam.update();
	}

	public Camera getCamera() {
		return cam;
	}

}
