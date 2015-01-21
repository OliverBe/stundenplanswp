package de.unibremen.swp.stundenplan.command;
/**
 * Interface als Schnittstelle für weitere Commands.
 * Implementiert NICHT die Methode execute(), da unsere Execute-Methoden
 * verschiedene Anzahlen von Parametern benötigen.
 * @author Roman
 *
 */
public interface Command {

	public abstract void undo();
}