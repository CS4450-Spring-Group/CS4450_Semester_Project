/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cs4450_semester_project;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.Sys;

public class CS4450_Semester_Project {


    
    private float x, y, z;
    private float yaw, pitch;
    private final float speed = 0.1f; //initial speed for the camera
    
    private void handleInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) z -= speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) z += speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) x -= speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) x += speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) y += speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) y -= speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) System.exit(0);

        yaw += Mouse.getDX() * 0.1f; //mouse rotation
        pitch -= Mouse.getDY() * 0.1f; //mouse rotation
    }
    
    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glTranslatef(x, y, z);
        glRotatef(yaw, 0, 1, 0);
        glRotatef(pitch, 1, 0, 0);
    }
    
    public static void main(String[] args) {
        
    }   
}
