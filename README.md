ShaderManager
=============

The ShaderManager is a wrapper for a small part of LWJGL.
The goal is to allow a user to easily create and manage shaders for any Java OpenGL project.

Initializing the ShaderManager:
ShaderManager is a singleton class and it's easy to get a hold of this instance by the static factory method
getInstance().

Example:
```java
ShaderManager sm = ShaderManager.getInstance();
```

Piece. Of. Cake.

There is only one small caveat. LWJGL requires you to have created a Display window before initialization. So do that!
ShaderManager handles all the rest for you.

Now lets get to the important part and make a shader. We'll be using the most trivial of shaders for this example.


```java
//We need to have a handle to get the shader back by, so we'll define this String name
//as a simple way to store it.
String shaderName = "trivial";

//Now we need to define the shader source code. This can be read in from a file or defined in the
//code and stored as a string. The important thing is that the ShaderManager requires a single
//String containing all the source code.

//Trivial Vertex shader
String vShaderSource =  "void main(void) {" +
                        "gl_Position = ftransform();" +
                        "}";

//Trivial Fragment shader
String fShaderSource =  "void main(void) {" +
                        "gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);" +
                        "}";

//Finally, let's create the shader by using the ShaderManager createShader method.
ShaderManager.createShader(shaderName, vShaderSource, fShaderSource);

```

ShaderManager is designed to make it easy to create and access shaders that you need for your project.

The most interesting shaders use values determined dynamically at runtime. So we need to have a way to pass
these values to the shader. Currently ShaderManager provides the following methods to pass Uniform values.

```java
putFloatBuffer(String name, FloatBuffer buf);
putFloat(String name, float f);
putVector2f(String name, Vector2f vec);
putVector3f(String name, Vector3f vec);
putVector4f(String name, Vector4f vec);
putIntBuffer(String name, IntBuffer buf);
putInt(String name, int i);
```


