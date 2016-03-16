//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package kr.pe.vanilet.health.protocol;

public class HealthContents {
    private final boolean DEBUG = false;
    private static final int NBUFFER = 120;
    private static final int NRB = 2;
    private static final int NBEA = 8;
    private static final int NSHIFT = 3;
    private static final int HANDOFF_LOW = 40;
    private static final int MSG_LEADOFF = 101;
    private static final int MSG_NORMAL = 102;
    private final int MAX_HEARTRATE = 250;
    private final float MAX_STRESS = 99.9F;
    private static int[] pRV = new int[120];
    private static int[] pHRV = new int[120];
    private static int[] pSHRV = new int[120];
    private static int offCount = 0;
    private static int fatCount = 0;
    private static int nBeat = 0;
    private static int iFatCount = 0;
    private static boolean leadOffFlag = false;
    private static float[] pSI = new float[120];
    private static int nSI = 0;
    public HealthContents.hCont hcont = new HealthContents.hCont();
    public HealthContents.User user = new HealthContents.User();
    private int[] wBuffer = new int[8];

    public HealthContents() {
    }

    public void init_fatmass() {
        iFatCount = 0;
        boolean i = false;

        int var2;
        for(var2 = 0; var2 < 20; ++var2) {
            pRV[var2] = 80;
        }

        for(var2 = 21; var2 < 40; ++var2) {
            pRV[var2] = 250;
        }

        for(var2 = 41; var2 < 120; ++var2) {
            pRV[var2] = 125;
        }

        for(var2 = 0; var2 < 120; ++var2) {
            pSI[var2] = 50.0F;
        }

    }

    public boolean rtc_leadoff_detection(byte RV_H, byte RV_L) {
        byte tmpFat = 0;
        int iRV_H = RV_H < 0?RV_H + 256:RV_H;
        int iRV_L = RV_L < 0?RV_L + 256:RV_L;
        int tmpx = iRV_H * 256 + iRV_L;
        if(fatCount == 20) {
            pHRV[iFatCount++] = (int)((float)tmpFat / 20.0F);
            fatCount = 0;
            boolean var7 = false;
        } else {
            int var10000 = tmpFat + tmpx;
        }

        if(tmpx < 40) {
            ++offCount;
            if(offCount >= 20) {
                leadOffFlag = true;
                this.hcont.DevState = 101;
                this.hcont.FatMass = 0.0F;
            }
        } else {
            offCount = 0;
        }

        return leadOffFlag;
    }

    public void calc_fatmass(int[] pRV) {
        leadOffFlag = false;
        float bVmin = this.sort_and_average(pHRV, 2, 19, 10);
        float bVmax = this.sort_and_average(pHRV, 22, 39, 10);
        float bVin = this.sort_and_average(pHRV, 42, 59, 10);
        if(bVin <= bVmax && bVin >= bVmin) {
            float a8;
            if(this.user.Age >= 20) {
                a8 = 0.028F;
            } else {
                a8 = 0.1F;
            }

            float Rvalue = 7.9846F * ((bVin - bVmin) * 100.0F / (bVmax - bVmin)) + 406.1F;
            float fTemp = -5.98F + 0.734F * (float)this.user.Height * (float)this.user.Height / Rvalue + 0.0146F * Rvalue + 0.2505F * (float)this.user.Weight + -0.0635F * (float)this.user.Age + (0.0474F * (float)this.user.Weight + 0.0562F * (float)this.user.Height - a8 * (float)this.user.Age - 9.596F) * (float)this.user.Gender;
            if(fTemp >= (float)(this.user.Weight >> 1) && fTemp <= (float)(this.user.Weight - (this.user.Weight >> 4))) {
                float FatMass = (float)this.user.Weight - fTemp;
                float pBF = 100.0F * FatMass / (float)this.user.Weight;
                float var10000 = 100.0F * (float)this.user.Weight / ((float)this.user.Height * (float)this.user.Height);
                this.hcont.FatMass = FatMass;
            } else {
                this.hcont.DevState = 101;
            }
        } else {
            leadOffFlag = true;
            this.hcont.DevState = 101;
        }

    }

    public void init_HR_STR() {
        nBeat = 0;
        this.hcont.HeartRate = 70;
        boolean i = false;

        int var2;
        for(var2 = 0; var2 < 120; ++var2) {
            pHRV[var2] = 160;
        }

        for(var2 = 0; var2 < 8; ++var2) {
            this.wBuffer[var2] = 160;
        }

        this.hcont.Stress = 50.0F;

        for(var2 = 0; var2 < 120; ++var2) {
            pSHRV[var2] = 700;
        }

    }

    public int rtc_hr_stress_calc(byte NS_H, byte NS_L) {
        boolean nSamples = false;
        int iNS_H = NS_H < 0?NS_H + 256:NS_H;
        int iNS_L = NS_L < 0?NS_L + 256:NS_L;
        int var6 = iNS_H * 256 + iNS_L;
        if(var6 == 0) {
            return var6;
        } else {
            ++this.hcont.Stress;
            if(var6 > 50 && var6 < 400) {
                if(nBeat == 120) {
                    nBeat = 0;
                }

                pSHRV[nBeat] = var6;
                pHRV[nBeat++] = var6;
                offCount = 0;
                if(nBeat > 6) {
                    this.calc_hr_stress();
                }
            } else {
                ++offCount;
                if(offCount > 5) {
                    this.hcont.DevState = 101;
                    ++this.hcont.HeartRate;
                    this.hcont.HeartRate = this.hcont.HeartRate > 250?250:this.hcont.HeartRate;
                    ++this.hcont.Stress;
                    this.hcont.Stress = this.hcont.Stress > 99.9F?99.9F:this.hcont.Stress;
                }
            }

            return var6;
        }
    }

    public void calc_hr_stress() {
        boolean aveSamples = false;
        boolean nHeartRate = false;
        float pStress = 0.0F;
        int beatSize = nBeat - 1;
        if(nBeat >= 8 && nBeat <= 88) {
            for(int i = 0; i < 8; ++i) {
                this.wBuffer[i] = pHRV[beatSize - i];
            }

            int var6 = (int)this.sort_and_average(this.wBuffer, 1, 8, 4);
            int var7 = 12000 / var6;
            if(var7 > 250) {
                var7 = 250;
            } else if(var7 < 40) {
                var7 = 40;
            }

            this.hcont.HeartRate = var7;
            this.hcont.DevState = 102;
            pStress = this.stress_average(this.stress_calculate(this.wBuffer, 8));
            if(pStress <= 0.0F) {
                leadOffFlag = true;
                this.hcont.DevState = 101;
            }

            if((double)pStress > 99.9D) {
                pStress = 99.9F;
            }

            this.hcont.Stress = pStress;
        } else {
            leadOffFlag = true;
            this.hcont.DevState = 101;
        }

    }

    private float stress_average(float val) {
        float fAve = 0.0F;
        if(nSI == 119) {
            nSI = 0;
            pSI[0] = pSI[119];
        }

        pSI[++nSI] = val;
        fAve = (pSI[nSI] + pSI[nSI - 1]) / 2.0F;
        pSI[nSI] = fAve;
        return fAve;
    }

    private float sort_and_average(int[] fatVal, int n1, int n2, int nSum) {
        int tmpSum = 0;
        boolean tmpVal = false;
        float fSum = 0.0F;
        int nHalf = n2 - n1 + 1 - nSum;
        nHalf >>= 1;

        int i;
        for(i = n1 - 1; i < n2; ++i) {
            for(int j = i + 1; j < n2; ++j) {
                if(fatVal[i] < fatVal[j]) {
                    int var11 = fatVal[i];
                    fatVal[i] = fatVal[j];
                    fatVal[j] = var11;
                }
            }
        }

        for(i = n1 - 1 + nHalf; i < n2 - nHalf; ++i) {
            tmpSum += fatVal[i];
        }

        fSum = (float)tmpSum / (float)nSum;
        return fSum;
    }

    private float stress_calculate(int[] qSHRV, int nBeats) {
        float dHRV = 0.0F;
        float tempSUM = 0.0F;
        float sHRV = 0.0F;
        float[] HRV = new float[nBeats];
        int bCond = 0;
        int gCond = 0;

        int i;
        for(i = 0; i < nBeats; ++i) {
            float value = (float)qSHRV[i];
            sHRV += value;
        }

        float aHRV = sHRV / (float)nBeats;
        float aHRV_TH = 1.2F * aHRV;
        float aHRV_TL = 0.8F * aHRV;

        for(i = 0; i < nBeats; ++i) {
            float tmp = (float)qSHRV[i];
            if(tmp <= aHRV_TH && tmp >= aHRV_TL) {
                HRV[gCond++] = tmp;
            } else {
                ++bCond;
            }
        }

        if(bCond > nBeats >> 2) {
            return 0.0F;
        } else {
            float SDNN = 0.0F;
            float daHRV = 0.0F;
            sHRV = 0.0F;

            for(i = 0; i < gCond - 1; ++i) {
                dHRV = HRV[i + 1] - HRV[i];
                daHRV += dHRV;
                tempSUM = dHRV * dHRV;
                sHRV += tempSUM;
            }

            float tmp1 = daHRV / (float)gCond;
            float xHRV = sHRV / (float)gCond - tmp1 * tmp1;
            float SDNN2 = xHRV * 5.0F;
            float a;
            if(this.user.Age < 20) {
                a = 0.005F;
            } else if(this.user.Age < 30) {
                a = 0.0055F;
            } else if(this.user.Age < 40) {
                a = 0.0072F;
            } else if(this.user.Age < 50) {
                a = 0.0086F;
            } else if(this.user.Age < 60) {
                a = 0.0104F;
            } else if(this.user.Age < 70) {
                a = 0.0124F;
            } else {
                a = 0.0124F;
            }

            float tmp2 = 100.0F / (1.0F + a * SDNN2);
            return tmp2;
        }
    }

    public class User {
        public int Age = 20;
        public int Weight = 150;
        public int Height = 50;
        public int Gender = 0;

        public User() {
        }
    }

    public class hCont {
        public int DevState = 0;
        public int HeartRate = 0;
        public float Stress = 0.0F;
        public float FatMass = 0.0F;

        public hCont() {
        }
    }
}
