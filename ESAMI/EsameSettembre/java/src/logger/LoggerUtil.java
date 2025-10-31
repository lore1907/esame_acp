package logger;
import java.io.File;
import java.io.IOException;
import java.util.logging.*;


public class LoggerUtil {
    
    private static boolean initialized = false;
    
    

    public static final void setup(){ 

        if (initialized) return; 

        try{
            Logger rootLogger = Logger.getLogger("");

            FileHandler fh = new FileHandler("task.log");
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.ALL);

            rootLogger.addHandler(fh);
            rootLogger.setLevel(Level.ALL);

            initialized = true;

        }catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 

    }
}
