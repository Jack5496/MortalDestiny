package com.uos.mortaldestiny;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.uos.mortaldestiny.objects.CameraController;
import com.uos.mortaldestiny.objects.GameObject;
import com.uos.mortaldestiny.objects.Player;

public class GameClass implements ApplicationListener {

	// PerspectiveCamera cam;
	// CameraInputController camController;

	public Environment environment;
	public CameraController cameraController;
	public ResourceManager resourceManager;

	public Player player;

	float spawnTimer;

	Physics physics;

	public static Array<GameObject> instances;

	private static GameClass application;

	private SpriteBatch batch;
	private BitmapFont font;

	public static GameClass getInstance() {
		return application;
	}

	@Override
	public void create() {
		application = this;

		initResourceManager();
		initEnvironment();
		initCamera();
		initPhysics();

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
	}

	public static Array<GameObject> getInstances() {
		return instances;
	}

	public void initEnvironment() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	public void initCamera() {
		cameraController = new CameraController();
	}

	public void initResourceManager() {
		resourceManager = new ResourceManager();
	}

	public void initPhysics() {
		physics = new Physics();
	}

	@Override
	public void render() {
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

		physics.dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);

		if ((spawnTimer -= delta) < 0) {
			physics.spawn();
			spawnTimer = 1.5f;
		}

		// camController.update();

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		physics.modelBatch.begin(cameraController.getCamera());
		physics.modelBatch.render(instances, environment);
		physics.modelBatch.end();

		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, getHeight() - 5);
		batch.end();
	}

	public int getWidth() {
		return Gdx.graphics.getWidth();
	}

	public int getHeight() {
		return Gdx.graphics.getHeight();
	}

	@Override
	public void dispose() {
		for (GameObject obj : instances)
			obj.dispose();
		instances.clear();

		physics.dispose();
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
