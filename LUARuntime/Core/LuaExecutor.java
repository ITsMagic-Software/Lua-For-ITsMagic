package JAVARuntime;

// Useful imports
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * @Author SpeakerFish (Discord community)
*/
public class LuaExecutor extends Component { 
 
    public PFile scriptFile = new PFile();
    public Map2 variables = new Map2(String.class, String.class);
    private int arrayCount = 0;
    
    private PFileTools pFileTools;
    
    public Map2 dataTypes = new Map2(String.class, String.class);
 
    private Globals globals = null;
    private LuaValue entireScript = null;
    
    private String error;
    
    private boolean refreshVars = false;

    private boolean variablesChecked = false;
    private ArrayList codeString;
    private String scriptText;
    
    private String loadedScriptPath = "";
    
    /// Run only once
    public void start() {
        variablesChecked = true;
        refreshVars = true;
        entireScript = null;
        loadScript();
        
        if(entireScript != null){
          LuaInvoker.invoke("start", globals);
        }
    }

    /// Repeat every frame
    public void repeat() {
        if(entireScript != null){
          LuaInvoker.invoke("update", globals);
        }
    }

    /// Repeat every frame when component or object is disabled
    public void disabledRepeat() {
        loadScript();
        
        if (!variablesChecked) {
            onScriptAttached();
        }
        
        if(entireScript != null){
          LuaInvoker.invoke("disabledUpdate", globals);
        }
    }
    
    
    // LOAD SCRIPT DATA
    private void loadScript(){
       if(entireScript != null){
          //Script already loaded, return
           return;   
       }
       
       if(!validateScriptFile()){
           error = "Invalid lua file";
           loadedScriptPath = "";
           return; 
       }
     
       scriptText = null;
       try{
          scriptText = FileLoader.loadTextFromFile(scriptFile);
          if (variablesChecked) {
              if (refreshVars) {
                  replaceVars();
                  refreshVars = false;
              }
              scriptText = scriptText.replace("publicvar", "").intern();
          }
       } catch (Exception e){
         error = e.toString();
         Console.log("Lua file loading error:" + error);
         loadedScriptPath = "";
         return;
       }
       
       if (variablesChecked) {
       try{
          loadedScriptPath = scriptFile.getFilePath();
          
          globals = LUAJUtils.getGlobals();
          entireScript = globals.load(scriptText);
          error = null;
          
          /// Initialize the script globals
          /// This is necessary, or the Invoke function wont work
          entireScript.call();
          globals.set("myObject", CoerceJavaToLua.coerce(myObject));
          globals.set("myTransform",CoerceJavaToLua.coerce(myTransform));
          globals.set("myPhysics",CoerceJavaToLua.coerce(myPhysics));
          pFileTools = new PFileTools();
          globals.set("pFileTools", CoerceJavaToLua.coerce(pFileTools));
       } catch (Exception e){
         error = e.toString();
          Console.log("Lua compiller error:" + error);
          loadedScriptPath = "";
       }
       }
    }
    
    //// SHOW SCRIPT NAME ON COMPONENT TITTLE
    public String getComponentTittle(){
       if(error != null && !error.isEmpty()){
         return getName() + " failed";
      }
       return getName();
    }
    
    //// DRAW A COLOR
    public Color getComponentColor(){
        if(error == null || error.isEmpty() && entireScript != null){ 
         //// Draw a purple color, if theres no error
         return new Color(48, 51, 107);
        }
        //// Draw original component color, if theres error
        return null;
    }
    
    //// CALCULATE THE SCRIPT FILE NAME
    private String getName(){
        String filePath = scriptFile.getFilePath(); 
        if(filePath != null) {
            if (filePath.contains("/")) {
                try {
                    filePath = filePath.substring(filePath.lastIndexOf("/") + 1);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        }
        
        return filePath;
    }
    
    //// CHECK IF SCRIPT IS A LUA FILE
    private boolean validateScriptFile(){
       String name = getName();
       if(name == null || name.isEmpty()){
         return false;
       }
       if(name.endsWith(".lua")){
         return true; 
       }
       return false;
    }
    
    public void onScriptAttached() {
        loadScript();
        
        // read line by line
        codeString = new ArrayList();
        String code[] = scriptText.split("\n");
        
        for (int i = 0; i < code.length; i++) {
            codeString.add(code[i].intern());
        }
        
        ArrayList vars = new ArrayList();
        int count = 0;
        
        for (int i = 0; i < codeString.size(); i++) {
            String line = (String) codeString.get(i);
            if (line.contains("publicvar")) {
                vars.add(line);
            }
        }
        
        ArrayList publicvar = new ArrayList();
        for (int i = 0; i < vars.size(); i++) {
            String line = (String) vars.get(i);
            
            if (isGlobal(codeString, line.intern())) {
                count += 1;
                publicvar.add(line.intern());
            }
        }
        
        variables = new Map2(String.class, String.class);
        dataTypes = new Map2(String.class, String.class);
        arrayCount = 0;
        for (int i = 0; i < publicvar.size(); i++) {
            String line = (String) publicvar.get(i);
            String varName = getVarName(line);
            String varValue = getVarValue(line);
            varName = varName.replace("publicvar ", "");
            
            addItem(varName.intern(), varValue.intern());
        }
        
        variablesChecked = true;
    }
    
    public void addItem(String vName, String vValue) {
        String finalValue = "";
        if (vValue.startsWith("\"") || vValue.startsWith(" \"")) {
            String vValueTrim = vValue.trim().intern();
            finalValue = vValueTrim.substring(1, vValueTrim.length()-1).intern();
            dataTypes.put(vName, "string");
        } else {
            finalValue = vValue;
            dataTypes.put(vName, "float");
        }
        variables.put(vName, finalValue);
    }
    
    // parser to check if variable is global
    private boolean isGlobal(ArrayList code, String var) {
        int declaredAt = 0;
        for (int i = 0; i < code.size(); i++) {
            String lineDirty = (String) code.get(i);
            String line = lineDirty.replace(" ", "").intern();
            
            if (line.contains(var.intern())) {
                declaredAt = i;
                break;
            }
        }
        boolean global = false;
        for (int i = declaredAt; i > -1; i--) {
            String lineDirty = (String) code.get(i);
            String line = lineDirty.intern();
            if (line.contains("function")) {
                // the variable is not global
                break;
            }
            if (line.contains("end") || i == 0) {
                global = true;
                break;
            }
        }
        
        return global;
    }
    
    public void replaceVars() {
        ArrayList keys = variables.getKeyList();
        ArrayList keysData = dataTypes.getKeyList();
        
        for (int i = 0; i < variables.getSize(); i++) {
            String codeAll[] = scriptText.split("\n");
            String currentKey = (String) keys.get(i);
            String var = (String) variables.get(currentKey);
            String varName = "publicvar " + currentKey.intern();
            
            for (int e = 0; e < codeAll.length; e++) {
                String next = codeAll[e].intern();
                if (next.contains(varName)) {
                    String preText = "";
                    String typeKey = (String) keysData.get(i);
                    String isStr = (String) dataTypes.get(typeKey);
                    if (isStr.intern() == "string") {
                        preText = "\"";
                    }
                    scriptText = scriptText.replace(next, "publicvar " + currentKey + " = " + preText + var + preText);
                    
                    break;
                }
            }
        }
    }
    
    public String getVarName(String line) {
        String allLine[] = line.split("=");
        String varName = allLine[0].intern();
        return varName;
    }
    public String getVarValue(String line) {
        String allLine[] = line.split("=");
        return allLine[1].intern();
    }
    
    // refresh variables
    public void refresh() {
        entireScript = null;
        variablesChecked = false;
        onScriptAttached ();
    }
    
}
