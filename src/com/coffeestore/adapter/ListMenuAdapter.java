/**
 * @author Haryadi, Matthew, Nouras
 */
package com.coffeestore.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coffeestore.R;

/**
 * 
 * List menu adapter for custome adapter in list menu
 *
 */
public class ListMenuAdapter extends ArrayAdapter<String>{
	private final Context context;
	private final List<String> values;
	
	public ListMenuAdapter(Context context, List<String> values) {
		super(context, R.layout.list_menu, values);
		this.context=context;
		this.values=values;
	}
	
	@Override
	public View getView(int position, View convertView,ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.list_menu, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.listfooddesc);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.listmenuimage);
		textView.setText(values.get(position));
		
		return rowView;
	}
	
	
}