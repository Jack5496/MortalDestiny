package com.uos.mortaldestiny.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.objects.BulletObject;
import com.uos.mortaldestiny.objects.GameObject;
import com.uos.mortaldestiny.objects.PlayerObject;
import com.uos.mortaldestiny.objects.VoidZoneObject;

public class MyContactListener extends ContactListener {

	// public final static short GROUND_FLAG = 1 << 8;
	// public final static short OBJECT_FLAG = 1 << 9;
	// public final static short ALL_FLAG = -1;

	public final static int DEATH_ZONE = 0;
	public final static int GROUND_FLAG = 1;
	public final static int OBJECT_FLAG = 2;
	public final static int ALL_FLAG = -1;

	// when body still touches other
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
	public void onContactStarted(btCollisionObject bodyObj0, btCollisionObject bodyObj1) {
		GameObject obj0 = (GameObject) bodyObj0.userData;
		GameObject obj1 = (GameObject) bodyObj1.userData;
		onContactStarted(obj0, obj1);
	}

	public void onContactStarted(GameObject obj0, GameObject obj1) {

		// fall off
		if (obj1 instanceof VoidZoneObject && obj0 instanceof PlayerObject) {
			((PlayerObject) obj0).respawn();
		}

		if (obj0 instanceof GameObject && !(obj0 instanceof BulletObject) && obj1 instanceof PlayerObject) {
			// GameClass.log(getClass(), "Player hit ground");
			PlayerObject p = (PlayerObject) obj1;
			p.onGround = true;
		}

		if (obj0 instanceof PlayerObject && obj1 instanceof BulletObject) {
			BulletObject bullet = (BulletObject) obj1;
			PlayerObject p = (PlayerObject) obj0;
			p.player.health -= bullet.damage;
			if (p.player.health <= 0) {
				bullet.shooter.player.points++;
			}
			bullet.myDelete();
		}

		if (obj1 instanceof VoidZoneObject && obj0 instanceof GameObject && !(obj0 instanceof PlayerObject)) {
			obj0.myDelete();
		}

		int userValue0 = obj0.body.getUserValue();
		int userValue1 = obj1.body.getUserValue();

		if (userValue0 != 0) {
			((ColorAttribute) GameClass.instances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color
					.set(Color.WHITE);
		}
		if (userValue1 != 0) {
			((ColorAttribute) obj1.materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
		}
	}

	// //on first contact
	// @Override
	// public void onContactStarted(int userValue0, int userValue1) {
	// GameObject obj0 = GameClass.instances.get(userValue0);
	// GameObject obj1 = GameClass.instances.get(userValue1);
	//
	// //lol buggy?
	//// if(obj0 instanceof VoidZoneObject && obj1 instanceof PlayerObject){
	//// System.out.println("1");
	//// }
	//
	// if(obj1 instanceof VoidZoneObject && obj0 instanceof PlayerObject){
	// ((PlayerObject)obj0).respawn();
	// }
	//
	// if(obj1 instanceof VoidZoneObject && obj0 instanceof GameObject && !(obj0
	// instanceof PlayerObject)){
	//
	// }
	//
	//
	// if (userValue0 != 0) {
	// ((ColorAttribute)
	// GameClass.instances.get(userValue0).materials.get(0).get(ColorAttribute.Diffuse)).color
	// .set(Color.WHITE);
	// }
	// if (userValue1 != 0) {
	// ((ColorAttribute)
	// GameClass.instances.get(userValue1).materials.get(0).get(ColorAttribute.Diffuse)).color
	// .set(Color.WHITE);
	// GameObject o = GameClass.instances.get(userValue1);
	// if (userValue0 == 0) { // this is the real ground
	// if (o instanceof PlayerObject) {
	// PlayerObject p = (PlayerObject) o;
	// p.onGround = true;
	// }
	//
	// } else { // that is an other object
	// if (o instanceof PlayerObject) {
	// PlayerObject p = (PlayerObject) o;
	// p.onGround = true;
	// }
	// }
	// }
	// }
}
