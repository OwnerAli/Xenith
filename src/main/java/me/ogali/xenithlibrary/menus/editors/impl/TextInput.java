package me.ogali.xenithlibrary.menus.editors.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.menus.editors.FieldInput;
import me.ogali.xenithlibrary.menus.editors.FieldInputProvider;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TextInput implements FieldInput {
    private final String field;

    public TextInput(String field) {
        this.field = field;
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public Material icon() {
        return Material.PAPER;
    }

    @Override
    public String hint() {
        return "&7Click to type a value";
    }

    @Override
    public void handle(Player player, FieldInputProvider target, Runnable onComplete) {
        player.closeInventory();

        new ConversationFactory(XenithLibrary.getInstance())
                .withModality(false)
                .withEscapeSequence("cancel")
                .withTimeout(60)
                .withFirstPrompt(new StringPrompt() {

                    @Override
                    public @NotNull String getPromptText(@NotNull ConversationContext context) {
                        return Chat.colorize(
                                "&aEnter a new value for &e" + field + "&a:\n" +
                                        "&7Type &ccancel &7to abort."
                        );
                    }

                    @Override
                    public Prompt acceptInput(@NotNull ConversationContext context, String input) {
                        try {
                            target.applyEdit(field, input);
                            persistIfAble(target);
                            Chat.tellFormatted(player, "&aUpdated &e%s &ato: &f%s", field, input);
                        } catch (Exception ex) {
                            Chat.tellFormatted(player,
                                    "&cInvalid value for &e%s&c: &f%s", field, ex.getMessage());
                        }
                        onComplete.run();
                        return Prompt.END_OF_CONVERSATION;
                    }
                })
                .addConversationAbandonedListener(event -> {
                    if (!event.gracefulExit()) {
                        Chat.tell(player, "&cEdit cancelled.");
                        onComplete.run();
                    }
                })
                .buildConversation(player)
                .begin();
    }
}