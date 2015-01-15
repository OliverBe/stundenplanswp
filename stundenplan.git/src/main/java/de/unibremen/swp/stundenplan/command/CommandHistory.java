package de.unibremen.swp.stundenplan.command;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.exceptions.CommandHistoryException;
import de.unibremen.swp.stundenplan.exceptions.StundenplanException;

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
	
	public static Command getLast() throws CommandHistoryException{
		if(commandHistory.size() > 0){ 
			return commandHistory.get(commandHistory.size() - 1);
		}else{
			throw new CommandHistoryException("Keine Befehle zum rückgängig machen verfügbar.");
		}
	}
	
	public static void deleteLast(){
		try{
			commandHistory.remove(CommandHistory.getLast());
		}catch(CommandHistoryException n){
			System.out.println("[COMMANDHISTORY]: No Command in history yet.");
		}
	}
}
