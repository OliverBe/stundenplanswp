/**
 * 
 */
package de.unibremen.swp.stundenplan.command;

import java.util.Set;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.exceptions.CommandHistoryException;

/**
 * @author Roman
 * 
 */
public class AddJahrgangToDB implements Command {

	private Jahrgang jrg;
	private Jahrgang alt;

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	public void execute(Jahrgang jahrgang) {
		alt = DataSchulklasse.getJahrgangByJahrgang(jahrgang.getJahrgang());
		jrg = jahrgang;
		DataSchulklasse.addJahrgang(jahrgang);
		CommandHistory.addCommand(this);
	}

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
