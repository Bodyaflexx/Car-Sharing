package carsharing;

import com.beust.jcommander.JCommander;


public class Main {
    public static void main(String[] args) {
        Args ourArgs = new Args();
        JCommander.newBuilder()
                .addObject(ourArgs)
                .build()
                .parse(args);
        String name = ourArgs.getDatabaseFileName();
        if (name == null || name.isEmpty()) {
            name = "coolDatabase";
        }
        Menu menu = new Menu(name);
        menu.startMenu();

    }
}