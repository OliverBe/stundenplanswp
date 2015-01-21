/**
 * 
 */
package de.unibremen.swp.stundenplan.command;

import java.util.Set;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.exceptions.CommandHistoryException;

/**
 * Command-Klasse zum Hinzuf�gen von Jahrgangsbedarfen in die DB.
 * @author Roman
 * 
 */
public class AddJahrgangToDB implements Command {
	/**
	 * Jahrgang, wie er nach dem Hinzuf�gen aussehen soll.
	 */
	private Jahrgang jrg;
	/**
	 * Jahrgang, wie er vor dem Hinzuf�gen aussieht.
	 */
	private Jahrgang alt;

	/**
	 * Execute Methode dieser Klasse. Speichert den jetzigen Jahrgang unter Attribut alt ab.
	 * Speichert den neuen unter jrg. Leitet Einf�gen an Datenbank weiter. F�gt dieses Objekt
	 * der CommandHIstory hinzu.
	 * @param jahrgang
	 */
	public void execute(Jahrgang jahrgang) {
		alt = DataSchulklasse.getJahrgangByJahrgang(jahrgang.getJahrgang());
		jrg = jahrgang;
		DataSchulklasse.addJahrgang(jahrgang);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methode dieser Klasse. Pr�ft, wo die Unterschiede in den Bedarfen zwischen
	 * dem Jahrgang vor dem Hinzuf�gen und dem Jahrgang nach dem Hinzuf�gen. Dort, wo der Unterschied
	 * gefunden wird, wird das Stundeninhaltkuerzel an die Datenbank weitergeleitet, wo die L�schanfrage
	 * realisiert wird.
	 * 
	 * @exception CommandHIstoryException
	 * 			Wenn die beiden Jahr�gnge keine Differenz aufzeigen.
	 */
	@Override
	public void undo() {
		String kuerzel = null;
		Set<String> kuerzelVonNeu = jrg.getStundenbedarf().keySet();
		for (String s : kuerzelVonNeu) {
			if (!alt.getStundenbedarf().containsKey(s)) {
				kuerzel = s;
				DataSchulklasse.deleteJahrgangbedarfByJAndSkuerzel(
						jrg.getJahrgang(), kuerzel);
			}
		}
		if (kuerzel == null) {
			try {
				throw new CommandHistoryException(
						"Fehler bei Undo von add Jahrgangsbedarf: Kuerzel des"
								+ " Stundeninhaltes wurde nicht beim  gefunden.");
			} catch (CommandHistoryException e) {
				e.printStackTrace();
			}
		}
	}
}
