package me.ogali.xenithlibrary.files;

import lombok.Getter;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.conditions.domain.ConditionType;
import me.ogali.xenithlibrary.utilities.Chat;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.*;

@Getter
public class YmlFileManager {
    private final Map<String, File> createdFiles;
    private final File directory;
    private final Yaml yml;

    public YmlFileManager(String subDirectory) {
        this.createdFiles = new HashMap<>();

        File file;
        if (subDirectory.isEmpty()) {
            file = new File(XenithLibrary.getInstance().getDataFolder().getPath());
        } else {
            file = new File(XenithLibrary.getInstance().getDataFolder(), subDirectory);
        }
        this.directory = file;
        this.yml = createYml();
    }

    public YmlFileManager(File directory) {
        this.createdFiles = new HashMap<>();
        this.directory = directory;
        this.yml = createYml();
    }

    public void createNewFile(String fileName) {
        File file = new File(directory, fileName + ".yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
                createdFiles.put(fileName, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<Map<String, Object>> get(String key) {
        File file = new File(directory, key + ".yml");
        if (!file.exists()) return Optional.empty();

        try (InputStream input = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Object data = yaml.load(input);
            if (data instanceof Map) {
                //noinspection unchecked
                return Optional.of((Map<String, Object>) data);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching value with key: " + key +
                    "\n" + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean save(String fileName, String key, Object toBeSaved) {
        File file = createdFiles.get(fileName);
        if (file == null) throw new IllegalArgumentException("Could not find a file with that name!");

        try {
            // Create a map to hold the object under the specified key
            Map<String, Object> dataToSave = Collections.singletonMap(key, toBeSaved);
            yml.dump(dataToSave, new FileWriter(file));
            return true;
        } catch (Exception e) {
            Chat.log("An error occurred while saving key: " + key +
                    "\n" + e.getMessage());
            return false;
        }
    }

    private Yaml createYml() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);

        Representer representer = new Representer(options);
        Arrays.stream(ConditionType.values())
                .forEach(conditionType -> representer.addClassTag(conditionType.getRepresentingClass(), Tag.MAP));

        return new Yaml(representer, options);
    }
}