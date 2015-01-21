package de.unibremen.swp.stundenplan.command;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.exceptions.StundenplanException;

/**
 * Verwaltet die History der ausgeführten Commands.
 * (Im Hinblick auf Undo-Funktion)
 * @author Roman
 *
 */
public class CommandHistory {

	private static boolean lastIsEditCommand = false;
	private static ArrayList<Command> commandHistory = new ArrayList<>();
	
	public CommandHistory(){
		commandHistory = new ArrayList<>();
	}
	
	public static void addCommand(Command c){
		commandHistory.add(c);
		if(c instanceof EditCommand) lastIsEditCommand = true;
	}
	
	public static Command getLast() throws StundenplanException{
		if(commandHistory.size() > 0){ 
			return commandHistory.get(commandHistory.size() - 1);
		}else{
			throw new StundenplanException("Keine Befehle zum rückgängig machen verfügbar.");
		}
	}
	
	public static void deleteLast(){
		try{
			commandHistory.remove(CommandHistory.getLast());
			if(getLast() instanceof EditCommand) lastIsEditCommand = true;
		}catch(StundenplanException n){
			System.out.println("[COMMANDHISTORY]: No Command in history yet.");
		}
	}
	
	public static boolean isLastEditCommand(){
		return lastIsEditCommand;
	}
}
