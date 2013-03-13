package com.cs3517.mud;

// Represents an path in the MUD (an edge in a graph).
class Edge {
    public Vertex _dest;   // Your destination if you walk down this path
    public String _view;   // What you see if you look down this path
    public boolean passable;
    
    public Edge( Vertex d, String v, String pass) {
        _dest = d;
        _view = v;
        if (pass.equals("T")) { passable = true;}
        else if (pass.equals("F")) { passable = false;}
    }
}
