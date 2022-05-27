package JAVARuntime;

public class LuaScriptCreator extends Module {

    // Zero-argument constructor required
    public LuaScriptCreator() {
        super("LuaScriptCreator"); // Initialize module with the name

        super.setExecution(new ExecutionAllow(true,false));

        super.setCloseWhenDetach(true);
        super.setShowInPanelAtStart(false);

        ModuleConfig moduleConfig = new ModuleConfig(); // Creates a new ModuleConfig instance
        moduleConfig.contextMenus.add(new ContextMenu("LuaScripts", "Create Lua Script"));

        super.setModuleConfigs(moduleConfig); // Set a extra config class in to the module
    }

    public void onStart() {}
    public void onStop() {}

    public void onOpenFile(PFile pFile) {
        Console.log("Creating script at " + pFile.getFilePath());
    }

    public void onObjectSelected(SpatialObject object) {}
}
