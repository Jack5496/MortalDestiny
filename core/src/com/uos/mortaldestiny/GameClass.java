package com.uos.mortaldestiny;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class GameClass extends ApplicationAdapter {

	// private SpriteBatch batch;
	// private BitmapFont font;
	// private Texture img;
	private Graphics grafics;

	public PerspectiveCamera cam;
	public Model model;
	public ModelInstance instance;
	public ModelBatch modelBatch;

	public Environment environment;
	
	public CameraInputController camController;

	@Override
	public void create() {
		// batch = new SpriteBatch();
		// font = new BitmapFont();
		// font.setColor(Color.RED);
		// img = new Texture(Gdx.files.internal("data/badlogic.jpg"));
		grafics = Gdx.app.getGraphics();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);
		instance = new ModelInstance(model);

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
	}

	@Override
	public void resize(int width, int height) {

	}

	public int getWidth() {
		return grafics.getWidth();
	}

	public int getHeight() {
		return grafics.getHeight();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		// batch.dispose();
		// font.dispose();
		model.dispose();
		modelBatch.dispose();
	}

	@Override
	public void render() {
		// Gdx.gl.glClearColor(1, 1, 1, 1);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//
		//
		// batch.begin();
		//// batch.draw(img, 0, 0);
		// font.draw(batch, getWidth()+" x "+getHeight(), 250, getHeight()/2);
		// batch.end();
		
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();
	}
}
