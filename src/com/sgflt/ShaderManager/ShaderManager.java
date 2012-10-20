/**
 *	Author: Jack Ramey
 *	File: ShaderManager.java
 * 	
 *  Copyright 2012 Jack Ramey
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.sgflt.ShaderManager;


import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.HashMap;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public enum ShaderManager {
	INSTANCE;

	private Map<String, Shader> shaderMap = new HashMap<String, Shader>();
	
	
	/*
	 * Default OpenGL pipeline. This shader is bound when unbind() is called
	 * and when a shader does not compile correctly.
	 */	
	private Shader defaultShader = new Shader();
	/*
	 * Active shader defaults to default shader. Client can set this value
	 * and it will be set when shaders are bound.
	 */
	private Shader activeShader = defaultShader;

	/**
	 * Bind the shader to the OpenGL pipeline. If the key for the
	 * shader returns null, the default OpenGL shaders are used.
	 * Whichever shader is used is set to be the active shader.
	 * 
	 * @param key String key of the shader to be bound.
	 * @return boolean value that returns true if the provided shader
	 * was bound or false if the provided shader was null.
	 */
	public boolean bind(String key) {
		Shader s = shaderMap.get(key);
		boolean ret = false;
		if(s != null) {
			activeShader = s;
			ret = true;
		} else {
			activeShader = defaultShader;
			ret = false;
		}
		activeShader.bind();
		return ret;
	}

	/**
	 * Bind the active shader to the OpenGL pipeline. If the active shader
	 * is null, the default OpenGL pipeline is used. The default shader
	 * is then set to be the active shader.
	 * 
	 * @return boolean value that returns true if the active shader
	 * was bound or false if the active shader was null.
	 */
	public boolean bind() {
		boolean ret = false;
		if(activeShader != null) {
			ret = true;
		} else {
			activeShader = defaultShader;
			ret = false;
		}
		activeShader.bind();
		return ret;
	}
	
	/**
	 * Bind the default OpenGL shaders. Sets the default shader to be the active shader.
	 */
	public void bindDefault() {
		activeShader = defaultShader;
		defaultShader.bind();
	}

	/**
	 * Create a shader with the provided shader source. The created shader is then stored by the key passed.
	 * 
	 * @param key String key that the shader will be referenced by.
	 * @param vertexShaderSource Source of the vertex shader to be compiled into shader.
	 * @param fragmentShaderSource Source of the fragment shader to be compiled into the shader.
	 * @throws Exception If the vertex shader or fragment shader failed to compile or if the
	 * 						shader program failed to link, an exception will be thrown with a message
	 * 						regarding which component broke.
	 */
	public void createShader(String key, String vertexShaderSource, String fragmentShaderSource) throws IllegalArgumentException {
		Shader shader = defaultShader;
		
		/*
		 * Try to build a new shader. If building the shader fails, an exception is thrown and the default shader
		 * is put into the shader map under the key that was given to the shader.
		 * 
		 */
		shader = new Shader.Builder().vertexShaderSource(vertexShaderSource).fragmentShaderSource(fragmentShaderSource)
							.compile().build();
		//Throw an exception if the fragment shader did not compile properly
		if(!shader.isFragmentShaderCompileStatus()) {
			shader = defaultShader;
			throw new IllegalArgumentException("Fragment shader failed to compile.");
		}
		//Throw an exception if the vertex shader did not compile properly
		if(!shader.isVertexShaderCompileStatus()) {
			shader = defaultShader;
			throw new IllegalArgumentException("Vertex shader failed to compile.");
		}
		//Throw an exception if the shader program did not link properly
		if(!shader.isShaderProgramLinkStatus()) {
			shader = defaultShader;
			throw new IllegalArgumentException("Shader program failed to link shaders.");
		}
		shaderMap.put(key, shader);
	}
	
	/**
	 * Pass a FloatBuffer to the active shader. The shader to which the value will be passed must be bound.
	 * 
	 * @param varName Name of the variable that is in the shader program. Must be an exact match.
	 * @param buf FloatBuffer to be passed to the shader.
	 */
	public void putFloatBuffer(String varName, FloatBuffer buf) {
		if(activeShader != null) {
			//Courtesy of Drew Malin (that dick made me put this in here)
			int location = GL20.glGetUniformLocation(activeShader.shaderProgram, varName); 
			buf.flip();
			GL20.glUniform1(location, buf);
		}
	}
	
	/**
	 * Pass a float to the active shader. The shader to which the value will be passed must be bound.
	 * 
	 * @param varName Name of the variable that is in the shader program. Must be an exact match.
	 * @param x Primitive float to be passed to the shader.
	 */
	public void putFloat(String varName, float x) {
		if(activeShader != null) {
			int location = GL20.glGetUniformLocation(activeShader.shaderProgram, varName);
			GL20.glUniform1f(location, x);
		}
	}
	
	/**
	 * Pass an LWJGL Vector2f to the active shader. The shader to which the value will be passed must be bound.
	 * 
	 * @param varName Name of the variable that is in the shader program. Must be an exact match.
	 * @param v LWJGL Vector2f.
	 */
	public void putVector2f(String varName, Vector2f v) throws NullPointerException {
		if(v == null) {
			throw new NullPointerException("Vector2f passed is null.");
		}
		if(activeShader != null) {
			int location = GL20.glGetUniformLocation(activeShader.shaderProgram, varName);
			GL20.glUniform2f(location, v.x, v.y);
		}
	}
	
	/**
	 * Pass an LWJGL Vector3f to the active shader. The shader to which the value will be passed must be bound.
	 * 
	 * @param varName Name of the variable that is in the shader program. Must be an exact match.
	 * @param v
	 */
	public void putVector3f(String varName, Vector3f v) throws NullPointerException {
		if(v == null) {
			throw new NullPointerException("Vector3f passed is null.");
		}
		if(activeShader != null) {
			int location = GL20.glGetUniformLocation(activeShader.shaderProgram, varName);
			GL20.glUniform3f(location, v.x, v.y, v.z);
		}
	}
	
	/**
	 * Pass an LWJGL Vector4f to the active shader. The shader to which the value will be passed must be bound.
	 * 
	 * @param varName  Name of the variable that is in the shader program. Must be an exact match.
	 * @param v
	 */
	public void putVector4f(String varName, Vector4f v) throws NullPointerException {
		if(v == null) {
			throw new NullPointerException("Vector4f passed is null.");
		}
		if(activeShader != null) {
			int location = GL20.glGetUniformLocation(activeShader.shaderProgram, varName);
			GL20.glUniform4f(location, v.x, v.y, v.z, v.w);
		}
	}
	
	/**
	 * Pass an IntBuffer to the active shader. The shader to which the value will be passed must be bound.
	 * 
	 * @param varName String name of the variable that is in the shader program. Must be an exact match.
	 * @param buf IntBuffer to be passed to the shader.
	 */
	public void putIntBuffer(String varName, IntBuffer buf) {
		if(activeShader != null) {
			int location = GL20.glGetUniformLocation(activeShader.shaderProgram, varName);
			buf.flip();
			GL20.glUniform1(location, buf);
		}
	}

	/**
	 * Pass an integer to the active shader. The shader to which the value will be passed must be bound.
	 * 
	 * @param varName String name of the variable that is in the shader program. Must be an exact match.
	 * @param x Primitive integer to be passed to the shader.
	 */
	public void putInt(String varName, int x) {
		if(activeShader != null) {
			//Grab the location of the variable
			int location = GL20.glGetUniformLocation(activeShader.shaderProgram, varName);
			//Pass the value to the variable location
			GL20.glUniform1i(location, x);
		}
	}
	
	/**
	 * Set the active shader to the shader bound to the key provided. If no shader
	 * exists at this location, return false.
	 * 
	 * @param key Name the shader is registered with.
	 * @return boolean - returns true if a value is at that location or false if it's null
	 */
	public boolean setActiveShader(String key) {
		activeShader = shaderMap.get(key);
		return (activeShader != null);
	}
	
	/**
	 * Get the singleton instance of the ShaderManager
	 * 
	 * @return singleton instance of ShaderManager
	 */
	public static ShaderManager getInstance() {
		return INSTANCE;
	}	
	
	/**
	 * Unbind any shader program currently in use. This sets to use the
	 * default OpenGL pipeline.
	 * 
	 */
	public void unbind() {
		//Set OpenGL to use the default pipeline.
		defaultShader.bind();
	}

}
