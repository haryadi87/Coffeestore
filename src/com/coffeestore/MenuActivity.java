/**
 * @author Haryadi,Matthew,Nouras
 */

package com.coffeestore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.coffeestore.communication.Communication;
import com.coffeestore.control.Mediator;
import com.coffeestore.utils.OrderMenu;
import com.coffeestore.view.ListMenuFragment;
import com.coffeestore.view.OrderMenuFragment;

public class MenuActivity extends Activity {

	private String[] tabledata;
	private Button button;
	
	private static Mediator mediator;
	private static ListMenuFragment listMenuFragment ;
	private static OrderMenuFragment orderMenuFragment;
	private static OrderMenu orderMenu;
	private static Communication communication;
	
	/**
	 * When class is created, all components is initialised
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		// Show the Up button in the action bar.
		setupActionBar();
		addButtonListener();
		Intent intent = getIntent();
		
		String buffer = intent.getStringExtra(CameraActivity.EXTRA_MESSAGE);
		tabledata = buffer.split(",");
		communication = communication.create(mediator, tabledata[0], Integer.parseInt(tabledata[1]));
		communication.connect();
		
		TextView textView = (TextView) findViewById(R.id.textview);
		textView.setTextSize(40);
		textView.setText("Order table no " +tabledata[2]);

	}
	
	/**
	 * Listener for a button
	 */	
	public void addButtonListener() {
		button = (Button) findViewById(R.id.send);
		button.setOnClickListener(new ButtonHandler());
	}
	
	/**
	 * 
	 * Send data to the server
	 *
	 */
	public class ButtonHandler implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			mediator.sendData(tabledata[2]);
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mediator = Mediator.create();
		orderMenu = OrderMenu.create(mediator);
		listMenuFragment = (ListMenuFragment) getFragmentManager().findFragmentById(R.id.listmenu);
		listMenuFragment.setMediator(mediator);
		orderMenuFragment = (OrderMenuFragment) getFragmentManager().findFragmentById(R.id.ordermenu);
		orderMenuFragment.setMediator(mediator);
		Log.d("Food", "Components initialised");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
