/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mactapplication.utils;

/**
 *
 * @author my
 */
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
