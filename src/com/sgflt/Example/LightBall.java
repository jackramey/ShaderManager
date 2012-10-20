package com.sgflt.Example;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;

public class LightBall {
	Vector3f position;
	Sphere s;
	
	public LightBall() {
		position = new Vector3f();
		s = new Sphere();
	}
	
	public void draw() {
		GL11.glPushMatrix();
		
		GL11.glTranslatef(position.x, position.y, position.z);
		s.draw(1.0f, 20, 20);
		
		GL11.glPopMatrix();
	}
	
	public void move(Vector3f moveDir) {
		Vector3f.add(position, moveDir, position);
	}
}
