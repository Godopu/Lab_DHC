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

package kr.re.keti.ncube.applicationmanager;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import kr.re.keti.ncube.ExecInstance;
import kr.re.keti.ncube.Software;
import kr.re.keti.ncube.devicemanager.DeviceManager;

/**
 * Class for device application management.
 * (This class will be supported later version)
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 */
public class ApplicationManager extends Thread {
	
	private BlockingQueue<ArrayList<Object>> applicationManagerQueue;
	private BlockingQueue<ArrayList<Object>> resourceManagerQueue;
	
	private ArrayList<Object> applicationManagerArrayList;
	private ArrayList<Object> softwareArrayList;
	
	private static final boolean debugPrint = DeviceManager.getDebugPrint();
	
	public ApplicationManager(
			BlockingQueue<ArrayList<Object>> myQueue,
			BlockingQueue<ArrayList<Object>> resourceQueue) {
		
		this.applicationManagerQueue = myQueue;
		this.resourceManagerQueue = resourceQueue;
		this.softwareArrayList = new ArrayList<Object>();
	}
	
	public void run() {

		if (debugPrint) {
			System.out.println("[ApplicationManager] Start");
			System.out.println("[ApplicationManager] BlockingQueue wait");
			System.out.println();
		}
		
		while(true) {
			try {
				applicationManagerArrayList = applicationManagerQueue.take();
				eventProcess(applicationManagerArrayList);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
		}
	}
	
	/**
	 * eventProcess method
	 * @param receivedArrayList
	 * Method for parsing and processing the event message.
	 */
	private void eventProcess(ArrayList<Object> receivedArrayList) {
		String msgHeader = (String) receivedArrayList.get(0);
		String labels;
		ExecInstance receivedControlData;
		
		switch(msgHeader) {
		
		case "requestMgmtCmdControl":
			if (debugPrint) {
				System.out.println("[ApplicationManager] Receive from Resource Manager - MgmtCmd Control request");
			}
			labels = (String) receivedArrayList.get(1);
			receivedControlData = (ExecInstance) receivedArrayList.get(2);
			
			if (labels.equals("appInstall")) {
				ArrayList<Object> resourceSendArrayList = new ArrayList<Object>();
				resourceSendArrayList.add("requestAppDownload");
				resourceSendArrayList.add(receivedControlData.execReqArgs);
				
				try {
					resourceManagerQueue.put(resourceSendArrayList);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			
		case "requestApplicationSet":
			
			if (debugPrint) {
				System.out.println("[ApplicationManager] Receive from Resource Manager - Application Set request");
			}
			
			Software receiveInfo = (Software) receivedArrayList.get(1);
			softwareArrayList.add(receiveInfo);
			
			try {
				runApplication(receiveInfo.fileName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
			
		default:
			System.out.println("[ApplicationManager] Message not found");
			break;
		}
	}
	
	/**
	 * runApplication method
	 * @param fileName
	 * @throws Exception
	 * Method for running the device application.
	 * Device Application (AE)를 실행하기 위한 Method
	 */
	public static void runApplication(String fileName) throws Exception {

		if (debugPrint) {
			System.out.println("[ApplicationManager] Run Application - " + fileName);
		}
		
		// Linux only
		String command = "java -jar /nCube/";
        Runtime runtime = Runtime.getRuntime();
        @SuppressWarnings("unused")
		Process process = runtime.exec(command + fileName);
	}
}