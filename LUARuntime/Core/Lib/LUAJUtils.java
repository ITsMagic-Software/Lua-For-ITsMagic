package JAVARuntime;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.Globals;
import java.util.List;

public class LUAJUtils {

    public static Globals getGlobals(){
        Globals globals = JsePlatform.standardGlobals();

        List classes = JCompiller.getEngineClasses();
        for (int i = 0; i < classes.size(); i++) {
            JClass jClass = (JClass) classes.get(i);
            Class<?> cls = jClass.getClassAddress();
            LuaValue test = CoerceJavaToLua.coerce(cls);
            globals.set(jClass.getName(), test);
        }

        List userClasses = JCompiller.getUsersClasses();
        for (int i = 0; i < userClasses.size(); i++) {
            JClass jClass = (JClass) userClasses.get(i);
            Class<?> cls = jClass.getClassAddress();
            LuaValue test = CoerceJavaToLua.coerce(cls);
            globals.set(jClass.getName(), test);
        }

        return globals;
    }
}
