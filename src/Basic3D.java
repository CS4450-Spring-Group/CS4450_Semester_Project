/***************************************************************
* file: Program1.java
* author: Juan Sarmiento
* class: CS 4450.01 Computer Graphics
*
* assignment: program 1
* date last modified: 2/8/2025
*
* purpose: This program reads from the file the specifications to draw primitives
*
****************************************************************/
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;

public class Basic3D {
    private FPCameraController fp = new FPCameraController(0f,0f,0f);
    private DisplayMode displayMode;
    
    public void start() {
        try {
            createWindow();
            initGL();
            fp.gameLoop();//render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640 && d[i].getHeight() == 480 && d[i].getBitsPerPixel() == 32) {
                displayMode = d[i];
                break;
            }
        }
        
        if (displayMode == null) {
            displayMode = Display.getDesktopDisplayMode(); // fallback to default mode
        }
        
        Display.setDisplayMode(displayMode);
        Display.setTitle("Hey Mom! I am using" + "OpenGL!!!");
        Display.create();
    }
    
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth()/(float)
        displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    public static void main(String[] args) {
        Basic3D basic = new Basic3D();
        basic.start();
    }   
}