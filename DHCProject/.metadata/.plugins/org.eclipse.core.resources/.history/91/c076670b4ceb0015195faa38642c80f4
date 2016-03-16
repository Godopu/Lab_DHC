//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.solmi.protocol.rev4;

import java.util.ArrayList;
import java.util.Date;

public class SHC_M1 {
    private static final boolean DEBUG = false;
    private static final String VERSION = "1.4.0";
    private static final byte SHC_U6 = 1;
    private static final byte SHC_U7 = 2;
    private static final byte SHC_M1 = 3;
    private static final byte SHC_U4 = 4;
    private static final byte SHC_Z1 = 5;
    private static final byte SHC_L1 = 6;
    private static final byte SHC_X1 = -128;
    private static final byte SHC_X2 = -127;
    public static final byte ACK_FAIL = 0;
    public static final byte ACK_SUCCESS = 1;
    public static final byte STX1 = 51;
    public static final byte STX2 = 85;
    public static final byte STX3 = 119;
    public static final byte EOC = 13;
    public static final byte DIF_CMD = -63;
    public static final byte DEV_IN = -34;
    public static final byte USR_IN = -18;
    public static final byte DAQ_CMD = -62;
    public static final byte DAQ_DAT = -94;
    public static final byte SET_DEV = -61;
    public static final byte DEV_ACK = -93;
    public static final byte SET_USR = -60;
    public static final byte USR_ACK = -92;
    public static final byte STP_CMD = -50;
    public static final byte STP_ACK = -82;
    public static final byte SET_ACC = -54;
    public static final byte ACC_ACK = -91;
    public static final byte FIF_CMD = -64;
    public static final byte FIF_IN = -96;
    public static final byte SET_TIME = -56;
    public static final byte TIME_ACK = -88;
    public static final byte SET_DATE = -55;
    public static final byte DATE_ACK = -87;
    public static final byte CAP_CMD = -58;
    public static final byte CAP_DAT = -90;
    private static byte[][] ACK_MAPPING = new byte[][]{{(byte)-63, (byte)-34}, {(byte)-62, (byte)-94}, {(byte)-61, (byte)-93}, {(byte)-60, (byte)-92}, {(byte)-50, (byte)-82}};
    private static final int COMMANDCODING = 1000;
    public static final int DIF_REQ_MSG = 1001;
    public static final int DIF_ACK_MSG = 2001;
    public static final int DAQ_CMD_MSG = 1002;
    public static final int DAQ_DAT_MSG = 2002;
    public static final int SET_DEV_MSG = 1003;
    public static final int DEV_ACK_MSG = 2003;
    public static final int SET_USR_MSG = 1004;
    public static final int USR_ACK_MSG = 2004;
    public static final int STP_CMD_MSG = 1005;
    public static final int STP_ACK_MSG = 2005;
    public static final int SET_ACC_MSG = 1006;
    public static final int ACC_ACK_MSG = 2006;
    public static final int FIF_CMD_MSG = 1007;
    public static final int FIF_ACK_MSG = 2007;
    public static final int SET_TIME_MSG = 1008;
    public static final int TIME_ACK_MSG = 2008;
    public static final int SET_DATE_MSG = 1009;
    public static final int DATE_ACK_MSG = 2009;
    public static final int CAP_CMD_MSG = 1010;
    public static final int CAP_RES_MSG = 2010;
    public static final int CAP_STEP_RES_MSG = 2011;
    public static final int CAP_FATMASS_RES_MSG = 2012;
    public static final int PKT_DEVINFO = 201;
    public static final int PKT_USERINFO = 202;
    public static final int PKT_RAWDATA_ECGACC = 203;
    public static final int PKT_HEADERDATA_ECGACC = 204;
    public static final int PKT_DISCONNECT = 205;
    public static final int PKT_ACCINFO = 206;
    public static final int PKT_RAWDATA_STEP = 207;
    public static final int PKT_HEADERDATA_STEP_ALL = 208;
    public static final int PKT_HEADERDATA_STEP_RT = 209;
    public static final int PKT_RAWDATA_FATMASS = 210;
    public static final int PKT_RAWDATA_WEIGHT = 211;
    public static final int PKT_HEADERDATA_DRINKSMOKE = 212;
    public static final int PKT_FIRMWARE_INFO = 213;
    public static final int PKT_TIME_INFO = 214;
    public static final int PKT_DATE_INFO = 215;
    public static final int PKT_CAP_INFO = 216;
    public static final byte REQSIG_ECG = 0;
    public static final byte REQSIG_HRSTR = 1;
    public static final byte REQSIG_HRSTRECG = 2;
    public static final byte REQSIG_ACC = 3;
    public static final byte REQSIG_ECGACC = 4;
    public static final byte REQSIG_ALL = 5;
    public static final byte REQSIG_STEP_REALTIME = 6;
    public static final byte REQSIG_STEP_ALL = 7;
    public static final byte REQSIG_WEIGHT = 8;
    public static final byte REQSIG_FATMASS = 9;
    public static final byte REQSIG_DRINKSMOKE = 10;
    public static final byte SR_200 = 0;
    public static final byte SR_250 = 1;
    public static final byte SR_500 = 2;
    public static final byte SR_1K = 3;
    public static final byte RT_15 = 0;
    public static final byte RT_30 = 1;
    public static final byte RT_60 = 2;
    public static final byte RT_120 = 3;
    public static final byte RT_180 = 4;
    public static final byte RT_CONTINUE = -1;
    public static final byte CH0_ECG0 = 0;
    public static final byte CH1_ECG1 = 1;
    public static final byte CH2_BODYFAT = 2;
    public static final byte ACC_X = 3;
    public static final byte ACC_Y = 4;
    public static final byte ACC_Z = 5;
    public static final byte DEVSTAT_NORMAL = 0;
    public static final byte DEVSTAT_LEADOFF1 = 1;
    public static final byte DEVSTAT_LEADOFF2 = 2;
    public static final byte CLR_STN_YES = -64;
    public static final byte CLR_STN_NO = -63;
    public static final byte DUR_1 = 0;
    public static final byte DUR_2 = 1;
    public static final byte DUR_3 = 2;
    public static final byte DUR_4 = 3;
    public static final byte ACC_RANGE_2G = 2;
    public static final byte ACC_RANGE_4G = 4;
    public static final byte ACC_RANGE_8G = 8;
    public static final byte ACC_RANGE_16G = 16;
    private static int CONTINUOUS_OPERATION = -1;
    public static SHC_M1.HandshakeListener g_HSL = new SHC_M1.HandshakeListener();

    public SHC_M1() {
    }

    public static String getVersion() {
        return "1.4.0";
    }

    public static byte[] getDeviceInfomation() {
        return com.solmi.protocol.rev4.SHC_M1.SendPacket.getPacket(1001, 
        		com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] setDataAcquisition(byte reqSig) {
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[0] = reqSig;
        return reqSig == 7?setStepAll():com.solmi.protocol.rev4.SHC_M1.SendPacket.
        		getPacket(1002, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] setupDevice(byte sRate, byte rTime) {
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[1] = sRate;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[2] = rTime;
        return com.solmi.protocol.rev4.SHC_M1.SendPacket.
        		getPacket(1003, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] setupUser(int gender, int age, int weight, int height) {
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[0] = (byte)gender;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[1] = (byte)age;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[2] = (byte)weight;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[3] = (byte)height;
        return com.solmi.protocol.rev4.SHC_M1.SendPacket.
        		getPacket(1004, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] stopDevice() {
        return com.solmi.protocol.rev4.SHC_M1.SendPacket.getPacket(1005, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] setupAccelerometer(byte clearSTN, byte threshold, byte duration) {
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[0] = clearSTN;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[1] = threshold;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[2] = duration;
        return com.solmi.protocol.rev4.SHC_M1.SendPacket.
        		getPacket(1006, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] setupAccelerometer(byte clearSTN, byte threshold, byte duration, byte range) {
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[0] = clearSTN;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[1] = threshold;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[2] = duration;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[3] = range;
        return com.solmi.protocol.rev4.SHC_M1.SendPacket.
        		getPacket(1006, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] setStepAll() {
        Date date = new Date();
        byte year = (byte)((byte)(date.getYear() - 113) << 4 & 240);
        byte month = (byte)(date.getMonth() + 1 & 15);
        byte day = (byte)((byte)date.getDate() << 3 & 248);
        byte hour_H = (byte)((byte)date.getHours() >> 2 & 7);
        byte hour_L = (byte)((byte)date.getHours() << 6 & 192);
        byte min = (byte)((byte)date.getMinutes() & 63);
        com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[0] = 7;
        com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[1] = (byte)(year | month);
        com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[2] = (byte)(day | hour_H);
        com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[3] = (byte)(hour_L | min);
        return com.solmi.protocol.rev4.SHC_M1.SendPacket.
        		getPacket(1002, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] setM1FStepAll() {
        Date date = new Date();
        byte year = (byte)((byte)(date.getYear() - 113) << 4 & 240);
        byte month = (byte)(date.getMonth() + 1 & 15);
        byte day = (byte)((byte)date.getDate() << 3 & 248);
        byte hour_H = (byte)((byte)date.getHours() >> 2 & 7);
        byte hour_L = (byte)((byte)date.getHours() << 6 & 192);
        byte min = (byte)((byte)date.getMinutes() & 63);
        com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[0] = 7;
        com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[1] = (byte)(year | month);
        com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[2] = (byte)(day | hour_H);
        com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[3] = (byte)(hour_L | min);
        return com.solmi.protocol.rev4.SHC_M1.SendPacket.
        		getPacket(1010, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] getFirmwareInformation() {
        return com.solmi.protocol.rev4.SHC_M1.SendPacket.
        		getPacket(1007, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] setupTime(int hour, int min, int sec) {
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[0] = (byte)hour;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[1] = (byte)min;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[2] = (byte)sec;
        return com.solmi.protocol.rev4.SHC_M1.SendPacket.
        		getPacket(1008, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] setupDate(int year, int month, int day, int dayOfweek) {
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[0] = (byte)year;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[1] = (byte)month;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[2] = (byte)day;
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[3] = (byte)dayOfweek;
        return com.solmi.protocol.rev4.SHC_M1.SendPacket.
        		getPacket(1009, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static byte[] setCapture(byte reqSig) {
    	com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer[0] = reqSig;
        return reqSig == 7?setM1FStepAll():com.solmi.protocol.rev4.SHC_M1.SendPacket.
        		getPacket(1010, com.solmi.protocol.rev4.SHC_M1.SendPacket.settingBuffer);
    }

    public static class AccInformation {
        public int steps = 0;
        public byte threshold = 0;
        public byte duration = 0;

        public AccInformation() {
        }
    }

    public static class DAQHeader {
        public int DevStatus = 0;
        public int ReqSig = 0;
        public int Tstp = 0;
        public int Hr = 0;
        public float Stress = 0.0F;
        public float Fat = 0.0F;
        public int Step = 0;
        public float Distance = 0.0F;
        public float Calorie = 0.0F;
        public int[] Samples = new int[5];
        public int drink = 0;
        public int smoke = 0;

        public DAQHeader() {
        }
    }

    public static class DeviceInformation {
        public byte DevID = 0;
        public byte SamplingRate = 0;
        public byte RunTime = 0;
        public byte ReqSig = 0;
        public byte SetStat = 0;
        public String FirmwareVersion = "0";
        public byte FirmwareFlag = 0;
        public String SettingTime = "0";
        public String SettingDate = "0";

        public DeviceInformation() {
        }
    }

    public static class HandshakeListener {
        public int lastCommand = 0;

        public HandshakeListener() {
        }

        public void regCommand(byte command) {
            this.lastCommand = command;
        }

        public byte checkHandshake(byte acknowledge) {
            byte ACK = 0;
            switch(acknowledge) {
            case -94:
                if(this.lastCommand == -62) {
                    ACK = 1;
                }
                break;
            case -93:
            case -92:
                if(this.lastCommand == -61 || this.lastCommand == -60) {
                    ACK = 1;
                }
                break;
            case -91:
                if(this.lastCommand == -54) {
                    ACK = 1;
                }
                break;
            case -82:
                if(this.lastCommand == -50) {
                    ACK = 1;
                }
                break;
            case -34:
            case -18:
                if(this.lastCommand == -63) {
                    ACK = 1;
                }
            }

            return ACK;
        }
    }

    public static class RawData {
        public int Ecg_0;
        public int Ecg_1;
        public int BodyFat;
        public int Acc_X;
        public int Acc_Y;
        public int Acc_Z;
        public int Steps;
        public int[] bodySample;

        public RawData() {
            this.Ecg_0 = this.Ecg_1 = this.BodyFat = this.Acc_X = this.Acc_Y = this.Acc_Z = this.Steps = 0;
            this.bodySample = new int[5];
        }
    }

    public static class SHC_Data {
        public int packetType = 0;
        public SHC_M1.DeviceInformation devInfo = new SHC_M1.DeviceInformation();
        public SHC_M1.UserInformation userInfo = new SHC_M1.UserInformation();
        public SHC_M1.DAQHeader daqHeader = new SHC_M1.DAQHeader();
        public SHC_M1.RawData rawData = new SHC_M1.RawData();
        public SHC_M1.AccInformation accInfo = new SHC_M1.AccInformation();
        public SHC_M1.StepAllData stepAllInfo = new SHC_M1.StepAllData();
        
        public SHC_Data() {
        }
    }

    public static class SendPacket {
        public static final int LENGTH = 10;
        private static byte[] sendPkt = new byte[10];
        public static byte[] settingBuffer = new byte[4];
        public SendPacket() {
            for(int i = 0; i < 10; ++i) {
                sendPkt[i] = 0;
            }
        }

        public static void initPkt() {
            sendPkt[0] = 51;
            sendPkt[1] = 85;
            sendPkt[2] = 119;
            sendPkt[9] = 13;
        }

        public static void setData(int requestMsg) {
            switch(requestMsg) {
            case 1001:
                sendPkt[4] = -63;
                com.solmi.protocol.rev4.SHC_M1.g_HSL.regCommand((byte)-63);
                break;
            case 1002:
                sendPkt[4] = -62;
                sendPkt[5] = settingBuffer[0];
                sendPkt[6] = settingBuffer[1];
                sendPkt[7] = settingBuffer[2];
                sendPkt[8] = settingBuffer[3];
                com.solmi.protocol.rev4.SHC_M1.g_HSL.regCommand((byte)-62);
                break;
            case 1003:
                sendPkt[4] = -61;
                sendPkt[5] = settingBuffer[0];
                sendPkt[6] = settingBuffer[1];
                sendPkt[7] = settingBuffer[2];
                sendPkt[8] = settingBuffer[3];
                com.solmi.protocol.rev4.SHC_M1.g_HSL.regCommand((byte)-61);
                break;
            case 1004:
                sendPkt[4] = -60;
                sendPkt[5] = settingBuffer[0];
                sendPkt[6] = settingBuffer[1];
                sendPkt[7] = settingBuffer[2];
                sendPkt[8] = settingBuffer[3];
                break;
            case 1005:
                sendPkt[4] = -50;
                com.solmi.protocol.rev4.SHC_M1.g_HSL.regCommand((byte)-50);
                break;
            case 1006:
                sendPkt[4] = -54;
                sendPkt[5] = settingBuffer[0];
                sendPkt[6] = settingBuffer[1];
                sendPkt[7] = settingBuffer[2];
                sendPkt[8] = settingBuffer[3];
                com.solmi.protocol.rev4.SHC_M1.g_HSL.regCommand((byte)-54);
                break;
            case 1007:
                sendPkt[4] = -64;
                break;
            case 1008:
                sendPkt[4] = -56;
                sendPkt[5] = settingBuffer[0];
                sendPkt[6] = settingBuffer[1];
                sendPkt[7] = settingBuffer[2];
                break;
            case 1009:
                sendPkt[4] = -55;
                sendPkt[5] = settingBuffer[0];
                sendPkt[6] = settingBuffer[1];
                sendPkt[7] = settingBuffer[2];
                sendPkt[8] = settingBuffer[3];
                break;
            case 1010:
                sendPkt[4] = -58;
                sendPkt[5] = settingBuffer[0];
                sendPkt[6] = settingBuffer[1];
                sendPkt[7] = settingBuffer[2];
                sendPkt[8] = settingBuffer[3];
                
                com.solmi.protocol.rev4.SHC_M1.g_HSL.regCommand((byte)-62);
                break;
            default:
                sendPkt[4] = -50;
            }

        }

        public static byte[] getPacket(int requestMsg, byte[] setBuffer) {
            initPkt();
            setData(requestMsg);
            String log = "";

            for(int i = 0; i < 10; ++i) {
                log = log + String.format(" 0x%02x", new Object[]{Byte.valueOf(sendPkt[i])});
            }

            return sendPkt;
        }
    }

    public static class StepAllData {
        public ArrayList<Integer> Steps = new ArrayList();
        public SHC_M1.m1_Time StartTime = new SHC_M1.m1_Time((byte)0, (byte)0, (byte)0);
        public SHC_M1.m1_Time CurrentTime = new SHC_M1.m1_Time((byte)0, (byte)0, (byte)0);
        public int CurrentStep = 0;

        public StepAllData() {
        }

        public void setStartTime(byte t1, byte t2, byte t3) {
            this.StartTime.setTime(t1, t2, t3);
        }

        public void setCurrentTime(byte t1, byte t2, byte t3) {
            this.CurrentTime.setTime(t1, t2, t3);
        }
    }

    public static class UserInformation {
        public byte Gender = 0;
        public byte Age = 0;
        public byte Weight = 0;
        public byte Height = 0;

        public UserInformation() {
        }
    }

    public static class m1_Time {
        public int year;
        public int month;
        public int day;
        public int hour;
        public int min;

        public m1_Time() {
            this.year = this.month = this.day = this.hour = this.min = 0;
        }

        public m1_Time(byte t1, byte t2, byte t3) {
            this.setTime(t1, t2, t3);
        }

        public void setTime(byte t1, byte t2, byte t3) {
            this.year = (t1 >> 4) + 2013;
            this.month = t1 & 15;
            this.day = t2 >> 3 & 31;
            this.hour = t2 << 2 & 28 | t3 >> 6 & 3;
            this.min = t3 & 63;
        }
    }
}
