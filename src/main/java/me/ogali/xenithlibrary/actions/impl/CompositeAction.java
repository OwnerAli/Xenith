package me.ogali.xenithlibrary.actions.impl;

import me.ogali.xenithlibrary.actions.domain.AbstractAction;
import me.ogali.xenithlibrary.actions.domain.ActionContext;
import me.ogali.xenithlibrary.actions.domain.ActionRegistry;
import me.ogali.xenithlibrary.shared.DomainConfig;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CompositeAction extends AbstractAction {
    private final List<String> actionIds;

    public CompositeAction(List<String> conditionIds) {
        this.actionIds = conditionIds;
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        List<String> ids = config.getStringList("actions");
        return new CompositeAction(ids);
    }

    @Override
    public void execute(ActionContext context) {
        List<AbstractAction> resolved = actionIds.stream()
                .map(ActionRegistry::get)
                .toList();
        resolved.forEach(action -> action.execute(context));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("actions", actionIds);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "actions" -> this.actionIds.add(value);
            default -> super.applyEdit(field, value);
        }
    }
}