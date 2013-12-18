
/**
 * Class Item - an item in an adventure game.
 * 
 * @author Patryk Przybysz 
 * @version 2013-12-17
 */
public class Item {
    private String name;
    private String description;
    private boolean movable;
    private int flags; //Keeps track of event flags. Increases for every change in the environment.
    
    public Item(String name, String description, boolean movable) {
        this.name = name;
        this.description = description;
        this.movable = movable;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
}
