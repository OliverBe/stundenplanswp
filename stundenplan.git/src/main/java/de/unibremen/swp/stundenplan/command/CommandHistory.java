package de.unibremen.swp.stundenplan.command;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.exceptions.StundenplanException;

/**
 * Verwaltet die History der ausgef�hrten Commands. Funktioniert nach dem LIFO-Prinzip.
 * @author Roman
 *
 */
public class CommandHistory {

	/**
	 * Attribut um festzuhalten, ob das letzte Command vom Typ EditCommand ist. Ist im Hinblick
	 * auf die MainFrame n�tig, damit gezielter Updates am Personaleinsatzplan vollzogen werden k�nnen.
	 */
	private static boolean lastIsEditCommand = false;
	
	/**
	 * Liste der Commands in der History.
	 */
	private static ArrayList<Command> commandHistory = new ArrayList<>();
	
	/**
	 * Einf�gen eines Commands in die History. Ist das Command vom Typ EditCommand, wird
	 * das Attribut auf true gesetzt.
	 * 
	 * @param c
	 * 		Das Command, das hinzugef�gt werden soll.
	 */
	public static void addCommand(Command c){
		commandHistory.add(c);
		if(c instanceof EditCommand) lastIsEditCommand = true;
	}
	
	/**
	 * Gibt das letzte Command aus der History zur�ck.
	 * @return	Das letzte Command, das sich in die History aufgenommen wurde.
	 * @throws StundenplanException
	 * 			Wenn sich keine Commands in der History befinden.
	 */
	public static Command getLast() throws StundenplanException{
		if(commandHistory.size() > 0){ 
			return commandHistory.get(commandHistory.size() - 1);
		}else{
			throw new StundenplanException("Keine Befehle zum r�ckg�ngig machen verf�gbar.");
		}
	}
	
	/**
	 * l�scht das letzte Command aus der History.
	 */
	public static void deleteLast(){
		try{
			commandHistory.remove(CommandHistory.getLast());
			if(commandHistory.size()>0) if(getLast() instanceof EditCommand) lastIsEditCommand = true;
		}catch(StundenplanException n){
			System.out.println("[COMMANDHISTORY]: No Command in history yet.");
		}
	}
	
	/**
	 * Gibt Attribut zur�ck, das verwaltet, ob letztes Command ein EditCommand ist.
	 * @return
	 * 		true oder false, wenn Commando ein EditCommand oder nicht
	 */
	public static boolean isLastEditCommand(){
		return lastIsEditCommand;
	}
}
