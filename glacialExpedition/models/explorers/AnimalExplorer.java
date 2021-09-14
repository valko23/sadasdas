package glacialExpedition.models.explorers;

public class AnimalExplorer extends BaseExplorer{
    private static final int ENERGY_VALUE=100;
    public AnimalExplorer(String name) {
        super(name, ENERGY_VALUE);
    }
}
