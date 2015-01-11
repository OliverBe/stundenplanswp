package de.unibremen.swp.stundenplan.command;

import java.util.ArrayList;

/**
 * Verwaltet die History der ausgeführten Commands.
 * (Im Hinblick auf Undo-Funktion)
 * @author Roman
 *
 */
public class CommandHistory {

	private static ArrayList<Command> commands = new ArrayList<>();
	
	public CommandHistory(){
		commands = new ArrayList<>();
	}
	
	public static void addCommand(Command c){
		commands.add(c);
	}
	
	public static Command getLast(){
		if(commands.size() > 0) return commands.get(commands.size() - 1);
		
		System.out.println("[COMMANDHISTORY]: No Command in history yet.");
		return null;
	}
	
	public static void deleteLast(){
		commands.remove(CommandHistory.getLast());	
	}
}
