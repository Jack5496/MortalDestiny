package com.uos.mortaldestiny.worldGenerators;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.player.Player;
import com.uos.mortaldestiny.player.PlayerHandler;
import com.uos.mortaldestiny.world.Physics;
import com.uos.mortaldestiny.worldTiles.WorldTile;

public class WorldGeneratorThread implements Runnable {
	Physics physics;
	public List<WorldTile> toGenerate;
	public List<WorldTile> toRemove;
	public WorldGenerator worldGenerator;

	public boolean running = true;

	public WorldGeneratorThread(WorldGenerator worldGenerator) {
		toGenerate = new ArrayList<WorldTile>();
		toRemove = new ArrayList<WorldTile>();
		physics = GameClass.getInstance().physics;
		this.worldGenerator = worldGenerator;
	}

	int size = 10;

	long sleepTime = 50;

	/**
	 * https://github.com/libgdx/libgdx/wiki/Threading
	 */
	@Override
	public void run() {
		while (running) {
			toRemove = new ArrayList<WorldTile>();
			for (final WorldTile tile : new ArrayList<WorldTile>(toGenerate)) {
				if (tile != null) {

					/**
					 * https://github.com/libgdx/libgdx/wiki/Threading Problem
					 * is that libGDX uses C helper which in Bullet(Physics
					 * Engine) can cause NullPointer Exceptions
					 */

					// Gdx.app.postRunnable(new Runnable() {
					// @Override
					// public void run() {
					tile.generate();
					// }
					// });

					toRemove.add(tile);
					worldGenerator.tiles.put(tile.p, tile);
				}
			}
			for (WorldTile tile : toRemove) {
				toGenerate.remove(tile);
			}

			PlayerHandler ph = GameClass.getInstance().playerHandler;
			if (ph != null) {
				for (Player p : ph.getPlayers()) {
					worldGenerator.addToGeneratePlayerArea(p);
				}
			}

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
