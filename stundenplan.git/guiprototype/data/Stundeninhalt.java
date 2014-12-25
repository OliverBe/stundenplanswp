package data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Stundeninhalt {
    /**
     * Die eineindeutige ID für Serialisierung.
     */
    private static final long serialVersionUID = 3137139574206115533L;

    /**
     * Die eindeutige, von der unterliegenden Persistenzschicht automatisch erzeugte ID.
     */
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private String name;
    
    private String kuerzel;
    
    private int Regeldauer;
    
    // 0 ist pause, 1 ist leicht, 2 ist schwer
    private int rythmusTyp;
}
