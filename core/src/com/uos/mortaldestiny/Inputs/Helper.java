package com.uos.mortaldestiny.Inputs;

public class Helper{
	
	public static float getDegree(double xpos, double ypos){
		float angle = (float) Math.toDegrees(Math.atan2(ypos, xpos));
		
	    return -angle;
	}
	
	public static float adjustMouseInput(float angleOfMouse){
		return angleOfMouse-=90;
	}

}
