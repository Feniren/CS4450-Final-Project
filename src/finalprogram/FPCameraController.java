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
import org.lwjgl.util.vector.Vector2f;
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
    
    
    
    private Vector3f velocity;
    //private Vector3Float acceleration;
    
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
    
    private Chunk[][] chunks;
    private int worldSize;
    private int currentChunkID;
    private MapBase coordinateRegistry;
    private Vector2f currentPosition;
    private int renderDistance;
    public static boolean worldBuilt = false;
    
    
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
        
        worldSize = 10;
        renderDistance = 2;
        chunks = new Chunk[worldSize][worldSize];
        
        coordinateRegistry = new MapBase();
        Vector2f coordinates;
        
        int chunkListPosition = 0;
        
        if (!worldBuilt) {
            for (int i = 0; i < worldSize; i++){
                for (int j = 0; j < worldSize; j++){
                    System.out.println("Building World: " + 100*(i*worldSize+j)/(worldSize*worldSize) + "%");
                    //System.out.println(i);
                    //System.out.println(j);
                    chunks[i][j] = new Chunk(i * -60, 0, j * -60, seed);
                    chunks[i][j].SetChunkID(chunkListPosition);

                    coordinates = new Vector2f(j, i);

                    coordinateRegistry.Add(coordinates, chunkListPosition);

                    //System.out.println(coordinates + " " + chunkListPosition + " ");

                    chunkListPosition++;
                }
            }
            System.out.println("Building World: Complete!");
            worldBuilt = true;
        }
        
        currentChunkID = 0;
        
        /*
        chunkBL = new Chunk(0, 0, 0, seed);
        chunkBC = new Chunk(-60, 0, 0, seed);
        chunkBR = new Chunk(-120, 0, 0, seed);
        
        chunkML = new Chunk(0, 0, -60, seed);
        chunkMC = new Chunk(-60, 0, -60, seed);
        chunkMR = new Chunk(-120, 0, -60, seed);
        
        chunkTL = new Chunk(0, 0, -120, seed);
        chunkTC = new Chunk(-60, 0, -120, seed);
        chunkTR = new Chunk(-120, 0, -120, seed);
        */
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
    
    public void CalculateRenderDistance(){
        ResetRenderDistance();
        
        for (int i = -renderDistance; i <= renderDistance; i++){
            for (int j = -renderDistance; j <= renderDistance; j++){
                try{
                    chunks[(int)(currentPosition.x) + i][(int)(currentPosition.y) + j].SetVisibility(true);
                }
                catch(Exception e){
                    // 0  1  2  3  4
                    // 5  6  7  8  9
                    // 10 11 12 13 14
                    // 15 16 17 18 19
                    // 20 21 22 23 24
                }
            }
        }
    }
    
    public void ResetRenderDistance(){
        for (int i = 0; i < worldSize; i++){
            for (int j = 0; j < worldSize; j++){
                chunks[i][j].SetVisibility(false);
            }
        }
    }
    
    // method: gameLoop
    // purpose: process player inputs, then display updated view
    public void gameLoop() {
        //FPCameraController camera = new FPCameraController(-45*2, -30*2, -45*2);
        FPCameraController camera = new FPCameraController(worldSize/2*-60,-50,worldSize/2*-60);
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
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            
            camera.lookThrough();
            Display.setTitle("Voxel World ("+
                    (int)-camera.position.x/2+", "+
                    (int)-camera.position.y/2+", "+
                    (int)-camera.position.z/2+
                    ")");
            
            //building
            if(Keyboard.isKeyDown(Keyboard.KEY_Q)&&time>timeActionable){
                if(BlockType.Bedrock!=chunks[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].getBlock((int)getCameraChunkCoords(camera).x, -camera.position.y / 2-1, (int)getCameraChunkCoords(camera).y)) {
                    chunks[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].setBlock((int)getCameraChunkCoords(camera).x, -camera.position.y / 2-1, (int)getCameraChunkCoords(camera).y, BlockType.Plank);
                    chunks[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].rebuildMesh();
                    timeActionable = time + 100;
                    System.out.println("Plank " + time);
                }
                
            //breaking
            } else if(Keyboard.isKeyDown(Keyboard.KEY_E)&&time>timeActionable){
                if(BlockType.Bedrock!=chunks[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].getBlock((int)getCameraChunkCoords(camera).x, -camera.position.y / 2-1, (int)getCameraChunkCoords(camera).y)
                        && BlockType.Bedrock!=chunks[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].getBlock((int)getCameraChunkCoords(camera).x, -camera.position.y / 2, (int)getCameraChunkCoords(camera).y)) {
                    chunks[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].destroyBlock((int)getCameraChunkCoords(camera).x, -camera.position.y / 2-1, (int)getCameraChunkCoords(camera).y);
                    chunks[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].destroyBlock((int)getCameraChunkCoords(camera).x, -camera.position.y / 2, (int)getCameraChunkCoords(camera).y);
                    chunks[(int)getCameraChunk(camera).x][(int)getCameraChunk(camera).y].rebuildMesh();
                    timeActionable = time + 100;
                    System.out.println("Air " + time);
                }
            }
            
            
            //chunkMC.Blocks[]
            
            currentPosition = new Vector2f((int)(-camera.position.x / 60), (int)(-camera.position.z / 60));
            
            currentChunkID = coordinateRegistry.Find(currentPosition);
            
            //System.out.println(currentPosition + " " + currentChunkID + " ");
            
            CalculateRenderDistance();
            
            for (int i = 0; i < worldSize; i++){
                for (int j = 0; j < worldSize; j++){
                    chunks[i][j].render();
                }
            }
            
            /*
            chunkBL.render();
            chunkBC.render();
            chunkBR.render();
            chunkML.render();
            chunkMC.render();
            chunkMR.render();
            chunkTL.render();
            chunkTC.render();
            chunkTR.render();
            */
            
            /*glBegin(GL_QUADS);
                glColor4f(1.0f, 1.0f, 1.0f, 1f);
                glVertex3f(1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, -1.0f, -1.0f);
                glVertex3f(-1.0f, 1.0f, -1.0f);
                glVertex3f(1.0f, 1.0f, -1.0f);
            glEnd();*/
            
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
