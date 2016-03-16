//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.solmi.protocol.rev4;

import java.util.Arrays;

import com.solmi.protocol.rev4.SHC_M1.SHC_Data;

import kr.pe.vanilet.health.protocol.HealthContents;

public class DataAnalysis {
    private static final boolean DEBUG = false;
    private static final String TAG = "DataAnalysis.class";
    SHC_Data g_SHCData = new SHC_Data();
    HealthContents g_hc = new HealthContents();
    public static final int BUFFER_SIZE = 2048;
    public byte[] tempPkt = new byte[2048];
    public int tempPktPos = 0;
    public boolean pktFlag = false;
    public boolean pktContinue = false;
    public int pktCnt = 0;
    boolean fflag = false;
    public byte[] rtcBuffer = new byte[10];

    public DataAnalysis() {
        this.g_hc.init_HR_STR();
    }

    public void setSHCData(SHC_Data data) {
        this.g_SHCData = data;
    }

    public static int getHeartRate(byte high, byte low) {
        int hrH = high < 0?high + 256:high;
        int hrL = low < 0?low + 256:low;
        int hr = hrH * 256 + hrL;
        return hr;
    }

    public static int getStress(byte high, byte low) {
        int stressH = high < 0?high + 256:high;
        int stressL = low < 0?low + 256:low;
        int stress = (stressH * 256 + stressL) / 10;
        return stress;
    }

    public static int getFatMass(byte high, byte low) {
        int fatH = high < 0?high + 256:high;
        int fatL = low < 0?low + 256:low;
        int fat = (fatH * 256 + fatL) / 10;
        return fat;
    }

    public static int getStepNumber(byte high, byte low) {
        int stepH = high < 0?high + 256:high;
        int stepL = low < 0?low + 256:low;
        int step = stepH * 256 + stepL;
        return step;
    }

    public static int getDistance(byte high, byte low) {
        int distanceH = high < 0?high + 256:high;
        int distanceL = low < 0?low + 256:low;
        int distance = distanceH * 256 + distanceL;
        return distance;
    }

    public static int getCalorie(byte high, byte low) {
        int calorieH = high < 0?high + 256:high;
        int calorieL = low < 0?low + 256:low;
        int calorie = (calorieH * 256 + calorieL) / 10;
        return calorie;
    }

    public static int getSample(byte NS_H, byte NS_L) {
        int iNS_H = NS_H < 0?NS_H + 256:NS_H;
        int iNS_L = NS_L < 0?NS_L + 256:NS_L;
        int nSamples = iNS_H * 256 + iNS_L;
        return nSamples;
    }

    public static int getDrink(byte high, byte low) {
        int iNS_H = high < 0?high + 256:high;
        int iNS_L = low < 0?low + 256:low;
        int nDrink = iNS_H * 256 + iNS_L;
        return nDrink;
    }

    public static int getSmoke(byte high, byte low) {
        int iNS_H = high < 0?high + 256:high;
        int iNS_L = low < 0?low + 256:low;
        int nSmoke = iNS_H * 256 + iNS_L;
        return nSmoke;
    }

    public static DataAnalysis.Element getRawData(byte high, byte low) {
        DataAnalysis.Element ele = new DataAnalysis.Element();
        if((high & 136) == 136 && (low & 128) == 128) {
            ele.value = ((high & 7) << 7) + (low & 127);
            ele.channel = (byte)((high & 112) >> 4);
        }

        return ele;
    }

    public static int getLow10bitValue(byte high, byte low) {
        boolean val = false;
        int valL = low < 0?low + 256:low;
        int val1 = ((high & 3) << 8) + valL;
        return val1;
    }

    public static int getChannel(byte high, byte low) {
        int channel = -1;
        if((high & 136) == 136 && (low & 128) == 128) {
            channel = (high & 112) >> 4;
        }

        return channel;
    }

    public static int getUInt(byte raw) {
        int uint = raw < 0?raw + 256:raw;
        return uint;
    }

    public boolean checkPacket(byte[] buffer, int length) {
        boolean bRtn = false;
        if(buffer[0] == 51 && buffer[1] == 85 && buffer[2] == 119) {
            this.pktFlag = true;
            if(buffer[4] == -94 || buffer[4] == -90) {
                this.pktContinue = true;
            }
        }

        if(this.pktFlag) {
            if(this.tempPktPos + length > 2048) {
                this.tempPktPos = 0;
            }

            System.arraycopy(buffer, 0, this.tempPkt, this.tempPktPos, 10);
            this.tempPktPos += length;
            if(this.pktContinue) {
                if(this.tempPktPos > 10 && this.tempPkt[this.tempPktPos - 1] == 13) {
                    this.g_SHCData = this.processHeaderPacket(this.tempPkt, this.tempPktPos, this.g_SHCData);
                    String msg = "";

                    for(int i = 0; i < this.tempPktPos; ++i) {
                        msg = msg + String.format("x%02x ", new Object[]{Byte.valueOf(this.tempPkt[i])});
                    }

                    this.pktFlag = false;
                    this.pktContinue = false;
                    this.tempPktPos = 0;
                    Arrays.fill(this.tempPkt, (byte)0);
                    bRtn = true;
                } else {
                    bRtn = false;
                }
            } else if(this.tempPkt[this.tempPktPos - 1] == 13) {
                this.pktFlag = false;
                this.pktCnt = this.tempPkt[8];
                this.g_SHCData = this.processHeaderPacket(this.tempPkt, this.tempPktPos, this.g_SHCData);
                this.tempPktPos = 0;
                Arrays.fill(this.tempPkt, (byte)0);
                bRtn = true;
            }
        } else {
            this.g_SHCData = this.processRawDataPacket(buffer, length, this.g_SHCData);
            bRtn = true;
        }

        return bRtn;
    }

    public SHC_Data getSHCData() {
        return this.g_SHCData;
    }

    public SHC_Data processHeaderPacket(byte[] buffer, int bufferLength, SHC_Data refData) {
        new SHC_Data();
        if(buffer[4] == -94) {
            if(buffer[6] == 7) {
                refData.packetType = 208;
                refData.daqHeader.DevStatus = buffer[5];
                refData.daqHeader.ReqSig = buffer[6];
                this.parsingStepAll(buffer, bufferLength, refData);
            } else if(buffer[6] == 6) {
                refData.packetType = 209;
                refData.daqHeader.DevStatus = buffer[5];
                refData.daqHeader.ReqSig = buffer[6];
                refData.daqHeader.Step = getStepNumber(buffer[20], buffer[21]);
            } else if(buffer[6] == 9) {
                refData.daqHeader.DevStatus = buffer[5];
                refData.daqHeader.ReqSig = buffer[6];
            } else if(buffer[6] == 8) {
                refData.daqHeader.DevStatus = buffer[5];
                refData.daqHeader.ReqSig = buffer[6];
            } else if(buffer[6] == 10) {
                refData.packetType = 212;
                refData.daqHeader.DevStatus = buffer[5];
                refData.daqHeader.ReqSig = buffer[6];
                refData.daqHeader.smoke = getSmoke(buffer[10], buffer[11]);
                refData.daqHeader.drink = getDrink(buffer[12], buffer[13]);
            } else {
                refData.packetType = 204;
                refData.daqHeader.DevStatus = buffer[5];
                refData.daqHeader.ReqSig = buffer[6];
                if(buffer[8] == 0) {
                    this.g_hc.init_HR_STR();
                }

                if(buffer[8] == 21) {
                    refData.daqHeader.Samples[0] = this.g_hc.rtc_hr_stress_calc(buffer[10], buffer[11]);
                    refData.daqHeader.Samples[1] = this.g_hc.rtc_hr_stress_calc(buffer[12], buffer[13]);
                    refData.daqHeader.Samples[2] = this.g_hc.rtc_hr_stress_calc(buffer[14], buffer[15]);
                    refData.daqHeader.Samples[3] = this.g_hc.rtc_hr_stress_calc(buffer[16], buffer[17]);
                    refData.daqHeader.Samples[4] = this.g_hc.rtc_hr_stress_calc(buffer[18], buffer[19]);
                    this.g_hc.calc_hr_stress();
                } else {
                    refData.daqHeader.Samples[0] = this.g_hc.rtc_hr_stress_calc(buffer[10], buffer[11]);
                    refData.daqHeader.Samples[1] = this.g_hc.rtc_hr_stress_calc(buffer[12], buffer[13]);
                    refData.daqHeader.Samples[2] = this.g_hc.rtc_hr_stress_calc(buffer[14], buffer[15]);
                    refData.daqHeader.Samples[3] = this.g_hc.rtc_hr_stress_calc(buffer[16], buffer[17]);
                    refData.daqHeader.Samples[4] = this.g_hc.rtc_hr_stress_calc(buffer[18], buffer[19]);
                }

                refData.daqHeader.Hr = this.g_hc.hcont.HeartRate;
                refData.daqHeader.Stress = (float)((int)this.g_hc.hcont.Stress);
            }
        } else if(buffer[4] == -90) {
            if(buffer[6] == 7) {
                refData.packetType = 208;
                refData.daqHeader.DevStatus = buffer[5];
                refData.daqHeader.ReqSig = buffer[6];
                this.parsingM1FStepAll(buffer, bufferLength, refData);
            } else if(buffer[6] == 9) {
                refData.packetType = 210;
                refData.daqHeader.DevStatus = buffer[5];
                refData.daqHeader.ReqSig = buffer[6];
                refData.daqHeader.Fat = (float)getStepNumber(buffer[27], buffer[26]);
            }
        } else {
            switch(buffer[4]) {
            case -96:
                refData.packetType = 213;
                refData.devInfo.FirmwareVersion = String.format("%d.%d.%d", new Object[]{Integer.valueOf(getUInt(buffer[5])), Integer.valueOf(getUInt(buffer[6])), Integer.valueOf(getUInt(buffer[7]))});
                refData.devInfo.FirmwareFlag = buffer[8];
                break;
            case -93:
                refData.packetType = 201;
                refData.devInfo.DevID = buffer[5];
                refData.devInfo.SamplingRate = buffer[6];
                refData.devInfo.RunTime = buffer[7];
                break;
            case -92:
                refData.packetType = 202;
                refData.userInfo.Gender = buffer[5];
                refData.userInfo.Age = buffer[6];
                refData.userInfo.Weight = buffer[7];
                refData.userInfo.Height = buffer[8];
                break;
            case -91:
                refData.packetType = 206;
                refData.accInfo.steps = getStepNumber(buffer[5], buffer[6]);
                refData.accInfo.threshold = buffer[7];
                refData.accInfo.duration = buffer[8];
                break;
            case -88:
                refData.packetType = 214;
                refData.devInfo.SettingTime = String.format("%d:%d:%d", new Object[]{Integer.valueOf(getUInt(buffer[5])), Integer.valueOf(getUInt(buffer[6])), Integer.valueOf(getUInt(buffer[7]))});
                break;
            case -87:
                refData.packetType = 215;
                refData.devInfo.SettingDate = String.format("%d-%d-%d", new Object[]{Integer.valueOf(getUInt(buffer[5])), Integer.valueOf(getUInt(buffer[6])), Integer.valueOf(getUInt(buffer[7]))});
                refData.devInfo.SettingDate = refData.devInfo.SettingDate + " " + this.getDayOfWeek(getUInt(buffer[8]));
                break;
            case -82:
                refData.packetType = 205;
                break;
            case -34:
                refData.packetType = 201;
                refData.devInfo.DevID = buffer[5];
                refData.devInfo.SamplingRate = buffer[6];
                refData.devInfo.RunTime = buffer[7];
                break;
            case -18:
                refData.packetType = 202;
                refData.userInfo.Gender = buffer[5];
                refData.userInfo.Age = buffer[6];
                refData.userInfo.Weight = buffer[7];
                refData.userInfo.Height = buffer[8];
            }
        }

        return refData;
    }

    public SHC_Data processRawDataPacket(byte[] buffer, int bufferLength, SHC_Data refData) {
        new SHC_Data();
        SHC_Data data = refData;
        int nCnt;
        if(refData.daqHeader.ReqSig == 7) {
            refData.packetType = 207;

            for(nCnt = 0; nCnt < bufferLength; nCnt += 2) {
                data.rawData.Steps = getStepNumber(buffer[nCnt], buffer[nCnt + 1]);
            }
        } else if(refData.daqHeader.ReqSig == 6) {
            refData.packetType = 207;
            refData.rawData.Steps = getStepNumber(buffer[0], buffer[1]);
            refData.rawData.Acc_X = getLow10bitValue(buffer[4], buffer[5]);
            refData.rawData.Acc_Y = getLow10bitValue(buffer[6], buffer[7]);
            refData.rawData.Acc_Z = getLow10bitValue(buffer[8], buffer[9]);
        } else {
            int i;
            if(refData.daqHeader.ReqSig == 9) {
                refData.packetType = 210;
                nCnt = 0;

                for(i = 0; i < bufferLength; i += 2) {
                    data.rawData.bodySample[nCnt++] = getRawData(buffer[i], buffer[i + 1]).value;
                }
            } else if(refData.daqHeader.ReqSig == 8) {
                refData.packetType = 211;
                nCnt = 0;

                for(i = 0; i < bufferLength; i += 2) {
                    data.rawData.bodySample[nCnt++] = getRawData(buffer[i], buffer[i + 1]).value;
                }
            } else {
                refData.packetType = 203;
                refData.rawData.Ecg_0 = getRawData(buffer[0], buffer[1]).value;
                refData.rawData.Ecg_1 = getRawData(buffer[2], buffer[3]).value;
                refData.rawData.Acc_X = getLow10bitValue(buffer[4], buffer[5]);
                refData.rawData.Acc_Y = getLow10bitValue(buffer[6], buffer[7]);
                refData.rawData.Acc_Z = getLow10bitValue(buffer[8], buffer[9]);
            }
        }

        return data;
    }

    public SHC_Data parsingStepAll(byte[] buffer, int bufferLength, SHC_Data data) {
        new SHC_Data();
        SHC_Data tmpData = data;
        boolean dLen_L = false;
        data.stepAllInfo.setStartTime(buffer[7], buffer[8], buffer[9]);
        data.stepAllInfo.setCurrentTime(buffer[bufferLength - 4], buffer[bufferLength - 3], buffer[bufferLength - 2]);
        data.stepAllInfo.CurrentStep = getStepNumber(buffer[bufferLength - 8], buffer[bufferLength - 7]);
        int dLen_H = (buffer[5] >> 7) * 256;
        int var10 = buffer[3] < 0?buffer[3] + 256:buffer[3];
        int dLen = dLen_H + var10;
        byte startIdx = 10;

        for(int i = 0; i < dLen; ++i) {
            tmpData.stepAllInfo.Steps.add(Integer.valueOf(getStepNumber(buffer[startIdx + i * 2], buffer[startIdx + i * 2 + 1])));
        }

        return this.g_SHCData;
    }

    public SHC_Data parsingM1FStepAll(byte[] buffer, int bufferLength, SHC_Data data) {
        new SHC_Data();
        SHC_Data tmpData = data;
        boolean dLen_L = false;
        data.stepAllInfo.setStartTime(buffer[7], buffer[8], buffer[9]);
        data.stepAllInfo.setCurrentTime(buffer[bufferLength - 4], buffer[bufferLength - 3], buffer[bufferLength - 2]);
        int nRecords = getStepNumber(buffer[bufferLength - 5], buffer[bufferLength - 6]);
        int currStep = getStepNumber(buffer[bufferLength - 7], buffer[bufferLength - 8]);
        data.stepAllInfo.CurrentStep = currStep;

        for(int i = 0; i < nRecords; ++i) {
            int rIdx = getStepNumber(buffer[(i + 1) * 10 + 1], buffer[(i + 1) * 10]);
            int step = getStepNumber(buffer[(i + 1) * 10 + 3], buffer[(i + 1) * 10 + 2]);
            tmpData.stepAllInfo.Steps.add(Integer.valueOf(step));
        }

        return this.g_SHCData;
    }

    public String getDayOfWeek(int dof) {
        String strDOF = "";
        switch(dof) {
        case 0:
            strDOF = "SUN";
            break;
        case 1:
            strDOF = "MON";
            break;
        case 2:
            strDOF = "TUE";
            break;
        case 3:
            strDOF = "WED";
            break;
        case 4:
            strDOF = "THU";
            break;
        case 5:
            strDOF = "FRI";
            break;
        case 6:
            strDOF = "SAT";
        }

        return strDOF;
    }

    public static class Element {
        int value = 0;
        byte channel = -1;

        public Element() {
        }
    }
}
