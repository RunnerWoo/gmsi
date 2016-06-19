/*
 * Copyright (C) 2016 Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
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

import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author Wu Yan Chang(吴彦昌) <wuyanchang@hotmail.com>
 */
public class SerialCommUtil {

    final private static int TIMEOUT = 2000;
    final private static int NEW_LINE_ASCII = 10;

    private SerialCommUtil() {
    }

    public static SerialPort getSerialPort(String portName) {
        SerialPort serialport = openSerialPort(portName);
        return serialport;
    }
    
    public static void closeSerialPort(SerialPort serialport) {
        if (serialport != null && serialport.isOpened()) {
            try {
                serialport.closePort();
            } catch (SerialPortException ex) {
                Logger.getLogger(SerialCommUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static SerialPort openSerialPort(String portName) {
        SerialPort serialport = null;
        if (serialport == null) {
            serialport = new SerialPort(portName);
        }
        try {
            serialport.openPort();
            serialport.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
//            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
//            serialport.setEventsMask(mask);
//            serialport.addEventListener(new SerialPortReader());
        } catch (SerialPortException ex) {
            Logger.getLogger(SerialCommUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return serialport;
    }

    //<editor-fold desc="Get available port names">
    public static String[] getPortNames() {
        return SerialPortList.getPortNames();
    }
    //</editor-fold>

//    static class SerialPortReader implements SerialPortEventListener {
//
//        @Override
//        public void serialEvent(SerialPortEvent event) {
//            if (event.isRXCHAR()) {
//                if (event.getEventValue() > 0) {
//                    try {
//                        byte buffer[] = serialPort.readBytes();
//                        System.out.println(new String(buffer));
//                    } catch (SerialPortException ex) {
//                        Logger.getLogger(SerialCommUtil.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            } else if (event.isCTS()) {
//                if (event.getEventValue() == 1) {
//                    System.out.println("CTS - ON");
//                } else {
//                    System.out.println("CTS - OFF");
//                }
//            } else if (event.isDSR()) {
//                if (event.getEventValue() == 1) {
//                    System.out.println("DSR - ON");
//                } else {
//                    System.out.println("DSR - OFF");
//                }
//            }
//        }
//
//    }
}
