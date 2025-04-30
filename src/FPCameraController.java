import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;


/**
 *
 *  * @author jellyj. sarmi, Max
 */

public class FPCameraController {
    //3d vector to store the camera's position in
    private Vector3f position = null;
    private Vector3f lPosition = null;
    //the rotation around the Y axis of the camera
    private float yaw = 0.0f;
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Vector3Float me;
    private final float minX = -100f;
    private final float maxX = 60f;
    private final float minZ = -100f;
    private final float maxZ = 60f;
    private final float minY = 60f;
    private final float maxY = 90f;
    Cube cube = new Cube();
    Chunk chunk;
    
    public FPCameraController(float x, float y, float z) {
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x, y, z);
    }

    
    //increment the camera's current yaw rotation
    public void yaw(float amount)
    {
        //increment the yaw by the amount param
        yaw += amount;
    }
    //increment the camera's current yaw rotation
    public void pitch(float amount)
    {
        //increment the pitch by the amount param
        pitch -= amount;
    }
    
    //moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance)
    {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        
        float x = position.x - xOffset; // possible x value
        float z = position.z + zOffset; // possible z value

        if (x >= minX && x <= maxX) position.x = x; //check if x is w/in bounds
        if (z >= minZ && z <= maxZ) position.z = z; //check if z is w/in bounds
    }
    
    //moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance)
    {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        
        float x = position.x + xOffset; // possible x value
        float z = position.z - zOffset; // possible z value

        if (x >= minX && x <= maxX) position.x = x; //check if x is w/in bounds
        if (z >= minZ && z <= maxZ) position.z = z; //check if z is w/in bounds
    }
    
    //strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance)
    {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        
        float x = position.x - xOffset; // possible x value
        float z = position.z + zOffset; // possible z value

        if (x >= minX && x <= maxX) position.x = x; //check if x is w/in bounds
        if (z >= minZ && z <= maxZ) position.z = z; //check if z is w/in bounds
    }
    
    //strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance)
    {
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        
        float x = position.x - xOffset; // possible x value
        float z = position.z + zOffset; // possible z value

        if (x >= minX && x <= maxX) position.x = x; //check if x is w/in bounds
        if (z >= minZ && z <= maxZ) position.z = z; //check if z is w/in bounds
    }

    //moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance)
    {
        float y = position.y - distance; // possible y value

        if (-y > maxY) return;  //check if y greater than the max y value
        position.y = y;
    }
    //moves the camera down
    public void moveDown(float distance)
    {
        float y = position.y + distance; // possible y value

        if (-y < minY) return; //check if y is less than the min y value
        position.y = y;
    }
    
    //translates and rotate the matrix so that it looks through the camera
    //this does basically what gluLookAt() does
    public void lookThrough()
    {
        //roatate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
    }
    
    public void gameLoop()
    {
        FPCameraController camera = new FPCameraController(-30, -65, -30);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        //hide the mouse
        Mouse.setGrabbed(true);
        chunk = new Chunk(0, 0, 0); // created chunk object
        
        // keep looping till the display window is closed the ESC key is down
        while (!Display.isCloseRequested() &&
        !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            time = Sys.getTime();
            lastTime = time;
            //distance in mouse movement
            //from the last getDX() call.
            dx = Mouse.getDX();
            //distance in mouse movement
            //from the last getDY() call.
            dy = Mouse.getDY();
            //control camera yaw from x movement from the mouse
            camera.yaw(dx * mouseSensitivity);
            //control camera pitch from y movement from the mouse
            camera.pitch(dy * mouseSensitivity);
            //when passing in the distance to move
            //we times the movementSpeed with dt this is a time scale
            //so if its a slow frame u move more then a fast frame
            //so on a slow computer you move just as fast as on a fast computer
            if (Keyboard.isKeyDown(Keyboard.KEY_W))//move forward
            {
                camera.walkForward(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S))//move backwards
            {
                camera.walkBackwards(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A))//strafe left 
            {
                camera.strafeLeft(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D))//strafe right 
            {
                camera.strafeRight(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))//move up 
            {
                camera.moveUp(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(movementSpeed);
            }
            //set the modelview matrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            //you would draw your scene here.
            chunk.render();   // call chunk render instead of this class' render
            //draw the buffer to the screen
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }
    
    public void render() {

        cube.render();
        
    } 
}