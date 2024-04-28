package me.ogali.xenithlibrary.action.domain;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.XenithLibrary;

import java.util.Random;

@Getter
@Setter
public abstract class AbstractAction<T, V> implements Executable<T> {

    private String id;
    private V value;
    private double chance;
    private boolean extraSettings;

    protected AbstractAction(String id, V value, double chance, boolean extraSettings) {
        this.id = id;
        this.value = value;
        this.chance = chance;
        this.extraSettings = extraSettings;
    }

    public void saveExtraSettings() {
    }

    ;

    public void loadExtraSettings(String[] settings) {
    }

    ;

    protected AbstractAction(String id) {
        this.id = id;
    }

    protected boolean isSuccessful(double chance) {
        Random random = XenithLibrary.getInstance().getRandom();
        double randomValue = random.nextDouble() * 100.0; // Generate a random value between 0.0 and 100.0
        return randomValue <= chance;
    }

    @Override
    public String toString() {
        if (getClass().getSimpleName().contains("Event")) return null;
        return getClass().getName() + "," + getValue() + "," + chance;
    }

}
