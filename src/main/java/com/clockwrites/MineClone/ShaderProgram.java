package com.clockwrites.MineClone;

import static org.lwjgl.opengl.GL30.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ShaderProgram {
    int sProgram;
    int vertex;
    int fragment;

    public ShaderProgram(String shaderName) {
        this.vertex = 0;
        this.fragment = 0;
        this.sProgram = createProgram(shaderName);
    }

    public int createProgram(String shaderName) {
        StringBuilder vertex_shader = new StringBuilder();
        StringBuilder fragment_shader = new StringBuilder();

        try {
            vertex_shader = getShader(shaderName+".vert");
            fragment_shader = getShader(shaderName+".frag");
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] error ao criar o programa de shaders");
            throw new RuntimeException(e);
        }

        int shaderP = glCreateProgram();
        this.vertex = glCreateShader(GL_VERTEX_SHADER);
        this.fragment = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(this.vertex, vertex_shader);
        glCompileShader(this.vertex);
        if (glGetShaderi(this.vertex, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Vertex shader wasn't able to be compiled correctly.");
        }

        glShaderSource(this.fragment, fragment_shader);
        glCompileShader(this.fragment);
        if (glGetShaderi(this.fragment, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Vertex shader wasn't able to be compiled correctly.");
        }

        glAttachShader(shaderP, this.vertex);
        glAttachShader(shaderP, this.fragment);
        glLinkProgram(shaderP);
        glValidateProgram(shaderP);

        return shaderP;
    }

    public void useProgram(Unit block) {
        glUseProgram(this.sProgram);
        block.execute();
        glUseProgram(0);
    }

    public void deleteProgram() {
        glDeleteShader(this.vertex);
        glDeleteShader(this.fragment);
        glDeleteProgram(this.sProgram);
    }

    public StringBuilder getShader(String path) throws FileNotFoundException {
        StringBuilder shader = new StringBuilder();
        Scanner source = new Scanner(new FileReader("src/main/resources/shaders/"+path));

        while (source.hasNext()) {
            shader.append(source.nextLine()).append('\n');
        }
        return shader;
    }
}
