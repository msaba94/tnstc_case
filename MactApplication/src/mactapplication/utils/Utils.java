/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mactapplication.utils;

import mactapplication.model.CaseDetail;

/**
 *
 * @author Sabapathi
 */
public class Utils {

    public static CaseDetail selectedCase;

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
