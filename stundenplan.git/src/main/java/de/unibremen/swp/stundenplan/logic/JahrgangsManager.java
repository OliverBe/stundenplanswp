package de.unibremen.swp.stundenplan.logic;

import de.unibremen.swp.stundenplan.command.AddJahrgangToDB;
import de.unibremen.swp.stundenplan.command.DeleteJahrgangsBedarfFromDB;
import de.unibremen.swp.stundenplan.command.EditJahrgangsBedarf;
import de.unibremen.swp.stundenplan.data.Jahrgang;

public class JahrgangsManager {
	
	public JahrgangsManager(){
		
	}
	
	public static void addBedarfToJahrgang(final Jahrgang jahrgang){
		AddJahrgangToDB addJrg = new AddJahrgangToDB();
		addJrg.execute(jahrgang);
	}
	
	public static void editBedarfFromJahrgang(final Jahrgang jahrgang){
		EditJahrgangsBedarf editJBdf = new EditJahrgangsBedarf();
		editJBdf.execute(jahrgang);
	}

	public static void deleteBedarfFromJahrgang(final Jahrgang jahrgang, final String kuerzelVonInhalt){
		DeleteJahrgangsBedarfFromDB deleteBdf = new DeleteJahrgangsBedarfFromDB();
		deleteBdf.execute(jahrgang, kuerzelVonInhalt);
	}
}
