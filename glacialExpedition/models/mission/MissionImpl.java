package glacialExpedition.models.mission;

import glacialExpedition.models.explorers.Explorer;
import glacialExpedition.models.states.State;

import java.util.List;

public class MissionImpl implements Mission {

    @Override
    public void explore(State state, List<Explorer> explorers) {

        for (int index = 0; index < explorers.size(); index++) {
            Explorer currentExplorer = explorers.get(index);

            for (int item = 0; item < state.getExhibits().size(); item++) {
                String currentItem=state.getExhibits().get(index);
                currentExplorer.getSuitcase().getExhibits().add(currentItem);
                state.getExhibits().remove(currentItem);
                item--;
                currentExplorer.search();
                if (!currentExplorer.canSearch()) {
                    explorers.remove(currentExplorer);
                    index--;
                    break;
                }
            }
        }
    }
}


