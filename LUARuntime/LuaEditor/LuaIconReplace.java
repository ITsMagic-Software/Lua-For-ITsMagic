package JAVARuntime;

// Useful imports
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;

/**
 * @Author 
*/
public class LuaIconReplace extends FilesPanelCustomIcon { 

    @Override
    public File getIconForFile(File file) {
        return new File(Directories.getProjectFolder() + "LUARuntime/LuaEditor/media/lua.png");
    }
    
    @Override
    public boolean supportFile(File file) {
        if (file.getAbsolutePath().endsWith(".lua")) {
            return true;
        }
        
        return false;
    }
    
}
