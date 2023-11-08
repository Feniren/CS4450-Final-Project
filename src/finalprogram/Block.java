/***************************************************************
* file: Block.java
* author: Aidan, Jace
* class: CS 4450 Computer Graphics
*
* assignment: final program
* date last modified: 11/7/2023
*
* purpose: This holds block data
*
****************************************************************/
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalprogram;

/**
 *
 * @author Aidan
 */
public class Block{
    private int blockID;
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
