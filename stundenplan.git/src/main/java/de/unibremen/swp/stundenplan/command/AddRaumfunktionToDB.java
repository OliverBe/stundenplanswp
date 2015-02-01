package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.DataRaum;

/**
 * Command-Klasse zum Einfuegen einer Raumfunktion in die Datenbank.
 * @author Roman
 *
 */
public class AddRaumfunktionToDB implements Command {
	
	/**
	 * RF, die eingefuegt wird.
	 */
	private Raumfunktion r;
	
	public AddRaumfunktionToDB(){
		
	}
	
	/**
     * Execute-Methode dieser Klasse. Fuegt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingefuegte RF als Attribut. Leitet Einfuegen an Datenbank weiter.
     * @param rf
     * 		RF die hinzugefuegt werden soll.
     */
	public void execute(Raumfunktion rf){
		r = rf;
		DataRaum.addRaumfunktion(rf);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet Loeschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataRaum.deleteRaumfunktionByName(r.getName());
	}

}
