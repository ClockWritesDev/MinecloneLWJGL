package com.clockwrites.MineClone;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class MineEngine {
    private final long window;
    double firstFrame;
    double deltaTime;
    Renderer renderer = new Renderer();
    ShaderProgram triangle;

    public MineEngine(int screenWidth, int screenHeight, String WindowTitle) {
        GLFWErrorCallback.createPrint(System.err).set();
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        this.window = glfwCreateWindow(screenWidth, screenHeight, WindowTitle, NULL, NULL);
        this.firstFrame = glfwGetTime();

        if ( this.window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(this.window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    this.window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwSetKeyCallback(this.window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        glfwMakeContextCurrent(this.window);
        glfwSwapInterval(1);
        glfwShowWindow(this.window);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
    }

    public void update() {
        while ( !glfwWindowShouldClose(this.window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            double currentFrameTime = GLFW.glfwGetTime();

            this.deltaTime = currentFrameTime - this.firstFrame;
            this.firstFrame = currentFrameTime;
            //System.out.println(this.deltaTime);
            this.render();
            glfwSwapBuffers(this.window);
            glfwPollEvents();
        }
    }

    public void render() {
        this.triangle.useProgram(
            ()-> {
                glBegin(GL_TRIANGLES);
                    glColor3f(1, 0, 0);
                    glVertex2f(-0.5f, -0.5f);
                    glColor3f(0, 1, 0);
                    glVertex2f(0.5f, -0.5f);
                    glColor3f(0, 0, 1);
                    glVertex2f(0.5f, 0.5f);
                glEnd();
                glBegin(GL_TRIANGLES);
                    glColor3f(1, 0, 0);
                    glVertex2f(-0.5f, -0.5f);
                    glColor3f(0, 0, 1);
                    glVertex2f(0.5f, 0.5f);
                    glColor3f(1, 0, 1);
                    glVertex2f(-0.5f, 0.5f);
                glEnd();
            }
        );
    }

    public boolean isKeyPressed(int keycode) {
        return GLFW.glfwGetKey(this.window, keycode) == GLFW_PRESS;
    }

    public void cleanUp() {
        glfwFreeCallbacks(this.window);
        glfwDestroyWindow(this.window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
        this.triangle.deleteProgram();
    }

    public void run() {
        this.renderer.init();
        this.triangle = new ShaderProgram("cubeshader");
        this.update();
        this.cleanUp();
    }
}