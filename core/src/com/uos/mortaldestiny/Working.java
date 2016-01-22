package com.uos.mortaldestiny;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btConeShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class Working implements ApplicationListener {

	PerspectiveCamera cam;
	CameraInputController camController;
	ModelBatch modelBatch;
	Environment environment;
	Model model;
	public static Array<GameObject> instances;
	ArrayMap<String, GameObject.Constructor> constructors;
	float spawnTimer;

	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	MyContactListener contactListener;
	btBroadphaseInterface broadphase;
	btCollisionWorld collisionWorld;

	@Override
	public void create() {
		Bullet.init();

		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(3f, 7f, 10f);
		cam.lookAt(0, 4f, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		assets = new AssetManager();

		assets.load(pp, Model.class);

		float progress = 0;
		System.out.println("Loaded: " + assets.getProgress() * 100 + "%");

		while (!assets.update()) {
			if (progress != assets.getProgress()) {
				progress = assets.getProgress();
				System.out.println("Loaded: " + assets.getProgress() * 100 + "%");
			}
		}
		progress = assets.getProgress();
		System.out.println("Loaded: " + assets.getProgress() * 100 + "%");

		
		
		Model anim = loadAnimation();
		
		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "ground";
		mb.part("ground", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.RED))).box(5f, 1f, 5f);
//		mb.node().id = "cylinder";
//		mb.part("cylinder", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
//				new Material(ColorAttribute.createDiffuse(Color.MAGENTA))).cylinder(1f, 2f, 1f, 10);
		mb.node("cylinder", anim);
		
		model = mb.end();

		constructors = new ArrayMap<String, GameObject.Constructor>(String.class, GameObject.Constructor.class);

		constructors.put("ground",
				new GameObject.Constructor(model, "ground", new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f)), 0f));
		constructors.put("cylinder",
				new GameObject.Constructor(model, "cylinder", new btCylinderShape(new Vector3(.5f, 1f, .5f)), 1f));

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
		contactListener = new MyContactListener();

		instances = new Array<GameObject>();
		GameObject object = constructors.get("ground").construct();
		instances.add(object);
		collisionWorld.addCollisionObject(object.body, MyContactListener.GROUND_FLAG, MyContactListener.ALL_FLAG);
	}

	public AssetManager assets;
	public Model playerModel;
	String pp = "data/models/Player/player.g3db";

	public Model getG3DBModel(String path) {
		// return modelLoader.loadModel(Gdx.files.getFileHandle(path,
		// FileType.Internal));
		return assets.get(path);
	}

	public Model loadAnimation() {
		return getG3DBModel(pp);
	}
	
	boolean done = true;

	public void spawn() {
		if(done){
		GameObject obj = constructors.values[1 + MathUtils.random(constructors.size - 2)].construct();
		obj.moving = true;

		
//		obj.transform.scl(0.01f);
		
//		obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));
		obj.transform.trn(MathUtils.random(-2.5f, 2.5f), 9f, MathUtils.random(-2.5f, 2.5f));
		obj.transform.scl(0.01f);
		
		obj.body.setWorldTransform(obj.transform);
		obj.body.setUserValue(instances.size);
		obj.body.setCollisionFlags(
				obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		
		
		instances.add(obj);
		// collisionWorld.addCollisionObject(obj.body, OBJECT_FLAG,
		// GROUND_FLAG);
		collisionWorld.addCollisionObject(obj.body, MyContactListener.OBJECT_FLAG, MyContactListener.ALL_FLAG);
		done = false;
		}
	}

	@Override
	public void render() {
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

		for (GameObject obj : instances) {
			if (obj.moving) {
				obj.transform.trn(0f, -delta, 0f);
				obj.body.setWorldTransform(obj.transform);
			}
		}

		collisionWorld.performDiscreteCollisionDetection();

		if ((spawnTimer -= delta) < 0) {
			spawn();
			spawnTimer = 1.5f;
		}

		camController.update();

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	@Override
	public void dispose() {
		for (GameObject obj : instances)
			obj.dispose();
		instances.clear();

		for (GameObject.Constructor ctor : constructors.values())
			ctor.dispose();
		constructors.clear();

		collisionWorld.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();

		contactListener.dispose();

		modelBatch.dispose();
		model.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
	}
}
