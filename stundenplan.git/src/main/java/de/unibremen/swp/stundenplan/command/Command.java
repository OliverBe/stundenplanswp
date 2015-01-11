package de.unibremen.swp.stundenplan.command;
/**
 * Interface als Schnittstelle f�r weitere Commands.
 * @author Roman
 *
 */
public interface Command {
	
	public abstract void execute();
	
	/**
	 * Muss " CommandHistory.deleteLast(); " implementieren
	 */
	public abstract void undo();
}