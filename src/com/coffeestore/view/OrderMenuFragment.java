/**
 *  @author haryadi, matthew, nouras
 */
package com.coffeestore.view;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.coffeestore.control.Mediator;
import com.coffeestore.utils.OrderFood;

public class OrderMenuFragment extends ListFragment{

	private Mediator mediator;
	
	private ArrayAdapter<String> listFoodAdapter;
	private List<String> listItems = new ArrayList<String>();
	
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
		this.mediator.register(this);
	}
	
	/**
	 * Creating view when this class is called
	 */
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		listFoodAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, listItems);
		setListAdapter(listFoodAdapter);
 
        return super.onCreateView(inflater, container, savedInstanceState);        
		}
	
	/**
	 * Delete food item if user click on the screen
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("Menu no: ", + position + " is selected.");
		mediator.deleteFood(position);
	}

	/**
	 * Clear all food
	 */
	public void clearFood() {
		// TODO Auto-generated method stub
		listFoodAdapter.clear();
	}

	public void showFood(List<OrderFood> food) {
		// TODO Auto-generated method stub
		for(OrderFood fd: food) {
			listFoodAdapter.add(fd.getFoodName().toString());
		}
	}
	
}


