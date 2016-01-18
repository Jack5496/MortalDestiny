package com.uos.mortaldestiny;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
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
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

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
	public boolean loading;
	
	public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
	
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

//		ModelBuilder modelBuilder = new ModelBuilder();
//		model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
//				Usage.Position | Usage.Normal);
//		instance = new ModelInstance(model);
		
		ModelLoader loader = new ObjLoader();
		model = loader.loadModel(Gdx.files.internal("data/player/player.obj"));
		instance = new ModelInstance(model);

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
        
        assets = new AssetManager();
        assets.load("data/ship/ship.obj", Model.class);
        assets.load("data/ship/ship.obj", Model.class);
        loading = true;
	}
	
	private void doneLoading() {
		Model ship = assets.get("data/ship/ship.obj", Model.class);
        for (float x = -5f; x <= 5f; x += 2f) {
            for (float z = -5f; z <= 5f; z += 2f) {
                ModelInstance shipInstance = new ModelInstance(ship);
                shipInstance.transform.setToTranslation(x, 0, z);
                instances.add(shipInstance);
            }
        }
        loading = false;
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
//		model.dispose();
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
		
		if (loading && assets.update())
            doneLoading();
		
		camController.update();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.render(instance, environment);
		modelBatch.end();
	}
}
