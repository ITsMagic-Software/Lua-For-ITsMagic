package JAVARuntime;

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
