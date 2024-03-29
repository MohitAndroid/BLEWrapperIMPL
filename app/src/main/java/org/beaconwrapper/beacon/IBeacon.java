/**
 * Radius Networks, Inc.
 * http://www.radiusnetworks.com
 *
 * @author David G. Young
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.beaconwrapper.beacon;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The <code>IBeacon</code> class represents a single hardware iBeacon detected by
 * an Android device.
 *
 * <pre>An iBeacon is identified by a three part identifier based on the fields
 * proximityUUID - a string UUID typically identifying the owner of a
 *                 number of ibeacons
 * major - a 16 bit integer indicating a group of iBeacons
 * minor - a 16 bit integer identifying a single iBeacon</pre>
 * <p>
 * An iBeacon sends a Bluetooth Low Energy (BLE) advertisement that contains these
 * three identifiers, along with the calibrated tx power (in RSSI) of the
 * iBeacon's Bluetooth transmitter.
 * <p>
 * This class may only be instantiated from a BLE packet, and an RSSI measurement for
 * the packet.  The class parses out the three part identifier, along with the calibrated
 * tx power.  It then uses the measured RSSI and calibrated tx power to do a rough
 * distance measurement (the accuracy field) and group it into a more reliable buckets of
 * distance (the proximity field.)
 *
 */
public class IBeacon {
    /**
     * Less than half a meter away
     */
    public static final int PROXIMITY_IMMEDIATE = 1;
    /**
     * More than half a meter away, but less than four meters away
     */
    public static final int PROXIMITY_NEAR = 2;
    /**
     * More than four meters away
     */
    public static final int PROXIMITY_FAR = 3;
    /**
     * No distance estimate was possible due to a bad RSSI value or measured TX power
     */
    public static final int PROXIMITY_UNKNOWN = 0;

    private static final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f'};

    /**
     * A 16 byte UUID that typically represents the company owning a number of iBeacons
     * Example: E2C56DB5-DFFB-48D2-B060-D0F5A71096E0
     */
    protected String proximityUuid;
    /**
     * A 16 bit integer typically used to represent a group of iBeacons
     */
    protected int major;
    /**
     * A 16 bit integer that identifies a specific iBeacon within a group
     */
    protected int minor;
    /**
     * An integer with four possible values representing a general idea of how far the iBeacon
     * is away
     *
     * @see #PROXIMITY_IMMEDIATE
     * @see #PROXIMITY_NEAR
     * @see #PROXIMITY_FAR
     * @see #PROXIMITY_UNKNOWN
     */
    protected Integer proximity;
    /**
     * A double that is an estimate of how far the iBeacon is away in meters. This name is
     * confusing, but is copied from the iOS7 SDK terminology. Note that this number fluctuates
     * quite a bit with RSSI, so despite the name, it is not super accurate. It is recommended
     * to instead use the proximity field, or your own bucketization of this value.
     */
    protected Double accuracy;
    /**
     * The measured signal strength of the Bluetooth packet that led do this iBeacon detection.
     */
    protected int rssi;
    /**
     * The calibrated measured Tx power of the iBeacon in RSSI
     * This value is baked into an iBeacon when it is manufactured, and
     * it is transmitted with each packet to aid in the distance estimate
     */
    protected int txPower;

    /**
     * The bluetooth mac address
     */
    protected String bluetoothAddress;

    /**
     * If multiple RSSI samples were available, this is the running average
     */
    protected Double runningAverageRssi = null;
    protected String bleDataPayload = "";
    protected String timezoneString = "";

    /**
     * @return accuracy
     * @see #accuracy
     */
    public double getAccuracy() {
        if (accuracy == null) {
            double bestRssiAvailable = rssi;
            if (runningAverageRssi != null) {
                bestRssiAvailable = runningAverageRssi;
            }
            accuracy = calculateAccuracy(txPower, bestRssiAvailable);
        }
        return accuracy;
    }

    /**
     * @return major
     * @see #major
     */
    public int getMajor() {
        return major;
    }

    /**
     * @return minor
     * @see #minor
     */
    public int getMinor() {
        return minor;
    }

    /**
     * @return proximity
     * @see #proximity
     */
    public int getProximity() {
        if (proximity == null) {
            proximity = calculateProximity(getAccuracy());
        }
        return proximity;
    }

    /**
     * @return rssi
     * @see #rssi
     */
    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public void setProximityUuid(String proximityUuid) {
        this.proximityUuid = proximityUuid;
    }

    public void setBleDataPayload(String bleDataPayload) {
        this.bleDataPayload = bleDataPayload;
    }

    /**
     * @return txPowwer
     * @see #txPower
     */
    public int getTxPower() {
        return txPower;
    }

    /**
     * @return proximityUuid
     * @see #proximityUuid
     */
    public String getProximityUuid() {
        return proximityUuid;
    }

    /**
     * @return bluetoothAddress
     * @see #bluetoothAddress
     */
    public String getBluetoothAddress() {
        return bluetoothAddress;
    }


    @Override
    public int hashCode() {
        return minor;
    }


    public String getTimezoneString() {
        return timezoneString;
    }

    public void setTimezoneString(String timezoneString) {
        this.timezoneString = timezoneString;
    }

    /**
     * Two detected iBeacons are considered equal if they share the same three identifiers,
     * regardless of their distance or RSSI.
     */
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof IBeacon)) {
            return false;
        }
        IBeacon thatIBeacon = (IBeacon) that;
        return (thatIBeacon.getMajor() == this.getMajor() && thatIBeacon.getMinor() ==
                this.getMinor() && thatIBeacon.getProximityUuid().equals(this.getProximityUuid()));
    }

    /**
     * Construct an iBeacon from a Bluetooth LE packet collected by Android's Bluetooth APIs
     *
     * @param scanData The actual packet bytes
     * @param rssi     The measured signal strength of the packet
     * @return An instance of an <code>IBeacon</code>
     */
    public static IBeacon fromScanData(byte[] scanData, int rssi) {
        return fromScanData(scanData, rssi, null);
    }

    /**
     * Construct an iBeacon from a Bluetooth LE packet collected by Android's Bluetooth APIs,
     * including the raw bluetooth device info
     *
     * @param scanData The actual packet bytes
     * @param rssi     The measured signal strength of the packet
     * @param device   The bluetooth device that was detected
     * @return An instance of an <code>IBeacon</code>
     */
    @TargetApi(5)
    public static IBeacon fromScanData(byte[] scanData, int rssi, BluetoothDevice device) {
        int startByte = 2;
        boolean patternFound = false;
        while (startByte <= 5) {
            if (((int) scanData[startByte + 2] & 0xff) == 0x02 &&
                    ((int) scanData[startByte + 3] & 0xff) == 0x15) {
                // yes!  This is an iBeacon
                patternFound = true;
                break;
            } else if (((int) scanData[startByte] & 0xff) == 0x2d &&
                    ((int) scanData[startByte + 1] & 0xff) == 0x24 &&
                    ((int) scanData[startByte + 2] & 0xff) == 0xbf &&
                    ((int) scanData[startByte + 3] & 0xff) == 0x16) {
                return getNewBeacon(scanData);
            } else if (((int) scanData[startByte] & 0xff) == 0xad &&
                    ((int) scanData[startByte + 1] & 0xff) == 0x77 &&
                    ((int) scanData[startByte + 2] & 0xff) == 0x00 &&
                    ((int) scanData[startByte + 3] & 0xff) == 0xc6) {
                return getNewBeacon(scanData);
            }
            startByte++;
        }


        if (!patternFound) {
            // This is not an iBeacon
            return null;
        }

        IBeacon iBeacon = new IBeacon();

        iBeacon.major = (scanData[startByte + 20] & 0xff) * 0x100 + (scanData[startByte + 21] &
                0xff);
        iBeacon.minor = (scanData[startByte + 22] & 0xff) * 0x100 + (scanData[startByte + 23] &
                0xff);
        iBeacon.txPower = (int) scanData[startByte + 24]; // this one is signed
        iBeacon.rssi = rssi;
        iBeacon.bleDataPayload = getPayloadData(scanData);
        iBeacon.timezoneString = getDateCurrentTimeZone();

        // AirLocate:
        // 02 01 1a 1a ff 4c 00 02 15  # Apple's fixed iBeacon advertising prefix
        // e2 c5 6d b5 df fb 48 d2 b0 60 d0 f5 a7 10 96 e0 # iBeacon profile uuid
        // 00 00 # major
        // 00 00 # minor
        // c5 # The 2's complement of the calibrated Tx Power

        // Estimote:
        // 02 01 1a 11 07 2d 24 bf 16
        // 394b31ba3f486415ab376e5c0f09457374696d6f7465426561636f6e00000000000000000000000000000000000000000000000000

        byte[] proximityUuidBytes = new byte[16];
        System.arraycopy(scanData, startByte + 4, proximityUuidBytes, 0, 16);
        String hexString = bytesToHex(proximityUuidBytes);
        StringBuilder sb = new StringBuilder();
        sb.append(hexString.substring(0, 8));
        sb.append("-");
        sb.append(hexString.substring(8, 12));
        sb.append("-");
        sb.append(hexString.substring(12, 16));
        sb.append("-");
        sb.append(hexString.substring(16, 20));
        sb.append("-");
        sb.append(hexString.substring(20, 32));
        iBeacon.proximityUuid = sb.toString();

        if (device != null) {
            iBeacon.bluetoothAddress = device.getAddress();
        }


        return iBeacon;
    }

    private static IBeacon getNewBeacon(byte[] scanData) {
        IBeacon iBeacon = new IBeacon();
        iBeacon.major = 0;
        iBeacon.minor = 0;
        iBeacon.proximityUuid = "00000000-0000-0000-0000-000000000000";
        iBeacon.txPower = -55;
        iBeacon.bleDataPayload = getPayloadData(scanData);
        iBeacon.timezoneString = getDateCurrentTimeZone();
        return iBeacon;
    }

    public String getBleDataPayload() {
        return bleDataPayload;
    }

    private static String getPayloadData(byte[] scanRecord) {
        StringBuilder dataPayload = new StringBuilder();
        if (scanRecord.length > 0) {
            //14
            for (int i = 9; i < scanRecord.length; i++) {
                try {
                    String data = new String(new byte[]{scanRecord[i]}, "UTF-8");
                    dataPayload.append(data);
                } catch (UnsupportedEncodingException e) {
                    Log.e(IBeacon.class.getName(), e.getMessage(), e);
                }
            }
        }

        dataPayload = new StringBuilder(cleanTextContent(dataPayload.toString()));
        return dataPayload.toString().trim();
    }

    private static String cleanTextContent(String text) {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");

        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }

    protected IBeacon(IBeacon otherIBeacon) {
        this.major = otherIBeacon.major;
        this.minor = otherIBeacon.minor;
        this.accuracy = otherIBeacon.accuracy;
        this.proximity = otherIBeacon.proximity;
        this.runningAverageRssi = otherIBeacon.runningAverageRssi;
        this.rssi = otherIBeacon.rssi;
        this.proximityUuid = otherIBeacon.proximityUuid;
        this.txPower = otherIBeacon.txPower;
        this.bluetoothAddress = otherIBeacon.bluetoothAddress;
    }

    protected IBeacon() {

    }


    public static String getDateCurrentTimeZone() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(new Date().getTime());
        } catch (Exception e) {
            Log.e(IBeacon.class.getName(), e.getMessage(), e);
        }
        return "";
    }

    protected static double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }


        Log.d("RSSI", "" + rssi);
        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
        }
    }

    protected static int calculateProximity(double accuracy) {
        if (accuracy < 0) {
            return PROXIMITY_UNKNOWN;
        }
        if (accuracy < 0.5) {
            return IBeacon.PROXIMITY_IMMEDIATE;
        }
        // forums say 3.0 is the near/far threshold, but it looks to be based on experience that
        // this is 4.0
        if (accuracy <= 4.0) {
            return IBeacon.PROXIMITY_NEAR;
        }
        // if it is > 4.0 meters, call it far
        return IBeacon.PROXIMITY_FAR;

    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
