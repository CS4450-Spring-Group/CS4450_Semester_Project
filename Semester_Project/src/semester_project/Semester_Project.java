package semester_project;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Mouse;

public class Semester_Project {
    private FPCameraController camera;
    private DisplayMode displayMode;

    public void start() {
        try {
            createWindow();
            initGL();
            camera = new FPCameraController(0, 0, 0);
            Mouse.setGrabbed(true);
            gameLoop();
            Display.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createWindow() throws Exception {
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for (DisplayMode mode : d) {
            if (mode.getWidth() == 640 && mode.getHeight() == 480 && mode.getBitsPerPixel() == 32) {
                displayMode = mode;
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("CS4450 Semester_Project");
        Display.create();
    }
    
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth() / (float)displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }
    
    private void gameLoop() {
        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            
            if (!Mouse.isGrabbed()) {
                Mouse.setGrabbed(true);
            }
            
            float mouseDX = Mouse.getDX() * 0.1f;
            float mouseDY = Mouse.getDY() * 0.1f;
            camera.yaw(mouseDX);
            camera.pitch(mouseDY);
            
            camera.gameLoop();
            drawCube();
            
            Display.update();
            Display.sync(60);
        }
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
        new Semester_Project().start();
    }
}
