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
import java.util.Random;
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
    private float roll = 0.0f;
    
    private Vector3Float me;
    
    private Random r = new Random();
    private int seed =r.nextInt();
    
    private Chunk chunkBL;
    private Chunk chunkBC;
    private Chunk chunkBR;
    private Chunk chunkML;
    private Chunk chunkMC;
    private Chunk chunkMR;
    private Chunk chunkTL;
    private Chunk chunkTC;
    private Chunk chunkTR;
    
    
    // method: FPCameraController
    // purpose: set up position locations
    public FPCameraController(float x, float y, float z) {
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x, y, z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
        
        chunkBL = new Chunk(0, 0, 0, seed);
        chunkBC = new Chunk(-60, 0, 0, seed);
        chunkBR = new Chunk(-120, 0, 0, seed);
        
        chunkML = new Chunk(0, 0, -60, seed);
        chunkMC = new Chunk(-60, 0, -60, seed);
        chunkMR = new Chunk(-120, 0, -60, seed);
        
        chunkTL = new Chunk(0, 0, -120, seed);
        chunkTC = new Chunk(-60, 0, -120, seed);
        chunkTR = new Chunk(-120, 0, -120, seed);
    }

    // method: yaw
    // purpose: change the yaw by given ammount
    public void yaw(float amount) {
        yaw += amount;
    }
    
    // method: roll
    // purpose: change the roll to simulate wip of camera
    public void roll(float amount) {
        if (amount > 4) {
            roll += 1;
        } else if (amount < -4) {
            roll += -1;
        } else {
            roll += (amount)/4;
        }
        roll = roll * 9 / 10;

        if (Math.abs(roll) < .5) {
            roll = 0;
        }
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
        glRotatef(roll, 0.0f, 0.0f, 1.0f);
        
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        
        glTranslatef(position.x, position.y, position.z);
        
    }
    
    // method: gameLoop
    // purpose: process player inputs, then display updated view
    public void gameLoop() {
        //FPCameraController camera = new FPCameraController(-45*2, -30*2, -45*2);
        FPCameraController camera = new FPCameraController(0,0,0);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f;
        float lastTime = 0.0f;
        long time = 0;
        long timeActionable = 0;
        float mouseSensitivity = 0.11f;
        float movementSpeed = .21f;
        Mouse.setGrabbed(true);
        while (!Display.isCloseRequested()&& !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            lastTime = time;
            time = Sys.getTime();
            
            
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            
            camera.yaw(dx * mouseSensitivity);
            camera.pitch(dy * mouseSensitivity);
            camera.roll(dx * mouseSensitivity);
            
            camera.handleMovement(
                    movementSpeed,
                    camera,
                    Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP),
                    Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN),
                    Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT),
                    Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT),
                    Keyboard.isKeyDown(Keyboard.KEY_SPACE),
                    Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
                            
            );
            
            glLoadIdentity();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            camera.lookThrough();
            Display.setTitle("Voxel World ("+
                    (int)-camera.position.x/2+", "+
                    (int)-camera.position.y/2+", "+
                    (int)-camera.position.z/2+
                    ")");
            
            if(Keyboard.isKeyDown(Keyboard.KEY_Q)&&time>timeActionable){
                chunkBL.setBlock(-camera.position.x / 2, -camera.position.y / 2-1, -camera.position.z / 2, BlockType.Plank);
                chunkBL.rebuildMesh();
                timeActionable = time + 100;
                System.out.println("Plank " + time);
            } else if(Keyboard.isKeyDown(Keyboard.KEY_E)&&time>timeActionable){
                chunkBL.destroyBlock(-camera.position.x / 2, -camera.position.y / 2-1, -camera.position.z / 2);
                chunkBL.rebuildMesh();
                timeActionable = time + 100;
                System.out.println("Air " + time);
            } 
            
            
            //chunkMC.Blocks[]
            
            
            chunkBL.render();
            chunkBC.render();
            chunkBR.render();
            chunkML.render();
            chunkMC.render();
            chunkMR.render();
            chunkTL.render();
            chunkTC.render();
            chunkTR.render();
            
            glBegin(GL_QUADS);
                glColor4f(1.0f, 1.0f, 1.0f, 1f);
                glVertex3f(1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(1.0f, 1.0f, -1.0f);
            glEnd();
            
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }
    
    public void handleMovement(float movementSpeed, FPCameraController camera,
            boolean forward, boolean backward, boolean left, boolean right, boolean up, boolean down) {
        if((forward ^ backward) && (left ^ right)) {
            if (forward){
                camera.walkForward(movementSpeed/(float) Math.sqrt(2));
            }
            if (backward){
                camera.walkBackwards(movementSpeed/(float) Math.sqrt(2));
            }
            if (left) {
                camera.strafeLeft(movementSpeed/(float) Math.sqrt(2));
                camera.roll(-10);
            }
            if (right) {
                camera.strafeRight(movementSpeed/(float) Math.sqrt(2));
                camera.roll(10);
            }
        } else {
            if (forward){
                camera.walkForward(movementSpeed);
            }
            if (backward){
                camera.walkBackwards(movementSpeed);
            }
            if (left) {
                camera.strafeLeft(movementSpeed);
                camera.roll(-10);
            }
            if (right) {
                camera.strafeRight(movementSpeed);
                camera.roll(10);
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            camera.moveUp(movementSpeed);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            camera.moveDown(movementSpeed);
        }
    }
    
}
