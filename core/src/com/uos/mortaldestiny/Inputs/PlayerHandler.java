package com.uos.mortaldestiny.Inputs;

import java.util.HashMap;

import com.uos.mortaldestiny.objects.Player;

public class PlayerHandler {

	HashMap<String, Player> localPlayers;

	public PlayerHandler() {
		localPlayers = new HashMap<String, Player>();
	}

	public Player getPlayerByInput(String inputHandlerName) {
		boolean found = localPlayers.containsKey(inputHandlerName);

		if (!found) {
			System.out.println("New Player found");
			localPlayers.put(inputHandlerName, new Player("Bob"));
		}

		return localPlayers.get(inputHandlerName);
	}
	
	public void updatePlayers(float delta){
		for(Player p : localPlayers.values()){
			p.updateMyGameObjects();
			p.updateAnimation(delta);
		}
	}

}
