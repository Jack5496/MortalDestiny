package com.uos.mortaldestiny;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.Array;
import com.uos.mortaldestiny.Inputs.InputHandler;
import com.uos.mortaldestiny.Inputs.PlayerHandler;
import com.uos.mortaldestiny.objects.CameraController;
import com.uos.mortaldestiny.objects.GameObject;
import com.uos.mortaldestiny.objects.Player;

public class GameClass implements ApplicationListener {

	// PerspectiveCamera cam;
	// CameraInputController camController;

	public Environment environment;
	public CameraController cameraController;
	public ResourceManager resourceManager;
	public InputHandler inputs;
	public PlayerHandler playerHandler;

	public Physics physics;

	public static Array<GameObject> instances;
	// public static Array<ModelInstance> instances;

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
		initInputHandler();
		initPlayerHandler();

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

	public void initInputHandler() {
		inputs = new InputHandler();
	}

	public void initPlayerHandler() {
		playerHandler = new PlayerHandler();
	}

	float angle = 90f;
	float speed = 40f;

	int amount = 0;

	@Override
	public void render() {
		updateInputsAndGameWorld();

		cameraController.update(); // Update Camera Position

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		physics.modelBatch.begin(cameraController.camera);
		physics.modelBatch.render(instances, environment);
		physics.modelBatch.end();

		renderHUD();
	}

	private void updateInputsAndGameWorld() {
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());

		inputs.updateInputLogic(); // Update Inputs which effect Players
		playerHandler.updatePlayers(); // Players set PlayerObjects animations
		updateGameObjectsAnimations(delta); // PlayerObjects calc animation

		physics.dynamicsWorld.stepSimulation(delta, 5, 1f / 60f); // Calcs
																	// Physics

		if ((physics.spawnTimer -= delta) < 0 && amount < 10) { // spawn 10
																// random
																// GameObjects
//			physics.spawn();
			physics.spawnTimer = 1.5f;
			amount++;
		}
	}

	private void renderHUD() {
		batch.begin();
		int height = 15;
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, getHeight() - 5);
		font.draw(batch, "Objects: " + instances.size, 5, getHeight() - 20);
		int i = 0;
		for(Player p : playerHandler.getPlayers()){
			font.draw(batch, "Player: "+(i+1)+" Health: "+playerHandler.getPlayer(i).health, 5, getHeight() - (60+i*height));
			i++;
		}
		batch.end();
	}

	private void updateGameObjectsAnimations(float delta) {
		for (GameObject o : instances) {
			o.update(delta);
		}
	}

	public int getWidth() {
		return Gdx.graphics.getWidth();
	}

	public int getHeight() {
		return Gdx.graphics.getHeight();
	}

	@Override
	public void dispose() {
		for (ModelInstance obj : instances)
			if (obj instanceof GameObject)
				((GameObject) obj).dispose();
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
