package de.unibremen.swp.stundenplan.command;
/**
 * Interface als Schnittstelle f�r weitere Commands.
 * Implementiert NICHT die Methode execute(), da unsere Execute-Methoden
 * verschiedene Anzahlen von Parametern ben�tigen.
 * @author Roman
 *
 */
public interface Command {

	public abstract void undo();
}