/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.Sys;

/**
 *
 * @author Max
 */

public class CS4450_Semester_Project {
 
    private float x, y, z;
    private float yaw, pitch;
    private float speed = 0.1f;
    
    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(640, 480));
            Display.setTitle("Minecraft-Style Scene");
            Display.create();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-5, 5, -5, 5, -5, 5);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_DEPTH_TEST);

        while (!Display.isCloseRequested()) {
            handleInput();
            render();
            Display.update();
            Display.sync(60);
        }

        Display.destroy();
    }

    private void handleInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) z -= speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) z += speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) x -= speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) x += speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) y += speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) y -= speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) System.exit(0);

        yaw += Mouse.getDX() * 0.1f;
        pitch -= Mouse.getDY() * 0.1f;
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glTranslatef(x, y, z);
        glRotatef(yaw, 0, 1, 0);
        glRotatef(pitch, 1, 0, 0);
        drawCube();
    }

    private void drawCube() {
        glBegin(GL_QUADS);
        
        // Front Face (red)
        glColor3f(1, 0, 0);
        glVertex3f(-1, -1, 1);
        glVertex3f(1, -1, 1);
        glVertex3f(1, 1, 1);
        glVertex3f(-1, 1, 1);
        
        // Back Face (green)
        glColor3f(0, 1, 0);
        glVertex3f(-1, -1, -1);
        glVertex3f(-1, 1, -1);
        glVertex3f(1, 1, -1);
        glVertex3f(1, -1, -1);
        
        // Top Face (blue)
        glColor3f(0, 0, 1);
        glVertex3f(-1, 1, -1);
        glVertex3f(-1, 1, 1);
        glVertex3f(1, 1, 1);
        glVertex3f(1, 1, -1);
        
        // Bottom Face (yellow)
        glColor3f(1, 1, 0);
        glVertex3f(-1, -1, -1);
        glVertex3f(1, -1, -1);
        glVertex3f(1, -1, 1);
        glVertex3f(-1, -1, 1);
        
        // Right Face (magenta)
        glColor3f(1, 0, 1);
        glVertex3f(1, -1, -1);
        glVertex3f(1, 1, -1);
        glVertex3f(1, 1, 1);
        glVertex3f(1, -1, 1);
        
        // Left Face (cyan)
        glColor3f(0, 1, 1);
        glVertex3f(-1, -1, -1);
        glVertex3f(-1, -1, 1);
        glVertex3f(-1, 1, 1);
        glVertex3f(-1, 1, -1);
        
        glEnd();
    }
    
    public static void main(String[] args) {
        new CS4450_Semester_Project().start();
    }
}
