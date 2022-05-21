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
public class LuaUtilsModule extends Module {

    // Zero-argument constructor required
    public LuaUtilsModule() {
        super("LuaUtilsModule"); // Initialize module with the name

        //// CONFIGURATIONS
        super.setExecution(new ExecutionAllow(
                true,  // Allow module execution while game is stopped
                false  // Allow module execution while game is running
        ));
        super.setCloseWhenDetach(true);       // Closes the module when the user exit.
        super.setShowInPanelAtStart(false);
       
        //// EXTRA CONFIGURATIONS
        ModuleConfig moduleConfig = new ModuleConfig(); // Creates a new ModuleConfig instance
        moduleConfig.contextMenus.add(new ContextMenu(".lua", "Attach to selected"));

        super.setModuleConfigs(moduleConfig); // Set a extra config class in to the module
    }

    /// Called when the user enter the module
    public void onStart() {
        super.detachAndClose(); // Force user to exit the module, then close it from panel
    }


    public void onStop(){
     
    }
 

    /// Called when a file is opened with the module
    public void onOpenFile(PFile pFile) {
       if(pFile != null && super.getSelectedObject() != null){
          final PFile fp = pFile;
            super.runOnEngine(new Runnable() {
               public void run() {
                  LuaExecutor le = new LuaExecutor();
                  le.scriptFile = fp;
                  le.onScriptAttached();
                  LuaUtilsModule.this.getSelectedObject().addComponent(le, LuaExecutor.class);
               }
            });
        }
    }
}
