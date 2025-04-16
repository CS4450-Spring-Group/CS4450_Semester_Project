
import static org.lwjgl.opengl.GL11.*;

/**
 *
 *  * @author jellyj. sarmi, Max
 */
public class Cube {
    
    public void render() {
        try{
            glBegin(GL_QUADS);
                //Top
                glColor3f(1.0f, 1.0f, 0.0f); // Yellow
                glVertex3f( 1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f( 1.0f, 1.0f, 1.0f);
                //Bottom
                glColor3f(0.0f, 1.0f, 1.0f);    //Cyan
                glVertex3f( 1.0f,-1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f( 1.0f,-1.0f,-1.0f);
                //Front
                glColor3f(1.0f, 1.0f, 1.0f);    // Red
                glVertex3f( 1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f, 1.0f);
                //Back
                glColor3f(0.0f, 0.5f, 1.0f);  // Green
                glVertex3f( 1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f( 1.0f, 1.0f,-1.0f);
                //Left
                glColor3f(0.5f, 0.0f, 1.5f);    // Purple
                glVertex3f(-1.0f, 1.0f,1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f, -1.0f,-1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
                //Right
                glColor3f(1.0f, 0.647f, 0.0f);  // Orange
                glVertex3f( 1.0f, 1.0f,-1.0f);
                glVertex3f( 1.0f, 1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f,-1.0f);
            glEnd();
            
            glBegin(GL_LINE_LOOP);
                //Top
                glColor3f(0.0f,0.0f,0.0f);
                glVertex3f( 1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f( 1.0f, 1.0f, 1.0f);
                glEnd();
                glBegin(GL_LINE_LOOP);
                //Bottom
                glVertex3f( 1.0f,-1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f( 1.0f,-1.0f,-1.0f);
                glEnd();
                glBegin(GL_LINE_LOOP);
                //Front
                glVertex3f( 1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f, 1.0f);
                glEnd();
                glBegin(GL_LINE_LOOP);
                //Back
                glVertex3f( 1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f( 1.0f, 1.0f,-1.0f);
                glEnd();
                glBegin(GL_LINE_LOOP);
                //Left
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f, 1.0f);
                glEnd();
                glBegin(GL_LINE_LOOP);
                //Right
                glVertex3f( 1.0f, 1.0f,-1.0f);
                glVertex3f( 1.0f, 1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f,-1.0f);
            glEnd();
            
        }catch(Exception e){
        }
        
    }

}
