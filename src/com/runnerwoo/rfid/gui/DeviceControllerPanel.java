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
import com.runnerwoo.rfid.util.SerialComm;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com | http://runnnerwoo.com>
 */
public class DeviceControllerPanel extends JPanel {

    JButton btnConnect;
    JButton btnDisConnect;
    JButton btnOpenDevice;
    JButton btnSearch;
    JComboBox<String> cmbSerial;
    JComboBox<String> cmbPorts;
    JSignalLight light;
    SerialComm serialComm;

    public DeviceControllerPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEADING, 10, 5));

        btnConnect = new JButton(I18N.getString("btn_connect"));
        btnDisConnect = new JButton(I18N.getString("btn_disconnect"));
        btnOpenDevice = new JButton(I18N.getString("btn_open_device"));
        btnSearch = new JButton(I18N.getString("btn_search"));
        cmbSerial = new JComboBox<>();
        cmbPorts = new JComboBox<>();
        light = new JSignalLight();

        btnConnect.setEnabled(false);
        btnDisConnect.setEnabled(false);
        cmbPorts.addItem("00");
        cmbPorts.addItem("01");
        cmbPorts.addItem("02");
        cmbPorts.addItem("FF");
        light.openStatus();

        add(btnOpenDevice);
        add(cmbSerial);
        add(btnConnect);
        add(btnDisConnect);
        add(cmbPorts);
        add(btnSearch);
        add(light);
        
        btnConnect.addActionListener((ActionEvent e) -> {
            btnConnectActionPerformed(e);
        });
        btnDisConnect.addActionListener((ActionEvent e) -> {
            btnDisConnectActionPerformed(e);
        });
        btnOpenDevice.addActionListener((ActionEvent e) -> {
            btnOpenDeviceActionPerformed(e);
        });
    }
    
    private void btnConnectActionPerformed(ActionEvent e) {
        String portName = (String) cmbSerial.getSelectedItem();
        serialComm.connect(portName);
        btnOpenDevice.setEnabled(false);
        btnConnect.setEnabled(false);
        btnDisConnect.setEnabled(true);
    }

    private void btnDisConnectActionPerformed(ActionEvent e) {
        serialComm.disconnect();
        btnOpenDevice.setEnabled(true);
        btnConnect.setEnabled(true);
        btnDisConnect.setEnabled(false);
    }

    private void btnOpenDeviceActionPerformed(ActionEvent e) {
        if (serialComm != null && serialComm.isIsConnected()) {
            serialComm.disconnect();
        }
        serialComm = new SerialComm();
        List<String> availablePorts = serialComm.getPortNames();
        cmbSerial.removeAllItems();
        if (availablePorts.size() > 0) {
            btnConnect.setEnabled(true);
        }
        for (String port : availablePorts) {
            cmbSerial.addItem(port);
        }
    }
}
