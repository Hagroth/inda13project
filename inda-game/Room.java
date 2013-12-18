import java.util.*;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Room {
    private String name;
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Room> lockedExits;    //Stores the locked exits.
    private ArrayList<Item> items;
    private boolean discovered;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        exits = new HashMap<String, Room>();
        lockedExits = new ArrayList<>();
        items = new ArrayList<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription() {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription() {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits. If the exits 
     * have been discovered, this method prints its name.
     * For example:
     * "Exits: north (beach) west (unknown)".
     * @return Details of the room's exits.
     */
    private String getExitString() {
        String returnString = "Exits:";
        Set<Map.Entry<String, Room>> entries = exits.entrySet();
        for(Map.Entry<String, Room> exit : entries) {
            String extraInfo = " (unknown)";
            if(exit.getValue().isDiscovered()) {
                extraInfo = " (" + exit.getValue() + ")";
            }
            returnString += " " + exit.getKey() + extraInfo;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) {
        return exits.get(direction);
    }
    
    /**
     * Overrides the standard toString method. Returns the room name.
     * @return The room name.
     */
    public String toString() {
        return name;
    }
    
    /**
     * Makes the room discovered.
     */
    public void discover() {
        discovered = true;
    }
    
    /**
     * Answers whether the room has been discovered or not.
     * @return True if already discovered, otherwise false.
     */
    public boolean isDiscovered() {
        return discovered;
    }
    
    /**
     * Locks the exit to the chosen room. Throws an IllegalArgumentException if
     * rooms are not connected.
     * @params A connected room to lock.
     */
    public void lockExit(Room room) {
        if(!exits.containsValue(room)) {
            throw new IllegalArgumentException("The rooms are not connected.");
        }
        lockedExits.add(room);
    }
    
    public void addItem(Item item) {
        items.add(item);
    }
    
    public void removeItem(String name) {
        for(Item i : items) {
            if(i.getName().equals(name)) {
                items.remove(i);
                break;
            }
        }
    }
}

