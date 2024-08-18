package me.ogali.xenithlibrary.registiry.impl;

import de.leonhard.storage.Json;
import lombok.Getter;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.files.impl.ActionsFile;
import me.ogali.xenithlibrary.registiry.domain.impl.AbstractMapRegistry;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ActionRegistry extends AbstractMapRegistry<String, AbstractAction<?, ?>> {

    private final List<Class<? extends AbstractAction<?, ?>>> actionTypesList = new ArrayList<>();

    @Override
    public void register(AbstractAction<?, ?> object) {
        getObjectMap().put(object.getId(), object);
    }

    @Override
    public void saveToFile() {
        ActionsFile actionsFile = XenithLibrary.getInstance().getActionsFile();

        getObjectMap().forEach((id, action) -> {
            if (action.toString() == null) return;
            actionsFile.set(id + ".action", action);
            actionsFile.set(id + ".extraSettings", action.getSettingHolder());
        });
    }

    @Override
    public void loadFromFile(Json file) {
        if (!(file instanceof ActionsFile actionsFile)) return;

        file.singleLayerKeySet()
                .forEach(key -> register(actionsFile.getAction(key)));
    }

    public void registerActionType(Class<? extends AbstractAction<?, ?>> abstractActionClass) {
        actionTypesList.add(abstractActionClass);
    }

}
