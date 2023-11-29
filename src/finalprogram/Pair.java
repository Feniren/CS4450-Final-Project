/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalprogram;

/**
 *
 * @author Aidan
 */
public class Pair<T, U>{
	private T first;
	private U second;
	
	public Pair(T first, U second){
		this.first = first;
		this.second = second;
	}
	
	public T GetFirst(){
		return first;
	}
	
	public U GetSecond(){
		return second;
	}
	
	public void SetFirst(T first){
		this.first = first;
	}
	
	public void SetSecond(U second){
		this.second = second;
	}
}
