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
public class LuaStyler extends  TextScriptingStyler {

   // the styler is responsible to give word colors in the scripting panel
   // we need to read the script (source) find words and then paint
   // to paint a word at script, we create a new TextScriptingSyntaxHighlightSpan
   // add it to the list'
   
   private String[] operators = {"function", "end", "while", "for", "in", ":", "if", "not", "or", "and", "else","do"};
   private String[] math = {"1","2","3","4","5","6","6","7","8","9","0","+","-","*","/"};
   private String[] types = {"myObject"};
   public static String[] methods = new String[0];
   
    @Override
    public void execute(String source, TextScriptingTheme theme, List<TextScriptingSyntaxHighlightSpan> highlightSpans){
         // read source and find words to paint
         // use colors from theme
         TextScriptingSyntaxHighlightSpan hightlight = new TextScriptingSyntaxHighlightSpan();
         
         TextScriptingStyleSpan span = new TextScriptingStyleSpan(null);
         span.color = theme.operatorColor;
         span.bold = false;
         span.italic = false;
         span.underline = false;
         span.strikethrough = false;
         
         hightlight.span = span;
         hightlight.start = 0; // the first index of span color
         hightlight.end = source.length(); // the ending index of the word to paint
         
         highlightSpans.add(hightlight); // add to list
         colorText(source,operators,"functions",theme,highlightSpans);
         colorText(source,math,"math",theme,highlightSpans); 
         colorText(source,types,"types",theme,highlightSpans);
    }
    
    private void colorText(String source, String[] set, String color,TextScriptingTheme theme,List<TextScriptingSyntaxHighlightSpan> highlightSpans) {
        Color c = new Color(255,255,255);
        if (color.equals("functions"))
        c = theme.keywordColor;
        else if (color.equals("math"))
        c = theme.numberColor;
        else if (color.equals("types"))
        c = theme.typeColor;
        
        for (int i = 0; i < set.length; i++) {
            String word = set[i];
            int index = 0;
            while (index != -1) {
                index = source.indexOf(word,index);
                if (index != -1) {
                    index += 1;
                    TextScriptingSyntaxHighlightSpan hightlight = new TextScriptingSyntaxHighlightSpan();
         
                    TextScriptingStyleSpan span = new TextScriptingStyleSpan(null);
                    span.color = c;
                    span.bold = false;
                    span.italic = false;
                    span.underline = false;
                    span.strikethrough = false;
         
                    hightlight.span = span;
                    hightlight.start = index - 1;
                    hightlight.end = index -1 + word.length();
                    highlightSpans.add(hightlight);
                }
            }
        }
    }
    
    public static void getAllTypes() {
        List<JCompillerClass> classes = JCompiller.getAllClasses();
        LuaStyler.methods = new String[classes.size()];
        for (int i = 0; i < classes.size(); i++) {
            JCompillerClass next = (JCompillerClass) classes.get(i);
            LuaStyler.methods[i] = next.getName();
        }
        Console.log(LuaStyler.methods.length);
    }
}
