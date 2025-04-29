
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.BufferUtils;

/**
 *
 * @author jellyj. sarmi, Max
 */

public class Basic3D {
    private FPCameraController fp;
    private DisplayMode displayMode;
    
    public void start() {
        try {
            createWindow();
            initGL();
            fp = new FPCameraController(0f,0f,0f);
            fp.gameLoop();//render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private java.nio.FloatBuffer asFloatBuffer(float[] values) {
        java.nio.FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();
        return buffer;
    }
    
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for (DisplayMode d1 : d) {
            if (d1.getWidth() == 640 && d1.getHeight() == 480 && d1.getBitsPerPixel() == 32) {
                displayMode = d1;
                break;
            }
        }
        
        if (displayMode == null) {
            displayMode = new DisplayMode(640, 480); // fallback if null
        }
        
        Display.setDisplayMode(displayMode);
        Display.setTitle("Minecraft-Style Scene");
        Display.create();
    }
    
    private void initGL() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_DEPTH_TEST);
        // Lighting setup
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_COLOR_MATERIAL);

        float[] ambientLight = { 0.3f, 0.3f, 0.3f, 1.0f };
        float[] diffuseLight = { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] lightPosition = { 0.5f, 1.0f, 0.5f, 0.0f }; // Directional light

        glLight(GL_LIGHT0, GL_AMBIENT, asFloatBuffer(ambientLight));
        glLight(GL_LIGHT0, GL_DIFFUSE, asFloatBuffer(diffuseLight));
        glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(lightPosition));

        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        
        glClearColor(0.53f, 0.81f, 0.92f, 1.0f); // sky blue
        
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