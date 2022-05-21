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
 * @Author 
*/
public class LuaInvoker { 

    //// FUNCTION USED TO INVOKE FUNCTIONS AT LUA SCRIPTS
    //// RETURN THE OBJECT, IF THERES ONE
    public static Object invoke(String func, Globals globals){
        return invoke(func, null, globals);
    }
    public static Object invoke(String func, ArrayList parameters, Globals globals) {
        if (parameters != null && parameters.size() > 0) {
            LuaValue[] values = new LuaValue[parameters.size()];
            for (int i = 0; i < parameters.size(); i++)
                values[i] = CoerceJavaToLua.coerce(parameters.get(i));
            return globals.get(func).call(LuaValue.listOf(values));
        } else {
            return globals.get(func).call();
        }
    }
}
