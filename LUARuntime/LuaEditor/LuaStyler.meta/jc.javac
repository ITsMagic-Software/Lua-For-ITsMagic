package JAVARuntime;

// Useful imports
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;

import java.util.regex.*;

/**
 * @Author 
*/
public class LuaStyler extends  TextScriptingStyler {

   // the styler is responsible to give word colors in the scripting panel
   // we need to read the script (source) find words and then paint
   // to paint a word at script, we create a new TextScriptingSyntaxHighlightSpan
   // add it to the list'
   
   private String[] operators = {"function", "end", "in", "not", "or", "and","if", "for", "while", "break", "continue", "else", "elseif","do",":"};
   private String[] math = {"1","2","3","4","5","6","6","7","8","9","0","+","-","*","/"};
   private String[] types = {"myObject","myPhysics","myTransform"};
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
         
         colorMethodsRegex(source,theme,highlightSpans);
         colorStringsRegex(source,theme,highlightSpans);
         colorMethodsSeparatedRegex(source,theme,highlightSpans);
    }
    
    private void colorText(String source, String[] set, String color,TextScriptingTheme theme,List<TextScriptingSyntaxHighlightSpan> highlightSpans) {
        Color c = new Color(255,255,255);
        if (color.equals("functions"))
        c = new Color("#ff5555");
        else if (color.equals("math"))
        c = theme.numberColor;
        else if (color.equals("types"))
        c = theme.typeColor;
        else if (color.equals("specials"))
        c = new Color(200,0,0);
        
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
    
    private void colorMethodsRegex(String source,TextScriptingTheme theme,List<TextScriptingSyntaxHighlightSpan> highlightSpans) {
        Color c = new Color("8be9fd");
        Pattern p = Pattern.compile("\\:.*?\\(");
        Matcher m = p.matcher(source);
        while (m.find()) {
            String res = m.group();
            TextScriptingSyntaxHighlightSpan hightlight = new TextScriptingSyntaxHighlightSpan();
         
            TextScriptingStyleSpan span = new TextScriptingStyleSpan(null);
             span.color = c;
             span.bold = false;
             span.italic = false;
             span.underline = false;
             span.strikethrough = false;
         
              hightlight.span = span;
              hightlight.start = m.start()+1;
              hightlight.end = m.end()-1;
              highlightSpans.add(hightlight);
        }
    }
    
    private void colorMethodsSeparatedRegex(String source,TextScriptingTheme theme,List<TextScriptingSyntaxHighlightSpan> highlightSpans) {
        Color c = new Color("#8be9fd");
        Pattern p = Pattern.compile("\\ .*?\\(");
        Matcher m = p.matcher(source);
        while (m.find()) {
            String res = m.group();
            TextScriptingSyntaxHighlightSpan hightlight = new TextScriptingSyntaxHighlightSpan();
            
            for (int i = 0; i < operators.length; i++) {
                if (operators[i].equals(res) || operators[i].contains(res)) {
                    continue;
                }
            }
         
            TextScriptingStyleSpan span = new TextScriptingStyleSpan(null);
             span.color = c;
             span.bold = false;
             span.italic = false;
             span.underline = false;
             span.strikethrough = false;
         
              hightlight.span = span;
              hightlight.start = m.start()+1;
              hightlight.end = m.end()-1;
              highlightSpans.add(hightlight);
        }
    }
    
    private void colorStringsRegex(String source,TextScriptingTheme theme,List<TextScriptingSyntaxHighlightSpan> highlightSpans) {
        Color c = new Color("#50fa7b");
        Pattern p = Pattern.compile("\\\".*?\\\"");
        Matcher m = p.matcher(source);
        while (m.find()) {
            String res = m.group();
            TextScriptingSyntaxHighlightSpan hightlight = new TextScriptingSyntaxHighlightSpan();
         
            TextScriptingStyleSpan span = new TextScriptingStyleSpan(null);
             span.color = c;
             span.bold = false;
             span.italic = false;
             span.underline = false;
             span.strikethrough = false;
         
              hightlight.span = span;
              hightlight.start = m.start();
              hightlight.end = m.end();
              highlightSpans.add(hightlight);
        }
    }
    
    public static void getAllTypes() {
        /*List<JCompilerClass> classes = JCompiler.getAllClasses();
        for (int i = 0; i < classes.size(); i++) {
            JCompilerClass next = (JCompilerClass) classes.get(i);
            Console.log(next.getName());
        }*/
    }
}
