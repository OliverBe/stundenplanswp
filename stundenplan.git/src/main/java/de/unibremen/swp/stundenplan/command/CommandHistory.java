package de.unibremen.swp.stundenplan.command;

import java.util.ArrayList;

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
		return null;
	}
	
	public static void deleteLast(){
		commands.remove(commands.get(commands.size()-1));	
	}
}
