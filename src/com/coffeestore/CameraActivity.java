package com.coffeestore;
/*
 * @author Haryadi, Matthew, Nouras
 * Get the file from: http://sourceforge.net/projects/zbar/files/AndroidSDK/
 */

import java.util.ArrayList;
import java.util.Arrays;

import com.coffeestore.client.Htcp2Client;
import com.coffeestore.qrcode.CameraPreview;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Button;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.widget.TextView;
import android.graphics.ImageFormat;

/* Import ZBar Class files */
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;
import android.view.Menu;

public class CameraActivity extends Activity {

		public final static String EXTRA_MESSAGE = "com.coffeestore.buffer";
	
		private Camera mCamera;
	    private CameraPreview mPreview;
	    private Handler autoFocusHandler;
	    private View view;
	    private Intent intent;

	    TextView scanText;
	    Button scanButton;
	    ImageScanner scanner;

	    private boolean barcodeScanned = false;
	    private boolean previewing = true;

	    static {
	        System.loadLibrary("iconv");
	    } 

	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.activity_main);

	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	        autoFocusHandler = new Handler();
	        mCamera = getCameraInstance();

	        /* Instance barcode scanner */
	        scanner = new ImageScanner();
	        scanner.setConfig(0, Config.X_DENSITY, 3);
	        scanner.setConfig(0, Config.Y_DENSITY, 3);

	        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
	        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
	        preview.addView(mPreview);

	      // scanText = (TextView)findViewById(R.id.scanText);

	       scanButton = (Button)findViewById(R.id.ScanButton);

	       scanButton.setOnClickListener(new OnClickListener() {
	                public void onClick(View v) {
	                    if (barcodeScanned) {
	                        barcodeScanned = false;
	                        scanText.setText("Scanning...");
	                        mCamera.setPreviewCallback(previewCb);
	                        mCamera.startPreview();
	                        previewing = true;
	                        mCamera.autoFocus(autoFocusCB);
	                    }
	                }
	            });
	    }
	    
	    @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

	    public void onPause() {
	        super.onPause();
	        releaseCamera();
	    }

	    /** A safe way to get an instance of the Camera object. */
	    public Camera getCameraInstance()
	    {

	        for(int i = 0; i < Camera.getNumberOfCameras(); i++)
	            return Camera.open(i);

	        return null;
	    }

	    private void releaseCamera() {
	        if (mCamera != null) {
	            previewing = false;
	            mCamera.setPreviewCallback(null);
	            mCamera.release();
	            mCamera = null;
	        }
	    }

	    private Runnable doAutoFocus = new Runnable() {
	            public void run() {
	                if (previewing)
	                    mCamera.autoFocus(autoFocusCB);
	            }
	        };

	    PreviewCallback previewCb = new PreviewCallback() {
	            public void onPreviewFrame(byte[] data, Camera camera) {
	                Camera.Parameters parameters = camera.getParameters();
	                Size size = parameters.getPreviewSize();

	                Image barcode = new Image(size.width, size.height, "Y800");
	                barcode.setData(data);

	                int result = scanner.scanImage(barcode);
	                
	                if (result != 0) {
	                    previewing = false;
	                    mCamera.setPreviewCallback(null);
	                    mCamera.stopPreview();
	                    
	                    SymbolSet syms = scanner.getResults();
	                    for (Symbol sym : syms) {
	                    	//TODO: create a socket connection to the server
	                        //scanText.setText("barcode result " + sym.getData());
	                    	sendMessage(view, sym.getData());
	                        barcodeScanned = true;
	                    }
	                }
	            }
	        };
	        
	    public void sendMessage(View view, String string) {
	    	 Intent intent = new Intent(this, MenuActivity.class);
	    	 intent.putExtra(EXTRA_MESSAGE, string);
	    	 startActivity(intent);

	    }    
	        
	    // Mimic continuous auto-focusing
	    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
	            public void onAutoFocus(boolean success, Camera camera) {
	                autoFocusHandler.postDelayed(doAutoFocus, 1000);
	            }
	        };
}
