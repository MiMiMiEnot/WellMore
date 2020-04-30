package me.enot.wellgui.gui.serializable.exception;

import com.typesafe.config.Config;
import me.enot.wellgui.gui.serializable.Serialization;

import java.io.File;

public class RequiredPathNotFoundException extends Exception {

    private String path;
    private String fileName;

    public RequiredPathNotFoundException(String path, String fileName){
        this.path = path;
        this.fileName = fileName;
    }

    public RequiredPathNotFoundException(String path, File file){
        this(path, file.getName().split("\\\\")[file.getName().split("\\\\").length - 1]);
    }

    public RequiredPathNotFoundException(String path, Config config){
        this(path, Serialization.getGUIIdByConfig(config));
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "Обязательный аргумент " + this.path + " в " + fileName + " не найден";
    }
}
