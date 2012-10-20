package com.sgflt.Example;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.sgflt.ShaderManager.ShaderManager;

public class Game {
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 640;
	
	public ShaderManager SM;
	
	public Sphere s;

	public Game() {
		initDisplay();
		initGL();
		initGraphics();
		initScene();
	}
	
	private void createShaders() {
		String fragShaderSource = new String();
		String vertShaderSource = new String();
		
		try {
			fragShaderSource = FileReader.readFile("res/basic.frag");
			vertShaderSource = FileReader.readFile("res/hemisphere.vert");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			SM.createShader("hemi", vertShaderSource, fragShaderSource);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void gameLoop() {
		while(!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			render();
			Display.update();
			handleInput();
		}
		Display.destroy();
	}
	
	private void handleInput() {
		handleKeyboardInput();
	}
	
	private void handleKeyboardInput() {
		while(Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					System.exit(0);
				}
			}
		}
	}
	
	private void initDisplay() {
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
	
	private void initGL() {
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f,0.0f,0.0f,0.0f); //Black Background
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}
	
	private void initGraphics() {
		SM = ShaderManager.getInstance();
		createShaders();
	}
	
	private void initScene() {
		s = new Sphere();
	}
	
	private void render() {
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		SM.setActiveShader("hemi");
		SM.bind();
		s.draw(25.0f, 50, 50);
		SM.unbind();
		
		DrawUtility.drawAxes(100);

		GL11.glPopMatrix();
	}
	
	private void resizeScreen(int width, int height) {
		if(height == 0) {
			height = 1;
		}
		
		GL11.glViewport(0, 0, width, height);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, (float)width/(float)height, 0.1f, 1000.0f);
		GLU.gluLookAt(
				100.0f, 50.0f, 100.0f,
				0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f
			);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
}
