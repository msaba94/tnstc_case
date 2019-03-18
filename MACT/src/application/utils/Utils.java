package application.utils;

public class Utils {
	
	public static boolean isValied(String str) {
        if (str == null || "null".equals(str)) {
            return false;
        }

        if (str.equals("")) {
            return false;
        } else {
            return true;
        }
    }

}
