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

package kr.re.keti.ncube.httpserver;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import kr.re.keti.ncube.AE;
import kr.re.keti.ncube.Container;
import kr.re.keti.ncube.ContentInstance;
import kr.re.keti.ncube.ExecInstance;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Class for parsing the HTTP post request.
 * (This class will be updated later version)
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 */
public class HttpPostParser {
	
	/**
	 * httpParse Method
	 * @param request
	 * @return requestArray
	 * @throws Exception
	 * HTTP post request parsing.
	 * HTTP request에 대한 파싱을 수행하기 위한 Method
	 */
	public static ArrayList<Object> httpParse(String request) throws Exception {
		String requestString = request;
		String[] lineArray = requestString.split("\n");
		int headerEndIndex = 0;
		int bodyStartIndex = 0;
		HttponeM2MHeader httpHeader = new HttponeM2MHeader();
		
		for (int i = 0; i < lineArray.length; i++) {
			if (lineArray[i].contains("Accept:")) {
				String[] accept = lineArray[i].split(": ");
				httpHeader.Accept = accept[1].substring(0, accept[1].length()-1);
			}
			else if (lineArray[i].contains("Content-Type:")) {
				String[] contentType = lineArray[i].split(": ");
				httpHeader.ContentType = contentType[1].substring(0, contentType[1].length()-1);
			}
			else if (lineArray[i].contains("X-M2M-RI:")) {
				String[] XM2MRI = lineArray[i].split(": ");
				httpHeader.XM2MRI = XM2MRI[1].substring(0, XM2MRI[1].length()-1);
			}
			else if (lineArray[i].contains("X-M2M-Origin:")) {
				String[] XM2MOrigin = lineArray[i].split(": ");
				httpHeader.XM2MOrigin = XM2MOrigin[1].substring(0, XM2MOrigin[1].length()-1);
			}
			else if (lineArray[i].contains("X-M2M-NM:")) {
				String[] XM2MNM = lineArray[i].split(": ");
				httpHeader.XM2MNM = XM2MNM[1].substring(0, XM2MNM[1].length()-1);
			}
			else if (lineArray[i].equals("\r")) {
				headerEndIndex = i;
				bodyStartIndex = i + 1;
				break;
			}
		}
		
		String headerString = "";
		for (int i = 0; i < headerEndIndex; i++) {
			headerString = headerString + lineArray[i];
		}
		
		System.out.println("headerString : \n" + headerString);
		
		String bodyString = "";
		for (int i = bodyStartIndex; i < lineArray.length; i++) {
			if (!lineArray[i].equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
				bodyString = bodyString + lineArray[i];
			}
			else if (!lineArray[i].equals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")) {
				bodyString = bodyString + lineArray[i];
			}
		}
		
		System.out.println("bodyString : \n" + bodyString);
		
		ArrayList<Object> requestArray = new ArrayList<Object>();
		
		if (httpHeader.XM2MOrigin.equals("")) {
			requestArray.add(HttponeM2MRSC.BAD_REQUEST);
		}
		else if (httpHeader.XM2MRI.equals("")) {
			requestArray.add(HttponeM2MRSC.BAD_REQUEST);
		}
		else if (httpHeader.Accept.equals("")) {
			requestArray.add(HttponeM2MRSC.BAD_REQUEST);
		}
		else if (httpHeader.ContentType.equals("")) {
			requestArray.add(HttponeM2MRSC.BAD_REQUEST);
		}
		else if (!httpHeader.Accept.equals("application/xml")) {
			requestArray.add(HttponeM2MRSC.CONTENTS_UNACCEPTABLE);
		}
		else if (!httpHeader.ContentType.contains("application/vnd.onem2m-res+xml;ty=")) {
			requestArray.add(HttponeM2MRSC.CONTENTS_UNACCEPTABLE);
		}
		else {
			ArrayList<String> urlArray = urlParse(lineArray, httpHeader);
			
			if (bodyString.contains("<")) {
				
				switch(urlArray.get(0)) {
					case "AECreate":
						AE requestAE = new AE();
						requestAE.AppID = aeCreateParse(bodyString);
						requestAE.name = urlArray.get(1);
						
						requestArray.add("requestAECreate");
						requestArray.add(requestAE);
						break;
					
					case "containerCreate":
						Container requestAEContainer = new Container();
						requestAEContainer = aeContainerCreateParse(bodyString);
						requestAEContainer.parentName = urlArray.get(1);
						requestAEContainer.resourceName = urlArray.get(2);
						
						requestArray.add("requestAEContainerCreate");
						requestArray.add(requestAEContainer);
						break;
						
					case "contentInstanceCreate":
						ContentInstance requestAEContentInstance = new ContentInstance();
						requestAEContentInstance = aeContentInstanceCreateParse(bodyString);
						requestAEContentInstance.appName = urlArray.get(1);
						requestAEContentInstance.containerName = urlArray.get(2);
						
						requestArray.add("requestAEContentInstanceCreate");
						requestArray.add(requestAEContentInstance);
						break;
						
					case "requestMgmtCmdFind":
						ExecInstance requestExecInstance = new ExecInstance();
						requestExecInstance = execInstanceParse(bodyString);

						requestArray.add("requestMgmtCmdFind");
						requestArray.add(requestExecInstance);
						requestArray.add(bodyString);
						break;
						
					case "Object Not Found":
						System.out.println("requestType : 404 Not Found");
						requestArray.add(HttponeM2MRSC.NOT_FOUND);
						break;
				}
			}
			else {
				requestArray.add(HttponeM2MRSC.CONTENTS_UNACCEPTABLE);
			}
		}

		return requestArray;
	}
	
	/**
	 * urlParse Method
	 * @param request
	 * @return ArryList<String>
	 * HTTP request 중 oneM2M 표준 리소스 url에 대한 분석을 수행하기 위한 Method
	 */
	private static ArrayList<String> urlParse(String[] request, HttponeM2MHeader httpHeader) {
		String resourceType = null;
		String resourceName = null;
		String urlStrings = null;
		String queryStrings = null;
		String[] urlString = null;
		int startIndex = 0;
		int endIndex = 0;
		ArrayList<String> returnArrayList = new ArrayList<String>();
		
		String[] requestString = request[0].split(" ");
		String httpMethod = requestString[0];
		String url = requestString[1];
		
		startIndex = url.indexOf("?") + 1;
		endIndex = url.length();

		if (startIndex == 0) {
			urlStrings = url.substring(1, url.length());
			urlString = urlStrings.split("/");			
		}
		else {
			urlStrings = url.substring(1, startIndex - 1);
			urlString = urlStrings.split("/");			
			queryStrings = url.substring(startIndex, endIndex);
		}
		
		System.out.println(urlStrings);
		System.out.println("urlString num : " + urlString.length);
		System.out.println(queryStrings);
		
		String[] contentTypeString = httpHeader.ContentType.split(";");
		String[] resourceTypeString = contentTypeString[1].split("=");
		resourceType = resourceTypeString[1];
		
		if (httpMethod.equals("POST") && urlString.length == 1 && resourceType.equals("2")) {
			returnArrayList.add("AECreate");
			
			System.out.println("requestType : AECreate");
			
			resourceName = httpHeader.XM2MNM;
			returnArrayList.add(resourceName);
			System.out.println("resourceName : " + resourceName);
		}
		
		else if (httpMethod.equals("POST") && urlString.length == 2 && resourceType.equals("3")) {
			returnArrayList.add("containerCreate");
			returnArrayList.add(urlString[1]);
			
			System.out.println("requestType : containerCreate");
			System.out.println("AppName : " + urlString[1]);
			
			resourceName = httpHeader.XM2MNM;
			returnArrayList.add(resourceName);
			System.out.println("resourceName : " + resourceName);
		}
		
		else if (httpMethod.equals("POST") && urlString.length == 2 && resourceType.equals("8")) {
			returnArrayList.add("requestMgmtCmdFind");
			returnArrayList.add(urlString[1]);
			returnArrayList.add(resourceName);
			
			System.out.println("requestType : requestMgmtCmdFind");
			System.out.println("resourceName : " + resourceName);
		}
		
		else if (httpMethod.equals("POST") && urlString.length == 3 && resourceType.endsWith("4")) {
			returnArrayList.add("contentInstanceCreate");
			returnArrayList.add(urlString[1]);
			returnArrayList.add(urlString[2]);
			
			System.out.println("requestType : contentInstanceCreate");
			System.out.println("AppName : " + urlString[1]);
			System.out.println("containerName : " + urlString[2]);
		}
		
		else {
			returnArrayList.add("Object Not Found");
			System.out.println(resourceType);
			System.out.println("requestType : 404 Not Found");
		}
		
		return returnArrayList;
	}
	
	
	/**
	 * aeCreateParse Method
	 * @param request
	 * @return response
	 * @throws Exception
	 * oneM2M 표준 AE Create 요청을 파싱하기 위한 Method
	 */
	private static String aeCreateParse(String request) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(request));
		Document document = builder.parse(xmlSource);
		
		String response = null;
		
		NodeList appIDNodeList = document.getElementsByTagName("App-ID");
		
		if (appIDNodeList.getLength() > 0 && appIDNodeList.item(0).getChildNodes().getLength() > 0) {
			Node appIDNode = appIDNodeList.item(0).getChildNodes().item(0);
			response = appIDNode.getNodeValue();
		}
		
		return response;
	}
	
	/**
	 * aeContainerCreateParse
	 * @param request
	 * @return response
	 * @throws Exception
	 * oneM2M 표준 AE의 Container Create 요청을 파싱하기 위한 Method
	 */
	private static Container aeContainerCreateParse(String request) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(request));
		@SuppressWarnings("unused")
		Document document = builder.parse(xmlSource);
		
		Container response = new Container();
		
		return response;
	}

	/**
	 * aeContentInstanceCreateParse Method
	 * @param request
	 * @return response
	 * @throws Exception
	 * oneM2M 표준 AE의 Container 하부의 contentInstance Create 요청을 파싱하기 위한 Method
	 */
	private static ContentInstance aeContentInstanceCreateParse(String request) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(request));
		Document document = builder.parse(xmlSource);
		
		ContentInstance response = new ContentInstance();
				
		
		NodeList contentInfoNodeList = document.getElementsByTagName("contentInfo");
		if (contentInfoNodeList.getLength() > 0 && contentInfoNodeList.item(0).getChildNodes().getLength() > 0) {
			Node contentInfoNode = contentInfoNodeList.item(0).getChildNodes().item(0);
			response.contentInfo = contentInfoNode.getNodeValue();
		}
				
		NodeList contentNodeList = document.getElementsByTagName("content");
		if (contentNodeList.getLength() > 0 && contentNodeList.item(0).getChildNodes().getLength() > 0) {
			Node contentNode = contentNodeList.item(0).getChildNodes().item(0);
			response.content = contentNode.getNodeValue();
		}

		return response;
	}
	
	/**
	 * execInstanceParse Method
	 * @param responseString
	 * @return mgmtCmdRegistProfile
	 * @throws Exception
	 * Mobius Mashup에 MgmtCmd 생성 후 받은 리턴메시지를 파싱하기 위한 Method 
	 */
	private static ExecInstance execInstanceParse(String responseString) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(responseString));
		Document document = builder.parse(xmlSource);
		
		ExecInstance execInstanceProfile = new ExecInstance();
		
		NodeList resourceTypeNodeList = document.getElementsByTagName("resourceType");
		Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
		execInstanceProfile.resourceType = resourceTypeNode.getNodeValue();
		
		NodeList resourceIDNodeList = document.getElementsByTagName("resourceID");
		Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
		execInstanceProfile.resourceID = resourceIDNode.getNodeValue();
		
		NodeList resourceNameNodeList = document.getElementsByTagName("resourceName");
		Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
		execInstanceProfile.resourceName = resourceNameNode.getNodeValue();
		
		NodeList parentIDNodeList = document.getElementsByTagName("parentID");
		Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
		execInstanceProfile.parentID = parentIDNode.getNodeValue();
		
		NodeList creationTimeNodeList = document.getElementsByTagName("creationTime");
		Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
		execInstanceProfile.creationTime = creationTimeNode.getNodeValue();
		
		NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lastModifiedTime");
		Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
		execInstanceProfile.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
		
//		NodeList labelsNodeList = document.getElementsByTagName("labels");
//		Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
//		mgmtCmdRegistProfile.labels = labelsNode.getNodeValue();
		
		NodeList execStatusNodeList = document.getElementsByTagName("execStatus");
		Node execStatusNode = execStatusNodeList.item(0).getChildNodes().item(0);
		execInstanceProfile.execStatus = execStatusNode.getNodeValue();
		
		NodeList execTargetNodeList = document.getElementsByTagName("execTarget");
		Node execTargetNode = execTargetNodeList.item(0).getChildNodes().item(0);
		execInstanceProfile.execTarget = execTargetNode.getNodeValue();
		
		NodeList execReqArgsNodeList = document.getElementsByTagName("execReqArgs");
		Node execReqArgsNode = execReqArgsNodeList.item(0).getChildNodes().item(0);
		execInstanceProfile.execReqArgs = execReqArgsNode.getNodeValue();
		
		return execInstanceProfile;
	}
}