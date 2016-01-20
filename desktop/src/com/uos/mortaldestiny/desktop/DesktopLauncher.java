package com.uos.mortaldestiny.desktop;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.uos.mortaldestiny.GameClass;

public class DesktopLauncher {
	
	static Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Mortal Destiny";
		config.useGL30 = false;
		config.height = size.height/2;
		config.width = size.width/2;
		new LwjglApplication(new GameClass(), config);
	}
}
