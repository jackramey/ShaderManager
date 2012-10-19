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

public enum ShaderManager {
	INSTANCE;

	private Map<String, Shader> shaderMap = new HashMap<String, Shader>();
	
	/*TODO: Set up an active shader to make passing parameters to shaders
	 * 	less tedious. This would involve a call close to setActive(shaderKey)
	 */
	private Shader activeShader;
	
	/*
	 * Default OpenGL pipeline. This shader is bound when unbind() is called
	 * and when a shader does not compile correctly.
	 */	
	private Shader defaultShader = new Shader();

	/**
	 * Bind the shader to the OpenGL pipeline. If the key for the
	 * shader returns null, the default OpenGL shaders are used.
	 * 
	 * @param key String key of the shader to be bound.
	 * @return boolean value that returns true if the provided shader
	 * was bound or false if the provided shader was null.
	 */
	public boolean bind(String key) {
		Shader s = shaderMap.get(key);
		boolean ret = false;
		if(s != null) {
			s.bind();
			ret = true;
		} else {
			bindDefault();
			ret = false;
		}
		return ret;
	}

	/**
	 * Bind the active shader to the OpenGL pipeline. If the active shader
	 * is null, the default OpenGL pipeline is used.
	 * 
	 * @return boolean value that returns true if the active shader
	 * was bound or false if the active shader was null.
	 */
	public boolean bind() {
		boolean ret = false;
		if(activeShader != null) {
			activeShader.bind();
			ret = true;
		} else {
			bindDefault();
			ret = false;
		}
		return ret;
	}
	
	/**
	 * Bind the default OpenGL shaders
	 */
	public void bindDefault() {
		defaultShader.bind();
	}

	/**
	 * Create a shader with the provided shader source.
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
		 */
		try{
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
		} finally {
			shaderMap.put(key, shader);
		}
	}
	
	/**
	 * Pass a FloatBuffer to the shader.
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
	 * Pass an IntBuffer to the shader.
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
