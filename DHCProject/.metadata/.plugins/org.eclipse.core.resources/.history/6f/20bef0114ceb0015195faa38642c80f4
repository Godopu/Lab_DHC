package ke.pe.vanilet.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Vector;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;

import com.intel.bluetooth.BluetoothRFCommClientConnection;
import com.solmi.protocol.rev4.DataAnalysis;
import com.solmi.protocol.rev4.SHC_M1;
import com.solmi.protocol.rev4.SHC_M1.SHC_Data;

public class BTConnection {

	public static final UUID SOLMI_UUID = new UUID("0000110100001000800000805F9B34FB", false);

	public static UUID[] SOLMI_UUIDS = new UUID[] { SOLMI_UUID };

	private boolean connected = false;
	private Object connectionCompletedEvent;
	private Object serviceSearchCompletedEvent;
	private DiscoveryListener listener = null;
	private int servicesFound = 0;
	private Vector<String> serviceFound;
	private BluetoothRFCommClientConnection solmiCon = null;

	public BTConnection(Object _connectionCompletedEvent) {
		connectionCompletedEvent = _connectionCompletedEvent;
		servicesFound = 0;
		serviceFound = new Vector<String>();
		serviceSearchCompletedEvent = new Object();

		listener = new DiscoveryListener() {
			public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {
				// TODO Auto-generated method stub

			}

			public void inquiryCompleted(int arg0) {
				// TODO Auto-generated method stub

			}

			public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
				for (int i = 0; i < servRecord.length; i++) {
					String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
					if (url == null) {
						servicesFound = 0;
						System.out.println("url is null");
						continue;
					}

					servicesFound = 1;
					serviceFound.add(url);
					if (!connected) {
						connected = openConnection(url);
					}
					DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
					if (serviceName != null) {
						System.out.println("service " + serviceName.getValue() + " found " + url);
					} else {
						System.out.println("service found " + url);
					}

				}
			}

			public void serviceSearchCompleted(int transID, int respCode) {
				System.out.println("service search completed!");
				synchronized (serviceSearchCompletedEvent) {
					serviceSearchCompletedEvent.notifyAll();
				}
			}
		};
	}

	public HashMap<String , Integer> BTSearchService(RemoteDevice rD) {
		int[] attrIDs = new int[] { 0x0100 };
		HashMap<String , Integer> hash = new  HashMap<String , Integer>() ;
		synchronized (serviceSearchCompletedEvent) {
			try {
				LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, SOLMI_UUIDS, rD, listener);

				serviceSearchCompletedEvent.wait();

				if (servicesFound >= 1) {
					if (solmiCon == null){
						System.out.println("solmiCon is null!!");
						return null;
					}

					InputStream in = solmiCon.openInputStream();
					byte[] inputByte = new byte[10];
					DataAnalysis dataAnalysis = new DataAnalysis();
					int length = 0 ;
					while(true)
					{
						if (connected) {
							if (in.available() > 0) {
								
								length += in.read(inputByte , length , 10-length);
								if(length < 10){
									continue ;
								}
								
								length = 0 ;
								dataAnalysis.checkPacket(inputByte, 10);
								SHC_Data shcData = dataAnalysis.getSHCData();
								if (shcData.packetType == SHC_M1.PKT_RAWDATA_ECGACC) {
//									System.out.println("connected : " + shcData.packetType);
								} else if (shcData.packetType == SHC_M1.PKT_HEADERDATA_ECGACC) {
//									System.out.println("connected : " + shcData.packetType);
									 receiveDAQData(shcData , hash);
								}else if(shcData.packetType == SHC_M1.PKT_DISCONNECT){
									break;
								}
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null ;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null ;
			} finally {
				servicesFound = 0;
				// System.out.println("Connection Closed: " + stopConn);
				closeConnection();
				connected = false;
			}

		}
		return hash;
	}

	private void receiveDAQData(SHC_Data data , HashMap<String , Integer> hash) {
		int hr;
		float stress;
		int iStress ; 
		hr = data.daqHeader.Hr;
		stress = data.daqHeader.Stress;
		iStress = (int)stress;
		hash.put("HR" , hr) ;
		hash.put("STRESS" , iStress) ;
		
		System.out.println("HR : " + hr + " , " + "Stress : " + stress);
	}

	private void receiveRawData(SHC_Data data) {
		int x, y, z;
		int ecg;
		x = data.rawData.Acc_X;
		y = data.rawData.Acc_Y;
		z = data.rawData.Acc_Z;
		ecg = data.rawData.Ecg_0;
		// System.out.println("(x,y,z) = " + x +" , " + y + " , " + z);
		System.out.println("ECG : " + ecg);
	}

	private boolean openConnection(String serverURL) {
		try {
			System.out.println("Connecting to " + serverURL);

			// clientSession = (ClientSession) Connector.open(serverURL);
			solmiCon = (BluetoothRFCommClientConnection) Connector.open(serverURL);
			System.out.println("pause");
			if (solmiCon != null)
				System.out.println("connection is success!!");

			OutputStream out = solmiCon.openOutputStream();
			out.write(SHC_M1.setDataAcquisition((byte) 2));

			System.out.println("remote address : " + solmiCon.getRemoteAddress());

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			closeConnection();
			return false;
		}
	}

	public void closeConnection() {
		try {
			System.out.println("close connection!!");
			solmiCon.shutdown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
