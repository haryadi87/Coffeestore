/**
 * @author haryadi, matthew, nouras
 *
 */
package com.coffeestore.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

public class CameraFragment extends Activity{
	int CAMERA_PIC_REQUEST = 1337;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if( requestCode == 1337)
	    {
	    //  data.getExtras()
	        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

	          /*Now you have received the bitmap..you can pass that bitmap to other activity
	          and play with it in this activity or pass this bitmap to other activity
	          and then upload it to server. */
	    }
	    else 
	    {
	        Toast.makeText(this, "Picture Not taken", Toast.LENGTH_LONG).show();
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}
}
