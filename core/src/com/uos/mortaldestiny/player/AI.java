package com.uos.mortaldestiny.player;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.helper.Helper;
import com.uos.mortaldestiny.objects.GameObject;

public class AI extends Player{

	public Array<Vector3> walkPoints;
	public Array<Player> targets;
	
	public AI(String name) {
		super(name);
	}
	
	public void initVariables(){
		walkPoints = new Array<Vector3>();
		targets = new Array<Player>();
	}
	
	public void setAllPlayerAsTargets(){
		targets = new Array<Player>(GameClass.getInstance().playerHandler.getPlayers());
	}
	
	public void updateInputVariables(){
		Vector3 nearest = findNearestPoint(getTranslationsOfTargets());
		if(nearest==null){
			stickLeft = new Vector3();
			stickRight = new Vector3();
		}
		else{
			Vector3 dir = Helper.getYawAsVector3(obj.myGetTranslation(), nearest);
			stickLeft = dir;
			stickRight = dir;
		}
	}
	
	private Array<Vector3> getTranslationsOfTargets(){
		Array<Vector3> trans = new Array<Vector3>();
		for(Player p : targets){
			trans.add(p.obj.myGetTranslation());
		}
		return trans;
	}
	
	private Vector3 findNearestPoint(Array<Vector3> points){
		Vector3 nearest = null;
		float distance = Float.POSITIVE_INFINITY;
		
		for(Vector3 toTest : points){
			float toTestDis = obj.myGetTranslation().dst(toTest);
			if(toTestDis < distance){
				distance = toTestDis;
				nearest = toTest;
			}
		}
		return nearest;
	}

}
