package de.unibremen.swp.stundenplan.data;

import java.io.Serializable;






import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.unibremen.swp.stundenplan.db.Data;

/**
 * Repr�sentiert ein Fach 
 * 
 * @author Belavic, Oliver
 *
 */
@Entity
public final class Subject implements Serializable {

    /**
     * Die eineindeutige ID für Serialisierung.
     */
    private static final long serialVersionUID = 2597139574206115533L;
    
    /**
     * Der Name diese Faches.
     */
    @Column(nullable = false, length = Data.MAX_NORMAL_STRING_LEN)
    private String name;

    /**
     * Das K�rzel dieses Faches. Ein Kürzel muss systemweit eindeutig sein.
     */
    @Column(length = Data.MAX_ACRONYM_LEN)
    private String acronym;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    
    private Timeslot timeslot;
    /**
     * Gibt den Namen dieses Faches zurück.
     * 
     * @return den Namen dieses Faches
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen dieses faches auf den gegebenen Wert. Ein Parameterwert von {@code null} wird ignoriert.
     * 
     * @param pName
     *            der neue Name dieses Faches (falls nicht {@code null}
     */
    public void setName(final String pName) {
        name = pName;
    }

	/**
     * Gibt das Kürzel dieses Faches zurück.
     * 
     * @return das Kürzel dieses LehrerIn
     */
    public String getAcronym() {
        return acronym;
    }

	    /**
     * Setzt das Kürzel dieses Faches auf das übergebene Kürzel. Falls das Kürzel länger als
     * {@linkplain Data#MAX_ACRONYM_LEN} Zeichen ist, wird es entsprechend gekürzt. Führende und folgende
     * Leerzeichen werden entfernt. Löst eine {@link IllegalArgumentException} aus, falls das Kürzel leer ist.
     * 
     * Die systemweite Eindeutigkeit des Kürzels wird hier NICHT geprüft!
     * 
     * @param pAcronym
     *            das neue Kürzel dieser LehrerIn
     */
    public void setAcronym(final String pAcronym) {
        if (pAcronym == null || pAcronym.trim().isEmpty()) {
            throw new IllegalArgumentException("Kürzel der LehrerIn ist leer");
        }
        acronym = pAcronym.trim().substring(0, Math.min(Data.MAX_ACRONYM_LEN, pAcronym.length()));
    }
    
    public Timeslot getTimeslot(){
    	return timeslot;
    }
    
    public void setTimeslot(final Timeslot t){
    	if(t!= null){
    		timeslot = t;
    	}
    }

}
