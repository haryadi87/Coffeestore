/**
 * @author Haryadi, Matthew, Nouras
 */
package com.coffeestore.control;

import java.awt.image.BufferedImage;
import java.util.List;

import com.coffeestore.communication.Communication;
import com.coffeestore.utils.OrderFood;
import com.coffeestore.utils.OrderMenu;
import com.coffeestore.view.ListMenuFragment;
import com.coffeestore.view.OrderMenuFragment;

/**
 * 
 * Mediator class that control every function
 *
 */
public class Mediator {
	private static Mediator instance;
	
	private OrderMenu orderMenu;
	private ListMenuFragment listMenuFragment;
	private OrderMenuFragment orderMenuFragment;
	private Communication communication;
	
	private boolean connected;
	
	private Mediator () {
		instance = null;
		this.orderMenu = null;
		this.listMenuFragment = null;
		this.communication = null;
		this.connected = false;
	}
	
	public static Mediator create() {
		if(instance == null) {
			instance = new Mediator();
		}
		return instance;
	}
	
	public void register(OrderMenu orderMenu) {
		this.orderMenu = orderMenu;
	}
	
	public void register(ListMenuFragment listMenuFragment) {
		this.listMenuFragment = listMenuFragment;
	}
	
	public void register(OrderMenuFragment orderMenuFragment) {
		this.orderMenuFragment = orderMenuFragment;
	}
	
	public void register(Communication communication) {
		this.communication = communication;
	}
	
	public int createOrderFood(String foodName, int quantity, BufferedImage image) {
		int foodID = -1;
		foodID = orderMenu.createOrderFood(foodName, quantity, image);
		this.updateDisplay();
		return foodID;
	}
	
	public int createOrderFood(String foodName, int quantity) {
		int foodID = -1;
		System.out.println(foodName + quantity);
		foodID = orderMenu.createOrderFood(foodName, quantity);
		this.updateDisplay();
		return foodID;
	}
	
	public List<OrderFood> listFood() {
		return orderMenu.listFood();
	}
	
	public void sendData(String string) {
		this.uploadOrder(string);
	}
	
	public void uploadOrder(String tableNumber) {
		List<OrderFood> food = this.listFood();
		communication.sendOrder(food,tableNumber);
	}
	
	public void deleteFood(int ID) {
		orderMenu.deleteFood(ID);
		this.updateDisplay();
	}
	
	public void updateDisplay() {
		List<OrderFood> food = this.listFood();
		
		orderMenuFragment.clearFood();
		orderMenuFragment.showFood(food);
	}
}