package com.uos.mortaldestiny.menu;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.uos.mortaldestiny.player.Player;

public class MenuHandler {

	public Menu activMenu;
	public Player p;
	
	public MenuHandler(Player p){
		activMenu = new Ingame(this);
		this.p = p;
	}
	
	public void renderActivMenu(ModelBatch batch){
		activMenu.render(batch);
	}
	
}
