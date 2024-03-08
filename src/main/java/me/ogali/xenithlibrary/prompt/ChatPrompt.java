package me.ogali.xenithlibrary.prompt;

import org.bukkit.entity.Player;

public interface ChatPrompt {

    void prompt(Player player);
    void unPrompt(Player player);
    boolean setValue(String value);

}
