package me.enot.wellgui.gui.guiitem;

import me.enot.wellgui.gui.guiitem.utils.Executor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GUIItemCommand implements GUIItemType {

    private Executor executor;
    private List<String> commands;

    public GUIItemCommand(Executor executor, List<String> commands){
        this.executor = executor;
        this.commands = commands;
    }

    public GUIItemCommand(Executor executor, String command){
        this(executor, Collections.singletonList(command));
    }
    public GUIItemCommand(Executor executor, String[] commands){
        this(executor, Arrays.asList(commands));
    }

    public Executor getExecutor() {
        return executor;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
}
