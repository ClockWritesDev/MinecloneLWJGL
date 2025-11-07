package com.clockwrites.MineClone;

import com.clockwrites.MineClone.meshes.CubeMesh;
import org.joml.Matrix4f;
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
    public int width;
    public int height;
    public String title;
    double firstFrame;
    double deltaTime;
    Renderer renderer = new Renderer(this);
    CubeMesh cubetest;
    public Matrix4f projectionMatrix;

    public MineEngine(int screenWidth, int screenHeight, String WindowTitle) {
        this.width = screenWidth;
        this.height = screenHeight;
        this.title = WindowTitle;

        GLFWErrorCallback.createPrint(System.err).set();
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        this.window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
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
            this.renderer.render(
                ()->{
                    this.cubetest.render();
                }
            );
            glfwSwapBuffers(this.window);
            glfwPollEvents();
        }
    }

    public boolean isKeyPressed(int keycode) {
        return GLFW.glfwGetKey(this.window, keycode) == GLFW_PRESS;
    }

    public void cleanUp() {
        glfwFreeCallbacks(this.window);
        glfwDestroyWindow(this.window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
        this.cubetest.cleanup();
    }

    public void run() {
        this.renderer.init();
        this.cubetest = new CubeMesh();
        this.update();
        this.cleanUp();
    }

    public int[] getScreenSize() {
        int[] size = new int[] {this.width, this.height};
        return size;
    }
}