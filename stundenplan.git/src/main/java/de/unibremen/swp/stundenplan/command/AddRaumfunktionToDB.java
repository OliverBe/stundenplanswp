package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.DataRaum;

/**
 * Command-Klasse zum Einf�gen einer Raumfunktion in die Datenbank.
 * @author Roman
 *
 */
public class AddRaumfunktionToDB implements Command {
	
	/**
	 * RF, die eingef�gt wird.
	 */
	private Raumfunktion r;
	
	public AddRaumfunktionToDB(){
		
	}
	
	/**
     * Execute-Methode dieser Klasse. F�gt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingef�gte RF als Attribut. Leitet Einf�gen an Datenbank weiter.
     * @param rf
     * 		RF die hinzugef�gt werden soll.
     */
	public void execute(Raumfunktion rf){
		r = rf;
		DataRaum.addRaumfunktion(rf);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet L�schanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataRaum.deleteRaumfunktionByName(r.getName());
	}

}
