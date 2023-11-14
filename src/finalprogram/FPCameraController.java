/***************************************************************
* file: FPCameraController.java
* author: Aidan, Jace
* class: CS 4450 Computer Graphics
*
* assignment: final program
* date last modified: 11/7/2023
*
* purpose: This creates and controls camera
*
****************************************************************/
// package
package finalprogram;

// imports
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;

public class FPCameraController {
    private Vector3f position = null;
    private Vector3f lPosition = null;
    
    private float yaw = 0.0f;
    
    private float pitch = 0.0f;
    private Vector3Float me;
    
    private Chunk chunk;
    
    // method: FPCameraController
    // purpose: set up position locations
    public FPCameraController(float x, float y, float z) {
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x, y, z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
        
        chunk = new Chunk(0, 0, 0);
    }

    // method: yaw
    // purpose: change the yaw by given ammount
    public void yaw(float amount) {
        yaw += amount;
    }

    // method: pitch
    // purpose: change the pitch by given ammount
    public void pitch(float amount) {
        pitch -= amount;
        if(pitch>90) {
            pitch = 90;
        } else if (pitch<-90) {
            pitch =-90;
        }
    }
    
    // method: walkForward
    // purpose: move camera forward
    public void walkForward(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    // method: walkBackwards
    // purpose: move camera backwards
    public void walkBackwards(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
    }
    
    // method: strafeLeft
    // purpose: move camera left
    public void strafeLeft(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw - 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw - 90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    // method: strafeRight
    // purpose: move camera right
    public void strafeRight(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw + 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw + 90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    // method: moveUp
    // purpose: move camera up
    public void moveUp(float distance) {
        position.y -= distance;
    }
    
    // method: moveDown
    // purpose: move camera down
    public void moveDown(float distance) {
        position.y += distance;
    }
    
    // method: lookThrough
    // purpose: move matrix to be looking through camera
    public void lookThrough() {
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        glTranslatef(position.x, position.y, position.z);
    }
    
    // method: gameLoop
    // purpose: process player inputs, then display updated view
    public void gameLoop() {
        FPCameraController camera = new FPCameraController(0, 0, 0);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f;
        float lastTime = 0.0f;
        long time = 0;
        float mouseSensitivity = 0.11f;
        float movementSpeed = .21f;
        Mouse.setGrabbed(true);
        while (!Display.isCloseRequested()&& !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            time = Sys.getTime();
            lastTime = time;
            
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            
            camera.yaw(dx * mouseSensitivity);
            camera.pitch(dy * mouseSensitivity);
            
            if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)){
                camera.walkForward(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
                camera.walkBackwards(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                camera.strafeLeft(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                camera.strafeRight(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                camera.moveUp(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(movementSpeed);
            }
            glLoadIdentity();
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            chunk.render();
            //render();
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }
    
    // method: render
    // purpose: render the world as quads
    private void render() {
        try {
            glBegin(GL_QUADS);
                //Top
                glColor3f(1.0f, 1.0f, 1.0f);
                glVertex3f(1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(1.0f, 1.0f, 1.0f);
                //Bottom
                glColor3f(0.0f, 0.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(1.0f, -1.0f, -1.0f);
                //Front
                glColor3f(0.0f, 1.0f, 0.0f);
                glVertex3f(1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, 1.0f);
                //Back
                glColor3f(1.0f, 0.0f, 0.0f);
                glVertex3f(1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(1.0f, 1.0f, -1.0f);
                //Left
                glColor3f(0.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, 1.0f);
                //Right
                glColor3f(1.0f, 1.0f, 0.0f);
                glVertex3f(1.0f, 1.0f, -1.0f);
                glVertex3f(1.0f, 1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, 1.0f);
                glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();
        } catch (Exception e) {
        }
    }
}
