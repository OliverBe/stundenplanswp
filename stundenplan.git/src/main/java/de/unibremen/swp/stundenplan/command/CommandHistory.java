package de.unibremen.swp.stundenplan.command;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.exceptions.StundenplanException;

/**
 * Verwaltet die History der ausgeführten Commands. Funktioniert nach dem LIFO-Prinzip.
 * @author Roman
 *
 */
public class CommandHistory {

	/**
	 * Attribut um festzuhalten, ob das letzte Command vom Typ EditCommand ist. Ist im Hinblick
	 * auf die MainFrame nötig, damit gezielter Updates am Personaleinsatzplan vollzogen werden können.
	 */
	private static boolean lastIsEditCommand = false;
	
	/**
	 * Liste der Commands in der History.
	 */
	private static ArrayList<Command> commandHistory = new ArrayList<>();
	
	/**
	 * Einfügen eines Commands in die History. Ist das Command vom Typ EditCommand, wird
	 * das Attribut auf true gesetzt.
	 * 
	 * @param c
	 * 		Das Command, das hinzugefügt werden soll.
	 */
	public static void addCommand(Command c){
		commandHistory.add(c);
		if(c instanceof EditCommand) lastIsEditCommand = true;
	}
	
	/**
	 * Gibt das letzte Command aus der History zurück.
	 * @return	Das letzte Command, das sich in die History aufgenommen wurde.
	 * @throws StundenplanException
	 * 			Wenn sich keine Commands in der History befinden.
	 */
	public static Command getLast() throws StundenplanException{
		if(commandHistory.size() > 0){ 
			return commandHistory.get(commandHistory.size() - 1);
		}else{
			throw new StundenplanException("Keine Befehle zum rückgängig machen verfügbar.");
		}
	}
	
	/**
	 * löscht das letzte Command aus der History.
	 */
	public static void deleteLast(){
		try{
			commandHistory.remove(CommandHistory.getLast());
			if(getLast() instanceof EditCommand) lastIsEditCommand = true;
		}catch(StundenplanException n){
			System.out.println("[COMMANDHISTORY]: No Command in history yet.");
		}
	}
	
	/**
	 * Gibt Attribut zurück, das verwaltet, ob letztes Command ein EditCommand ist.
	 * @return
	 * 		true oder false, wenn Commando ein EditCommand oder nicht
	 */
	public static boolean isLastEditCommand(){
		return lastIsEditCommand;
	}
}
