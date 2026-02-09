package xzeroair.trinkets.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.annotation.Nonnull;
import java.io.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithModifiers(Modifier.PRIVATE).create();

    public static File getFileLocation(String parent, String file) {
        final File newInstance = new File(parent, file);
        final File parentDirectory = newInstance.getParentFile();
        final File fileReturn = parentDirectory.exists() ? newInstance : parentDirectory.mkdirs() ? newInstance : null;
        return fileReturn;
    }

    public static Type getClassTypeToken(Class<?> object) {
        return TypeToken.get(object).getType();
    }

    public static <T> Type getClassTypeToken(T object) {
        return getClassTypeToken(object.getClass());
    }

    public static <T> Type getClassTypeTokenParameterized(Class<?> classObj, Class<?>... typeObj) {
        return TypeToken.getParameterized(classObj, typeObj).getType();
    }

    public static <T> T loadJson(File file, Type type) {
        try {
            T loadedClass;
            final FileReader reader = new FileReader(file);
            loadedClass = gson.fromJson(reader, type);
            reader.close();
            return loadedClass;
        } catch (final FileNotFoundException e) {
            System.out.println("Failed to find file: " + file.getPath());
            //			e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void saveJson(File file, T object, Type type) {
        try {
            final FileWriter writer = new FileWriter(file);
            final String jsonString = gson.toJson(object, type);
            writer.write(jsonString);
            writer.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> void saveJson(File file, T object) {
        final Type type = Utils.getClassTypeToken(object);
        saveJson(file, object, type);
    }

    public static byte[] getDataFromURL(String url) {
        if ((url != null) && !url.isEmpty()) {
            try {
                final HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
                http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                http.connect();
                final InputStream stream = http.getInputStream();
                final byte[] output = readAllBytes(stream);
                stream.close();
                http.disconnect();
                return output;
            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    public static byte[] getDataFromFile(File file) {
        if (file != null) {
            try {
                final InputStream stream = new FileInputStream(file);
                final byte[] output = readAllBytes(stream);
                stream.close();
                return output;
            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    @Nonnull
    public static byte[] readAllBytes(@Nonnull InputStream stream) throws IOException {
        int count = 0, pos = 0;
        byte[] output = new byte[0];
        final byte[] buf = new byte[1024];
        while ((count = stream.read(buf)) > 0) {
            if ((pos + count) >= output.length) {
                final byte[] tmp = output;
                output = new byte[pos + count];
                System.arraycopy(tmp, 0, output, 0, tmp.length);
            }

            for (int i = 0; i < count; i++) {
                output[pos++] = buf[i];
            }
        }
        return output;
    }

    /**
     * I need to stop making these and find a reliable lib that includes one.
     * Simple Pair class for specific use cases.
     *
     * @param <A>
     * @param <B>
     */
    public static class TempCache<A, B> {

        protected A first;
        protected B second;

        public TempCache(A first, B second) {
            setFirst(first);
            setSecond(second);
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }

        protected void setFirst(A first) {
            this.first = first;
        }

        protected void setSecond(B second) {
            this.second = second;
        }

    }

    public static class TempTupleCache<A, B, C> extends TempCache<A, B> {

        protected C third;

        public TempTupleCache(A first, B second, C third) {
            super(first, second);
            setThird(third);
        }

        public C getThird() {
            return third;
        }

        protected void setThird(C third) {
            this.third = third;
        }

    }

}
