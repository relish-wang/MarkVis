package wang.relish.markvis;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author relish
 * @since 2017/08/29
 */
final class Util {

    private Util() {
        throw new UnsupportedOperationException("can not instantiate a tool class.");
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);

    static String longToString(long time){
        return DATE_FORMAT.format(new Date(time));
    }


    static String readJSONFromFile(@NotNull String path) {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) return null;

        InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        byte[] buffer;
        try {
            int max = 64 * 1024 * 1024;//64M
            int available = is.available();
            if (available > max) {
                available = max;
                System.err.println("JSON is too large!");
            }
            buffer = new byte[available];
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(buffer);
    }

}
