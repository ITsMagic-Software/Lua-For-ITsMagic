package JAVARuntime;

// Useful imports
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;
import android.widget.*;

/**
 * @Author 
*/
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

    /// Called when the user enter the module
    public void onStart() {
    
    }

    /// Called when the user exits the module
    public void onStop() {
     
    }

    /// Called when a file is opened with the module
    public void onOpenFile(PFile pFile) {
        Console.log("Creating script at " + pFile.getFilePath());
    }

    /// Called when a object is selected while using your module
    public void onObjectSelected(SpatialObject object) {
        
    }
}
