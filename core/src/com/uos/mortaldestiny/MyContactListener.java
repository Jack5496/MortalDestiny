package com.uos.mortaldestiny;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;

public class MyContactListener extends ContactListener {
	
	final static short GROUND_FLAG = 1 << 8;
	final static short OBJECT_FLAG = 1 << 9;
	final static short ALL_FLAG = -1;
	
	@Override
	public boolean onContactAdded(int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
		GameClass.instances.get(userValue0).moving = false;
		GameClass.instances.get(userValue1).moving = false;
		return true;
	}
}
