package com.uos.mortaldestiny;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.player.Player;
import com.uos.mortaldestiny.world.Physics;

public class Renderer {

	public Environment environment;
	Physics physics;

	private SpriteBatch batch;
	private BitmapFont font;

	public Renderer() {
		initEnvironment();
		physics = GameClass.getInstance().physics;

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
	}

	public void initEnvironment() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	public void renderForPlayers() {
		int amountPlayers = GameClass.getInstance().playerHandler.getPlayerAmount();

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		switch (amountPlayers) {
		case 1:
			renderForOnePlayer();
			break;
		case 2:
			renderForTwoPlayer();
			break;
		case 3:
			renderForThreePlayer();
			break;
		case 4:
			renderForFourPlayer();
			break;
		}
	}

	int border = 1;

	private void renderForPlayer(Player p, int x, int y, int width, int height) {
		p.cameraController.updateViewPort(width - border, height - border);
		p.cameraController.update(); // Update Camera Position

		Gdx.gl.glViewport(x, y, width - border, height - border);

		physics.modelBatch.begin(p.cameraController.camera);
		physics.modelBatch.render(GameClass.instances, environment);
		physics.modelBatch.end();

		renderHUD(p);
	}

	private void renderForOnePlayer() {
		Player p = GameClass.getInstance().playerHandler.getPlayer(0);
		renderForPlayer(p, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void renderForTwoPlayer() {
		Player p0 = GameClass.getInstance().playerHandler.getPlayer(0);
		renderForPlayer(p0, 0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());

		Player p1 = GameClass.getInstance().playerHandler.getPlayer(1);
		renderForPlayer(p1, Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
	}

	public void renderForThreePlayer() {
		Player p0 = GameClass.getInstance().playerHandler.getPlayer(0);
		renderForPlayer(p0, 0, Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);

		Player p1 = GameClass.getInstance().playerHandler.getPlayer(1);
		renderForPlayer(p1, 0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

		Player p2 = GameClass.getInstance().playerHandler.getPlayer(2);
		renderForPlayer(p2, Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
	}

	public void renderForFourPlayer() {
		Player p0 = GameClass.getInstance().playerHandler.getPlayer(0);
		renderForPlayer(p0, 0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

		Player p1 = GameClass.getInstance().playerHandler.getPlayer(1);
		renderForPlayer(p1, 0, Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

		Player p2 = GameClass.getInstance().playerHandler.getPlayer(2);
		renderForPlayer(p2, Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

		Player p3 = GameClass.getInstance().playerHandler.getPlayer(3);
		renderForPlayer(p3, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
	}

	private void renderHUD(Player p) {
		int height = (int) p.cameraController.camera.viewportHeight;
		// int width = (int) p.cameraController.camera.viewportWidth;

		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, height - 5);
		font.draw(batch, "Objects: " + GameClass.instances.size, 80, height - 5);
		Vector3 pos = p.getObjPos();
		font.draw(batch, "X: " + (int) pos.x + "    | Y: " + (int) pos.y + "    | Z: " + (int) pos.z, 5, height - 20);
		font.draw(batch, "Player Health: " + p.health, 5, height - 60);
		batch.end();
	}

}
