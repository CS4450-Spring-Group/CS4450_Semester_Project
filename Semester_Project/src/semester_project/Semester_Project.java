package semester_project;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Max
 * @author sarmi
 * @author jellyj.
 */

public class Semester_Project {
    private FPCameraController camera;
    private DisplayMode displayMode;
    private Cube cube;

    public void start() {
        try {
            createWindow();
            initGL();
            camera = new FPCameraController(0, 0, 0);
            cube = new Cube();
            cube.initVertices();
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
    cube = new Cube();
    cube.initVertices(); // Initialize cube geometry

    while (!Display.isCloseRequested()) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        
        GLU.gluLookAt(3, 3, 8,   // Camera position
                      0, 0, 0,   // Look at center
                      0, 1, 0);  // Up vector
        
        glPushMatrix();
        glRotatef(20, 1, 1, 0); // Rotate for 3D effect
        cube.cube();            // Render cube
        glPopMatrix();

        Display.update();
        Display.sync(60);
    }
}
    public static void main(String[] args) {
        new Semester_Project().start();
    }
}
