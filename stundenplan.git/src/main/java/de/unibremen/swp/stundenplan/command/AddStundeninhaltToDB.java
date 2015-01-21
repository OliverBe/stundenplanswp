package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

/**
 * Command-Klasse zum Einf�gen eines Stundeninhalts in die Datenbank.
 * @author Roman
 *
 */
public class AddStundeninhaltToDB implements Command {

	/**
	 * SI, der eingef�gt wird.
	 */
	private Stundeninhalt inhalt;
	
	/**
     * Execute-Methode dieser Klasse. F�gt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingef�gte SI als Attribut. Leitet Einf�gen an Datenbank weiter.
     * @param st
     * 		SI der hinzugef�gt werden soll.
     */
	public void execute(Stundeninhalt st){
		inhalt = st;
		DataStundeninhalt.addStundeninhalt(st);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet L�schanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataStundeninhalt.deleteStundeninhaltByKuerzel(inhalt.getKuerzel());
	}

}
