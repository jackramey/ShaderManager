/**
 *	Author: Jack Ramey
 *	File: Shader.java
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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

class Shader {

	//Handle for the shader program. Set to Package-Private
	final int shaderProgram;
	//Handles for the shaders
	private final int fragmentShader;
	private final int vertexShader;
	//Strings to hold the shader code that will be compiled
	private final String fragmentShaderSource;
	private final String vertexShaderSource;
	//Shader status variables
	private final boolean fragmentShaderCompileStatus;
	private final boolean vertexShaderCompileStatus;
	//Shader program link status
	private final boolean shaderProgramLinkStatus;

	static class Builder {
		//Handle for the shader program
		private final int shaderProgram;
		//Handles for the shaders
		private final int fragmentShader;
		private final int vertexShader;
		//Strings to hold the shader code that will be compiled
		private String fragmentShaderSource;
		private String vertexShaderSource;
		//Shader status variables
		private boolean fragmentShaderCompileStatus;
		private boolean vertexShaderCompileStatus;
		//Shader program link status
		private boolean shaderProgramLinkStatus;
		
		/**
		 * Shader internal builder class. Used to stage the data
		 * and create an immutable shader object in one step.
		 */
		Builder() {
			//Initialize the shader program and vertex shader and fragment shader
			shaderProgram = GL20.glCreateProgram();
			vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
			fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		}

		/**
		 * Set the Fragment Shader source code.
		 * 
		 * @param src String containing the fragment shader source.
		 * @return Builder object to allow for a single line instantiation.
		 */
		Builder fragmentShaderSource(String src) {
			fragmentShaderSource = src;
			return this;
		}

		/**
		 * Set the Vertex Shader source code.
		 * 
		 * @param src String containing the vertex shader source.
		 * @return Builder object to allow for a single line instantiation.
		 */
		Builder vertexShaderSource(String src) {
			vertexShaderSource = src;
			return this;
		}
		
		/**
		 * Create the Shader object
		 * 
		 * @return Immutable shader object.
		 */
		Shader build() {
			return new Shader(this);
		}
		
		/**
		 * Load and link the Shaders
		 * 
		 * @return Builder object to allow for a single line instantiation.
		 */
		Builder compile() {
			loadShaders();
			linkShaders();

			return this;
		}
		
		/**
		 * Attach the shaders to the program and then link and validate.
		 * 
		 * @return Builder object to allow for a single line instantiation.
		 */
		Builder linkShaders() {
			//Attach the shader to the shader program
			GL20.glAttachShader(shaderProgram, vertexShader);
			GL20.glAttachShader(shaderProgram, fragmentShader);
			//Link the shader program (I have no idea what this does).
			GL20.glLinkProgram(shaderProgram);
	        //Validate the shader program
			GL20.glValidateProgram(shaderProgram);
			//Check to see if the shader program was linked correctly
			shaderProgramLinkStatus = (GL20.glGetProgram(shaderProgram, GL20.GL_LINK_STATUS) == GL11.GL_TRUE);
			
			return this;
		}
		
		/**
		 * Load the shader source code and compile the frag and vert shaders
		 * 
		 * @return Builder object to allow for a single line instantiation.
		 */
		Builder loadShaders() {
			//Fragment shader...
			//Attach the shader source to the shader
	        GL20.glShaderSource(fragmentShader, fragmentShaderSource);
	        //Compile the shader
	        GL20.glCompileShader(fragmentShader);
	        //Check to see if the fragment shader compiled correctly
	        fragmentShaderCompileStatus =(GL20.glGetShader(fragmentShader, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE);
	        //Vertex Shader...
	        //Attach the shader source to the shader
	        GL20.glShaderSource(vertexShader, vertexShaderSource);
	        //Compile the shader
	        GL20.glCompileShader(vertexShader);
	        //Check to see if the vertex shader compiled correctly
	        vertexShaderCompileStatus =(GL20.glGetShader(vertexShader, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE);
	        
	        return this;
		}
		
	}//End Builder class
	
	/**
	 * Constructor for the Shader.
	 * 
	 * @param builder Builder object that has been staged for Shader instantiation.
	 */
	private Shader(Builder builder) {
		this.fragmentShaderSource = builder.fragmentShaderSource;
		this.vertexShaderSource = builder.vertexShaderSource;
		this.fragmentShader = builder.fragmentShader;
		this.vertexShader = builder.vertexShader;
		this.shaderProgram = builder.shaderProgram;
		this.fragmentShaderCompileStatus = builder.fragmentShaderCompileStatus;
		this.vertexShaderCompileStatus = builder.vertexShaderCompileStatus;
		this.shaderProgramLinkStatus = builder.shaderProgramLinkStatus;
	}
	
	/**
	 * Constructor to "build" the default OpenGL shader program. Just sets 
	 * shaderProgram to 0 and everything else to Null strings.
	 */
	Shader() {
		this.shaderProgram = 0;
		this.fragmentShaderSource = "";
		this.vertexShaderSource = "";
		this.fragmentShader = 0;
		this.vertexShader = 0;
		this.fragmentShaderCompileStatus = true;
		this.vertexShaderCompileStatus = true;
		this.shaderProgramLinkStatus = true;
	}
	
	/**
	 * Bind the Shader Program to the OpenGL pipeline.
	 * 
	 */
	public void bind() {
		GL20.glUseProgram(shaderProgram);
	}

	//GETTERS
	int getShaderProgram() {
		return shaderProgram;
	}

	int getFragmentShader() {
		return fragmentShader;
	}

	int getVertexShader() {
		return vertexShader;
	}

	String getFragmentShaderSource() {
		return fragmentShaderSource;
	}

	String getVertexShaderSource() {
		return vertexShaderSource;
	}

	boolean isFragmentShaderCompileStatus() {
		return fragmentShaderCompileStatus;
	}

	boolean isVertexShaderCompileStatus() {
		return vertexShaderCompileStatus;
	}

	boolean isShaderProgramLinkStatus() {
		return shaderProgramLinkStatus;
	}
	
	
}
