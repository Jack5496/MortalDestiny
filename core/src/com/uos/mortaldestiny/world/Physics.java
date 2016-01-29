package com.uos.mortaldestiny.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.collision.btStaticPlaneShape;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.ResourceManager;
import com.uos.mortaldestiny.objects.BulletObject;
import com.uos.mortaldestiny.objects.GameObject;
import com.uos.mortaldestiny.objects.PlayerObject;
import com.uos.mortaldestiny.objects.VoidZoneObject;
import com.uos.mortaldestiny.player.Player;

public class Physics implements Disposable {

	public ModelBatch modelBatch;

	Model model;
	ModelBuilder mb;
	

	ArrayMap<String, GameObject.Constructor> constructors;

	public btCollisionConfiguration collisionConfig;
	public btDispatcher dispatcher;
	public MyContactListener contactListener;
	public btBroadphaseInterface broadphase;
	public btDynamicsWorld dynamicsWorld;
	public btConstraintSolver constraintSolver;
	public Environment environment;

	public float spawnTimer;

	float size = 10;
	
	public void initEnvironment() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	public Physics() {
		Bullet.init();
		initEnvironment();

//		String fs = Gdx.files.internal("data/shaders/toon-shader-iii.fs.glsl").readString();
//		String vs = Gdx.files.internal("data/shaders/toon-shader-iii.vs.glsl").readString();

		modelBatch = new ModelBatch();
		constructors = new ArrayMap<String, GameObject.Constructor>(String.class, GameObject.Constructor.class);

		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "sphere";
		mb.part("sphere", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.GREEN))).sphere(1f, 1f, 1f, 10, 10);
		model = mb.end();
		constructors.put("sphere", new GameObject.Constructor(model, "sphere", new btSphereShape(.5f), 1f));

		Model anim = GameClass.getInstance().resourceManager.getPlayer();
		constructors.put("player", new GameObject.Constructor(anim, new btCylinderShape(new Vector3(2f, 4f, 2f)), 1f));

		Model GroundTile5x5 = ResourceManager.getInstance()
				.getG3DBModel(ResourceManager.getInstance().pathGrounds + "GroundTile5x5.g3db");
		constructors.put("GroundTile5x5",
				new GameObject.Constructor(GroundTile5x5, new btBoxShape(new Vector3(5, 1, 5)), 0f));

		Model box = ResourceManager.getInstance().getG3DBModel(ResourceManager.getInstance().pathModels + "1x1.g3db");
		constructors.put("box", new GameObject.Constructor(box, new btBoxShape(new Vector3(1, 1, 1)), 1f));

		Model sack = ResourceManager.getInstance().getG3DBModel(ResourceManager.getInstance().pathModels + "sack.g3db");
		constructors.put("sack", new GameObject.Constructor(sack, new btSphereShape(0.5f), 1f));

		Model ground = ResourceManager.getInstance()
				.getG3DBModel(ResourceManager.getInstance().pathGrounds + "GroundTile5x5.g3db");
		constructors.put("ground",
				new GameObject.Constructor(ground, new btStaticPlaneShape(new Vector3(0, 1, 0), 1), 0f));

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
		contactListener = new MyContactListener();

		GameClass.instances = new Array<GameObject>();
		spawnGround();
	}

	public void spawnGround() {
		VoidZoneObject fo = new VoidZoneObject(constructors.get("ground").construct());
		fo.mySetScale(1f);
		fo.transform.trn(new Vector3(0, -10, 0));
		fo.calculateTransforms();
		fo.body.setCollisionFlags(fo.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		GameClass.instances.add(fo);
		dynamicsWorld.addRigidBody(fo.body);
		fo.body.setContactCallbackFlag(MyContactListener.DEATH_ZONE);
		fo.body.setContactCallbackFilter(0);
		fo.body.setActivationState(Collision.DISABLE_DEACTIVATION);
	}

	public void spawnGroundTile(Vector3 pos) {
		GameObject fo = constructors.get("GroundTile5x5").construct();
		fo.mySetScale(1f);
		fo.transform.trn(pos);
		fo.calculateTransforms();
		fo.body.setCollisionFlags(fo.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
		GameClass.instances.add(fo);
		dynamicsWorld.addRigidBody(fo.body);
		fo.body.setContactCallbackFlag(MyContactListener.GROUND_FLAG);
		fo.body.setContactCallbackFilter(0);
		fo.body.setActivationState(Collision.DISABLE_DEACTIVATION);
	}

	public void addRigidBodyToDynamicsWorld(GameObject obj) {
		GameClass.instances.add(obj);
		dynamicsWorld.addRigidBody(obj.body);
	}

	public PlayerObject spawnPlayer(Player p) {
		GameObject obj = constructors.get("box").construct();
		PlayerObject player = new PlayerObject(obj, p);
		// player.transform.trn(MathUtils.random(size*-2.5f, size*2.5f), 15f,
		// MathUtils.random(size*-2.5f, size*2.5f));
		player.transform.trn(0, 15f, 0);
		player.body.proceedToTransform(player.transform);
		player.mySetScale(1f);
		player.calculateTransforms();
		player.body.setCollisionFlags(
				player.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		addRigidBodyToDynamicsWorld(player);
		player.body.setContactCallbackFlag(MyContactListener.OBJECT_FLAG);
		// obj.body.setContactCallbackFilter(0);
		player.body.setContactCallbackFilter(MyContactListener.GROUND_FLAG);

		return player;
	}

	public BulletObject spawnSack(int damage) {
		GameObject obj = constructors.get("sack").construct();
		BulletObject bullet = new BulletObject(obj, 20);
		bullet.transform.trn(MathUtils.random(-2.5f, 2.5f), 15f, MathUtils.random(-2.5f, 2.5f));
		bullet.body.proceedToTransform(bullet.transform);
		bullet.body.setCollisionFlags(
				bullet.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		GameClass.instances.add(bullet);
		dynamicsWorld.addRigidBody(bullet.body);
		bullet.body.setContactCallbackFlag(MyContactListener.OBJECT_FLAG);
		bullet.body.setContactCallbackFilter(MyContactListener.GROUND_FLAG);

		return bullet;
	}

	public GameObject spawn(String key) {
		GameObject obj = constructors.get(key).construct();
		obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));
		obj.transform.trn(0, 15f, 0);
		obj.mySetScale(1f);
		obj.body.proceedToTransform(obj.transform);
		obj.body.setCollisionFlags(
				obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		GameClass.instances.add(obj);
		dynamicsWorld.addRigidBody(obj.body);
		obj.body.setContactCallbackFlag(MyContactListener.OBJECT_FLAG);
		obj.body.setContactCallbackFilter(MyContactListener.GROUND_FLAG);
		return obj;
	}
	
	public GameObject constructGameObject(String key){
		return constructors.get(key).construct();
	}
	
	public void registerGameObject(GameObject obj){
		obj.body.proceedToTransform(obj.transform);
		obj.body.setCollisionFlags(
				obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		GameClass.instances.add(obj);
		dynamicsWorld.addRigidBody(obj.body);
		obj.body.setContactCallbackFlag(MyContactListener.OBJECT_FLAG);
		obj.body.setContactCallbackFilter(MyContactListener.GROUND_FLAG);
	}

	public void spawn() {
		spawn(constructors.getKeyAt(2 + MathUtils.random(constructors.size - 3)));
	}

	@Override
	public void dispose() {
		for (GameObject.Constructor ctor : constructors.values())
			ctor.dispose();
		constructors.clear();

		dynamicsWorld.dispose();
		constraintSolver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();

		contactListener.dispose();

		modelBatch.dispose();
		model.dispose();
	}

}
