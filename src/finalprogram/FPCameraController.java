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
import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector2f;

public class FPCameraController {
    private Vector3f position = null;
    private Vector3f lPosition = null;
    private float yaw = 0.0f;
    
    private float pitch = 0.0f;
    private float roll = 0.0f;
    
    
    
    private Vector3f velocity;
    //private Vector3Float acceleration;
    
    private Random r = new Random();
    private int seed =r.nextInt();
    
    /*private Chunk chunkBL;
    private Chunk chunkBC;
    private Chunk chunkBR;
    private Chunk chunkML;
    private Chunk chunkMC;
    private Chunk chunkMR;
    private Chunk chunkTL;
    private Chunk chunkTC;
    private Chunk chunkTR;*/
    
    int world_length = 100;
    int world_width = 100;
    int view_distance = 3;
    
    private Chunk[][] chunkStorage = new Chunk[world_length][world_width];
    
    
    // method: FPCameraController
    // purpose: set up position locations
    public FPCameraController(float x, float y, float z) {
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x, y, z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
        
        velocity = new Vector3f(x, y, z);
        velocity.x = 0f;
        velocity.y = 0f;
        velocity.z = 0f;
        
        /*for(int i = 0; i < world_length; i++) {
            for(int j = 0; j < world_width; j++) {
                chunkStorage[i][j] = new Chunk(-60*i, 0, -60*j, seed);
            }
        }*/
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
        velocity.x -= xOffset;
        velocity.z += zOffset;
    }
    
    // method: walkBackwards
    // purpose: move camera backwards
    public void walkBackwards(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        velocity.x += xOffset;
        velocity.z -= zOffset;
    }
    
    // method: strafeLeft
    // purpose: move camera left
    public void strafeLeft(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw - 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw - 90));
        velocity.x -= xOffset;
        velocity.z += zOffset;
    }
    
    // method: strafeRight
    // purpose: move camera right
    public void strafeRight(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw + 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw + 90));
        velocity.x -= xOffset;
        velocity.z += zOffset;
    }
    
    // method: moveUp
    // purpose: move camera up
    public void moveUp(float distance) {
        velocity.y -= distance;
    }
    
    // method: moveDown
    // purpose: move camera down
    public void moveDown(float distance) {
        velocity.y += distance;
    }
    
    // method: lookThrough
    // purpose: move matrix to be looking through camera
    public void lookThrough() {
        glRotatef(roll, 0.0f, 0.0f, 1.0f);
        
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        
        glTranslatef(position.x, position.y, position.z);
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(60.0f).put(60.0f).put(100.0f).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }
    
    // method: gameLoop
    // purpose: process player inputs, then display updated view
    public void gameLoop() {
        //FPCameraController camera = new FPCameraController(-45*2, -30*2, -45*2);
        FPCameraController camera = new FPCameraController(-50*30,-60,-50*30);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f;
        float lastTime = 0.0f;
        long time = 0;
        long timeActionable = 0;
        float mouseSensitivity = 0.11f;
        float movementSpeed = .02f;
        float friction = 1.1f;
        
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
            
            // body specs: .5 blocks above | 1.5 blocks below | .25 blocks left,right,forward,back
            /*
            // forceField starts at minimum of each | key: x, y, z
            BlockType[][][] forceField  = new BlockType[3][4][3];
            for(int i = 0; i<3; i++) {
                for (int j = 0; j < 3; j++) {
                    for(int k = 0; k<4; k++) {
                        try{
                            forceField[i][j][k] = chunkStorage[(int) (- camera.position.x / 2 / 30)+(i-1)]
                                    [(int) (- camera.position.z / 2 / 30)+(k-1)].getBlock
                                    ((int) (- camera.position.x / 2)+(i-1) - 30 * ((int) (- camera.position.x / 2 / 30)+(i-1)),
                                    (-camera.position.y / 2)+(j-2),
                                    (int) (- camera.position.z / 2)+(k-1) - 30 * ((int) (- camera.position.z / 2 / 30)+(k-1)));
                        } catch (Exception e) {}
                        
                    }
                }
            }
            
            // currently janky floor, not working ceilings, and no walls
            float xOfBlock= - camera.position.x / 2 - (int) (- camera.position.x / 2);
            float zOfBlock= - camera.position.z / 2 - (int) (- camera.position.z / 2);
            float yOfBlock= - camera.position.y / 2 - (int) (- camera.position.y / 2) - .5f;
            // upward collision
            boolean ceilingHit = false;
            System.out.println(forceField[0][3][1]);
            System.out.println(xOfBlock);
            if(yOfBlock>0) {
                if(forceField[1][3][1]!=null) {
                    ceilingHit = true;
                } else {
                    if (xOfBlock<.25 && forceField[0][3][1]!=null) {
                        ceilingHit = true;
                    } else if (xOfBlock>.75 && forceField[2][3][1]!=null) {
                        ceilingHit = true;
                    } else if (zOfBlock<.25 && forceField[1][3][0]!=null) {
                        ceilingHit = true;
                    } else if (zOfBlock>.75 && forceField[1][3][2]!=null) {
                        ceilingHit = true;
                    } else {
                        if (xOfBlock < .25 && zOfBlock < .25 && forceField[0][3][0] != null) {
                            ceilingHit = true;
                        } else if (xOfBlock > .75 && zOfBlock > .75 && forceField[2][3][2] != null) {
                            ceilingHit = true;
                        } else if (xOfBlock < .25 && zOfBlock > .75 && forceField[0][3][2] != null) {
                            ceilingHit = true;
                        } else if (zOfBlock > .75 && zOfBlock > .25 && forceField[2][3][0] != null) {
                            ceilingHit = true;
                        }
                    }
                }
            }
            
            // downward collision
            boolean floorHit = false;
            if(yOfBlock<0) {
                if(forceField[1][0][1]!=null) {
                    floorHit = true;
                } else {
                    if (xOfBlock<.25 && forceField[0][0][1]!=null) {
                        floorHit = true;
                    } else if (xOfBlock>.75 && forceField[2][0][1]!=null) {
                        floorHit = true;
                    } else if (zOfBlock<.25 && forceField[1][0][0]!=null) {
                        floorHit = true;
                    } else if (zOfBlock>.75 && forceField[1][0][2]!=null) {
                        floorHit = true;
                    } else {
                        if (xOfBlock < .25 && zOfBlock < .25 && forceField[0][0][0] != null) {
                            floorHit = true;
                        } else if (xOfBlock > .75 && zOfBlock > .75 && forceField[2][0][2] != null) {
                            floorHit = true;
                        } else if (xOfBlock < .25 && zOfBlock > .75 && forceField[0][0][2] != null) {
                            floorHit = true;
                        } else if (zOfBlock > .75 && zOfBlock > .25 && forceField[2][0][0] != null) {
                            floorHit = true;
                        }
                    }
                }
            }
            
            if(!floorHit && null!=chunkStorage[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y]) {
                camera.velocity.y += .02;
            }
            if(floorHit) {
                camera.velocity.y = 0;
                if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                    camera.velocity.y = -.3f;
                }
            }
            if (!floorHit || camera.velocity.y<0) {
                if(!ceilingHit) {
                    camera.position.y += camera.velocity.y;
                } else {
                    camera.velocity.y = 0;
                }
            }*/
            
            // handle velocity
            camera.position.x += camera.velocity.x;
            camera.position.y += camera.velocity.y;
            camera.position.z += camera.velocity.z;
            
            // handle friction
            camera.velocity.x /= friction;
            camera.velocity.z /= friction;
            camera.velocity.y /= friction;
            
            glLoadIdentity();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            camera.lookThrough();
            
            
            //building
            if(Keyboard.isKeyDown(Keyboard.KEY_Q)&&time>timeActionable){
                chunkStorage[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].setBlock((int)getCameraChunkCoords(camera).x, -camera.position.y / 2-2, (int)getCameraChunkCoords(camera).y, BlockType.Plank);
                chunkStorage[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].rebuildMesh();
                timeActionable = time + 100;
                System.out.println("Plank " + time);
                
            //breaking
            } else if(Keyboard.isKeyDown(Keyboard.KEY_E)&&time>timeActionable){
                chunkStorage[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].destroyBlock((int)getCameraChunkCoords(camera).x, -camera.position.y / 2-2, (int)getCameraChunkCoords(camera).y);
                chunkStorage[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].rebuildMesh();
                timeActionable = time + 100;
                System.out.println("Air " + time);
            }
            
            boolean loadedOneChunk = false;
            for (int i = (int)getCameraChunk(camera).x-view_distance; i < (int)getCameraChunk(camera).x+view_distance; i++) {
                for (int j = (int)getCameraChunk(camera).y-view_distance; j < (int)getCameraChunk(camera).y+view_distance; j++) {
                    
                    if(i<world_length && i>=0 && j<world_width && j>=0) {
                        if(chunkStorage[i][j] == null && !loadedOneChunk) {
                            loadedOneChunk = true;
                            chunkStorage[i][j] = new Chunk(-60*i, 0, -60*j, seed);
                            chunkStorage[i][j].render();
                        } else if (chunkStorage[i][j] != null) {
                            chunkStorage[i][j].render();
                        }
                    }
                }
            }
            Display.setTitle("Voxel World g("+
                    (int)-camera.position.x/2+","+
                    (int)-camera.position.y/2+","+
                    (int)-camera.position.z/2+
                    ") c("+
                    (int)getCameraChunk(camera).x+","+
                    (int)getCameraChunk(camera).y+") cc("+
                    (int)getCameraChunkCoords(camera).x+","+
                    (int)getCameraChunkCoords(camera).y+")");
            
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
    public Vector2f getCameraChunk(FPCameraController camera) {
         return new Vector2f((int) - camera.position.x / 2 / 30, (int) -camera.position.z / 2 / 30);
    }
    
    public Vector2f getCameraChunkCoords(FPCameraController camera) {
        return new Vector2f(- camera.position.x / 2 - 30 * getCameraChunk(camera).x,-camera.position.z / 2 - 30 * getCameraChunk(camera).y);
    }
    
}
