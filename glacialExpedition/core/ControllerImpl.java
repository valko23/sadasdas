package glacialExpedition.core;

import glacialExpedition.common.ConstantMessages;
import glacialExpedition.common.ExceptionMessages;
import glacialExpedition.models.explorers.AnimalExplorer;
import glacialExpedition.models.explorers.Explorer;
import glacialExpedition.models.explorers.GlacierExplorer;
import glacialExpedition.models.explorers.NaturalExplorer;
import glacialExpedition.models.mission.Mission;
import glacialExpedition.models.mission.MissionImpl;
import glacialExpedition.models.states.State;
import glacialExpedition.models.states.StateImpl;
import glacialExpedition.repositories.ExplorerRepository;
import glacialExpedition.repositories.StateRepository;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerImpl implements Controller {
    private ExplorerRepository explorerRepository;
    private StateRepository stateRepository;
    private int countExploredStates;

    public ControllerImpl() {
        explorerRepository=new ExplorerRepository();
        stateRepository=new StateRepository();
    }


    @Override
    public String addExplorer(String type, String explorerName) {
        Explorer explorer;
        switch (type) {
            case "AnimalExplorer":
                explorer = new AnimalExplorer(explorerName);
                break;
            case "GlacierExplorer":
                explorer = new GlacierExplorer(explorerName);
                break;
            case "NaturalExplorer":
                explorer = new NaturalExplorer(explorerName);
                break;
            default:
                throw new IllegalArgumentException("Explorer type doesn't exists.");
        }
        this.explorerRepository.add(explorer);
        return String.format(ConstantMessages.EXPLORER_ADDED,type,explorerName);
    }


    @Override
    public String addState(String stateName, String... exhibits) {

        State state = new StateImpl(stateName);
        state.getExhibits().addAll(Arrays.asList(exhibits));
        this.stateRepository.add(state);

        return String.format(ConstantMessages.STATE_ADDED,stateName);
    }

    @Override
    public String retireExplorer(String explorerName) {

        if (this.explorerRepository.getCollection().stream().noneMatch(a -> a.getName().equals(explorerName))) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.EXPLORER_DOES_NOT_EXIST,explorerName));
        }
        Explorer explorerToRemove = this.explorerRepository.byName(explorerName);
        this.explorerRepository.remove(explorerToRemove);
        return String.format(ConstantMessages.EXPLORER_RETIRED,explorerName);
    }


    @Override
    public String exploreState(String stateName) {

        List<Explorer> suitableExplorers = this.explorerRepository.getCollection().stream()
                .filter(a -> a.getEnergy() > 50).collect(Collectors.toList());

        if (suitableExplorers.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.STATE_EXPLORERS_DOES_NOT_EXISTS);
        }
        int countBeforeMission = suitableExplorers.size();
       Mission mission=new MissionImpl();
        State state = this.stateRepository.byName(stateName);
        mission.explore(state,suitableExplorers);
        countExploredStates++;
        int countAfterMission = suitableExplorers.size();
        return String.format(ConstantMessages.STATE_EXPLORER,stateName,countBeforeMission-countAfterMission);
    }


    @Override
    public String finalResult() {

        StringBuilder builder = new StringBuilder();
        builder.append(String.format(ConstantMessages.FINAL_STATE_EXPLORED, countExploredStates)).append(System.lineSeparator());
        builder.append(ConstantMessages.FINAL_EXPLORER_INFO).append(System.lineSeparator());

        this.explorerRepository.getCollection().forEach(a -> {
            builder.append(String.format(ConstantMessages.FINAL_EXPLORER_NAME, a.getName())).append(System.lineSeparator());
            builder.append(String.format(ConstantMessages.FINAL_EXPLORER_ENERGY, a.getEnergy())).append(System.lineSeparator());

            if (a.getSuitcase().getExhibits().size() <= 0) {
                builder.append(String.format(ConstantMessages.FINAL_EXPLORER_SUITCASE_EXHIBITS, "None")).append(System.lineSeparator());
            } else {
                Collection<String> items = a.getSuitcase().getExhibits();
                builder.append(String.format("Suitcase exhibits: %s", String.join(", ", items))).append(System.lineSeparator());
            }
        });
        return builder.toString().trim();
    }
}
