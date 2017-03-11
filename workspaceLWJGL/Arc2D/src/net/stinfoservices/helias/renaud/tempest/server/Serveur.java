package net.stinfoservices.helias.renaud.tempest.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Serveur implements Runnable {
	private final static boolean STOP_DTO = false;
	private final static boolean DEBUG = false;
	
	ServerSocket socket;
	private ObjectOutputStream output;
	private Socket pipe;
	private Thread thread;
	private boolean stop = false;
	private long pollSize = 0;
	
	private IPullMissiles pullable;
	
	public Serveur() {
		try {
			socket = new ServerSocket(12321);
			pipe = socket.accept();
			System.out.println("hello ");
			output = new ObjectOutputStream(pipe.getOutputStream());
			thread = new Thread(this);
			thread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public synchronized void poll(IPullMissiles pullable) {
		if (!STOP_DTO) {
			this.pullable = pullable;
		}
		if (DEBUG) {
			pollSize++;
		}
	}

	@Override
	public void run() {
		try {
			while (!stop ) {
				kick();
				Thread.sleep(100);
			}
			output.writeBoolean(false);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void kick() throws IOException {
		if (pullable == null) return;
		output.writeObject(pullable.getMissiles());
		if (DEBUG) {
			System.out.println("Serveur polling : "+pollSize);
			pollSize = 0;
		}
	}

	public synchronized void stop() {
		stop = true;
	}
}
