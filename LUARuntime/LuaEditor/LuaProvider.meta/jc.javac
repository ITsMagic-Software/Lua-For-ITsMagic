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
public class LuaProvider extends TextScriptingProvider {
    
    // the provider is responsible to give word suggestions
    // to give a suggestion we need to add it to a list at getAll() method
    // all suggestions returned at getAll() method, will display at screen
    
    // the correct way to implement this
    // is to store all your words and possible suggestions in a list
    // like script attributes and methods
    // while processLine is being called
    
    // the last call of processLine will be the current line
    // which user is editing
    // so you can use this to create suggestions specific for that line
    
    // then you return all suggestions when getAll is called
    // only matching suggestions will be displayed
    
    @Override
    public List<TextScriptingSuggestion> getAll() {
        // return the suggestions list
        
        // example (dont create suggestions everytime this method is called, cache it)
        TextScriptingSuggestion suggestion = new TextScriptingSuggestion(null);
        suggestion.text = "test";
        suggestion.returnType = "";
        
        List<TextScriptingSuggestion> list = new LinkedList();
        list.add(suggestion);
        collectNames(list);
        return list;
    }

    @Override
    public void processLine(int lineNumber, String text) {
        // the current line user is writing
    }

    @Override
    public void deleteLine(int lineNumber) {
        // called to notify you that a line was deleted
    }

    @Override
    public void clearLines() {
        // called to notify you to clear all your local data
    }
    
    private void collectNames(List<TextScriptingSuggestion> list) {
        List<JCompillerClass> classes = JCompiller.getAllClasses();
        for (int i = 0; i < classes.size(); i++) {
            JCompillerClass current = (JCompillerClass) classes.get(i);
            String name = current.getName();
            TextScriptingSuggestion suggestion = new TextScriptingSuggestion(null);
            suggestion.text = name;
            suggestion.returnType = "";
            list.add(suggestion);
        }
    }
}
