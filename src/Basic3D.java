
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
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
    private float timeOfDay = 0.0f;
    private float daySpeed = 0.005f; // Smaller = slower transition
    private List<float[]> stars = new ArrayList<>();
    private final int STAR_COUNT = 100;
    private FloatBuffer whiteLight;
    
    public void start() {
        try {
            createWindow();
            initGL();
            fp = new FPCameraController(0f,0f,0f);
            fp.setBasic3D(this);
            fp.gameLoop();//render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
    * Sky night cycle
    */
    public void updateDayNightCycle() {
        timeOfDay += daySpeed;
        if (timeOfDay > 2.0f) {
            timeOfDay -= 2.0f;
        }

        float brightness = Math.abs((timeOfDay % 2.0f) - 1.0f);
        float r = 0.5f + 0.5f * brightness;
        float g = 0.5f + 0.5f * brightness;
        float b = 1.0f;

        whiteLight.clear();
        whiteLight.put(r).put(g).put(b).put(0.0f).flip();

        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);
        
        // Adjust sky color based on time of day
        float skyR = 0.1f + 0.4f * brightness;
        float skyG = 0.1f + 0.4f * brightness;
        float skyB = 0.3f + 0.5f * brightness;

        glClearColor(skyR, skyG, skyB, 1.0f);
    }
    
    /**
    * Draws the twinkling stars against the sky.
    */
    public void renderStars() {
        float brightness = 1.0f - Math.abs((timeOfDay % 2.0f) - 1.0f);
        if (brightness < 0.2f) return;

        glDisable(GL_LIGHTING);
        glColor4f(1.0f, 1.0f, 1.0f, brightness);

        glPointSize(2.0f);
        glBegin(GL_POINTS);
        for (float[] star : stars) {
            glVertex3f(star[0], star[1], star[2]);
        }
        glEnd();
        glEnable(GL_LIGHTING);
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
            
        //remove the flower cube white backgound
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.1f);

        float[] ambientLight = { 0.3f, 0.3f, 0.3f, 1.0f };
        float[] diffuseLight = { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] lightPosition = { 0.5f, 1.0f, 0.5f, 0.0f }; // Directional light

        glLight(GL_LIGHT0, GL_AMBIENT, asFloatBuffer(ambientLight));
        glLight(GL_LIGHT0, GL_DIFFUSE, asFloatBuffer(diffuseLight));
        glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(lightPosition));

        glEnable(GL_TEXTURE_2D);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        
        //glClearColor(0.53f, 0.81f, 0.92f, 1.0f); // sky blue
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth()/(float)
            displayMode.getHeight(), 0.1f, 300.0f);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        // Initialize light color
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();

        // Optional: light position (e.g. from the side)
        FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
        lightPos.put(30.0f).put(10.0f).put(0.0f).put(1.0f).flip();

        // OpenGL light setup
        glLight(GL_LIGHT0, GL_POSITION, lightPos);
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        
        for (int i = 0; i < STAR_COUNT; i++) {
            float x = (float)(Math.random() * 300 - 150);   // wider
            float y = (float)(Math.random() * 100 + 80);    // higher up
            float z = (float)(Math.random() * 300 - 150);
            stars.add(new float[]{x, y, z});
        }
    }
    
    public static void main(String[] args) {
        Basic3D basic = new Basic3D();
        basic.start();
    }   
}