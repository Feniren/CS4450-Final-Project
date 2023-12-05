/***************************************************************
* file: Block.java
* author: Aidan, Jace
* class: CS 4450 Computer Graphics
*
* assignment: final program
* date last modified: 12/5/2023
*
* purpose: This holds block data
*
****************************************************************/

package finalprogram;

public class Block{
    private int blockID;
    private int chunkID;
    private boolean isActive;
    private BlockType type;
    private float x;
    private float y;
    private float z;
    
    //constructor
    public Block(BlockType type){
        this.type = type;
    }
    
    //sets block coordinates
    public void SetCoordinates(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    //returns block ID (depricated)
    public int GetBlockID(){
        return blockID;
    }
    
    public void SetChunkID(int ID){
        chunkID = ID;
    }
    
    public int GetChunkID(){
        return chunkID;
    }
    
    
    //returns if the block should be visible
    public boolean GetIsActive(){
        return isActive;
    }
    
    //returns block type
    public BlockType GetBlockType(){
        return type;
    }
    
    
    //sets if block is active
    public void SetIsAvtive(boolean active){
        isActive = active;
    }
}
