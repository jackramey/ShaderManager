package com.sgflt.Example;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import com.sgflt.ShaderManager.ShaderManager;

public class Main {
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 640;

	private static Sphere s;
	private static ShaderManager SM;
	
	private static FloatBuffer fb;
	
	public static void main(String[] args) {
		/*
		 * Initialize ALL THE THINGS!
		 */
		initDisplay();
		initGL();
		initScene();

		/*
		 * ShaderManager is a singleton, so there is no need
		 * to instantiate the ShaderManager, as it will do it
		 * for you! Just use getInstance()
		 */
		SM = ShaderManager.getInstance();

		/*
		 * Do stuff to create the shaders! Check this part out
		 * as it concerns you! It makes shaders! And stuff!
		 */
		createShaders();
		
		/*
		 * Draw loop! Check out the draw() function for how to bind a shader.
		 */
		drawloop();
	}
			
	private static void createShaders() {
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
	
	private static FloatBuffer calcFloatBuf() {
		
		FloatBuffer buf = BufferUtils.createFloatBuffer(3);
		buf.put(0.0f).put(100.0f).put(0.0f);
		return buf;
	}
	
	private static void draw() {
		GL11.glPushMatrix();
		GL11.glLoadIdentity();

		drawAxes(10);
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		SM.setActiveShader("hemi");
		SM.bind();
		SM.putFloatBuffer("buf", fb);
		s.draw(25.0f, 50, 50);
		SM.unbind();

		GL11.glPopMatrix();
	}
	
	private static void drawloop() {
		while(!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			draw();
			Display.update();
		}
		Display.destroy();
	}

////////////////////////////////////////////////////////////////////////////////
//////////////////////////////Display Methods///////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
	public static void resizeScreen(int width, int height) {
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

////////////////////////////////////////////////////////////////////////////////
//////////////////////////////Initializers//////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
	
	private static void initDisplay() {
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

	private static void initGL() {
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f,0.0f,0.0f,0.0f); //Black Background
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}

	private static void initScene() {
		s = new Sphere();
		fb = calcFloatBuf();
	}

////////////////////////////////////////////////////////////////////////////////
////////////////////////////Some simple draw functions//////////////////////////
////////////////////////////////////////////////////////////////////////////////

	public static void drawAxes(float scale) {
		GL11.glBegin(GL11.GL_LINES); {
			GL11.glColor3f(1.0f,0.0f,0.0f);
			GL11.glVertex3f(0.0f,0.0f,0.0f);
			GL11.glVertex3f(scale,0.0f,0.0f);
			GL11.glColor3f(0.0f,1.0f,0.0f);
			GL11.glVertex3f(0.0f,0.0f,0.0f);
			GL11.glVertex3f(0.0f,scale,0.0f);
			GL11.glColor3f(0.0f,0.0f,1.0f);
			GL11.glVertex3f(0.0f,0.0f,0.0f);
			GL11.glVertex3f(0.0f,0.0f,scale);
		} GL11.glEnd();
	}	

	public static void drawFloor(int size) {
		int end = size/2;
		for(int x = -size/2; x < end; x++) {
			for(int z = -size/2; z < end; z++) {
				GL11.glPushMatrix();
				GL11.glLoadIdentity();
				GL11.glTranslatef((float) x, 0.0f, (float) z);
				GL11.glColor3f(1.0f,1.0f,1.0f);
				GL11.glBegin(GL11.GL_QUADS); {
					GL11.glNormal3f(0.0f,1.0f,0.0f);
					GL11.glVertex3f(-1.0f, 0.0f, -1.0f);
					GL11.glNormal3f(0.0f,1.0f,0.0f);
					GL11.glVertex3f(1.0f, 0.0f, -1.0f);
					GL11.glNormal3f(0.0f,1.0f,0.0f);
					GL11.glVertex3f(1.0f, 0.0f, 1.0f);
					GL11.glNormal3f(0.0f,1.0f,0.0f);
					GL11.glVertex3f(-1.0f, 0.0f, 1.0f);
				} GL11.glEnd();
				GL11.glPopMatrix();
			}
		}
	}


}
