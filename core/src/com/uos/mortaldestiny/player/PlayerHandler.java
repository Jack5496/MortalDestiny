package com.uos.mortaldestiny.player;

import java.util.HashMap;

public class PlayerHandler {

	HashMap<String, Player> localPlayers;

	public Player getPlayer(int id){
		Player[] players = getPlayers();
		return players[id];
	}
	
	public Player[] getPlayers(){
		Player[] players = new Player[localPlayers.values().size()];
		localPlayers.values().toArray(players);
		return players;
	}
	
	public int getPlayerAmount(){
		return getPlayers().length;
	}
	
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
	
	public void updatePlayers(){
		for(Player p : localPlayers.values()){
			p.updateMyGameObjects();
		}
	}

}
