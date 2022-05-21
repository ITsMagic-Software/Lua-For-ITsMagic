package JAVARuntime;

// Useful imports
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;
import android.widget.*;

import android.view.ViewGroup.LayoutParams;
import android.view.*;
import android.view.ViewGroup.*;
import android.view.Gravity;

import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;

import android.text.TextWatcher;
import java.lang.CharSequence;
import android.text.Editable;

// coloring code
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

/**
 * @Author 
*/
public class LuaScripting extends Module {

    public boolean validated;
    
    // widgets
    private EditText editor;
    private LinearLayout layoutEditor;
    
    private String code = "";
    
    private PFile pFile;
    
    public int cursorPos = 0;
    
    private String newScriptName = "";
    
    // creating script ui
    private ImageView newScript;
    private ImageView luaLogo;
    private TextView moduleName;
    private TextView empty;
    
    private PopupDialog downloadDialog;
    private InputDialog createScriptDialog;
    
    // colors
    private int RED = 0xffff0000;
    private int GREEN = 0xff00ff00;
    private int BLUE = 0xff0000ff;
    private int YELLOW = 0xffffff00;
    private int LIGHT_BLUE = 0xff00aaff;
    
    // utils
    private LayoutParams editP;
    private PopupDialog dialog;
    // Zero-argument constructor required
    public LuaScripting() {
        super("LuaScripting"); // Initialize module with the name
    
        //// CONFIGURATIONS
        super.setExecution(new ExecutionAllow(
                true,  // Allow module execution while game is stopped
                false  // Allow module execution while game is running
        ));
        //super.setCloseWhenDetach(true);       // Closes the module when the user exit.
        //super.setShowInPanelAtStart(false);
       
        //// EXTRA CONFIGURATIONS
        ModuleConfig moduleConfig = new ModuleConfig(); // Creates a new ModuleConfig instance
        moduleConfig.contextMenus.add(new ContextMenu(".lua", "Edit script"));

        super.setModuleConfigs(moduleConfig);
        super.addLeftPanel(PROJECT_FILES);
        super.addBottomPanel(PROJECT_FILES);
    }

    /// Called when the user enter the module
    public void onStart() {
        validated = false;
        cursorPos = 0;
        
        // open create creating script ui
        if (pFile == null) {
            buildNewScriptUI();
        }
    }
    
    private void buildNewScriptUI() {
        // basic layout params
        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(Math.dptopx(80), Math.dptopx(80),1.0f);
        LinearLayout.LayoutParams image2Params = new LinearLayout.LayoutParams(Math.dptopx(80),Math.dptopx(80), 0f);
        // main layout init
        LinearLayout mainLayout = LayoutInflator.newLinearLayout();
        mainLayout.setBackgroundColor(0xff111111);
        mainLayout.setLayoutParams(mainLayoutParams);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        
        LinearLayout infoLayout = LayoutInflator.newLinearLayout();
        infoLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        LinearLayout addScript = LayoutInflator.newLinearLayout();
        addScript.setLayoutParams(mainLayoutParams);
        addScript.setGravity(Gravity.CENTER);
        // ui
        newScript = LayoutInflator.newImageView();
        LayoutUtils.setImage(newScript, new PFile("LUARuntime/Module/resources/add_lua_script.png"));
        newScript.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // create script
                createScriptDialog = new InputDialog("Create new Lua script", "enter the name", "OK", "Cancel", new InputDialogListener() {
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
        });
        newScript.setLayoutParams(imageParams);
        addScript.addView(newScript);
        
        luaLogo = LayoutInflator.newImageView();
        LayoutUtils.setImage(luaLogo, new PFile("LUARuntime/Module/resources/lua_logo.png"));
        luaLogo.setLayoutParams(image2Params);
        infoLayout.addView(luaLogo);
        
        moduleName = LayoutInflator.newTextView();
        moduleName.setText("ITsMagic LUA Coding");
        moduleName.setTextSize(30);
        moduleName.setTextColor(0xffffffff);
        moduleName.setGravity(Gravity.LEFT);
        infoLayout.addView(moduleName);
        
        empty = LayoutInflator.newTextView();
        empty.setText(" ");
        empty.setTextSize(60);
        // add all ui to layo    ut
        mainLayout.addView(infoLayout);
        mainLayout.addView(addScript);
        
        // add all ui to super
        super.addView(mainLayout);
    }
    
    private void loadCode() {
        // build ui
        editor = LayoutInflator.newEditText();
        editor.setMinLines(100);
        editor.setBackgroundColor(0xff111111);
        editor.setTextColor(0xffffffff);
        editor.setGravity(Gravity.TOP);
        editor.setText(code);
        
        if (editor.getText().toString().intern() == "") {
            String codeDefault = "function start() \n" +
            "\n" +
            "end \n" +
            "\n" +
            "function update() \n" +
            "\n" +
            "end \n" +
            "\n" +
            "function disabledUpdate() \n" +
            "\n" +
            "end \n";
            
            editor.setText(codeDefault);
        }
        
        editor.addTextChangedListener(new TextWatcher() {
            boolean ignore = false;
            String line = "";
            
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                line = s.subSequence(start, start + count).toString();
            }
            
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }
            
            public void afterTextChanged(Editable s) {
                if (ignore) {
                    return;
                }
                
                ignore = true;
                
                int pos = editor.getSelectionStart();
                if (line.contains("\n")) {
                    colorSyntax();
                }
                editor.setSelection(pos);
                
                ignore = false;
            }
        });
        
        // color lua syntax
        colorSyntax();
        
        // add edit text to view list
        super.addView(editor);
    }
    
    public void colorSyntax() {
        // RED
        colorCode("function", RED);
        colorCode("end", RED);
        colorCode("if", RED);
        colorCode("while", RED);
        colorCode("for", RED);
        colorCode("foreach", RED);
        colorCode("else", RED);
        colorCode("elseif", RED);
        colorCode("publicvar", RED);
        // YELLOW
        colorCode(".", GREEN);
        colorCode(",", GREEN);
        colorCode(":", GREEN);
        // BLUE
        colorCode("+", YELLOW);
        colorCode("-", YELLOW);
        colorCode("/", YELLOW);
        colorCode("*", YELLOW);
        colorCode("=", YELLOW);
        colorCode("\"", YELLOW);
        
        // LIGHT_BLUE
        for (int i = 0; i < 10; i++) {
            colorCode(String.valueOf(i), LIGHT_BLUE);
        }
    }

    /// Called when the user exits the module
    public void onStop() {
        validated = false;
        
        // save changes to script file
            try {    
            FileLoader.exportTextToFile(editor.getText().toString(), Directories.getProjectFolder() + pFile.getFilePath());
            } catch (IOException e) {
                
            }
        dialog = new PopupDialog(PopupDialog.ALERT, "Lua script saved!", pFile.getFilePath() + " saved!");
        dialog.setConfirmButton("OK", new PopupDialogListener() {
            public void onClicked() {
                dialog.dismiss();
            }
        });
        dialog.show();
        // clear pFile
        pFile = null;
        
        // refresh all scripts
        super.runOnEngine(new Runnable() {
            
            public void run() {
                try {
                    ArrayList comps = WorldController.listAllComponents(LuaExecutor.class);
                    for (int i = 0; i < comps.size(); i++) {
                        LuaExecutor comp = (LuaExecutor) comps.get(i);
                        comp.refresh();
                    }
                } catch (Exception e) {}
            }
            
        });
    }

    /// Called when a file is opened with the module
    public void onOpenFile(PFile pFile) {
        if (pFile.getFilePath().contains(".lua")) {
            validated = true;
            this.pFile = pFile;
            
            // load text from file
            try {
                this.code = FileLoader.loadTextFromFile(pFile);
                loadCode();
            } catch (Exception e) {
                code = "";
            }
        } else {
            validated = false;
        }
    }

    /// Called when a object is selected while using your module
    public void onObjectSelected(SpatialObject object) {
        
    }
    
    public void colorCode(String textToHighlight, int color) {
    String tvt = editor.getText().toString();
        int ofe = tvt.indexOf(textToHighlight, 0);
        Spannable wordToSpan = new SpannableString(editor.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1)
                break;
            else {
                wordToSpan.setSpan(new ForegroundColorSpan(color), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editor.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
 }
}
