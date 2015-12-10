package com.company.network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.LinkedList;

public class Server {
	private int port;
	InetAddress ip;

	public ServerSocket serverSocket;

	private LinkedList<Connection> connections;

	public ConnectionListener connectionListener;

	private WaitForClients waitForClients;

	private boolean isRunning;

	public Server(int port) {
		this.port = port;
		isRunning = false;
	}

	public boolean start() {
		try {
			serverSocket = new ServerSocket(port);
			//Inet4Address  adres = new Inet4Address("tosh",);"172.24.22.14"
//			ip =   Inet4Address.getAllByName("tosh")[2];
//			int backlog = 0;
//			serverSocket = new ServerSocket(port,backlog, ip);
//			System.out.println(serverSocket.getInetAddress().toString());


		} catch (Exception ex) {
			return false;
		}

		isRunning = true;

		connections = new LinkedList<Connection>();
		connectionListener = new ConnectionListener(this);
		connectionListener.start();
		waitForClients = new WaitForClients(this);
		waitForClients.start();

		return true;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void stop() {
		isRunning = false;

		connectionListener.interrupt();
		waitForClients.interrupt();

		try {
			for (int i = 0; i < connections.size(); i++) {
				Connection connection = (Connection) connections.get(i);
				connection.close();
			}
			connections.clear();
			serverSocket.close();
		} catch (IOException ex) {
		}
	}

	public LinkedList<Connection> getConnections() {
		return connections;
	}
	
	public int getConnectionsCount() {
		return connections.size();
	}
}
