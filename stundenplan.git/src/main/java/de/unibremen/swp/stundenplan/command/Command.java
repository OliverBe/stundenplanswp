package de.unibremen.swp.stundenplan.command;
/**
 * Interface als Schnittstelle fuer weitere Commands.
 * Implementiert NICHT die Methode execute(), da unsere Execute-Methoden
 * verschiedene Anzahlen von Parametern benoetigen.
 * 
 * @author Roman
 *
 */
public interface Command {
	
	public abstract void undo();
}