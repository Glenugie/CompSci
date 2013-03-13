package com.cs3517.mud;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class MUDClient {
    public static void main(String args[]) throws RemoteException {
	    if (args.length < 2) {
            System.err.println( "Usage:\njava mudClient  " ) ;
	        return;
        }
        String hostname = args[0];
	    int registryport = Integer.parseInt(args[1]);
        System.setProperty("java.security.policy", "rmimud.policy");
        System.setSecurityManager(new RMISecurityManager());
        try {
            String regURL = "rmi://" + hostname + ":" + registryport + "/mudService";
            System.out.println("Looking up " + regURL);
            MUDServiceInterface mudservice = (MUDServiceInterface)Naming.lookup(regURL);

            //Running Code
            /*BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter message:");
            String message = in.readLine();
            String mudResult = mudservice.mud(message);
            System.out.println(mudResult);*/
            
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            //MUD mud = null;
            
            //Set up mud server
            System.out.println("Welcome to the MUD Service");
            String mudList = mudservice.getMUDList();
        	String mud = "";
            if (mudservice.getMUDNumber() == 0) {
            	System.out.println("There are currently no MUDs running, please enter a MUD name to start one");
                while (mud.equals("") || !mudservice.createMUD(mud)) { 
                	if (!mud.equals("")) { System.out.println("There are too many MUDs running at the present, please join an existing MUD");}
                	System.out.print("> ");
                	mud = in.readLine();
            	}
            } else {
            	System.out.println(mudList);
            	System.out.println("Enter a MUD name to start, if it doesn't exist then it will be created");
                while (mud.equals("") || ((!mudservice.mudExists(mud) && !mudservice.createMUD(mud)) || (mudservice.mudExists(mud) && mudservice.serverFull(mud)))) { 
                	if (!mud.equals("")) { 
                		if ((!mudservice.mudExists(mud) && !mudservice.createMUD(mud))) { System.out.println("There are too many MUDs running at the present, please join an existing MUD");}
                		else { System.out.println("This MUD is full, please create a new MUD or join another");}
                	}
                	System.out.print("> ");
                	mud = in.readLine();
            	}
            }
        	System.out.println("\nWelcome to "+mud);
        	
            //Set up player name
            System.out.println("What is your name?");
            String playerName = "";
            while (playerName.equals("") || mudservice.playerExists(mud, playerName)) { 
            	if (mudservice.playerExists(mud, playerName)) { System.out.println("Player name already exists");}
            	System.out.print("> ");
            	playerName = in.readLine();
            }
            
            int playerGold = 100;
            ArrayList<String> inventory = new ArrayList<String>();

            mudservice.addPlayer(mud,playerName);
            System.out.println("You find yourself trapped in one of the Land of Ennui's many areas");
            System.out.println("You are carrying "+playerGold+"G in your wallet");

            String playerLocation = mudservice.startLocation(mud);
            System.out.println(mudservice.locationInfo(mud, playerLocation,playerName));
            
            String command = "";
            boolean quit = false;
            while (!quit) {
            	System.out.println("Enter Command:");
            	System.out.print("> ");
            	command = in.readLine().toLowerCase();
        		String preLocation = playerLocation;
            	switch (command) {
	            	case "go north": case "north": case "up":
	            		playerLocation = mudservice.moveThing(mud, playerLocation,"north",playerName);
	            		if (preLocation.equals(playerLocation)) {
	            			System.out.println("You are unable to travel North");
	            		} else {
	            			System.out.println("You travel North");
	            			System.out.println(mudservice.locationInfo(mud, playerLocation,playerName));
	            		}
	            		break;
	            	case "go east": case "east": case "right":
	            		playerLocation = mudservice.moveThing(mud, playerLocation,"east",playerName);
	            		if (preLocation.equals(playerLocation)) {
	            			System.out.println("You are unable to travel East");
	            		} else {
	            			System.out.println("You travel East");
	            			System.out.println(mudservice.locationInfo(mud, playerLocation,playerName));
	            		}
	            		break;
	            	case "go south": case "south": case "down":
	            		playerLocation = mudservice.moveThing(mud, playerLocation,"south",playerName);
	            		if (preLocation.equals(playerLocation)) {
	            			System.out.println("You are unable to travel South");
	            		} else {
	            			System.out.println("You travel South");
	            			System.out.println(mudservice.locationInfo(mud, playerLocation,playerName));
	            		}
	            		break;
	            	case "go west": case "west": case "left":
	            		playerLocation = mudservice.moveThing(mud, playerLocation,"west",playerName);
	            		if (preLocation.equals(playerLocation)) {
	            			System.out.println("You are unable to travel West");
	            		} else {
	            			System.out.println("You  travel West");
	            			System.out.println(mudservice.locationInfo(mud, playerLocation,playerName));
	            		}
	            		break;
	            	case "check wallet": case "gold": case "money": case "wallet":
	                    System.out.println("You are carrying "+playerGold+"G in your wallet");
	                    break;
	            	case "check inventory": case "inventory": case "items":
	            		if (inventory.size() == 0) {
		            		System.out.println("You have nothing in your inventory");
	            		} else {
	            			String inventoryString = "You have the following items in your inventory: ";
	            			for (String thing : inventory) {
	            				inventoryString += thing+", ";
	            			}
	            			inventoryString = inventoryString.substring(0,(inventoryString.length()-2));
	            			System.out.println(inventoryString);
	            		}
	            		break;
	            	case "look around": case "look": case "search":
	            		System.out.println(mudservice.locationInfo(mud, playerLocation,playerName));
	            		break;
	            	case "pick up gold": case "take gold": case "get gold":
	            		int gold = mudservice.getGold(mud, playerLocation);
	            		if (gold > 0) {
	            			playerGold += gold;
	            			System.out.println("You put the gold in your wallet");
		                    System.out.println("You are carrying "+playerGold+"G in your wallet");
	            		} else {
	            			System.out.println("There's no gold to pick up");
	            		}
	            		break;
	            	case "mud": case "server":
	            		System.out.println(mudservice.getMUDDetails(mud));
	            		break;
	            	case "quit": case "bye": case "exit": case "end": case "stop":
	            		System.out.println("Goodbye");
	            		mudservice.removePlayer(mud, playerName);
	            		quit = true;
	            		break;
	            	default:
	            		if (command.startsWith("pick up ") || command.startsWith("take ") || command.startsWith("get ")) {
	            			String item = command.substring(8);
	            			boolean found = false;
	            			for (String thing : mudservice.getItems(mud, playerLocation))  { if (thing.equals(item)) { found = true;}}
	            			if (found) {
	            				inventory.add(item);
	            				mudservice.delItem(mud, playerLocation, item);
	            				System.out.println("You add "+item+" to you inventory");
	            			} else {
	            				System.out.println("Item "+item+" not recognised");
	            			}
	            		} else {
	            			System.out.println("Unrecognised Command");
	            		}
	            		break;
            	}
            	System.out.println("");
            	
            	if (!quit && playerLocation.equals(mudservice.endLocation(mud))) {
            		System.out.println("You have reached the end of the MUD, congratulations "+playerName);
            		System.out.println("You have escaped successfully from the Land of Ennui, thank you for playing");
            		quit = true;
            	}
            }
        } catch (java.io.IOException e) {
            System.err.println("I/O error.");
	        System.err.println(e.getMessage());
        } catch (java.rmi.NotBoundException e) {
            System.err.println("Server not bound.");
	        System.err.println(e.getMessage());
        }
    }
}