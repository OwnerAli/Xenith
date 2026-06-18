package me.ogali.xenithlibrary.shared;

/**
 * Implemented by FieldInputProviders that know how to persist themselves.
 * The resolver calls this after an edit if the target implements it.
 */
public interface Persistable {
    void persist();
}