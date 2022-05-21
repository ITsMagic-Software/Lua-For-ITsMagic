package JAVARuntime;

// Useful imports
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;
import java.io.IOException;

/**
 * @Author 
*/
public class LuaScriptCreatorUtil extends FilesPanelDirectoryMenu { 

private InputDialog createScriptDialog;

    public LuaScriptCreatorUtil() {
        super("Lua Scripting/New Lua Script");
    }
    
    @Override
    public void onClick(File folder) {
        createScriptDialog = new InputDialog("Create new Lua script", "MyLuaScript", "OK", "Cancel", new InputDialogListener() {
                    public void onFinish(String name) {
                        File folder = new File(Directories.getProjectFolder() + "Files/LuaScripts/");
                        folder.mkdirs();
                        File newFile = new File(Directories.getProjectFolder() + "Files/LuaScripts/" + name + ".lua");
                        PFile newScriptFile = null;
                        try {
                            newFile.createNewFile();
                            newScriptFile = new PFile(Directories.getProjectFolder() + "Files/LuaScripts/" + name + ".lua");
                        } catch (IOException e) {}
                    }
                    
                    public void onCancel() {
                    
                    }
                });
    }
}
