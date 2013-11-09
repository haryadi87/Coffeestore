/**
 * @author Haryadi,Matthew,Nouras
 */

package com.coffeestore.utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.coffeestore.control.Mediator;


public class OrderMenu {
	private static OrderMenu instance;
	
	private Mediator mediator;
	private List<OrderFood> orderFood;
	private final int MAX_FOOD = 100;
	
	/**
	 * Constructor of OrderMenu
	 * @param mediator
	 */
	private OrderMenu(Mediator mediator) {
		this.mediator = mediator;
		this.orderFood = new ArrayList<OrderFood> (100);
	}
	
	public static OrderMenu create(Mediator mediator) {
		if (instance == null) {
			instance = new OrderMenu(mediator);
			mediator.register(instance);
		}
		return instance;
	}
	
	/**
	 * Create orderFood 
	 * @param foodName
	 * @param quantity
	 * @param image
	 * @return
	 */
	public int createOrderFood(String foodName, int quantity, BufferedImage image) {
		int sequence = -1;
		if (orderFood.size() < MAX_FOOD) {
			sequence = orderFood.size();
			orderFood.add(new OrderFood(sequence, foodName, quantity, image));
		}
		return sequence;
	}
	
	public int createOrderFood(String foodName, int quantity) {
		int sequence = -1;
		if (orderFood.size() < MAX_FOOD) {
			sequence = orderFood.size();
			orderFood.add(new OrderFood(sequence, foodName, quantity));
		}
		return sequence;
	}
	
	/**
	 * Return a list of orderFood
	 * @return
	 */
	public List<OrderFood> listFood() {
		return orderFood;
	}
	
	/**
	 * delete food
	 * @param ID
	 */
	public void deleteFood(int ID) {
		orderFood.remove(ID);
		this.resequenceFoods();
	}
	
	/**
	 * get orderfood id
	 * @param orderFoodID
	 * @return
	 */
	public OrderFood getOrderFoodByID(int orderFoodID) {
		return orderFood.get(orderFoodID);
	}
	
	/**
	 * resequence food
	 */
	private void resequenceFoods() {
		int i = 0;
		for (OrderFood order : orderFood) {
			order.setSequenceNumber(i++);
		}
	}
	
	/**
	 * edit food
	 * @param sequence
	 * @param quantity
	 * @return
	 */
	public OrderFood editFood(int sequence, int quantity) {
		OrderFood food = this.getOrderFoodByID(sequence);
		food.setQuantity(quantity);
		return food;
	}
	
	/**
	 * clear food
	 */
	public void clearFoods() {
		this.orderFood = new ArrayList<OrderFood>(MAX_FOOD);
	}
}