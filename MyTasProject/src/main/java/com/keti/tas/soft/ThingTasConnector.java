package com.keti.tas.soft;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.intel.bluetooth.BlueCoveConfigProperties;
import com.intel.bluetooth.BlueCoveImpl;
import com.intel.bluetooth.BluetoothRFCommClientConnection;

import jssc.SerialPort;
import jssc.SerialPortException;
import ke.pe.vanilet.bluetooth.BTConnection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ChenNan
 */
public class ThingTasConnector extends TasAbstractServer {
	private static final Log log = LogFactory.getLog(ThingTasConnector.class);
	public static RemoteDevice rD;
	public static final UUID SOLMI_UUID = new UUID("0000110100001000800000805F9B34FB", false);

	private final String seriaPortName = "/dev/ttyS0";
	private final SerialPort serialPort;
	private boolean isActive = false;

	private DiscoveryListener listener = null;
	private Vector<RemoteDevice> devicesDiscovered;
	private DiscoveryAgent agent;
	private boolean started = false;
	private static Object inquiryCompletedEvent = new Object();

	private boolean isConnection = false;
	private BluetoothRFCommClientConnection my_con = null;

	public Vector<RemoteDevice> getDevices() {
		return devicesDiscovered;
	}

	public ThingTasConnector() {
		serialPort = new SerialPort(seriaPortName);
		devicesDiscovered = new Vector<RemoteDevice>();

		try {
			agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
		} catch (Exception e) {
			e.printStackTrace();
			agent = null;
			return;
		}

		listener = new DiscoveryListener() {

			public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
				System.out.println("Device " + btDevice.getBluetoothAddress() + " found");
				System.out.println(Thread.currentThread().getName());
				devicesDiscovered.addElement(btDevice);
				try {
					System.out.println("     name " + btDevice.getFriendlyName(false));
				} catch (IOException cantGetDeviceName) {
				}

			}

			public void inquiryCompleted(int discType) {
				System.out.println("Device Inquiry completed!");

				synchronized (inquiryCompletedEvent) {
					inquiryCompletedEvent.notifyAll();
				}
				started = false;
			}

			public void serviceSearchCompleted(int transID, int respCode) {
			}

			public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
			}
		};

	}

	public void startScan() {
		if (started == true) {
			System.out.println("already scan have been started ");
			return;
		}
		try {
			started = agent.startInquiry(DiscoveryAgent.GIAC, listener);
			if (started == true) {
				synchronized (inquiryCompletedEvent) {
					inquiryCompletedEvent.wait();
				}
			}

			System.out.println("wait for device inquiry to complete...");
			System.out.println(devicesDiscovered.size() + " devices (s) found!!");
		} catch (BluetoothStateException e) {
			System.out.println("already scan have been started");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Override
	// public void start() {
	// isActive = true;
	//
	// log.info("A TAS server for Thing is running.");
	//
	// Runnable runnable = new Runnable() {
	// public void run() {
	// try {
	// serialPort.openPort();
	// serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
	// SerialPort.STOPBITS_1,
	// SerialPort.PARITY_NONE);
	//
	// Pattern pattern = Pattern.compile(
	// "(\\{\\'[a-z]+\\'\\:\\'[0-9\\.]+\\'\\,\\'[a-z]+\\'\\:\\'[0-9\\.]+\\'\\})",
	// Pattern.CASE_INSENSITIVE);
	//
	// while (isActive) {
	// String sensorValue = serialPort.readString(4);
	// String read = "{'temperature':'56','humidity':'" + sensorValue + "'}";
	//
	// Matcher matcher = pattern.matcher(read);
	//
	// if (matcher.find()) {
	// log.info("Data received from thing: " + matcher.group(1));
	//
	// activeReceiveEvent(matcher.group(1));
	// }
	// }
	// } catch (SerialPortException ex) {
	// log.error(ex);
	// }
	// }
	// };
	// new Thread(runnable).start();
	// }

	@Override
	public void start() {
		isActive = true;

		log.info("A TAS server for Thing is running.");

		Runnable runnable = new Runnable() {
			public void run() {
				BTConnection btCon = new BTConnection(null);
				BlueCoveImpl.setConfigProperty(BlueCoveConfigProperties.PROPERTY_INQUIRY_DURATION,
						"3");
				
				int i = 0 ;
				Random rand = new Random();
				while(isActive)
				{
					//test code
					int HR_randValue = rand.nextInt(30) + 60 ;
					int Stress_randValue = rand.nextInt(50) + 50 ;
					String read = "{'HR':'" + HR_randValue + "','STRESS':'" + 
							Stress_randValue + "','ECG':'0'}";
					System.out.println(read);
					try{
						Thread.sleep(3000);
					}catch(Exception e)
					{
						e.printStackTrace();
					}
					
					activeReceiveEvent(read);
//					
					
					//start real code
//					devicesDiscovered.clear();
//					startScan();
//					log.error("start scan!!");
//					
//					for(int j = 0 ; j < devicesDiscovered.size() ; j++)
//					{
//						System.out.println("devicesDiscovered.size() : " + devicesDiscovered.size());
//						
//						try {
//							if(!(devicesDiscovered.get(j).getBluetoothAddress().equals("00189A219B1B")))
//							{
//								log.error("can not find deivce");
//								continue ;
//							}
//						} catch (Exception e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//						
//						HashMap<String , Integer> sensor = btCon.BTSearchService(devicesDiscovered.get(j));
//						
//						log.info("Close bluetooth !!!");
//						if(sensor == null)
//						{
//							log.error("hash is null");
//						}
//						
//						if(sensor.get("HR") == null)
//							return ;
//						String read = "{'HR':'" + sensor.get("HR") + "','STRESS':'" + 
//						sensor.get("STRESS") + "','ECG':'0'}";
//						log.info("READ : " + read);
////						Matcher matcher = pattern.matcher(read);
//						
//						activeReceiveEvent(read);
//						
//						try{
//							Thread.sleep(10000);
//						}catch(Exception e)
//						{
//							e.printStackTrace();
//						}
						
					}
					i++;
				}

			}
		};
		new Thread(runnable).start();
	}

	@Override
	public void stop() {
		isActive = false;

		if (serialPort != null && serialPort.isOpened()) {
			try {
				serialPort.closePort();
			} catch (SerialPortException e) {
				log.error(e);
			}
		}
	}
}
