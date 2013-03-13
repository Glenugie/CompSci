package com.cs3517.mud;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MUDServiceInterface extends Remote {
	public boolean createMUD(String name) throws RemoteException;
	public boolean mudExists(String name) throws RemoteException;
	public String getMUDDetails(String name) throws RemoteException;
	public int getMUDNumber() throws RemoteException;
    public String getMUDList() throws RemoteException;
	public boolean playerExists(String mud, String playerName) throws RemoteException;
	public boolean serverFull(String mud) throws RemoteException;
	public boolean addPlayer(String mud, String playerName) throws RemoteException;
	public void removePlayer(String mud, String playerName) throws RemoteException;
	public String startLocation(String mud) throws RemoteException;
	public String endLocation(String mud) throws RemoteException;
	public String locationInfo(String mud, String loc, String currentThing) throws RemoteException;
	public String moveThing(String mud, String loc, String direction, String thing) throws RemoteException;
	public int getGold(String mud, String loc) throws RemoteException;
	public String[] getItems(String mud, String loc) throws RemoteException;
	public void delItem(String mud, String loc, String item) throws RemoteException;
}