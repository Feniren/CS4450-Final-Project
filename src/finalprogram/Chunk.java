/***************************************************************
* file: Chunk.java
* author: Aidan, Jace
* class: CS 4450 Computer Graphics
*
* assignment: final program
* date last modified: 11/7/2023
*
* purpose: This holds chunk data
*
****************************************************************/
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalprogram;

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Aidan
 */
public class Chunk{
    static final int CHUNK_SIZE = 30;
    static final int CUBE_LENGTH = 2;
    static final int WATER_LEVEL = 16;
    private Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int StartX, StartY, StartZ;
    private Random r;
    private int VBOTextureHandle;
    private Texture texture;
    private boolean isVisible;
    private int chunkID;
    
    //constructor
    public Chunk(int startX, int startY, int startZ, int seed){
        try{
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("Terrain.png"));
        }
        catch(Exception e){
            System.out.print("ER-ROAR!");
        }

        r= new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        /*
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    Blocks[x][y][z] = new
                    Block(BlockType.Grass);
                }
            }
        }*/
        
        isVisible = true;
        
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        
        genMesh(seed);
        rebuildMesh();
    }
    
    //creates quads for cubes
    public static float[] createCube(float x, float y, float z){
        int offset = CUBE_LENGTH / 2;
        
        return new float[] {
        // TOP QUAD
        x + offset, y + offset, z,
        x - offset, y + offset, z,
        x - offset, y + offset, z - CUBE_LENGTH,
        x + offset, y + offset, z - CUBE_LENGTH,
        // BOTTOM QUAD
        x + offset, y - offset, z - CUBE_LENGTH,
        x - offset, y - offset, z - CUBE_LENGTH,
        x - offset, y - offset, z,
        x + offset, y - offset, z,
        // FRONT QUAD
        x + offset, y + offset, z - CUBE_LENGTH,
        x - offset, y + offset, z - CUBE_LENGTH,
        x - offset, y - offset, z - CUBE_LENGTH,
        x + offset, y - offset, z - CUBE_LENGTH,
        // BACK QUAD
        x + offset, y - offset, z,
        x - offset, y - offset, z,
        x - offset, y + offset, z,
        x + offset, y + offset, z,
        // LEFT QUAD
        x - offset, y + offset, z - CUBE_LENGTH,
        x - offset, y + offset, z,
        x - offset, y - offset, z,
        x - offset, y - offset, z - CUBE_LENGTH,
        // RIGHT QUAD
        x + offset, y + offset, z,
        x + offset, y + offset, z - CUBE_LENGTH,
        x + offset, y - offset, z - CUBE_LENGTH,
        x + offset, y - offset, z };
    }
    
    //returns a float array that holds vertex color data
    private static float[] createCubeVertexCol(float[] CubeColorArray){
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        }
        
        return cubeColors;
    }
    
    //creates a float array to hold texture data based on the type of block
    public static float[] createTexCube(float x, float y, Block block){
        float offset = (96f/16)/96f;
        switch (block.GetBlockType()) {
            case Grass:
                
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*3, y + offset*10,
                x + offset*2, y + offset*10,
                x + offset*2, y + offset*9,
                x + offset*3, y + offset*9,
                // TOP!
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                // FRONT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // BACK QUAD
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                // LEFT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1,
                // RIGHT QUAD
                x + offset*3, y + offset*0,
                x + offset*4, y + offset*0,
                x + offset*4, y + offset*1,
                x + offset*3, y + offset*1
                };
                
            case Stone:
                
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*1, y + offset*0,
                x + offset*2, y + offset*0,
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1,
                // TOP!
                x + offset*1, y + offset*0,
                x + offset*2, y + offset*0,
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1,
                // FRONT QUAD
                x + offset*1, y + offset*0,
                x + offset*2, y + offset*0,
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1,
                // BACK QUAD
                x + offset*1, y + offset*0,
                x + offset*2, y + offset*0,
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1,
                // LEFT QUAD
                x + offset*1, y + offset*0,
                x + offset*2, y + offset*0,
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1,
                // RIGHT QUAD
                x + offset*1, y + offset*0,
                x + offset*2, y + offset*0,
                x + offset*2, y + offset*1,
                x + offset*1, y + offset*1
                };
                
            case Bedrock:
                
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*1, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*2,
                x + offset*1, y + offset*2,
                // TOP!
                x + offset*1, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*2,
                x + offset*1, y + offset*2,
                // FRONT QUAD
                x + offset*1, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*2,
                x + offset*1, y + offset*2,
                // BACK QUAD
                x + offset*1, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*2,
                x + offset*1, y + offset*2,
                // LEFT QUAD
                x + offset*1, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*2,
                x + offset*1, y + offset*2,
                // RIGHT QUAD
                x + offset*1, y + offset*1,
                x + offset*2, y + offset*1,
                x + offset*2, y + offset*2,
                x + offset*1, y + offset*2
                };
                
            case Dirt:
                
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1,
                // TOP!
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1,
                // FRONT QUAD
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1,
                // BACK QUAD
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1,
                // LEFT QUAD
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1,
                // RIGHT QUAD
                x + offset*2, y + offset*0,
                x + offset*3, y + offset*0,
                x + offset*3, y + offset*1,
                x + offset*2, y + offset*1
                };
                
            case Sand:
                
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2,
                // TOP!
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2,
                // FRONT QUAD
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2,
                // BACK QUAD
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2,
                // LEFT QUAD
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2,
                // RIGHT QUAD
                x + offset*2, y + offset*1,
                x + offset*3, y + offset*1,
                x + offset*3, y + offset*2,
                x + offset*2, y + offset*2
                };
                
            case Water:
                
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*14, y + offset*1,
                x + offset*15, y + offset*1,
                x + offset*15, y + offset*0,
                x + offset*14, y + offset*0,
                // TOP!
                x + offset*14, y + offset*1,
                x + offset*15, y + offset*1,
                x + offset*15, y + offset*0,
                x + offset*14, y + offset*0,
                // FRONT QUAD
                x + offset*14, y + offset*1,
                x + offset*15, y + offset*1,
                x + offset*15, y + offset*0,
                x + offset*14, y + offset*0,
                // BACK QUAD
                x + offset*14, y + offset*1,
                x + offset*15, y + offset*1,
                x + offset*15, y + offset*0,
                x + offset*14, y + offset*0,
                // LEFT QUAD
                x + offset*14, y + offset*1,
                x + offset*15, y + offset*1,
                x + offset*15, y + offset*0,
                x + offset*14, y + offset*0,
                // RIGHT QUAD
                x + offset*14, y + offset*1,
                x + offset*15, y + offset*1,
                x + offset*15, y + offset*0,
                x + offset*14, y + offset*0,
                };
                
            case Plank:
                
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*4, y + offset*1,
                x + offset*5, y + offset*1,
                x + offset*5, y + offset*0,
                x + offset*4, y + offset*0,
                // TOP!
                x + offset*4, y + offset*1,
                x + offset*5, y + offset*1,
                x + offset*5, y + offset*0,
                x + offset*4, y + offset*0,
                // FRONT QUAD
                x + offset*4, y + offset*1,
                x + offset*5, y + offset*1,
                x + offset*5, y + offset*0,
                x + offset*4, y + offset*0,
                // BACK QUAD
                x + offset*4, y + offset*1,
                x + offset*5, y + offset*1,
                x + offset*5, y + offset*0,
                x + offset*4, y + offset*0,
                // LEFT QUAD
                x + offset*4, y + offset*1,
                x + offset*5, y + offset*1,
                x + offset*5, y + offset*0,
                x + offset*4, y + offset*0,
                // RIGHT QUAD
                x + offset*4, y + offset*1,
                x + offset*5, y + offset*1,
                x + offset*5, y + offset*0,
                x + offset*4, y + offset*0,
                };
                
            case Coal:
                
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*2, y + offset*2,
                x + offset*3, y + offset*2,
                x + offset*3, y + offset*3,
                x + offset*2, y + offset*3,
                // TOP!
                x + offset*2, y + offset*2,
                x + offset*3, y + offset*2,
                x + offset*3, y + offset*3,
                x + offset*2, y + offset*3,
                // FRONT QUAD
                x + offset*2, y + offset*2,
                x + offset*3, y + offset*2,
                x + offset*3, y + offset*3,
                x + offset*2, y + offset*3,
                // BACK QUAD
                x + offset*2, y + offset*2,
                x + offset*3, y + offset*2,
                x + offset*3, y + offset*3,
                x + offset*2, y + offset*3,
                // LEFT QUAD
                x + offset*2, y + offset*2,
                x + offset*3, y + offset*2,
                x + offset*3, y + offset*3,
                x + offset*2, y + offset*3,
                // RIGHT QUAD
                x + offset*2, y + offset*2,
                x + offset*3, y + offset*2,
                x + offset*3, y + offset*3,
                x + offset*2, y + offset*3,
                };
                
            case Gold:
                
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*0, y + offset*2,
                x + offset*1, y + offset*2,
                x + offset*1, y + offset*3,
                x + offset*0, y + offset*3,
                // TOP!
                x + offset*0, y + offset*2,
                x + offset*1, y + offset*2,
                x + offset*1, y + offset*3,
                x + offset*0, y + offset*3,
                // FRONT QUAD
                x + offset*0, y + offset*2,
                x + offset*1, y + offset*2,
                x + offset*1, y + offset*3,
                x + offset*0, y + offset*3,
                // BACK QUAD
                x + offset*0, y + offset*2,
                x + offset*1, y + offset*2,
                x + offset*1, y + offset*3,
                x + offset*0, y + offset*3,
                // LEFT QUAD
                x + offset*0, y + offset*2,
                x + offset*1, y + offset*2,
                x + offset*1, y + offset*3,
                x + offset*0, y + offset*3,
                // RIGHT QUAD
                x + offset*0, y + offset*2,
                x + offset*1, y + offset*2,
                x + offset*1, y + offset*3,
                x + offset*0, y + offset*3,
                };
            
            case Iron:
                
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2,
                x + offset*2, y + offset*3,
                x + offset*1, y + offset*3,
                // TOP!
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2,
                x + offset*2, y + offset*3,
                x + offset*1, y + offset*3,
                // FRONT QUAD
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2,
                x + offset*2, y + offset*3,
                x + offset*1, y + offset*3,
                // BACK QUAD
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2,
                x + offset*2, y + offset*3,
                x + offset*1, y + offset*3,
                // LEFT QUAD
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2,
                x + offset*2, y + offset*3,
                x + offset*1, y + offset*3,
                // RIGHT QUAD
                x + offset*1, y + offset*2,
                x + offset*2, y + offset*2,
                x + offset*2, y + offset*3,
                x + offset*1, y + offset*3,
                };
                
            case Diamond:
                
                return new float[] {
                // BOTTOM QUAD(DOWN=+Y)
                x + offset*2, y + offset*3,
                x + offset*3, y + offset*3,
                x + offset*3, y + offset*4,
                x + offset*2, y + offset*4,
                // TOP!
                x + offset*2, y + offset*3,
                x + offset*3, y + offset*3,
                x + offset*3, y + offset*4,
                x + offset*2, y + offset*4,
                // FRONT QUAD
                x + offset*2, y + offset*3,
                x + offset*3, y + offset*3,
                x + offset*3, y + offset*4,
                x + offset*2, y + offset*4,
                // BACK QUAD
                x + offset*2, y + offset*3,
                x + offset*3, y + offset*3,
                x + offset*3, y + offset*4,
                x + offset*2, y + offset*4,
                // LEFT QUAD
                x + offset*2, y + offset*3,
                x + offset*3, y + offset*3,
                x + offset*3, y + offset*4,
                x + offset*2, y + offset*4,
                // RIGHT QUAD
                x + offset*2, y + offset*3,
                x + offset*3, y + offset*3,
                x + offset*3, y + offset*4,
                x + offset*2, y + offset*4,
                };
                
            default:
                return new float[]{0.0f};
        }
    }
    
    //gets the brightness for the block (white)
    private static float[] getCubeColor(Block block){
        return new float[] { 1, 1, 1 };
    }
    
    public void genMesh(int seed){
        SimplexNoise primaryNoise = new SimplexNoise(30, .29, seed);
        SimplexNoise cavexzNoise = new SimplexNoise(30, .5, seed+1);
        SimplexNoise caveyNoise = new SimplexNoise(30, .2, seed+2);
        SimplexNoise coalxzNoise = new SimplexNoise(30, .30, seed+3);
        SimplexNoise coalyNoise = new SimplexNoise(30, .2, seed+4);
        SimplexNoise coalyThicknessNoise = new SimplexNoise(30, .2, seed+5);
        SimplexNoise ironxzNoise = new SimplexNoise(30, .33, seed+6);
        SimplexNoise ironyNoise = new SimplexNoise(30, .2, seed+7);
        SimplexNoise ironyThicknessNoise = new SimplexNoise(25, .2, seed+8);
        SimplexNoise goldxzNoise = new SimplexNoise(30, .37, seed+9);
        SimplexNoise goldyNoise = new SimplexNoise(30, .2, seed+10);
        SimplexNoise goldyThicknessNoise = new SimplexNoise(20, .2, seed+11);
        SimplexNoise diamondxzNoise = new SimplexNoise(30, .40, seed+12);
        SimplexNoise diamondyNoise = new SimplexNoise(30, .2, seed+13);
        SimplexNoise diamondyThicknessNoise = new SimplexNoise(15, .2, seed+14);
        float height;
        for (float x = 0; x < CHUNK_SIZE; x += 1) {
            for (float z = 0; z < CHUNK_SIZE; z += 1) {
                height = (float) (primaryNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)) + .9) * 15 + 5;

                for (float y = 0; y < CHUNK_SIZE; y++) {

                    if (y <= height || (height <= WATER_LEVEL && y <= WATER_LEVEL)) {

                        if (height <= WATER_LEVEL && y >= height) {
                            //generate rivers
                            if (y <= height + 1) {
                                Blocks[(int) (x)][(int) (y)][(int) (z)] = new Block(BlockType.Sand);
                            } else if (y <= WATER_LEVEL) {
                                Blocks[(int) (x)][(int) (y)][(int) (z)] = new Block(BlockType.Water);
                            }
                        } else {
                            //generate normal land
                            if (y == 0) {
                                Blocks[(int) (x)][(int) (y)][(int) (z)] = new Block(BlockType.Bedrock);
                            } else if (y <= height - 4) {
                                Blocks[(int) (x)][(int) (y)][(int) (z)] = new Block(BlockType.Stone);
                                //caves
                                if(Math.abs(cavexzNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))>.05 &&
                                        2>Math.abs(y-7-25*caveyNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))) {
                                    Blocks[(int) (x)][(int) (y)][(int) (z)] = null;
                                }
                                //ores
                                else if(Math.abs(coalxzNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))<.05 &&
                                        Math.abs(10*coalyThicknessNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))>Math.abs(y-12-15*coalyNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))) {
                                    Blocks[(int) (x)][(int) (y)][(int) (z)] = new Block(BlockType.Coal);
                                } else if(Math.abs(ironxzNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))<.015 &&
                                        Math.abs(10*ironyThicknessNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))>Math.abs(y-10-25*ironyNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))) {
                                    Blocks[(int) (x)][(int) (y)][(int) (z)] = new Block(BlockType.Iron);
                                } else if(Math.abs(goldxzNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))<.01 &&
                                        Math.abs(10*goldyThicknessNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))>Math.abs(y-7-15*goldyNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))) {
                                    Blocks[(int) (x)][(int) (y)][(int) (z)] = new Block(BlockType.Gold);
                                } else if(Math.abs(diamondxzNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))<.005 &&
                                        Math.abs(10*diamondyThicknessNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))>Math.abs(y-3-13*diamondyNoise.getNoise((int) (-x + StartX / 2), (int) (-z + StartZ / 2)))) {
                                    Blocks[(int) (x)][(int) (y)][(int) (z)] = new Block(BlockType.Diamond);
                                }
                            } else {
                                if (y <= height - 1 || height <= WATER_LEVEL) {
                                    Blocks[(int) (x)][(int) (y)][(int) (z)] = new Block(BlockType.Dirt);
                                } else {
                                    Blocks[(int) (x)][(int) (y)][(int) (z)] = new Block(BlockType.Grass);
                                }

                            }
                        }
                    }
                }
            }
        }
    }
    
    //rebuilds the whole chunk mesh whenever it is updated
    public void rebuildMesh(){
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE)* 6 * 12);
        
        for (float x = 0; x < CHUNK_SIZE; x += 1){
            for (float z = 0; z < CHUNK_SIZE; z += 1){
                for(float y = 0; y < CHUNK_SIZE; y++){
                    try {
                        Blocks[(int)x][(int)y][(int)z].GetBlockType();
                        VertexPositionData.put(createCube((float) (x * CUBE_LENGTH), (float) (y * CUBE_LENGTH + (int) (CHUNK_SIZE * .8)), (float) (z * CUBE_LENGTH)));
                        VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int) x][(int) y][(int) z])));
                        VertexTextureData.put(createTexCube((float) 0, (float) 0, Blocks[(int) (x)][(int) (y)][(int) (z)]));
                    } catch (Exception e) {}
                }
            }
        }
        
        VertexColorData.flip();
        VertexPositionData.flip();
        VertexTextureData.flip();
        
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    //renders chunk
    public void render(){
        if (isVisible){
            glPushMatrix();
            glTranslatef(1 - StartX, -23 - StartY, 2 - StartZ);
            glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
            glVertexPointer(3, GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
            glColorPointer(3,GL_FLOAT, 0, 0L);
            glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
            glBindTexture(GL_TEXTURE_2D, 1);
            glTexCoordPointer(2,GL_FLOAT,0,0L);
            glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
            glPopMatrix();
        }
    }
    
    public void setBlock(float x, float y, float z, BlockType bt) {
        try{
            Blocks[(int)x][(int)y][(int)z] = new Block(bt);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void SetVisibility(boolean visibility){
        isVisible = visibility;
    }
    
    public boolean GetVisibility(){
        return isVisible;
    }
    
    public void SetChunkID(int ID){
        chunkID = ID;
    }
    
    public int GetChunkID(){
        return chunkID;
    }
    
    public void destroyBlock(float x, float y, float z) {
        try{
            Blocks[(int)x][(int)y][(int)z] = null;
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public BlockType getBlock(float x, float y, float z) {
        try{
            return Blocks[(int)x][(int)y][(int)z].GetBlockType();
        } catch (Exception e) {
            return null;
        }
    }
}
