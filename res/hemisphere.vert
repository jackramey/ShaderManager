uniform vec3 lightPos;

varying vec4 FinalColor;

void main() {
  vec4 lightIntensity;
    vec3 LightPosition = vec3(lightPos.x, lightPos.y, lightPos.z);
    vec3 ecPosition = vec3(gl_ModelViewMatrix * gl_Vertex);
    vec3 tnorm = normalize(gl_NormalMatrix * gl_Normal);
    vec3 lightVec = normalize(LightPosition - ecPosition);

    float costheta = dot(tnorm, lightVec);
    float a = costheta * 0.5 + 0.5;
    
    //Additive lighting...
//    lightIntensity = clamp(mix(vec4(0,0,0,1), vec4(1,1,1,1), a) + lightIntensity, 0.0, 1.0);
  lightIntensity = mix(vec4(0,0,0,1), vec4(1,1,1,1), a);
  
  FinalColor = lightIntensity * gl_Color;

  gl_Position = ftransform();
}

