package com.uos.mortaldestiny.player;

import java.util.HashMap;

import com.badlogic.gdx.utils.Array;
import com.uos.mortaldestiny.GameClass;

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
			GameClass.log(getClass(), "New AI: "+name);
			AI p = new AI(name);
			localPlayers.put(name, p);
		}
	}
	
	public void updateEntityHealth(){
		Array<AI> toDie = new Array<AI>();
		for(AI p : localPlayers.values()){
			if(!p.isStillAlive()){
				toDie.add(p);
			}
		}
		for(AI p : toDie){
//			localPlayers.remove(p);
			localPlayers.remove(p.name);
		}
	}
	
	public void updateAIs(){
		for(AI p : localPlayers.values()){
			p.setAllPlayerAsTargets();//this should be done only once
			p.updateInputVariables();
			p.updateMyGameObjects();
		}
	}

}
