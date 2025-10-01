package dev.cosmic.signalintel;

import android.os.Environment;
import androidx.multidex.MultiDexApplication;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class MainApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        
        // This is the implementation of your idea.
        // We are setting up a global "catcher" for any crash.
        final UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            // When a crash occurs, this code will run.
            
            // Get the full error message (the "stack trace").
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            String stackTrace = sw.toString();

            // Write the error to a file in your phone's "Downloads" folder.
            try {
                File logFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "signal_intel_crash_log.txt"
                );
                FileWriter writer = new FileWriter(logFile);
                writer.append(stackTrace);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // If writing the log fails, do nothing.
            }

            // Finally, let the app crash as it normally would.
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable);
            }
        });
    }
}
