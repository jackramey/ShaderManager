package com.sgflt.Example;

import org.lwjgl.opengl.GL11;

public class DrawUtility {

	private DrawUtility() {
		//Static utility class should never be instantiated!
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
