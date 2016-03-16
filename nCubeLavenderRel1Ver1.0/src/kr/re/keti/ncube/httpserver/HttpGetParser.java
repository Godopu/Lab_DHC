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

import java.util.ArrayList;

/**
 * Class for parsing the HTTP get request.
 * (This class will be updated later version)
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 */
public class HttpGetParser {
	
	/**
	 * httpParse Method
	 * @param request
	 * @return requestArray
	 * @throws Exception
	 * HTTP get request parsing.
	 */
	public static ArrayList<Object> httpParse(String request) throws Exception {
		String requestString = request;
		String[] lineArray = requestString.split("\n");
		@SuppressWarnings("unused")
		int headerEndIndex = 0;
		@SuppressWarnings("unused")
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
		for (int i = 0; i < lineArray.length; i++) {
			headerString = headerString + lineArray[i];
		}
		
		System.out.println("headerString : \n" + headerString);
		
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
		else if (!httpHeader.ContentType.contains("application/vnd.onem2m-res+xml;ty=")) {
			requestArray.add(HttponeM2MRSC.CONTENTS_UNACCEPTABLE);
		}
		else {
			ArrayList<String> urlArray = urlParse(lineArray, httpHeader);
			
			switch(urlArray.get(0)) {
				case "requestCSEBaseRetrieve":				
					requestArray.add("requestCSEBaseRetrieve");
					break;
				case "Object Not Found":
					System.out.println("requestType : 404 Not Found");
					requestArray.add("not found");
					break;
			}
		}

		return requestArray;
	}
	
	/**
	 * urlParse Method
	 * @param request
	 * @return ArryList<String>
	 * Divide the resource url from HTTP request.
	 */
	@SuppressWarnings("null")
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

		if (httpMethod.equals("GET") && urlString.length == 1) {
			returnArrayList.add("requestCSEBaseRetrieve");
			
			System.out.println("requestType : requestCSEBaseRetrieve");
		}
		
		else if (httpMethod.equals("GET") && urlString.length == 2) {
			returnArrayList.add("containerCreate");
			returnArrayList.add(urlString[1]);
			
			System.out.println("requestType : containerCreate");
			System.out.println("AppId : " + urlString[1].substring(3));
			
			for (int i = 0; i < request.length - 1; i++) {
				String[] tempString = request[i].split(":");
				if (tempString[0].equals("X-M2M-NM")) {
					resourceName = tempString[1].trim();
					returnArrayList.add(resourceName);
					System.out.println("resourceName : " + resourceName);
					break;
				}
			}
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
			System.out.println("AppId : " + urlString[1].substring(3));
			System.out.println("containerName : " + urlString[2].substring(10));
		}
		
		else {
			returnArrayList.add("Object Not Found");
			System.out.println(resourceType);
			System.out.println("requestType : 404 Not Found");
		}
		
		return returnArrayList;
	}
}