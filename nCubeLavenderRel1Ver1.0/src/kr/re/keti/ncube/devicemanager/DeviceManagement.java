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

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import kr.re.keti.ncube.ExecInstance;
import kr.re.keti.ncube.MgmtCmd;

/**
 * Class for device managerment (e.g., device reset, etc). 
 * (This class will be supported later version)
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 */
public class DeviceManagement extends Thread {
	
	private BlockingQueue<ArrayList<Object>> deviceManagerQueue;
	private BlockingQueue<ArrayList<Object>> resourceManagerQueue;
	
	private ArrayList<Object> deviceManagementArrayList;
	
	@SuppressWarnings("unused")
	private MgmtCmd deviceMgmtCmd;
	@SuppressWarnings("unused")
	private MgmtCmd firmwareUpgradeMgmtCmd;
	@SuppressWarnings("unused")
	private MgmtCmd appInstallMgmtCmd;
	
	private static final boolean debugPrint = true;
	
	public DeviceManagement(
			BlockingQueue<ArrayList<Object>> myQueue,
			BlockingQueue<ArrayList<Object>> resourceQueue,
			MgmtCmd dMgmtCmd,
			MgmtCmd fMgmtCmd,
			MgmtCmd appMgmtCmd) {
		
		this.deviceManagerQueue = myQueue;
		this.resourceManagerQueue = resourceQueue;
		this.deviceMgmtCmd = dMgmtCmd;
		this.firmwareUpgradeMgmtCmd = fMgmtCmd;
		this.appInstallMgmtCmd = appMgmtCmd;
	}
	
	public void run() {
		if (debugPrint) {
			System.out.println("[DeviceManager] Start");
			System.out.println("[DeviceManager] BlockingQueue wait");
			System.out.println();
		}
		
		while(true) {
			try {
				deviceManagementArrayList = deviceManagerQueue.take();
				eventProcess(deviceManagementArrayList);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
		}
	}
	
	/**
	 * eventProcess Method
	 * @param receivedArrayList
	 * Method for parsing and processing the event message.
	 */
	private void eventProcess(ArrayList<Object> receivedArrayList) {
		String msgHeader = (String) receivedArrayList.get(0);
		String labels;
		MgmtCmd receivedControlData;
		ExecInstance receivedControlInstance;
		
		switch(msgHeader) {
		
		case "registMgmtCmd":
			if (debugPrint) {
				System.out.println("[DeviceManager] Receive from Resource Manager - MgmtCmd regist request");
			}
			labels = (String) receivedArrayList.get(1);
			receivedControlData = (MgmtCmd) receivedArrayList.get(2);
			
			System.out.println("[DeviceManager] Received labels : " + labels);
			System.out.println("[DeviceManager] Received body : " + receivedControlData.resourceID);

			break;
		
		case "requestMgmtCmdControl":
			if (debugPrint) {
				System.out.println("[DeviceManager] Receive from Resource Manager - MgmtCmd Control request");
			}
			labels = (String) receivedArrayList.get(1);
			receivedControlInstance = (ExecInstance) receivedArrayList.get(2);
			
			System.out.println("[DeviceManager] Received labels : " + labels);
			System.out.println("[DeviceManager] Received body : " + receivedControlInstance.resourceID + "," + receivedControlInstance.execReqArgs);
			
			if (labels.equals("firmwareUpgrade")) {
				ArrayList<Object> resourceSendArrayList = new ArrayList<Object>();
				resourceSendArrayList.add("requestFirmwareUpgrade");
				resourceSendArrayList.add(receivedControlInstance.execReqArgs);
				
				try {
					resourceManagerQueue.put(resourceSendArrayList);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			
		case "requestFirmwareSet":
			if (debugPrint) {
				System.out.println("[DeviceManager] Receive Set firmware information...");
			}
			
			ArrayList<Object> resourceSendArrayList = new ArrayList<Object>();
			resourceSendArrayList.add("requestFirmwareUpdate");

			try {
				if (debugPrint) {
					System.out.println("[DeviceManager] Send to Resource Man ager - Firmware Update request");
					System.out.println();
				}
				
				resourceManagerQueue.put(resourceSendArrayList);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		default:
			if (debugPrint) {
				System.out.println("[DeviceManager] Message not found");
			}
			break;
		}
	}
}