package me.ogali.xenithlibrary.files.domain;

import de.leonhard.storage.Json;

public abstract class JsonFile<T> extends Json {

    public JsonFile(String name, String directory) {
        super(name, "plugins/" + directory);
    }

    public abstract void save(T object);

    public abstract void load();

}
