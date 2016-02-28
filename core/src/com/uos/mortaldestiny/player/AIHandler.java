package com.uos.mortaldestiny.player;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.uos.mortaldestiny.world.WorldManager;

public class AIHandler {

	HashMap<String, AI> localPlayers;

	public AI getPlayer(int id){
		AI[] players = getPlayers();
		return players[id];
	}
	
	public AI[] getPlayers(){
		AI[] players = new AI[localPlayers.values().size()];
		localPlayers.values().toArray(players);
		return players;
	}
	
	public int getPlayerAmount(){
		return getPlayers().length;
	}
	
	public AIHandler() {
		localPlayers = new HashMap<String, AI>();
	}
	
	public void createAI(String name) {
		if(localPlayers.containsKey(name)){
			
		}
		else{
			AI p = new AI(name, (int)(Math.random()*5));
			
			int width = 10*10;
			int height = 10*10;
			p.obj.mySetTranslation(new Vector3(width*((float)Math.random())-5,7,height*((float)Math.random())-5));
			localPlayers.put(name, p);
		}
	}
	
	public void updateEntityHealth(){
		Array<AI> toDie = new Array<AI>();
		for(AI p : localPlayers.values()){
			if(p.isStillAlive()){
				
			}
			else{
				toDie.add(p);
			}
		}
		for(AI p : toDie){
			String name = p.name;
			localPlayers.remove(p.name,p);
			p.dispose();
			createAI(name);
		}
	}
	
	public void updateAIs(){
		for(AI p : localPlayers.values()){
			p.initVariables();
//			p.addAllPlayerAsTargets();//this should be done only once
			p.addAllOtherFractionsAsEnemy();
			p.updateInputVariables();
			p.updateMyGameObjects();
		}
	}

}
