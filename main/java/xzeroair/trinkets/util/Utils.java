package xzeroair.trinkets.util;

import java.io.File;
import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

public class Utils {

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
		return TypeToken
				.getParameterized(classObj, typeObj)
				.getType();
	}

}
