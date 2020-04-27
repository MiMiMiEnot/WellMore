package me.enot.wellgui.gui.serializable.exception;

import com.typesafe.config.Config;
import me.enot.wellgui.gui.serializable.Serialization;

import java.io.File;

public class SlotInvalidException extends Exception {

    private String id;
    private int slot;
    private String fileName;

    public SlotInvalidException(String id, int slot, String fileName){
        this.id = id;
        this.slot = slot;
        this.fileName = fileName;
    }
    public SlotInvalidException(String id, int slot, File file){
        this(id, slot, file.getName().split(File.separator)[file.getName().split(File.separator).length - 1]);
    }

    public SlotInvalidException(String id, int slot, Config config){
        this(id, slot, Serialization.getGUIIdByConfig(config));
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "Слот " + this.slot + " предмета " + this.id + " не является правильным для " + this.fileName;
    }

    public String getId() {
        return id;
    }

    public int getSlot() {
        return slot;
    }

    public String getFileName() {
        return fileName;
    }
}
