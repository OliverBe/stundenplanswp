package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.DataRaum;

/**
 * Command-Klasse zum Einfügen einer Raumfunktion in die Datenbank.
 * @author Roman
 *
 */
public class AddRaumfunktionToDB implements Command {
	
	/**
	 * RF, die eingefügt wird.
	 */
	private Raumfunktion r;
	
	public AddRaumfunktionToDB(){
		
	}
	
	/**
     * Execute-Methode dieser Klasse. Fügt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingefügte RF als Attribut. Leitet Einfügen an Datenbank weiter.
     * @param rf
     * 		RF die hinzugefügt werden soll.
     */
	public void execute(Raumfunktion rf){
		r = rf;
		DataRaum.addRaumfunktion(rf);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet Löschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataRaum.deleteRaumfunktionByName(r.getName());
	}

}
