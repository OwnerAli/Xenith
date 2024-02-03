package me.ogali.xenithlibrary.condition.impl.impl;

public class ItemMaterialCondition extends StringMatchItemCondition {

    public ItemMaterialCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }
    
    public ItemMaterialCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, value);
    }
    
}
