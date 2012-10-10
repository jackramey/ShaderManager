package com.sgflt.Sample;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import com.sgflt.ShaderManager.ShaderManager;

public class Main {
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 640;

	public static void main(String[] args) {
		initDisplay();
		
		ShaderManager SM = ShaderManager.getInstance();
		SM.createShader("default", "aaa", "bbb");
		System.out.println("Hello World!");
	}
	
	public static void initDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Create the viewport to the width and height of the display
		resizeScreen(WIDTH, HEIGHT);
	}

	public static void resizeScreen(int width, int height) {
		if(height == 0) {
			height = 1;
		}
		
		glViewport(0, 0, width, height);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(45.0f, (float)width/(float)height, 0.1f, 1000.0f);
		GLU.gluLookAt(
				100.0f, 50.0f, 100.0f,
				0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f
			);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

}
