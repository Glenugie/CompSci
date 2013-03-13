package com.cs3517.mud;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Random;

public class MUDServiceImpl implements MUDServiceInterface {
	private HashMap<String,MUD> mudList;
	private final int serverCap = 10;
	private final int playerCap = 10;
	
    public MUDServiceImpl() throws RemoteException {
    	mudList = new HashMap<String,MUD>();
    }
    
    public boolean createMUD(String name) throws RemoteException {
    	String[] mudTypes = new String[]{"forest"};
    	if (mudList.size() < serverCap && !name.equals("")) {
    		String mudName = mudTypes[new Random().nextInt(mudTypes.length)];
    		MUD newMUD = new MUD(mudName+".edg", mudName+".msg", mudName+".thg");
    		mudList.put(name, newMUD);
    		return true;
    	}
    	return false;
    }
    
    public boolean mudExists(String name) throws RemoteException {
    	return mudList.keySet().contains(name);
    }
    
    public String getMUDDetails(String name) throws RemoteException {
    	return "Currently connected to: "+name+" ("+mudList.get(name).getPlayerList().size()+"/"+playerCap+")";
    }
    
    public int getMUDNumber() throws RemoteException {
    	return mudList.size();
    }
    
    public String getMUDList() throws RemoteException {
    	String keys = "The following MUDs are currently running (MUD Limit "+mudList.size()+"/"+serverCap+"):\n";
    	for (String key : mudList.keySet()) { keys += key+" ("+mudList.get(key).getPlayerList().size()+"/"+playerCap+")\n";}
    	return keys;
    }

	public boolean playerExists(String mud, String playerName) throws RemoteException {
		boolean found = false;
		for (String name : mudList.get(mud).getPlayerList()) {
			if (name.equals(playerName)) { 
				found = true;
				break;
			}
		}
		return found;
	}
	
	public boolean serverFull(String mud) throws RemoteException {
		if (mudList.get(mud).getPlayerList().size() < playerCap) {
			return false;
		} else {
			return true;
		}
	}
	
    //Mud manipulation methods
	public boolean addPlayer(String mud, String playerName) throws RemoteException {
    	if (mudList.get(mud).getPlayerList().size() < playerCap) {
    		mudList.get(mud).addPlayer(playerName);
    		return true;
    	}
    	return false;
    }
	
	public void removePlayer(String mud, String playerName) throws RemoteException {
    	mudList.get(mud).removePlayer(playerName);
    	if (mudList.get(mud).getPlayerList().size() == 0) { mudList.remove(mud);}
    }
	
	public String startLocation(String mud) throws RemoteException {
		return mudList.get(mud).startLocation();
	}
	
	public String endLocation(String mud) throws RemoteException {
		return mudList.get(mud).endLocation();
	}
	
	public String locationInfo(String mud, String loc, String currentThing) throws RemoteException {
		return mudList.get(mud).locationInfo(loc, currentThing);
	}
	
	public String moveThing(String mud, String loc, String direction, String thing) throws RemoteException {
		return mudList.get(mud).moveThing(loc,direction,thing);
	}
	
	public int getGold(String mud, String loc) throws RemoteException {
		return mudList.get(mud).getGold(loc);
	}
	
	public String[] getItems(String mud, String loc) throws RemoteException {
		return (String[]) mudList.get(mud).getItems(loc).toArray();
	}
	
	public void delItem(String mud, String loc, String item) throws RemoteException {
		mudList.get(mud).delItem(loc,item);
	}
}
