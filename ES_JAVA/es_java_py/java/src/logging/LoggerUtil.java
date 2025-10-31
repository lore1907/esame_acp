package logging;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {
    
    private static boolean initialized = false;

    public static void setup() {
        
        if (initialized) return;
        Logger rootlogger = Logger.getLogger("");

        try {
            
            FileHandler fh = new FileHandler("magazzino.log", true);

            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.ALL);

            rootlogger.addHandler(fh);
            rootlogger.setLevel(Level.ALL);
            //rootlogger.setUseParentHandlers(false);

            initialized = true;

        } catch (IOException|SecurityException  e) {
            e.printStackTrace();
        } 
    }

}

