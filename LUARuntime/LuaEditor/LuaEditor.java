package JAVARuntime;

// Useful imports
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;
import android.text.Editable;

/**
 * @Author 
*/
public class LuaEditor extends TextScriptingExtension { 

    private LuaEditorTheme theme = new LuaEditorTheme();
    private LuaStyler styler = new LuaStyler();
    private LuaProvider provider = new LuaProvider();
    private File loadedFile = null;
    
    private PopupDialog error;
    
    private String defaultCode = "function start() \n" +
                                                    "Console:log(\"Hello, Lua!\") \n" +
                                                    "end \n\n" +
                                                    "function update() \n\n" +
                                                    "end\n\n" +
                                                    "function disabledUpdate()\n\n" +
                                                    "end";
    
    // for checking changes in text
    private boolean ignore = false;
    private String line = "";

    @Override
    public void init(){
        // set configs and themes
        super.setTheme(theme.get());
        super.setStyler(styler);
        super.setProvider(provider);
    }
    
    @Override
    public TextScriptingExtension.LineTip getTipForLine(int line){
        // return if theres a error or warning on this line
        // options:
        // LineTip.Error
        // LineTip.Alert
        // LineTip.None
        return TextScriptingExtension.LineTip.None;
    }
    
    @Override
    public void replaceScript(File file){
        // save the loaded script, if present
        // then load the new one
        if(hasScript()){
            if(saveScript()){
                 openScript(file);
            } else {
                // failed to save
            }
        } else{
            openScript(file);
        }
    }
    
    @Override
    public void openScript(File file){
        // load the script
        loadedFile = file;
        
        // you can use FileLoader.loadTextFromFile(file);
        String code = "";
        try{
             code = FileLoader.loadTextFromFile(file);
        } catch(Exception e){
             // handle
             errorLog(e);
        }
        
        // set the script text using
        if (code == "")
            code = defaultCode;
        super.setText(code);
    }
    
    @Override
    public void onClose(){
        // module will be dettached
        // save the loaded script and release
        if(saveScript()){
            loadedFile = null;
        }
        try {
            ArrayList comps = WorldController.listAllComponents(LuaExecutor.class);
            for (int i = 0; i < comps.size(); i++) {
                LuaExecutor comp = (LuaExecutor) comps.get(i);
                comp.refresh();
             }
         } catch (Exception e) {}
    }
    
    @Override
    public boolean saveScript(){
        // save script if present
        // return true if saved
        if(hasScript()){
            try{
                FileLoader.exportTextToFile(getText(), loadedFile);
                return true;
            } catch (Exception e){
                return false;
            }
        }
        return false;
    }
    
    
    @Override
    public String getTipTextForLine(int line){
        // return message text which will be
        // displayed on the popup dialog
        // when user taps on the error line number
        // on the line counter at left side
        return "";
    }
   
    @Override
    public boolean hasScript(){
        return loadedFile != null;
    }

    @Override
    public boolean supportFile(File file){
        // check if file is supported
        Console.log("aboslute:" + file.getAbsolutePath());
        if(file.getAbsolutePath().endsWith(".lua")){
             return true;
        }
        return false;
    }
    
    private void errorLog(Exception e) {
        error = new PopupDialog(PopupDialog.ERROR,"ERROR",e.toString());
    }
    
    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
        
    }
    
    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        line = charSequence.subSequence(start,start+count).toString();
    }
    
    @Override
    public void afterTextChanged(Editable editable) {
        if (ignore)
            return;
            
        ignore = true;
        
        // coloring stuff
        
        ignore = false;
    }
}
