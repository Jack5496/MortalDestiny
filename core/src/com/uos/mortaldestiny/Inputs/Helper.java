package com.uos.mortaldestiny.Inputs;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.uos.mortaldestiny.GameClass;

public class Helper{
		
	public static float getDegree(double xpos, double ypos){
		float angle = (float) Math.toDegrees(Math.atan2(ypos, xpos));
		
	    return -angle;
	}
	
	public static float getYawInDegree(Vector3 from){
		return getDegree(from.x, from.z);
	}
	
	public static float getYawInDegree(Vector3 from, Vector3 to){
		return getDegree(from.x-to.x, from.z-to.z);
	}
	
	public static Vector3 getMousePointAt(float screenX, float screenY){
		Vector3 tmpVector = new Vector3();
		
		Ray ray = GameClass.getInstance().cameraController.camera.getPickRay(screenX, screenY);
	    final float distance = -ray.origin.y / ray.direction.y;
	    tmpVector.set(ray.direction).scl(distance).add(ray.origin);

	    return tmpVector;
	}
	
	public static Vector3 getVectorFromModelInstance(ModelInstance model){
		Vector3 v = new Vector3();
		model.transform.getTranslation(v);
		return v;
	}

}
