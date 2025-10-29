package com.clockwrites.MineClone;

import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    public void init() {
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH);
        glEnable(GL_BLEND);
        glEnable(GL_CULL_FACE);
    }
    public void setClearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }
}
