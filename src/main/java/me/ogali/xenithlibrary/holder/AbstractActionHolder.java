package me.ogali.xenithlibrary.holder;

import lombok.Getter;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.impl.AbstractPlayerAction;
import me.ogali.xenithlibrary.action.impl.impl.CancellableTypeAction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractActionHolder implements IActionHolder {

    private final List<AbstractAction<?, ?>> actionList = new ArrayList<>();

    @Override
    public boolean contains(AbstractAction<?, ?> abstractAction) {
        return actionList.contains(abstractAction);
    }

    protected void executePlayerActions(Player player) {
        actionList.forEach(abstractAction -> {
            if (abstractAction instanceof AbstractPlayerAction<?> abstractPlayerAction) {
                abstractPlayerAction.execute(player);
            }
        });
    }

    protected void executeCancellableActions(Cancellable cancellable) {
        actionList.forEach(abstractAction -> {
            if (abstractAction instanceof CancellableTypeAction cancellableTypeAction) {
                cancellableTypeAction.execute(cancellable);
            }
        });
    }

    public List<String> toIdList() {
        return actionList.stream().map(AbstractAction::getId).toList();
    }

}
