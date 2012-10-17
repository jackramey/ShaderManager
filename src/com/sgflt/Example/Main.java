package com.sgflt.Example;

import org.lwjgl.opengl.GL11;
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
	
	public static void main(String[] args) {
		System.out.println("Initializing...");
		initDisplay();
		System.out.println("initDisplay complete");
		initGL();
		System.out.println("initGL complete");
		initScene();
		System.out.println("initScene complete");
		
		SM = ShaderManager.getInstance();

		createShaders();
		
		drawloop();
	}
	
	private static void drawloop() {
		while(!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			draw();
			Display.update();
		}
		Display.destroy();
	}

	private static void draw() {
		GL11.glPushMatrix();
		GL11.glLoadIdentity();

		drawAxes(10);
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		SM.bind("hemi");
		s.draw(25.0f, 50, 50);
		SM.unbind();

		GL11.glPopMatrix();
	}

	private static void initScene() {
		s = new Sphere();
	}

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

	private static void createShaders() {
		String fragShaderSource = 	"varying vec4 FinalColor;" +
									"void main()" +
									"{" +
									"gl_FragColor = FinalColor;" +
									"}";
		String vertShaderSource =	"varying vec4 FinalColor;" +
									"void main() {  " +
								 	"vec3 LightPosition = vec3(0, 100, 0);" +
									"vec3 ecPosition = vec3(gl_ModelViewMatrix * gl_Vertex);" +
									"vec3 tnorm = normalize(gl_NormalMatrix * gl_Normal);" +
									"vec3 lightVec = normalize(LightPosition - ecPosition);" +
									"float costheta = dot(tnorm, lightVec);" +
									"float a = costheta * 0.5 + 0.5;" +
									"FinalColor = mix(vec4(0,0,0,1), vec4(1,1,1,1), a) * gl_Color;" +
									"gl_Position = ftransform();" +
									"}";
		
		SM.createShader("hemi", vertShaderSource, fragShaderSource);

	}

}
