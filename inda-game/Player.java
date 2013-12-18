import java.util.HashSet;
/**
 * Class Player - a player in an adventure game.
 * 
 * A Player object represents a person playing a game.
 * This object specifies the location of the player
 * and what he/she is carrying.
 * 
 * @author Patryk Przybysz   
 * @version 2013-12-17
 */
public class Player {
    private Room currentRoom;
    private HashSet<Item> inventory;
    
    public Player() {
        inventory = new HashSet<>();
    }
    
    /**
     * Moves directly to the specified room.
     * @param The room where the player should move.
     */
    public void move(Room room) {
        currentRoom = room;
        room.discover();
    }
    
    /**
     * Returns the current location of the player.
     * @return The current room of the player.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }
}
