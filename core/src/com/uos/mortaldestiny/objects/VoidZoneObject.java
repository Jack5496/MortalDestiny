package com.uos.mortaldestiny.objects;

import com.uos.mortaldestiny.GameClass;

public class VoidZoneObject extends GameObject{
	
	public VoidZoneObject(GameObject obj) {
		super(obj.model, obj.constructionInfo);
		GameClass.totalObjectsSpawned++;
	}
}