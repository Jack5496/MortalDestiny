package com.uos.mortaldestiny.objects;

import com.uos.mortaldestiny.GameClass;

public class BulletObject extends GameObject{
	
	public int damage;

	public BulletObject(GameObject obj, int damage) {
		super(obj.model, obj.constructionInfo);
		GameClass.getInstance().physics.registerGameObject(this);
		init(damage);
	}
	
	public void init(int damage){
		this.damage = damage;
	}
}