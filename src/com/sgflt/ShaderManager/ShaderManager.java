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


import java.util.Map;
import java.util.HashMap;

import org.lwjgl.opengl.GL20;

public enum ShaderManager {
	INSTANCE;

	private Map<String, Shader> shaderMap = new HashMap<String, Shader>();

	/**
	 * Bind the shader to the OpenGL pipeline
	 * 
	 * @param key String key of the shader to be bound.
	 */
	public void bind(String key) {
		shaderMap.get(key).bind();
	}

	/**
	 * Create a shader with the provided shader source.
	 * 
	 * @param key String key that the shader will be referenced by.
	 * @param vertexShaderSource Source of the vertex shader to be compiled into shader.
	 * @param fragmentShaderSource Source of the fragment shader to be compiled into the shader.
	 */
	public void createShader(String key, String vertexShaderSource, String fragmentShaderSource) {
		Shader shader = new Shader.Builder().vertexShaderSource(vertexShaderSource).fragmentShaderSource(fragmentShaderSource)
							.compile().build();
		shaderMap.put(key, shader);
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
		GL20.glUseProgram(0);
	}

}
