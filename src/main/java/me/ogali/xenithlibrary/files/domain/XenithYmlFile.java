package me.ogali.xenithlibrary.files.domain;

import lombok.Getter;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.files.YmlFileManager;

@Getter
public class XenithYmlFile {
    public static final YmlFileManager fileManager = new YmlFileManager(XenithLibrary.getInstance().getDataFolder());
    private final String fileName;

    public XenithYmlFile(String fileName) {
        this.fileName = fileName;
    }
}
