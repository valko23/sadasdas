package glacialExpedition.models.explorers;

public class GlacierExplorer extends BaseExplorer{
    private static final int ENERGY_VALUE=40;
    public GlacierExplorer(String name) {
        super(name, ENERGY_VALUE);
    }
}
