package com.clockwrites.MineClone.meshes;

import com.clockwrites.MineClone.ShaderProgram;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.memFree;

public class CubeMesh implements IMesh {
    private int vbo;
    private int vao;
    private int idxVbo;
    private int colorVbo;
    public final int vertexCount;
    private ShaderProgram sprogram;
    public Map<String, Object> attributes;
    private FloatBuffer verticesBuffer;
    private IntBuffer indicesBuffer;
    private FloatBuffer colorBuffer;
    private float[] vertices = new float[]{
            0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
    };
    private int[] indices = new int[]{
            0, 1, 3,
            3, 1, 2,
    };
    private float[] colors = new float[]{
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 1.0f,
    };

    public CubeMesh() {
        this.verticesBuffer = null;
        this.sprogram = new ShaderProgram("cubeshader");
        try {
            this.verticesBuffer = MemoryUtil.memAllocFloat(this.vertices.length);
            this.indicesBuffer = MemoryUtil.memAllocInt(this.indices.length);
            this.colorBuffer = MemoryUtil.memAllocFloat(colors.length);

            this.vertexCount = this.indices.length;

            this.verticesBuffer.put(this.vertices).flip();
            this.indicesBuffer.put(this.indices).flip();
            this.colorBuffer.put(this.colors).flip();

            this.idxVbo = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,  this.idxVbo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indicesBuffer, GL_STATIC_DRAW);

            this.colorVbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, this.colorVbo);
            glBufferData(GL_ARRAY_BUFFER, this.colorBuffer, GL_STATIC_DRAW);

            this.vao = glGenVertexArrays();
            glBindVertexArray(this.vao);

            this.vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindVertexArray(0);
        } finally {
            memFree(this.verticesBuffer);
            memFree(this.indicesBuffer);
            memFree(this.colorBuffer);
        }
    }
    public void render() {
        this.sprogram.useProgram(
            () -> {
                glBindVertexArray(this.vao);
                glEnableVertexAttribArray(0);

                glEnableVertexAttribArray(0);
                glEnableVertexAttribArray(1);
                glDrawArrays(GL_TRIANGLES, 0, this.vertexCount);

                glDisableVertexAttribArray(0);
                glBindVertexArray(0);
            }
        );
    }

    @Override
    public void update(float deltatime) {

    }

    public void cleanup() {
        if (this.sprogram != null) {
            this.sprogram.deleteProgram();
        }
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(this.vbo);
        glDeleteBuffers(this.idxVbo);

        glBindVertexArray(0);
        glDeleteVertexArrays(this.vao);

    }
}

