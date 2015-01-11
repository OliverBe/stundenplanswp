package de.unibremen.swp.stundenplan.command;
/**
 * Interface als Schnittstelle f�r weitere Commands.
 * @author Roman
 *
 */
public interface Command {
	
	/**
	 * Muss " CommandHistory.addCommand(this); " implementieren
	 */
	public abstract void execute();
	
	public abstract void undo();
}