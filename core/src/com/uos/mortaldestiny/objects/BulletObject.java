package com.uos.mortaldestiny.objects;

import com.badlogic.gdx.math.Vector3;

public class BulletObject extends GameObject{
	
	int damage;

	public BulletObject(GameObject obj, int damage) {
		super(obj.model, obj.constructionInfo);
		init(damage);
	}
	
	private void init(int damage){
		this.damage = damage;
		
		body.setAngularFactor(new Vector3(0,1,0));	//wont let object rotate around given axe
	}
}