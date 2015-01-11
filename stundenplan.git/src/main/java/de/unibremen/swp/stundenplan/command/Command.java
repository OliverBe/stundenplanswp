package de.unibremen.swp.stundenplan.command;
/**
 * Interface als Schnittstelle für weitere Commands.
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