/**
 * @author Haryadi, Matthew, Nouras
 */
package com.coffeestore.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.util.Log;

import com.coffeestore.control.Mediator;
import com.coffeestore.utils.OrderFood;

/**
 * 
 * Communication class to create a connection between client and server
 *
 */
public class Communication {
	private static final String TAG = "Coffeestore:Communication";
	private static Communication instance = null;
	
	private Mediator mediator;
	
	private volatile String serverHost;
	private volatile int serverPort;
	private volatile Socket socket;
	private volatile boolean ServerConnected;
	private volatile int nextMessageId;
	
	private ExecutorService threads;
	private Handler receiveHandler = new Handler();
	
	private Communication(Mediator mediator, String serverHost, int serverPort) {
		this.mediator = mediator;
		mediator.register(this);
		
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.nextMessageId = 1000;
	}
	
	public static Communication create(Mediator mediator, String serverHost, int serverPort) {
		if(instance == null) {
			instance = new Communication(mediator, serverHost, serverPort);
		}
		return instance;
	}
	
	public boolean connect() {
		threads = Executors.newCachedThreadPool();
		threads.submit(new TcpServer(receiveHandler));
		return true;
	}
	
	public void disconnect() {
		if (this.socket != null) {
			try {
				threads.shutdownNow();
				socket.close();
			} catch (IOException e) {
				// Do nothing;
			} finally {
				socket = null;
			}
		}
	}
	
	private class TcpServer implements Runnable {
		private Handler handler;
		
		public TcpServer(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String message;
			BufferedReader tcpIn = null;
			int attempt = 0;
			System.out.println("Inside run method");
			InetSocketAddress address  = new InetSocketAddress(serverHost, serverPort);
			while (attempt < 3) {
				if (socket == null)	{
					try {
						socket = new Socket();
						socket.connect(address, 30*1000);
						tcpIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						break;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void sendOrder(List<OrderFood> order, String tableNumber) {
		String encoded = String.format("Table number:" + tableNumber +" Food order: ", order.size());
		for(OrderFood food: order) {
			encoded += String.format("%s,%d|", food.getFoodName(), food.getQuantity());
		}
		encoded = encoded.substring(0, encoded.length() -1);
		encoded = encoded.concat(" \n");
		threads.submit(new SendMessageRunner(encoded));
	}
	
	private class SendMessageRunner implements Runnable {
		String msg;
		
		public SendMessageRunner(String msg) {
			this.msg = msg;
		}
		@Override
		public void run() {
			try {
				sendMessage(msg); 
			} catch (MessageSendException e) {
				// Ignore
			}
		}
		
	}

	private void sendMessage(String msg) throws MessageSendException {
		Log.d(TAG, String.format("Sending data to %s:%d", 
				serverHost, serverPort));
		try {
			byte data[] = (msg).getBytes();
			socket.getOutputStream().write(data);
			socket.getOutputStream().flush();
		} catch (IOException e) {
			Log.e(TAG, "Error sending message to server");
			disconnect();
		}
	}
	
	private class MessageSendException extends Exception {
		public MessageSendException(String msg) {
			super(msg);
		}
	}
	
	
	
}