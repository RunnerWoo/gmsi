/*
 * Copyright (C) 2016 Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runnerwoo.rfid.util;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTextField;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class RfidData {
    
    private static JTextField tagField;
    private static JTextField posField;
    public static int type;
    public static ArrayList<String> data = new ArrayList<>();
    
    public static void dataAvailableEvent(String s) {
        parseData(s);
        
    }
    
    private static void parseData(String s) {
        if (s != null && !s.equals("")) {
            if (s.length() == 1 && s.charAt(0) == '#') {
                type = 0;
            }
            else {
                data.clear();
                String[] array = s.split("[,@]");
                data.addAll(Arrays.asList(array));
                if (tagField != null) {
                    tagField.setText(data.get(0));
                }
                if (posField != null) {
                    posField.setText(data.get(data.size() - 1));
                }
            }
        }
    }

    public static void setTagField(JTextField tagField) {
        RfidData.tagField = tagField;
    }

    public static void setPosField(JTextField posField) {
        RfidData.posField = posField;
    }
    
}
