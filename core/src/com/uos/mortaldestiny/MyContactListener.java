package com.uos.mortaldestiny;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.bullet.collision.CollisionJNI;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.uos.mortaldestiny.objects.GameObject;

public class MyContactListener extends ContactListener {

	final static short GROUND_FLAG = 1 << 8;
	final static short OBJECT_FLAG = 1 << 9;
	final static short ALL_FLAG = -1;

	@Override
	public boolean onContactAdded(int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
		// if (userValue0 != 0) {
		// ((ColorAttribute)
		// GameClass.instances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color
		// .set(Color.WHITE);
		// System.out.println("ContactAdded: u0");
		// }
		// if (userValue1 != 0) {
		// ((ColorAttribute)
		// GameClass.instances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color
		// .set(Color.WHITE);
		// System.out.println("ContactAdded: u1");
		// }
		return true;
	}

	@Override
	public void onContactStarted(int userValue0, int userValue1) {
		if (userValue0 != 0) {
			((ColorAttribute) GameClass.instances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color
					.set(Color.WHITE);
			// System.out.println("ContactStarted: u0");
		}
		if (userValue1 != 0) {
			((ColorAttribute) GameClass.instances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color
					.set(Color.WHITE);
			// System.out.println("ContactStarted: u1");
			GameObject o = GameClass.instances.get(userValue1);
			if (o.player != null) {
				if (userValue0 == 0) {	//this is the real ground
					System.out.println("Hit ground");
					o.player.ground = true;
				}
				else{	//that is an other object
					o.player.ground = true;
				}
			}
		}
	}

}
