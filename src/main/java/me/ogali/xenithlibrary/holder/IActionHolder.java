package me.ogali.xenithlibrary.holder;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import org.bukkit.entity.Player;

public interface IActionHolder {

    void execute(Player player, Object... values);
    boolean contains(AbstractAction<?, ?> abstractAction);

}
