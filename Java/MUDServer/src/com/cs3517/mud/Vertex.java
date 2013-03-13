package com.cs3517.mud;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;

// Represents a location in the MUD (a vertex in the graph).
class Vertex {
    public String _name;             // Vertex name
    public String _msg = "";         // Message about this location
    public Map<String,Edge> _routes; // Association between direction: (e.g. "north") and a path, (Edge)
    public List<String> _things;     // The things (e.g. players) at: this location
    public ArrayList<String> _items;
    public ArrayList<String> _players;
    public int _gold;

    public Vertex( String nm ) {
		_name = nm; 
		_routes = new HashMap<String,Edge>(); // Not synchronised
		_things = new Vector<String>();       // Synchronised
		_items = new ArrayList<String>();
		_players = new ArrayList<String>();
		_gold = 0;
    }

    public String toString(String currentThing) {
		String summary = "\n";
		summary += _msg + "\n";
		for (String direction : _routes.keySet()) {
		    summary += "To the " + direction + " there is " + ((Edge)_routes.get( direction ))._view + "\n";
		}
		if (_items.size() > 0) {
			summary += "You can see the following items: ";
			for (String thing : _items) { summary += thing+", ";}
			summary = summary.substring(0,(summary.length()-2));
			summary += "\n";
		}
		if (_players.size() > 1) {
			summary += "You can see the following players: ";
			for (String thing : _players) { if (!thing.equals(currentThing)) { summary += thing+", ";}}
			summary = summary.substring(0,(summary.length()-2));
			summary += "\n";
		}
		if (_gold > 0) { summary += "You can see "+_gold+" gold on the ground\n";}
		return summary;
    }
}
