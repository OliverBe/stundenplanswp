package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

/**
 * Command-Klasse zum Einfuegen eines Stundeninhalts in die Datenbank.
 * @author Roman
 *
 */
public class AddStundeninhaltToDB implements Command {

	/**
	 * SI, der eingefuegt wird.
	 */
	private Stundeninhalt inhalt;
	
	/**
     * Execute-Methode dieser Klasse. Fuegt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingefuegte SI als Attribut. Leitet Einfuegen an Datenbank weiter.
     * @param st
     * 		SI der hinzugefuegt werden soll.
     */
	public void execute(Stundeninhalt st){
		inhalt = st;
		DataStundeninhalt.addStundeninhalt(st);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet Loeschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataStundeninhalt.deleteStundeninhaltByKuerzel(inhalt.getKuerzel());
	}

}
