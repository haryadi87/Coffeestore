/**
 * @author Haryadi, Matthew, Nouras
 */

package com.coffeestore.utils;

import java.awt.image.BufferedImage;

public class OrderFood {
	private int sequenceNumber;
	
	private String foodName;
	private int quantity;
	private BufferedImage image;
	
	/**
	 * Constructor to create a food
	 * @param sequence
	 * @param foodname
	 * @param quantity
	 * @param image
	 */
	public OrderFood(int sequence, String foodname, int quantity, BufferedImage image) {
		this.sequenceNumber = sequence;
		this.foodName = foodname;
		this.quantity = quantity;
		this.image = image;		
	}
	
	public OrderFood(int sequence, String foodname, int quantity) {
		this.sequenceNumber = sequence;
		this.foodName = foodname;
		this.quantity = quantity;
	}
	/**
	 *Accessor method for returning the sequence number
	 * @return
	 */
	public int getSequence() {
		return this.sequenceNumber;
	}
	
	/**
	 * Accessor method for returning food name
	 * @return
	 */
	public String getFoodName() {
		return this.foodName;
	}
	
	/**
	 * Accessor method for returning quantity
	 * @return
	 */
	public int getQuantity() {
		return this.quantity;
	}
	
	/**
	 * Accessor method for getting Image
	 * @return
	 */
	public BufferedImage getImage() {
		return this.image;
	}
	
	/**
	 * Set sequence number
	 * @param sequence
	 */
	public void setSequenceNumber (int sequence) {
		this.sequenceNumber = sequence;
	}
	
	/**
	 * Set food name
	 * @param foodname
	 */
	public void setFoodName(String foodname) {
		this.foodName = foodname;
	}
	
	/**
	 * set quantity
	 * @param quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * set Image
	 * @param image
	 */
	public void setImage (BufferedImage image) {
		this.image = image;
	}
}