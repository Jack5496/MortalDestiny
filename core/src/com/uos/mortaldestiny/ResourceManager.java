package com.uos.mortaldestiny;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

public class ResourceManager {

	public AssetManager assets;
	private boolean loaded;
	private boolean init;

	public String pathModels = "data/models/";
	public String pathGrounds = pathModels + "MapParts/Ground/";
	String pp = pathModels+"Player/player.g3db";

	public Model model;
	public Model playerModel;
	public Model obstacle;
	public ModelBatch modelBatch;
	
	private static ResourceManager instance;

	public ResourceManager() {
		instance = this;
		loaded = false;
		assets = new AssetManager();
		load();
		update();
	}
	
	public static ResourceManager getInstance(){
		return instance;
	}

	public void update() {
		boolean doneLoading = !loaded && assets.update() && init;

		if (doneLoading) {
			doneLoading();
		}
	}

	public void dispose() {
		modelBatch.dispose();
	}

	public void load() {

		enqueAssets();

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
	}

	public void enqueAssets() {
		assets.load(pathModels + "1x1.g3db", Model.class);
		assets.load(pathGrounds + "GroundTile5x5.obj", Model.class);
		assets.load(pathGrounds + "spielfeld.g3db", Model.class);
		assets.load(pp, Model.class);
	}
	
	public Model getG3DBModel(String path) {
		return assets.get(path);
	}
	
	public Model getPlayer() {
		Model anim = getG3DBModel(pp);
		
		for(Node n : anim.nodes){
			n.scale.set(0.5f,0.5f,0.5f);
		}
		anim.calculateTransforms();
		return anim;
	}

	public void doneLoading() {
		float test = 0f;
		int width = 3;
		int height = 3;

		float stepx = 10f + test;
		float maxx = width * (stepx + 2);

		float stepz = 10f + test;
		float maxz = height * (stepz + 2);

//		GameClass.getInstance().player = new Player(animInstance);
//
//		GameClass.getInstance().cameraController.setTrack(GameClass.getInstance().player.getModelInstance());
//
//		GameClass.getInstance().add(GameClass.getInstance().player.getModelInstance());
//
//		Entity obb = new Entity(new ModelInstance(obstacle));
//		obb.translateTo(new Vector3(-maxx + 3 * stepx, 0, -maxz + stepz));
//		GameClass.getInstances().add(obb.getModelInstance());
//
//		for (float x = -maxx; x <= maxx; x += stepx) {
//			for (float z = -maxz; z <= maxz; z += stepz) {
//				ModelInstance shipInstance = new ModelInstance(model);
//				shipInstance.transform.setToTranslation(x, 0, z);
//				instances.add(shipInstance);
//			}
//		}
		loaded = true;
	}
}
