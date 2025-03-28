/*
   java -cp "build/classes:/Users/jellyj./Desktop/CS4450/lwjgl-2.9.2/jar/lwjgl.jar:/Users/jellyj./Desktop/CS4450/lwjgl-2.9.2/jar/lwjgl_util.jar:/Users/jellyj./Desktop/CS4450/lwjgl-2.9.2/jar/jinput.jar"      -Djava.library.path="/Users/jellyj./Desktop/CS4450/lwjgl-2.9.2/native/macosx"      Cube.java

 */
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 *
 * @author jellyj.
 */
public class Cube {
    
    float cubeSize = 1f;
    int x0 = 0;
    int y0 = 0;
    FloatBuffer v0, v1, v2, v3, v4, v5, v6, v7;

    public static void main(String[] args) {
        //Cube cube = new Cube();
        //cube.start();
    }
    
    public void initVertices(){
        // bottom left vertex
        //float x0 = (x/2) - (cubeSize/2);
        //float y0 = (y/2) - (cubeSize/2);
        float s = cubeSize;
        
        v0 = createVertex(x0, y0, 0);
        v1 = createVertex(x0, y0 + s, 0);
        v2 = createVertex(x0 + s, y0, 0);
        v3 = createVertex(x0 + s, y0 + s, 0);
        v4 = createVertex(x0, y0, s);
        v5 = createVertex(x0, y0 + s, s);
        v6 = createVertex(x0 + s, y0, s);
        v7 = createVertex(x0 + s, y0 + s, s);
    }
    
    private FloatBuffer createVertex(float x, float y, float z) {
        FloatBuffer vertex = BufferUtils.createFloatBuffer(3);
        vertex.put(x).put(y).put(z);
        vertex.flip();
        return vertex;
    }
    
    private void quad(FloatBuffer va, FloatBuffer vb, FloatBuffer vc, FloatBuffer vd){
        glColor3f(1.0f, 0.0f, 0.0f); // Red color
        glBegin(GL_QUADS);
            glVertex3f(va.get(0), va.get(1), va.get(2));
            glVertex3f(vb.get(0), vb.get(1), vb.get(2));
            glVertex3f(vc.get(0), vc.get(1), vc.get(2));
            glVertex3f(vd.get(0), vd.get(1), vd.get(2));
        glEnd(); 
    }
    
    private void cube(){
        glColor3f(1.0f, 0.0f, 0.0f); // Red color
        quad(v6, v2, v3, v7);
        glColor3f(1.000f, 0.647f, 0.000f); // Orange color
        quad(v5, v1, v0, v4);
        glColor3f(1.0f, 1.0f, 0.0f); // Yellow color
        quad(v7, v3, v1, v5);
        glColor3f(0.0f, 0.502f, 0.0f); // Green color
        quad(v4, v0, v2, v6);
        glColor3f(0.0f, 1.0f, 1.0f); // Cyan color
        quad(v2, v0, v1, v3);
        glColor3f(0.502f, 0.000f, 0.502f); // Purple color
        quad(v7, v5, v4, v6);
    }
   
    public void start() {
        try {
            //createWindow();
            //initGL();
            initVertices();
            cube();
            //render();
        } catch (Exception e) {
        e.printStackTrace();
       
        }
    }
    
    /**
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        Display.setDisplayMode(new DisplayMode(x, y));
        Display.setTitle("Hey Mom! I am using OpenGL!!!");
        Display.create();
    }
    
    private void initGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 640, 0, 480, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

    }
    
    private void render() {
        while (!Display.isCloseRequested()) {
            try{
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                glColor3f(1.0f,1.0f,0.0f);
                glPointSize(10);
                
                    cube();
                
                Display.update();
                Display.sync(60);
            }catch(Exception e){
            }
        }
        Display.destroy();
    }
    */
    
}
