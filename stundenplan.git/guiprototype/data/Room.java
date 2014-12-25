package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public final class Room implements Serializable {

    private static final long serialVersionUID = 3137139574206115533L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Id
    private String name;
    
    private String kuerzel;
    
    private int gebaude;
    
    private ArrayList<Integer> moeglicheStundeninhalte; 
    
    private ArrayList<Planungseinheit> planungseinheiten;

    /**
     * Gibt den Namen dieses Raumes zurück.
     * 
     * @return den Namen dieses Raumes
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen dieses Raumes auf den gegebenen Wert. Ein Parameterwert von {@code null} wird ignoriert.
     * 
     * @param pName
     *            der neue Name dieses Raumes (falls nicht {@code null}
     */
    public void setName(final String pName) {
        name = pName;
    }
}
