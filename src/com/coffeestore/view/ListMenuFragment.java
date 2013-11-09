/**
 * 
 */
package com.coffeestore.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.coffeestore.R;
import com.coffeestore.adapter.ListMenuAdapter;
import com.coffeestore.control.Mediator;

/**
 * Fragment of list menu layout
 * @author haryadi, matthew, nouras
 *
 */
public class ListMenuFragment extends ListFragment{
	private Mediator mediator;
	public ArrayList<String> menu;
	private List<String> listItems = new ArrayList<String>();
	


	public void setMediator(Mediator mediator) {
		// TODO Auto-generated method stub
		this.mediator = mediator;
		this.mediator.register(this);
	}
	
	/**
	 * Create an adapter when this class is called
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] names = getResources().getStringArray(R.array.code1_array);
		listItems = Arrays.asList(names);
		
		setListAdapter(new ListMenuAdapter(this.getActivity(), listItems));
	}
	
	/**
	 * Create order food when food item is click
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d("Menu no: ", + position + " is selected.");
		int newFoodIndex = mediator.createOrderFood((String)getListAdapter().getItem(position), 1);
	}

}
