package zorochase.neoarsenal.api;

public enum NeoArsenalTraits {
    // Tool traits
    NONE("None"),
    DEADLY("Deadly"),
    SILKY("Silky"),
    LUCKY("Lucky"),
    INCENDIARY("Incendiary"),
    EFFICIENT("Efficient"),
    MASSIVE("Massive"),
    // Portable Charger modes
    BURNING("Burning"),
    CHARGING("Charging");

    private final String IDENTIFIER;

    NeoArsenalTraits(String identifier) {
        this.IDENTIFIER = identifier;
    }

    public String getIdentifier() {
        return this.IDENTIFIER;
    }
}
