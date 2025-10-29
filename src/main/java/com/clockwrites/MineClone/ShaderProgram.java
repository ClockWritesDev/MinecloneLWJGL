package com.clockwrites.MineClone;

import static org.lwjgl.opengl.GL30.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

@FunctionalInterface
interface Unit {
    void execute();
}

public class ShaderProgram {
    int sProgram;
    int vertex;
    int fragment;

    public ShaderProgram(String shaderName) {
        this.sProgram = createProgram(shaderName);

    }

    public int generateVertex() {

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
        v = glCreateShader(GL_VERTEX_SHADER);
        f = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(v, vertex_shader);
        glCompileShader(v);
        if (glGetShaderi(v, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Vertex shader wasn't able to be compiled correctly.");
        }

        glShaderSource(fragment, fragment_shader);
        glCompileShader(fragment);
        if (glGetShaderi(fragment, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Vertex shader wasn't able to be compiled correctly.");
        }

        glAttachShader(sProgram, vertex);
        glAttachShader(sProgram, fragment);
        glLinkProgram(sProgram);
        glValidateProgram(sProgram);

        return sProgram;
    }

    public void useProgram(Unit block) {
        glUseProgram(this.sProgram);
        block.execute();
        glUseProgram(0);
    }

    public void deleteProgram() {
        glDeleteShader(vertex);
        glDeleteShader(fragment);
        glDeleteProgram(sProgram);
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
