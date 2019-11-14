#version 300 es
precision mediump float;
uniform sampler2D sTexture;//纹理内容数据
in vec2 vTextureCoord; //接收从顶点着色器过来的参数
out vec4 fragColor;

void main()
{
   //进行纹理采样
   vec4 texColor=texture(sTexture, vTextureCoord);
   if(texColor.a!=1.0)
        discard;
   fragColor =texColor;
}