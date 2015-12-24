package es.uvigo.esei.dgss.letta.domain.entities;

public class CapitalParameters {
	
	//Disallow constructor
	private CapitalParameters() { }
	

    /**
     * Returns a valid {@link Capital}.
     *
     * @return a valid name of {@link Capital}.
     */
    public static String aCapital() {
        return "Segovia";
    }
    
    /**
     * Returns a non existent valid {@link Capital}.
     *
     * @return a non existent valid name of {@link Capital}.
     */
    public static String aNonExistentCapital() {
        return "Paris";
    }

    /**
     * Returns a valid title different from {@link #aNonExistentCapital()}.
     *
     * @return a valid event title different from {@link #aNonExistentCapital()}.
     */
    public static String anotherNonExistentCapital() {
        return "London";
    }
    
    /**
     * Returns a valid distance.
     *
     * @return a valid distance.
     */
    public static int aDistance() {
        return 327;
    }
    
    
    /**
     * Returns a valid distance different from {@link #aDistance()}.
     *
     * @return a valid distance different from {@link #aDistance()}.
     */
    public static int anotherDistance() {
        return 672;
    }

}
