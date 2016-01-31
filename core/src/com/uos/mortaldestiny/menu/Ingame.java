package com.uos.mortaldestiny.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.player.Player;
import com.uos.mortaldestiny.world.Physics;

public class Ingame implements Menu {

	public MenuHandler menuHandler;
	Physics physics;
	public Environment environment;

	public Ingame(MenuHandler menuHandler) {
		this.menuHandler = menuHandler;
		this.physics = GameClass.getInstance().physics;
		this.environment = GameClass.getInstance().physics.environment;
	}

	@Override
	public void up() {
		// TODO Auto-generated method stub

	}

	@Override
	public void down() {
		// TODO Auto-generated method stub

	}

	@Override
	public void left() {
		// TODO Auto-generated method stub

	}

	@Override
	public void right() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(ModelBatch batch) {
		// TODO Auto-generated method stub
		batch.begin(menuHandler.p.cameraController.camera);
		batch.render(GameClass.instances, environment);
		batch.end();

		renderHUD(menuHandler.p);
	}

	private void renderHUD(Player p) {
		int height = (int) p.cameraController.camera.viewportHeight;
		// int width = (int) p.cameraController.camera.viewportWidth;
		SpriteBatch batch = GameClass.getInstance().renderer.batch;
		BitmapFont font = GameClass.getInstance().renderer.font;

		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, height - 5);
		font.draw(batch, "Objects: " + GameClass.instances.size, 80, height - 5);
		Vector3 pos = p.getObjPos();
		font.draw(batch, "X: " + (int) pos.x + "    | Y: " + (int) pos.y + "    | Z: " + (int) pos.z, 5, height - 20);
		font.draw(batch, "Player Health: " + p.health, 5, height - 60);
		font.draw(batch, "Points: " + p.points, 5, height - 75);
		batch.end();

	}

	@Override
	public void setAsActivMenu() {
		this.menuHandler.activMenu = this;
	}

}
