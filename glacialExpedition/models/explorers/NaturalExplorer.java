package glacialExpedition.models.explorers;

public class NaturalExplorer extends BaseExplorer{
    private static final int ENERGY_VALUE=60;

    public NaturalExplorer(String name) {
        super(name,ENERGY_VALUE);
    }
    @Override
    public void search() {
        this.setEnergy(this.getEnergy()-7);
    }
}
