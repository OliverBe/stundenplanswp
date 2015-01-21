package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

/**
 * Command-Klasse zum Einfügen eines Stundeninhalts in die Datenbank.
 * @author Roman
 *
 */
public class AddStundeninhaltToDB implements Command {

	/**
	 * SI, der eingefügt wird.
	 */
	private Stundeninhalt inhalt;
	
	/**
     * Execute-Methode dieser Klasse. Fügt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingefügte SI als Attribut. Leitet Einfügen an Datenbank weiter.
     * @param st
     * 		SI der hinzugefügt werden soll.
     */
	public void execute(Stundeninhalt st){
		inhalt = st;
		DataStundeninhalt.addStundeninhalt(st);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet Löschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataStundeninhalt.deleteStundeninhaltByKuerzel(inhalt.getKuerzel());
	}

}
