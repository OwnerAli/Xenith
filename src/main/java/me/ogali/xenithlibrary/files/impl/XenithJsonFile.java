package me.ogali.xenithlibrary.files.impl;

import me.ogali.xenithlibrary.files.domain.JsonFile;

public abstract class XenithJsonFile<T> extends JsonFile<T> {

    public XenithJsonFile(String name) {
        super(name, "Xenith");
    }

}
