package JAVARuntime;

/**
 * @Author SpeakerFish (Discord community)
 */
public class PFileTools {

    public PFile createPFile(String path) {
        PFile file = null;
        try {
            file = new PFile(path);
        } catch (Exception e) {}

        return file;
    }

    public PFile createPFile() {
        PFile file = new PFile();
        return file;
    }
}
