package com.uos.mortaldestiny.objects;

import com.uos.mortaldestiny.GameClass;

public class BulletObject extends GameObject{
	
	public int damage;
	public PlayerObject shooter;

	public BulletObject(GameObject obj, int damage, PlayerObject shooter) {
		super(obj.model, obj.constructionInfo);
		GameClass.getInstance().physics.registerGameObject(this);
		this.shooter = shooter;
		init(damage);
	}
	
	public void init(int damage){
		this.damage = damage;
	}
}