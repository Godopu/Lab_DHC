/*
 * ------------------------------------------------------------------------
 * Copyright 2014 Korea Electronics Technology Institute
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */

package kr.re.keti.ncube.devicemanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import kr.re.keti.ncube.CSEBase;
import kr.re.keti.ncube.DeviceInfo;
import kr.re.keti.ncube.Firmware;
import kr.re.keti.ncube.MgmtCmd;
import kr.re.keti.ncube.applicationmanager.ApplicationManager;
import kr.re.keti.ncube.httpserver.HttpServerKeti;
import kr.re.keti.ncube.interactionmanager.InteractionManager;
import kr.re.keti.ncube.resourcemanager.ResourceManager;
import kr.re.keti.ncube.securitymanager.SecurityManager;
import kr.re.keti.ncube.thingmanager.ThingManager;

/**
 * &Cube Device Manager class that is initiating the other managers.
 * &Cube의 Device Manager로서 프로그램 시작 시 가장 먼저 실행되어 다른 Manager들을 실행시킴
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 */
public class DeviceManager {
	
	// Initialize manager threads
	private static Thread deviceManagementThread;
	private static Thread resourceManagerThread;
	private static Thread interactionManagerThread;
	private static Thread thingManagerThread;
	private static Thread securityManagerThread;
	private static Thread applicationManagerThread;
	private static Thread httpServer;
	
	// Initialize blocking queues
	private static BlockingQueue<ArrayList<Object>> resourceManagerQueue = new LinkedBlockingQueue<>();
	private static BlockingQueue<ArrayList<Object>> resourceManagerResponseQueue = new LinkedBlockingQueue<>();
	private static BlockingQueue<ArrayList<Object>> interactionManagerQueue = new LinkedBlockingQueue<>();
	private static BlockingQueue<ArrayList<Object>> interactionManagerResponseQueue = new LinkedBlockingQueue<>();
	private static BlockingQueue<ArrayList<Object>> thingManagerQueue = new LinkedBlockingQueue<>();
	private static BlockingQueue<ArrayList<Object>> thingManagerResponseQueue = new LinkedBlockingQueue<>();
	private static BlockingQueue<ArrayList<Object>> securityManagerQueue = new LinkedBlockingQueue<>();
	private static BlockingQueue<ArrayList<Object>> applicationManagerQueue = new LinkedBlockingQueue<>();
	private static BlockingQueue<ArrayList<Object>> deviceManagerQueue = new LinkedBlockingQueue<>();
	private static BlockingQueue<ArrayList<Object>> httpServerQueue = new LinkedBlockingQueue<>();
	
	// Initialize values
	private static CSEBase CSEBase = new CSEBase();
	private static Firmware firmwareInformation = new Firmware();
	private static DeviceInfo deviceInformation = new DeviceInfo();
	private static MgmtCmd deviceMgmtCmd = new MgmtCmd();
	private static MgmtCmd firmwareUpgradeMgmtCmd = new MgmtCmd();
	private static MgmtCmd appInstallMgmtCmd = new MgmtCmd();
	private static String inCSEAddress = null;
	private static String mqttBrokerAddress = null;
	
	private static boolean interopType = false; 
	private static boolean primitiveType = false;
	private static boolean protocolBinding = false;
	private static boolean debugPrint = true;
	
	/**
	 * getCSEProfile Method
	 * Load the device profile from local storage for saving the CSE profile.
	 * CSE Profile 저장을 위해 Local Storage에서 Device 기본 정보를 읽어오는 Method
	 * @throws Exception
	 */
	private static void getTotalInformation() throws Exception {
		String configString;
		
		CSEBase.from = "nCube";

		// Windows only
		BufferedReader in = new BufferedReader(new FileReader("c:\\nCube\\reg.conf"));
		
		// Linux only
		//BufferedReader in = new BufferedReader(new FileReader("/nCube/reg.conf"));
		while ((configString = in.readLine()) != null) {
			
			// CSEBase Information
			if (configString.matches(".*CSEid.*")) {
				CSEBase.CSEID = configString.substring(6, configString.length());
				System.out.println("[DeviceManager] CSEProfile - CSE-ID = " + CSEBase.CSEID);
			}
			else if (configString.matches(".*CSEpasscode.*")) {
				CSEBase.passcode = configString.substring(12, configString.length());
				System.out.println("[DeviceManager] CSEProfile - passcode = " + CSEBase.passcode);
			}
			else if (configString.matches(".*CSEName.*")) {
				CSEBase.resourceName = configString.substring(8, configString.length());
				System.out.println("[DeviceManager] CSEProfile - resourceName = " + CSEBase.resourceName);
			}
			else if (configString.matches(".*CSEPointOfAccess.*")) {
				CSEBase.pointOfAccess = configString.substring(17, configString.length());
				System.out.println("[DeviceManager] CSEProfile - pointOfAccess = " + CSEBase.pointOfAccess);
			}
			else if (configString.matches(".*requestReachability.*")) {
				CSEBase.requestReachability = configString.substring(20, configString.length());
				System.out.println("[DeviceManager] CSEProfile - requestReachability = " + CSEBase.requestReachability);
			}
			
			// Firmware Information
			else if (configString.matches(".*firmwareName.*")) {
				firmwareInformation.name = configString.substring(13, configString.length());
				System.out.println("[DeviceManager] Firmware - name = " + firmwareInformation.name);
			}
			else if (configString.matches(".*firmwareDescription.*")) {
				firmwareInformation.description = configString.substring(20, configString.length());
				System.out.println("[DeviceManager] Firmware - description = " + firmwareInformation.description);
			}
			else if (configString.matches(".*firmwareVersion.*")) {
				firmwareInformation.version = configString.substring(16, configString.length());
				System.out.println("[DeviceManager] Firmware - version = " + firmwareInformation.version);
			}
			else if (configString.matches(".*firmwareURL.*")) {
				firmwareInformation.url = configString.substring(12, configString.length());
				System.out.println("[DeviceManager] Firmware - URL = " + firmwareInformation.url);
			}
			else if (configString.matches(".*firmwareStatus.*")) {
				firmwareInformation.status = configString.substring(15, configString.length());
				System.out.println("[DeviceManager] Firmware - Status = " + firmwareInformation.status);
			}
			
			// Device Information
			else if (configString.matches(".*deviceName.*")) {
				deviceInformation.resourceName = configString.substring(11, configString.length());
				System.out.println("[DeviceManager] DeviceInfo - deviceName = " + deviceInformation.resourceName);
			}
			else if (configString.matches(".*deviceLabel.*")) {
				deviceInformation.deviceLabel = configString.substring(12, configString.length());
				System.out.println("[DeviceManager] DeviceInfo - deviceLabel = " + deviceInformation.deviceLabel);
			}
			else if (configString.matches(".*deviceDescription.*")) {
				deviceInformation.description = configString.substring(18, configString.length());
				System.out.println("[DeviceManager] DeviceInfo - description = " + deviceInformation.description);
			}
			else if (configString.matches(".*deviceManufacturer.*")) {
				deviceInformation.manufacturer = configString.substring(19, configString.length());
				System.out.println("[DeviceManager] DeviceInfo - manufacturer = " + deviceInformation.manufacturer);
			}
			else if (configString.matches(".*deviceModel.*")) {
				deviceInformation.model = configString.substring(12, configString.length());
				System.out.println("[DeviceManager] DeviceInfo - model = " + deviceInformation.model);
			}
			else if (configString.matches(".*deviceType.*")) {
				deviceInformation.deviceType = configString.substring(11, configString.length());
				System.out.println("[DeviceManager] DeviceInfo - deviceType = " + deviceInformation.deviceType);
			}
			else if (configString.matches(".*deviceFwVersion.*")) {
				deviceInformation.fwVersion = configString.substring(16, configString.length());
				System.out.println("[DeviceManager] DeviceInfo - deviceFwVersion = " + deviceInformation.fwVersion);
			}
			else if (configString.matches(".*deviceSwVersion.*")) {
				deviceInformation.swVersion = configString.substring(16, configString.length());
				System.out.println("[DeviceManager] DeviceInfo - deviceSwVersion = " + deviceInformation.swVersion);
			}
			else if (configString.matches(".*deviceHwVersion.*")) {
				deviceInformation.hwVersion = configString.substring(16, configString.length());
				System.out.println("[DeviceManager] DeviceInfo - deviceHwVersion = " + deviceInformation.hwVersion);
			}
			
			// IN-CSE Address
			else if (configString.matches(".*INCSEAddress.*")) {
				inCSEAddress = configString.substring(13, configString.length());
				System.out.println("[DeviceManager] IN-CSE Address = " + inCSEAddress);
			}
			
			// MQTT Broker Address
			else if (configString.matches(".*MQTTBrokerAddress.*")) {
				mqttBrokerAddress = configString.substring(18, configString.length());
				System.out.println("[DeviceManager] MQTT Broker Address = " + mqttBrokerAddress);
			}
			
			// System Config
			else if (configString.matches(".*interopType.*")) {
				String interopTypeString = configString.substring(12, configString.length());
				
				if (interopTypeString.equals("1")) {
					interopType = true;
				}
				else {
					interopType = false;
				}
				System.out.println("[DeviceManager] Interop Type = " + Boolean.toString(interopType));
			}
			
			else if (configString.matches(".*primitiveType.*")) {
				String primitiveTypeString = configString.substring(14, configString.length());
				
				if (primitiveTypeString.equals("1")) {
					primitiveType = true;
				}
				else {
					primitiveType = false;
				}
				System.out.println("[DeviceManager] Primitive Type = " + Boolean.toString(primitiveType));
			}
			
			else if (configString.matches(".*protocolBinding.*")) {
				String protocolBindingString = configString.substring(16, configString.length());
				
				if (protocolBindingString.equals("1")) {
					protocolBinding = true;
				}
				else {
					protocolBinding = false;
				}
				System.out.println("[DeviceManager] Protocol Binding = " + Boolean.toString(protocolBinding));
			}
			
			else if (configString.matches(".*debugPrint.*")) {
				String debugPrintString = configString.substring(11, configString.length());
				
				if (debugPrintString.equals("1")) {
					debugPrint = true;
				}
				else {
					debugPrint = false;
				}
				System.out.println("[DeviceManager] Debug Print = " + Boolean.toString(debugPrint));
			}
			
			// error
			else {
				if (debugPrint) {
					System.out.println("[DeviceManager] Config tap is not defined");
				}
			}
		}
		in.close();
		
		if (debugPrint) {
			System.out.println("[DeviceManager] CSE Profile / Firmware Info / Device Info load... OK");
			System.out.println();
		}
	}
	
	/**
	 * requestCSERegistration Method 
	 * @param CSEProfile
	 * Request remoteCSE registration to the Resource manager.
	 * CSE를 등록하기 위해 Resource Manager로 요청하는 Method
	 */
	private static void requestCSERegistration(CSEBase CSEProfile) {
		ArrayList<Object> resourceSendArrayList = new ArrayList<Object>();
		
		//Date creationTime = new Date();
		
		CSEProfile.CSEBase = "nCube";
		CSEProfile.CSEType = "3";
		CSEProfile.resourceType = "5";
		CSEProfile.resourceID = "CSE000001";
		
		resourceSendArrayList.add("requestCSERegistration");
		resourceSendArrayList.add(CSEProfile);
		
		if (debugPrint) {
			System.out.println("[DeviceManager] CSE Registration start...");
		}
		
		try {
			if (debugPrint) {
				System.out.println("[DeviceManager] Send to Resource Manager - CSE Registration");
				System.out.println();
			}
			
			resourceManagerQueue.put(resourceSendArrayList);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * requestFirmwareCreate Method
	 * @param firmwareInfo
	 * Request firmware resource creation to the Resource manager.
	 * Firmware 정보를 등록하기 위해 Resource Manager로 요청하는 Method
	 */
	private static void requestFirmwareCreate(Firmware firmwareInfo) {
		ArrayList<Object> resourceSendArrayList = new ArrayList<Object>();
		resourceSendArrayList.add("requestFirmwareCreate");
		resourceSendArrayList.add(firmwareInfo);
		
		if (debugPrint) {
			System.out.println("[DeviceManager] Firmware Create start...");
		}
		
		try {
			if (debugPrint) {
				System.out.println("[DeviceManager] Send to Resource Manager - Firmware Create");
				System.out.println();
			}
			
			resourceManagerQueue.put(resourceSendArrayList);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * requestDeviceInfoCreate Method
	 * @param deviceInfo
	 * Request deviceInfo resource creation to the Resource manager.
	 * Device 정보를 등록하기 위해 Resource Manager로 요청하는 Method
	 */
	private static void requestDeviceInfoCreate(DeviceInfo deviceInfo) {
		ArrayList<Object> resourceSendArrayList = new ArrayList<Object>();
		resourceSendArrayList.add("requestDeviceInfoCreate");
		resourceSendArrayList.add(deviceInfo);
		
		if (debugPrint) {
			System.out.println("[DeviceManager] DeviceInfo Create start...");
		}
		
		try {
			if (debugPrint) {
				System.out.println("[DeviceManager] Send to Resource Manager - DeviceInfo Create");
				System.out.println();
			}
			
			resourceManagerQueue.put(resourceSendArrayList);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * requestDeviceManagementCmdCreate Method
	 * Request device mgmtCmd resource creation to the Resource manager.
	 * Device 관리를 위한 제어 객체를 생성하기 위해 Resource Manager로 요청하는 Method
	 */
	private static void requestDeviceManagementCmdCreate() {
		
		deviceMgmtCmd.CSEID = CSEBase.CSEID;
		deviceMgmtCmd.resourceName = "deviceManagement";
		deviceMgmtCmd.description = "Device Management resource";
		deviceMgmtCmd.cmdType = "90000001";
		deviceMgmtCmd.execEnable = "true";
		deviceMgmtCmd.execTarget = CSEBase.CSEID;
		
		ArrayList<Object> resourceSendArrayList = new ArrayList<Object>();
		resourceSendArrayList.add("requestMgmtCmdCreate");
		resourceSendArrayList.add(deviceMgmtCmd);
		
		if (debugPrint) {
			System.out.println("[DeviceManager] Device mgmtCmd Create start...");
		}
		
		try {
			if (debugPrint) {
				System.out.println("[DeviceManager] Send to Resource Manager - Device mgmtCmd Create");
				System.out.println();
			}
			
			resourceManagerQueue.put(resourceSendArrayList);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * requestFirmwareUpgradeCmdCreate Method
	 * Request firmware upgrade mgmtCmd resource creation to the Resource manager.
	 * Firmware Upgrade를 위한 제어 객체를 생성하기 위해 Resource Manager로 요청하는 Method
	 */
	private static void requestFirmwareUpgradeCmdCreate() {
		
		firmwareUpgradeMgmtCmd.CSEID = CSEBase.CSEID;
		firmwareUpgradeMgmtCmd.resourceName = "firmwareUpgrade";
		firmwareUpgradeMgmtCmd.description = "Firmware Upgrade resource";
		firmwareUpgradeMgmtCmd.cmdType = "90000002";
		firmwareUpgradeMgmtCmd.execEnable = "true";
		firmwareUpgradeMgmtCmd.execTarget = CSEBase.CSEID;
		
		ArrayList<Object> resourceSendArrayList = new ArrayList<Object>();
		resourceSendArrayList.add("requestMgmtCmdCreate");
		resourceSendArrayList.add(firmwareUpgradeMgmtCmd);
		
		if (debugPrint) {
			System.out.println("[DeviceManager] Firmware Upgrade Create start...");
		}
		
		try {
			if (debugPrint) {
				System.out.println("[DeviceManager] Send to Resource Manager - Firmware Upgrade Create");
				System.out.println();
			}
			
			resourceManagerQueue.put(resourceSendArrayList);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * requestAppInstallCmdCreate Method
	 * Request device application mgmtCmd resource creation to the Resource manager.
	 * Device Application을 위한 제어 객체를 생성하기 위해 Resource Manager로 요청하는 Method
	 */
	private static void requestAppInstallCmdCreate() {
		
		appInstallMgmtCmd.CSEID = CSEBase.CSEID;
		appInstallMgmtCmd.resourceName = "appInstall";
		appInstallMgmtCmd.cmdType = "90000003";
		appInstallMgmtCmd.execEnable = "true";
		appInstallMgmtCmd.execTarget = CSEBase.CSEID;
		
		ArrayList<Object> resourceSendArrayList = new ArrayList<Object>();
		resourceSendArrayList.add("requestMgmtCmdCreate");
		resourceSendArrayList.add(appInstallMgmtCmd);
		
		if (debugPrint) {
			System.out.println("[DeviceManager] Application Install cmd Create start...");
		}
		
		try {
			if (debugPrint) {
				System.out.println("[DeviceManager] Send to Resource Manager - Application Install cmd Create");
				System.out.println();
			}
			
			resourceManagerQueue.put(resourceSendArrayList);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * getInteropType Method
	 * @return interopType
	 * Get interoperability type (0 : Mobius only, 1 : oneM2M standard)
	 */
	public static boolean getInteropType() {
		return interopType;
	}
	
	/**
	 * getPrimitiveType Method
	 * @return primitiveType
	 * Get primitive parameter length type (0 : short name, 1 : long name)
	 */
	public static boolean getPrimitiveType() {
		return primitiveType;
	}
	
	/**
	 * getDebugPrint Method
	 * @return debugPrint
	 * Get debug message printing type (0: disable, 1 : enable)
	 */
	public static boolean getDebugPrint() {
		return debugPrint;
	}
	
	public static void main(String[] args) throws Exception {
		
		// Load CSE profile
		if (debugPrint) {
			System.out.println("[DeviceManager] &CUBE Software Platform loading...");
		}
		getTotalInformation();
		
		// Each manager thread initialize
		deviceManagementThread = new DeviceManagement(
				deviceManagerQueue,
				resourceManagerQueue,
				deviceMgmtCmd,
				firmwareUpgradeMgmtCmd,
				appInstallMgmtCmd);
		resourceManagerThread = new ResourceManager(
				deviceManagerQueue,
				resourceManagerQueue,
				resourceManagerResponseQueue,
				interactionManagerQueue,
				interactionManagerResponseQueue,
				thingManagerQueue,
				thingManagerResponseQueue,
				applicationManagerQueue,
				securityManagerQueue,
				httpServerQueue);
		interactionManagerThread = new InteractionManager(
				interactionManagerQueue,
				interactionManagerResponseQueue,
				resourceManagerQueue,
				resourceManagerResponseQueue,
				CSEBase.from,
				inCSEAddress,
				mqttBrokerAddress);
		thingManagerThread = new ThingManager(
				thingManagerQueue,
				thingManagerResponseQueue,
				resourceManagerQueue);
		securityManagerThread = new SecurityManager(
				securityManagerQueue,
				resourceManagerResponseQueue);
		applicationManagerThread = new ApplicationManager(
				applicationManagerQueue,
				resourceManagerQueue);
		httpServer = new HttpServerKeti(
				resourceManagerQueue,
				interactionManagerQueue,
				httpServerQueue, 80, 10);
		
		// Each manager thread start
		deviceManagementThread.start();
		resourceManagerThread.start();
		interactionManagerThread.start();
		thingManagerThread.start();
		securityManagerThread.start();
		applicationManagerThread.start();
		httpServer.start();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (debugPrint) {
			System.out.println("[DeviceManager] &CUBE Software Platform loading... OK");
		}

		
		if (interopType) {
			requestCSERegistration(CSEBase);
		} 
		else {
			// Request CSE registration to Resource manager
			requestCSERegistration(CSEBase);
			
			// Request Firmware create to Resource manager
			requestFirmwareCreate(firmwareInformation);
			
			// Request DeviceInfo create to Resource manager
			requestDeviceInfoCreate(deviceInformation);
			
			// Request mgmtCmds create to Resource manager
			requestDeviceManagementCmdCreate();
			requestFirmwareUpgradeCmdCreate();
			requestAppInstallCmdCreate();
		}
		
		// Waiting for each Manager thread & monitoring thread
		try {
			resourceManagerThread.join();
			interactionManagerThread.join();
			thingManagerThread.join();
			securityManagerThread.join();
			applicationManagerThread.join();
			deviceManagementThread.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}