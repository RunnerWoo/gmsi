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
package com.runnerwoo.rfid.gui;

import static com.runnerwoo.rfid.locale.I18N.I18N;
import java.awt.Color;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class LoginForm extends JForm {

    private final static String USERNAME = I18N.getString("lbl_username");
    private final static String PASSWORD = I18N.getString("lbl_password");
    
    public final static Object[] OPTIONS = {
        I18N.getString("btn_login"),
        I18N.getString("btn_exit")
    };
    
    public LoginForm() {
        super();
        initComponents("");
    }
    
    public LoginForm(String s) {
        super();
        initComponents(s);
    }
    
    private void initComponents(String s) {
        addField(USERNAME, false);
        addField(PASSWORD, true);
        addLabel("", Color.BLACK);
        addLabel(s, Color.RED);
        pack();
    }
    
    public String getUsername() {
        return fields.get(0).getText();
    }
    
    public String getPassword() {
        return fields.get(1).getText();
    }
}
