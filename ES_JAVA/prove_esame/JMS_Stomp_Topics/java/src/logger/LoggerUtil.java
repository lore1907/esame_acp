package logger;
import java.io.IOException;
import java.util.logging.*;


public class LoggerUtil {
    
    private static boolean initialized = false;

    public static void setup() {

        if (initialized) return;

        try {

            Logger rootLogger = Logger.getLogger("");
            FileHandler fh = new FileHandler("biglietteria.log", true);

            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.ALL);

            rootLogger.addHandler(fh);
            rootLogger.setLevel(Level.ALL);

            //Se non voglio il log nel terminale
            //rootLogger.setUseParentHandlers(false);

            initialized = true; 

        } catch (IOException | SecurityException e) {

            System.out.println("Impossibile configurare il FileHandler" + e.getMessage());
            e.printStackTrace();
        }
    }

}
