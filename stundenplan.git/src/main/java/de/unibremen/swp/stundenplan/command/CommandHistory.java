package de.unibremen.swp.stundenplan.command;

import java.util.ArrayList;

/**
 * Verwaltet die History der ausgeführten Commands.
 * (Im Hinblick auf Undo-Funktion)
 * @author Roman
 *
 */
public class CommandHistory {

	private static ArrayList<Command> commandHistory = new ArrayList<>();
	
	public CommandHistory(){
		commandHistory = new ArrayList<>();
	}
	
	public static void addCommand(Command c){
		commandHistory.add(c);
	}
	
	public static Command getLast(){
		if(commandHistory.size() > 0) return commandHistory.get(commandHistory.size() - 1);
		
		System.out.println("[COMMANDHISTORY]: No Command in history yet.");
		return null;
	}
	
	public static void deleteLast(){
		try{
			commandHistory.remove(CommandHistory.getLast());
		}catch(NullPointerException n){
			System.out.println("[COMMANDHISTORY]: No Command in history yet.");
		}
	}
}
