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
import java.awt.BorderLayout;
import javax.swing.JLabel;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class StatusBarPanel extends JStatusBar {

    private JLabel lblDate;
    private JLabel lblLeft;
    private JLabel lblTime;
    private TimerThread timerThread;

    public StatusBarPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        lblLeft = new JLabel(I18N.getString("status_default"));     // left status
        lblDate = new JLabel();     // data in right status
        lblTime = new JLabel();     // time in right status
        timerThread = new TimerThread(lblDate, lblTime);    // Timer controller

        lblDate.setHorizontalAlignment(JLabel.CENTER);
        lblTime.setHorizontalAlignment(JLabel.CENTER);
        setLeftComponent(lblLeft);
        addRightComponent(lblDate);
        addRightComponent(lblTime);

        timerThread.start();
    }

    public void stopTimer() {
        if (timerThread.isRunning) {
            timerThread.setRunning(false);
        }
    }
}
