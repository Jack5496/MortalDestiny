package com.uos.mortaldestiny.entitys;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.Inputs.Helper;

public class Player extends Entity{
		
	AnimationController controller;
	
	public Player(ModelInstance model){
		super(model);
		controller = new AnimationController(model);
	    
	}
	
	public void animate(){
		controller.setAnimation("walk");
	}
	
}
