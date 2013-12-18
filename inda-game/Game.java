/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Game {
    private Parser parser;
    private Player player;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        player = new Player();
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together. Add items to the rooms.
     */
    private void createRooms() {
        Room landingSpot, beach, shallows, jungle, jungleClearing, mountainSide, mountainTop,
            otherShallows, otherBeach, otherJungle, caveEntrance, caveInside, meadow;
      
        // create the rooms
        landingSpot = new Room("landing spot", "at the landing spot");
        beach = new Room("beach", "on a peaceful beach");
        shallows = new Room("shallows", "in shallow water near the island");
        jungle = new Room("jungle", "in the jungle");
        jungleClearing = new Room("jungle clearing", "in a jungle clearing");
        mountainSide = new Room("mountain side", "on a rocky mountain side");
        mountainTop = new Room("mountain top", "on top of a mountain");
        otherShallows = new Room("shallows, second island", "in shallow water near the second island");
        otherBeach = new Room("beach", "on the beach of the second island");
        otherJungle = new Room("jungle", "in the jungle on the second island");
        caveEntrance = new Room("cave entrance", "outside of a cave");
        caveInside = new Room("cave", "inside of a cave");
        meadow = new Room("meadow", "in a meadow");
        
        // initialise room exits
        landingSpot.setExit("north", mountainSide);
        landingSpot.setExit("south", beach);

        mountainSide.setExit("west", mountainTop);
        mountainSide.setExit("south", landingSpot);

        mountainTop.setExit("east", mountainSide);

        beach.setExit("north", landingSpot);
        beach.setExit("east", jungle);
        beach.setExit("south", shallows);
        
        jungle.setExit("west", beach);
        jungle.setExit("east", jungleClearing);

        shallows.setExit("north", beach);
        shallows.setExit("south", otherShallows);
        
        otherShallows.setExit("north", shallows);
        otherShallows.setExit("south", otherBeach);
        
        otherBeach.setExit("north", otherShallows);
        otherBeach.setExit("east", otherJungle);
        otherBeach.setExit("west", caveEntrance);
        
        caveEntrance.setExit("east", otherBeach);
        caveEntrance.setExit("down", caveInside);
        
        caveInside.setExit("up", caveEntrance);
        
        otherJungle.setExit("west", otherBeach);
        otherJungle.setExit("south", meadow);
        
        meadow.setExit("north", otherJungle);
        
        //Lock certain exits
        mountainSide.lockExit(mountainTop);
        
        shallows.lockExit(otherShallows);
        
        caveEntrance.lockExit(caveInside);
        
        //Create all the base items
        Item batteries = new Item("Batteries", "A pair of batteries. Seem to be rather new.", true);
        Item fish = new Item("Fish", "A dead fish. Seems to have been in this state for a while.", true);
        Item book = new Item("Book \"How To: Macgyver\"", "An old, dusty book about how to be Macgyver.", true);
        Item stick = new Item("Wooden Stick", "A wooden stick. Perfect for fighting of small mammals.", true);
        Item lighter = new Item("Lighter", "A seemingly new zippo lighter.", true);
        
        Item vines = new Item("Vines", "Sprawling vines run through this area.", false);
        Item bush = new Item("Bush", "A big bush. Looks mischievious.", false);
        Item muddyPuddle = new Item("Muddy Puddle", "A small puddle of mud. Something's moving in it", false);
        
        landingSpot.addItem(muddyPuddle);
        
        beach.addItem(stick);
        
        jungle.addItem(vines);
        
        jungleClearing.addItem(bush);
        
        mountainTop.addItem(batteries);
        mountainTop.addItem(lighter);
        
        caveEntrance.addItem(fish);
        
        caveInside.addItem(book);
        
        
        player.move(landingSpot);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() {     
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to Bear Grylls Simulator!");
        System.out.println("Bear Grylls Simulator is a new, incredibly amazing adventure game....about Bear Grylls.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            player.move(nextRoom);
            System.out.println(nextRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
