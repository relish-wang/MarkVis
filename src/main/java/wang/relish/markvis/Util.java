package wang.relish.markvis;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;
import org.json.JSONArray;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author relish
 * @since 2017/08/29
 */
final class Util {

    private Util() {
        throw new UnsupportedOperationException("can not instantiate a tool class.");
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);

    static String longToString(long time) {
        return DATE_FORMAT.format(new Date(time));
    }

    static String readStringFromFile(@NotNull String path) {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) return null;

        InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
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
            return "";
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(buffer);
    }

    static JSONArray readJSONFromFile(@NotNull String path) {
        String stringFromFile = readStringFromFile(path);
        if (stringFromFile == null || stringFromFile.equals("")) return new JSONArray("[]");
        return new JSONArray(stringFromFile);
    }

    static List<Map<String, String>> jsonArrToList(@NotNull String path) {
        return jsonArrToList(readJSONFromFile(path));
    }

    private static List<Map<String, String>> jsonArrToList(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.length() == 0) return new ArrayList<Map<String, String>>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String json = jsonArray.getJSONObject(i).toString();
            //noinspection unchecked
            Map<String, String> map = new Gson().fromJson(json, Map.class);
            list.add(map);
        }
        return list;
    }


}
