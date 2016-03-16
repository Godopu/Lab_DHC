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

package kr.re.keti.ncube.interactionmanager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import kr.re.keti.ncube.AE;
import kr.re.keti.ncube.CSEBase;
import kr.re.keti.ncube.Container;
import kr.re.keti.ncube.ContentInstance;
import kr.re.keti.ncube.DeviceInfo;
import kr.re.keti.ncube.ExecInstance;
import kr.re.keti.ncube.Firmware;
import kr.re.keti.ncube.MgmtCmd;
import kr.re.keti.ncube.Software;

/**
 * Mobius와 Interaction을 하기 위한 Method를 모아놓은 Class로서 각 Method는 HTTP Client로 구현됨
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 */
public class InteractionHTTPRequest {
	
	private static int requestIndex = 0;
	private String INCSEAddress = null;
	
	private boolean interopType = false;
	private boolean debugPrint = false;
	private boolean primitiveType = false;
	
	public InteractionHTTPRequest (String inCSEIP, boolean interopType, boolean primitiveType, boolean debugPrint) {
		this.INCSEAddress = inCSEIP;
		this.interopType = interopType;
		this.primitiveType = primitiveType;
		this.debugPrint = debugPrint;
	}
	
	/**
	 * CSERegistrationMessage Method
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * Mobius로 CSE 등록을 하기 위한 Method로서 HTTP POST를 사용함
	 */
	public String[] CSERegistrationMessage(CSEBase CSEProfile) throws Exception {
		String requestBody = ""; 

		if (primitiveType) {
			requestBody = 			
					"<m2m:remoteCSE\n" +
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
							"<cseType>3</cseType>\n" +
							"<CSE-ID>" + CSEProfile.CSEID + "</CSE-ID>\n" + 
							"<CSEBase>nCube</CSEBase>" +
							"<pointOfAccess>" + CSEProfile.pointOfAccess + "</pointOfAccess>\n" + 
							"<requestReachability>" + CSEProfile.requestReachability + "</requestReachability>\n" + 
					"</m2m:remoteCSE>";
		}
		else {
			requestBody = 			
					"<m2m:csr\n" +
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
							"<cst>3</cst>\n" +
							"<csi>" + CSEProfile.CSEID + "</csi>\n" + 
							"<poa>" + CSEProfile.pointOfAccess + "</poa>\n" + 
							"<rr>" + CSEProfile.requestReachability + "</rr>\n" + 
					"</m2m:csr>";
		}
		
		StringEntity entity = new StringEntity(
				new String(requestBody.getBytes()));

		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius")
				.build();
		
		HttpPost post = new HttpPost(uri);
				post.setHeader("Accept", "application/xml");
				post.setHeader("Content-Type", "application/vnd.onem2m-res+xml;ty=16");
				post.setHeader("locale", "ko");
				post.setHeader("passCode", CSEProfile.passcode);
				post.setHeader("X-M2M-Origin", CSEProfile.from);
				post.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				post.setHeader("X-M2M-NM", CSEProfile.resourceName);
				post.setEntity(entity);
				requestIndex++;
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(post);
		
		HttpEntity responseEntity = response.getEntity();
		
		int responseCode = response.getStatusLine().getStatusCode();

		String[] responseString = new String[2];
		
		Header[] responseHeader = response.getHeaders("dKey");
		
		if (responseHeader.length > 0) {
			responseString[0] = responseHeader[0].getValue();
		}
		else {
			responseString[0] = Integer.toString(responseCode);
		}
		
		responseString[1] = EntityUtils.toString(responseEntity);
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString[1]);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**
	 * CSERegistrationHttpsMessage Method
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * Mobius로 CSE 등록을 하기 위한 Method로서 HTTPS POST를 사용함
	 */
	public String CSERegistrationHttpsMessage(CSEBase CSEProfile) throws Exception {

		URL url = new URL(INCSEAddress + "/Mobius");
		
		HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("ty", "remoteCSE");
		conn.setRequestProperty("deviceID", CSEProfile.CSEID);
		conn.setRequestProperty("Accept", "application/xml");
		conn.setRequestProperty("Content-Type", "application/xml");
		conn.setRequestProperty("locale", "ko");
		conn.setRequestProperty("passcode", CSEProfile.passcode);
		
		String requestBody = 
				"<CSEBase>\n" + 
						"<resourceName>" + CSEProfile.resourceName + "</resourceName>\n" + 
						"<pointOfAccess>" + CSEProfile.pointOfAccess + "</pointOfAccess>\n" + 
				"</CSEBase>";
		
		OutputStream os = conn.getOutputStream();
		os.write(requestBody.getBytes());
		os.flush();
		os.close();
		
		BufferedReader br = new BufferedReader
							(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		
		String responseString = null;
		String receivedData = null;
		
		while((receivedData = br.readLine()) != null) {
			responseString = responseString + receivedData;
		}
		
		System.out.println(responseString);
		
		return responseString;
	}
	
	/**
	 * firmwareCreateMessage Method
	 * @param firmwareInfo
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * Mobius로 Firmware Create를 하기 위한 Method로서 HTTP POST를 사용함
	 */
	public String firmwareCreateMessage(Firmware firmwareInfo, CSEBase CSEProfile) throws Exception {
		
		String requestBody = "";
		
		if (primitiveType) {
			requestBody = 
					"<m2m:firmware\n" + 
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
							"<version>" + firmwareInfo.version + "</version>\n" +
							"<name>" + firmwareInfo.name + "</name>" +
							"<URL>" + firmwareInfo.url + "</URL>\n" +
							"<update>false</update>\n" +
							"<updateStatus>" + 
								"<action></action>" + 
								"<status>" + firmwareInfo.status + "</status>" +
							"</updateStatus>\n" +
					"</m2m:firmware>";
		}
		else {
			requestBody = 
					"<m2m:fwr\n" + 
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
							"<vr>" + firmwareInfo.version + "</vr>\n" +
							"<nam>" + firmwareInfo.name + "</nam>" +
							"<url>" + firmwareInfo.url + "</url>\n" +
							"<ud>false</ud>\n" +
					"</m2m:fwr>";
		}

		StringEntity entity = new StringEntity(
				new String(requestBody.getBytes()));

		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/node-" + CSEProfile.CSEID)
				.build();
		
		HttpPost post = new HttpPost(uri);
				post.setHeader("Accept", "application/xml");
				post.setHeader("Content-Type", "application/vnd.onem2m-res+xml;ty=13;mgd=1001");
				post.setHeader("locale", "en");
				post.setHeader("X-M2M-Origin", CSEProfile.from);
				post.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				post.setHeader("X-M2M-NM", firmwareInfo.name);
				post.setHeader("dKey", CSEProfile.dKey);
				post.setEntity(entity);
				requestIndex++;
				
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(post);
		
		HttpEntity responseEntity = response.getEntity();
		
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**
	 * firmwareUpdateMessage Method
	 * @param firmwareInfo
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * Mobius로 Firmware Update 결과를 전송하기 위한 Method로서 HTTP PUT을 사용함
	 */
	public String firmwareUpdateMessage(Firmware firmwareInfo, CSEBase CSEProfile) throws Exception {
String requestBody = "";
		
		if (primitiveType) {
			requestBody = 
					"<m2m:firmware\n" + 
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
							"<version>" + firmwareInfo.version + "</version>\n" +
							"<name>" + firmwareInfo.name + "</name>" +
							"<URL>" + firmwareInfo.url + "</URL>\n" +
							"<update>false</update>\n" +
							"<updateStatus>" + firmwareInfo.status + "</updateStatus>\n" +
					"</m2m:firmware>";
		}
		else {
			requestBody = 
					"<m2m:fwr\n" + 
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
							"<vr>" + firmwareInfo.version + "</vr>\n" +
							"<nam>" + firmwareInfo.name + "</nam>" +
							"<url>" + firmwareInfo.url + "</url>\n" +
							"<ud>false</ud>\n" +
					"</m2m:fwr>";
		}

		StringEntity entity = new StringEntity(
				new String(requestBody.getBytes()));

		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/node-" + CSEProfile.nodeLink + "/firmware-" + firmwareInfo.resourceID)
				.build();
		
		HttpPut put = new HttpPut(uri);
				put.setHeader("Accept", "application/xml");
				put.setHeader("Content-Type", "application/vnd.onem2m-res+xml");
				put.setHeader("locale", "ko");
				put.setHeader("X-M2M-Origin", CSEProfile.from);
				put.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				put.setHeader("dKey", CSEProfile.dKey);
				put.setEntity(entity);
				requestIndex++;
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(put);
		
		HttpEntity responseEntity = response.getEntity();
		
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();

		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**
	 * softwareCreateMessage Method
	 * @param softwareInfo
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * Mobius로 software Create를 하기 위한 Method로서 HTTP POST를 사용함
	 */
	public String softwareCreateMessage(Software softwareInfo, CSEBase CSEProfile) throws Exception {
		String requestBody = "";
		
		if (primitiveType) {
			requestBody = 
					"<m2m:software\n" + 
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
							"<description>" + softwareInfo.description + "</description>\n" + 
							"<version>" + softwareInfo.version + "</version>\n" +
							"<install>" + softwareInfo.install + "</install>\n" +
							"<uninstall>" + softwareInfo.uninstall + "</uninstall>\n" +
							"<installStatus>" + softwareInfo.installStatus + "</installStatus>\n" +
					"</m2m:software>";
		}
		else {
			requestBody = 
					"<m2m:swr\n" + 
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
							"<vr>" + softwareInfo.version + "</vr>\n" +
							"<nam>" + softwareInfo.name + "</nam>\n" +
							"<in>" + softwareInfo.install + "</in>\n" +
							"<un>" + softwareInfo.uninstall + "</in>\n" +
					"</m2m:swr>";
		}

		StringEntity entity = new StringEntity(
				new String(requestBody.getBytes()));

		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/node-" + CSEProfile.nodeLink)
				.build();
		
		HttpPost post = new HttpPost(uri);
				post.setHeader("Accept", "application/xml");
				post.setHeader("Content-Type", "application/vnd.onem2m-res+xml;ty=13;mgd=1002");
				post.setHeader("locale", "ko");
				post.setHeader("X-M2M-Origin", CSEProfile.from);
				post.setHeader("X-M2M-NM", softwareInfo.name);
				post.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				post.setHeader("dKey", CSEProfile.dKey);
				post.setEntity(entity);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(post);
		
		HttpEntity responseEntity = response.getEntity();
		
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**
	 * deviceInfoCreateMessage Method
	 * @param deviceInfo
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * Mobius로 DeviceInfo Create를 하기 위한 Method로서 HTTP POST를 사용함
	 */
	public String deviceInfoCreateMessage(DeviceInfo deviceInfo, CSEBase CSEProfile) throws Exception {
		
		String requestBody = "";
		
		if (primitiveType) {
			requestBody = 
					"<m2m:deviceInfo\n" + 
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
							"<deviceLabel>" + deviceInfo.deviceLabel + "</deviceLabel>\n" + 
							"<manufacturer>" + deviceInfo.manufacturer + "</manufacturer>\n" +
							"<model>" + deviceInfo.model + "</model>\n" +
							"<deviceType>" + deviceInfo.deviceType + "</deviceType>\n" +
							"<fwVersion>" + deviceInfo.fwVersion + "</fwVersion>\n" +
							"<swVersion>" + deviceInfo.swVersion + "</swVersion>\n" +
							"<hwVersion>" + deviceInfo.hwVersion + "</hwVersion>\n" +
					"</m2m:deviceInfo>";
		}
		else {
			requestBody = 
					"<m2m:dvi\n" + 
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
							"<dc>" + deviceInfo.deviceLabel + "</dc>\n" + 
					"</m2m:dvi>";
		}
		
		StringEntity entity = new StringEntity(
				new String(requestBody.getBytes()));

		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/node-" + CSEProfile.CSEID)
				.build();
		
		HttpPost post = new HttpPost(uri);
				post.setHeader("Accept", "application/xml");
				post.setHeader("Content-Type", "application/vnd.onem2m-res+xml;ty=13;mgd=1007");
				post.setHeader("locale", "ko");
				post.setHeader("X-M2M-Origin", CSEProfile.from);
				post.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				post.setHeader("X-M2M-NM", deviceInfo.deviceLabel);
				post.setHeader("dKey", CSEProfile.dKey);
				post.setEntity(entity);
				
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(post);
		
		HttpEntity responseEntity = response.getEntity();
		
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**
	 * locationPolicyCreateMessage Method
	 * @param deviceInfo
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * (Next version 적용 예정) oneM2M locationPolicy resource create를 지원하기 위한 Method 
	 */
	public String locationPolicyCreateMessage(DeviceInfo deviceInfo, CSEBase CSEProfile) throws Exception {
		String requestBody = 
				"<deviceInfo>\n" + 
						"<description>" + deviceInfo.description + "</description>\n" + 
						"<manufacturer>" + deviceInfo.manufacturer + "</manufacturer>\n" +
						"<model>" + deviceInfo.model + "</model>\n" +
						"<deviceType>" + deviceInfo.deviceType + "</deviceType>\n" +
						"<fwVersion>" + deviceInfo.fwVersion + "</fwVersion>\n" +
						"<hwVersion>" + deviceInfo.hwVersion + "</hwVersion>\n" +
				"</deviceInfo>";

		StringEntity entity = new StringEntity(
				new String(requestBody.getBytes()));

		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/locationPolicy-" + CSEProfile.nodeLink)
				.setParameter("ty", "deviceInfo")
				.setParameter("nm", deviceInfo.resourceName)
				.build();
		
		HttpPost post = new HttpPost(uri);
				post.setHeader("Accept", "application/onem2m-resource+xml");
				post.setHeader("Content-Type", "application/onem2m-resource+xml");
				post.setHeader("locale", "ko");
				post.setHeader("From", CSEProfile.from);
				post.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				post.setHeader("dKey", CSEProfile.dKey);
				post.setEntity(entity);
		
		System.out.println(uri);
		System.out.println(requestBody);
				
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(post);
		
		HttpEntity responseEntity = response.getEntity();
		
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**
	 * containerCreateMessage Method
	 * @param registrationProfile
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * Mobius Mashup으로 Container 생성 요청을 하기 위한 Method로서 HTTP POST를 사용함
	 */
	public String containerCreateMessage(Container registrationProfile, CSEBase CSEProfile) throws Exception {
	
		String requestBody = "";
		
		if (primitiveType) {
			requestBody = 			
				"<m2m:container\n" +
						"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
				"</m2m:container>";
		}
		else {
			requestBody = 			
				"<m2m:cnt\n" +
						"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
				"</m2m:cnt>";
		}
		
		StringEntity entity = new StringEntity(
							new String(requestBody.getBytes()));
		
		URI uri = null;
		
		if (interopType) {
			uri = new URIBuilder()
					.setScheme("http")
					.setHost(INCSEAddress)
					.setPath("/Mobius/" + CSEProfile.resourceName)
					.build();
		}
		else {
			uri = new URIBuilder()
					.setScheme("http")
					.setHost(INCSEAddress)
					.setPath("/Mobius/remoteCSE-" + CSEProfile.CSEID)
					.build();
		}
		
		HttpPost post = new HttpPost(uri);
				post.setHeader("Accept", "application/xml");
				post.setHeader("Content-Type", "application/vnd.onem2m-res+xml;ty=3");
				post.setHeader("locale", "ko");
				post.setHeader("X-M2M-Origin", CSEProfile.from);
				post.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				post.setHeader("X-M2M-NM", registrationProfile.resourceName);
				post.setHeader("dKey", CSEProfile.dKey);
				post.setEntity(entity);
				
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(post);
	
		HttpEntity responseEntity = response.getEntity();
			
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**
	 * contentInstanceCreate Method
	 * @param uploadThingData
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * Mobius Mashup으로 Thing 데이터 업로드 요청을 하기 위한 Method로서 HTTP POST를 사용함
	 */
	public String contentInstanceCreate(ContentInstance uploadThingData, CSEBase CSEProfile) throws Exception {
		
		String requestBody = "";
		
		if (primitiveType) {
			requestBody = 			 
				"<m2m:contentInstance\n" +
						"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
						"<contentInfo>" + uploadThingData.contentInfo + "</contentInfo>\n" + 
						//"<content>" + Base64.encode(uploadThingData.content.getBytes()) + "</content>\n" +
						"<content>" + uploadThingData.content + "</content>\n" +
				"</m2m:contentInstance>";
		}
		else {
			requestBody =
				"<m2m:cin\n" +
						"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
						"<cnf>" + uploadThingData.contentInfo + "</cnf>\n" + 
						//"<con>" + Base64.encode(uploadThingData.content.getBytes()) + "</con>\n" +
						"<con>" + uploadThingData.content + "</con>\n" +
				"</m2m:cin>";
		}
		
		StringEntity entity = new StringEntity(
							new String(requestBody.getBytes()));
		
		URI uri = null;
		
		if (interopType) {
			uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/" + CSEProfile.resourceName + "/" + uploadThingData.containerName)
				.build();
		}
		else {
			uri = new URIBuilder()
			.setScheme("http")
			.setHost(INCSEAddress)
			.setPath("/Mobius/remoteCSE-" + CSEProfile.CSEID + "/container-" + uploadThingData.containerName)
			.build();
		}
		
		HttpPost post = new HttpPost(uri);
				post.setHeader("Accept", "application/xml");
				post.setHeader("Content-Type", "application/vnd.onem2m-res+xml;ty=4");
				post.setHeader("locale", "ko");
				post.setHeader("X-M2M-Origin", CSEProfile.from);
				post.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				//post.setHeader("X-M2M-NM", uploadThingData.containerName);
				post.setHeader("dKey", CSEProfile.dKey);
				post.setEntity(entity);
				
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(post);
	
		HttpEntity responseEntity = response.getEntity();
			
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**
	 * mgmtCmdRegistrationMessage Method
	 * @param controlProfile
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * Mobius Mashup으로 mgmtCmd 등록 요청을 하기 위한 Method로서 HTTP POST를 사용함
	 */
	public String mgmtCmdRegistrationMessage(MgmtCmd controlProfile, CSEBase CSEProfile) throws Exception {
		
		String requestBody = "";
		
		if (primitiveType) {
			requestBody = 
					"<m2m:mgmtCmd\n" +
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
							"<cmdType>" + controlProfile.cmdType + "</cmdType>\n" +
							"<execEnable>" + "true" + "</execEnable>\n" +
							"<execTarget>" + CSEProfile.CSEID + "</execTarget>\n" +
					"</m2m:mgmtCmd>";
		}
		else {
			requestBody = 
					"<m2m:mgc\n" +
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
							"<cmt>" + controlProfile.cmdType + "</cmt>\n" +
							"<exe>true</exe>\n" +
							"<ext>" + CSEProfile.CSEID + "</ext>\n" +
					"</m2m:mgc>";
		}
		
		StringEntity entity = new StringEntity(
							new String(requestBody.getBytes()));
		
		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/remoteCSE-" + CSEProfile.CSEID)
				.build();
		
		HttpPost post = new HttpPost(uri);
				post.setHeader("Accept", "application/xml");
				post.setHeader("Content-Type", "application/vnd.onem2m-res+xml;ty=12");
				post.setHeader("locale", "en");
				post.setHeader("X-M2M-Origin", CSEProfile.from);
				post.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				post.setHeader("X-M2M-NM", controlProfile.resourceName);
				post.setHeader("dKey", CSEProfile.dKey);
				post.setEntity(entity);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(post);
	
		HttpEntity responseEntity = response.getEntity();
			
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**e
	 * mgmtCmdUpdateMessage Method
	 * @param controlProfile
	 * @param deviceKey
	 * @return responseString
	 * @throws Exception
	 */
	public String mgmtCmdUpdateMessage(ExecInstance controlProfile, String deviceKey, String requestFrom) throws Exception {
		
		String requestBody = "";
		
		if (primitiveType) {
			requestBody = 
					"<m2m:execInstance\n" +
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
					"</m2m:execInstance>";
		}
		else {
			requestBody = 
					"<m2m:exin\n" +
							"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
							"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
					"</m2m:exin>";
		}
			
		
		StringEntity entity = new StringEntity(
							new String(requestBody.getBytes()));
		
		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/remoteCSE-" + controlProfile.execTarget + "/mgmtCmd-" +
										controlProfile.mgmtCmdName + "/execInstance-" + controlProfile.resourceID)
				.build();
		
		HttpPut put = new HttpPut(uri);
				put.setHeader("Accept", "application/xml");
				put.setHeader("Content-Type", "application/vnd.onem2m-res+xml");
				put.setHeader("locale", "ko");
				put.setHeader("X-M2M-Origin", requestFrom);
				put.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				put.setHeader("dKey", deviceKey);
				put.setEntity(entity);
		
		System.out.println("requestURI : " + uri);
		System.out.println("deviceKey : " + deviceKey);
		System.out.println("requestBody : " + requestBody);
				
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(put);
	
		HttpEntity responseEntity = response.getEntity();
			
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**
	 * aeCreateMessage Method
	 * @param aeProfile
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * Mobius로 oneM2M AE resource Create 요청을 위한  Method로서 HTTP POST를 사용함
	 */
	public String aeCreateMessage(AE aeProfile, CSEBase CSEProfile) throws Exception {
		
		String requestBody = "";
		
		if (primitiveType) {
			requestBody = 			 
				"<m2m:AE\n" +
						"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
						"<App-ID>" + aeProfile.AppID + "</App-ID>\n" +
						"<AE-ID>" + aeProfile.AppID + "</AE-ID>\n" +
						"<pointOfAccess>" + "MQTT|" + aeProfile.AppID + "</pointOfAccess>\n" +
						"<nodeLink>" + CSEProfile.nodeLink + "</nodeLink>\n" +
				"</m2m:AE>";
		}
		else {
			requestBody = 			 
				"<m2m:ae\n" +
						"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
						"<api>" + aeProfile.AppID + "</api>\n" +
						"<aei>" + aeProfile.AppID + "</aei>\n" +
						"<poa>" + "MQTT|" + aeProfile.AppID + "</poa>\n" +
						"<nl>" + CSEProfile.nodeLink + "</nl>\n" +
				"</m2m:ae>";
		}

		StringEntity entity = new StringEntity(
						new String(requestBody.getBytes()));
		
		URI uri = null;
		
		if (interopType) {
			uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/" + CSEProfile.resourceName)
				.build();
		}
		else {
			uri = new URIBuilder()
			.setScheme("http")
			.setHost(INCSEAddress)
			.setPath("/Mobius/remoteCSE-" + CSEProfile.CSEID)
			.build();
		}
		
		HttpPost post = new HttpPost(uri);
			post.setHeader("Content-Type", "application/vnd.onem2m-res+xml;ty=2");
			post.setHeader("Accept", "application/xml");
			post.setHeader("locale", "ko");
			post.setHeader("X-M2M-Origin", CSEProfile.from);
			post.setHeader("X-M2M-RI", Integer.toString(requestIndex));
			post.setHeader("X-M2M-NM", aeProfile.name);
			post.setHeader("dKey", CSEProfile.dKey);
			post.setEntity(entity);
		
		System.out.println("requestURI : " + uri);
		System.out.println("deviceKey : " + CSEProfile.dKey);
		System.out.println("requestBody : " + requestBody);
			
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(post);
		
		HttpEntity responseEntity = response.getEntity();
		
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**
	 * aeContainerCreateMessage Method
	 * @param registrationProfile
	 * @param CSEProfile
	 * @return responseString
	 * @throws Exception
	 * Mobius로 oneM2M AE Container resource Create 요청을 위한  Method로서 HTTP POST를 사용함
	 */
	public String aeContainerCreateMessage(Container registrationProfile, CSEBase CSEProfile) throws Exception {
		
		String requestBody = "";
		
		if (primitiveType) {
			requestBody = 			 
				"<m2m:container\n" +
						"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
				"</m2m:container>";
		}
		else {
			requestBody = 			 
				"<m2m:cnt\n" +
						"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
				"</m2m:cnt>";
		}
		
		StringEntity entity = new StringEntity(
							new String(requestBody.getBytes()));
		
		URI uri = null;
		
		if (interopType) {
			uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/" + CSEProfile.resourceName + "/" + registrationProfile.parentName)
				.build();
		}
		else {
			uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/remoteCSE-" + CSEProfile.CSEID + "/AE-" + registrationProfile.parentID)
				.build();
		}
		
		HttpPost post = new HttpPost(uri);
				post.setHeader("Content-Type", "application/vnd.onem2m-res+xml;ty=3");
				post.setHeader("Accept", "application/xml");
				post.setHeader("locale", "ko");
				post.setHeader("X-M2M-Origin", CSEProfile.from);
				post.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				post.setHeader("X-M2M-NM", registrationProfile.resourceName);
				post.setHeader("dKey", CSEProfile.dKey);
				post.setEntity(entity);
				
				System.out.println("requestURI : " + uri);
				System.out.println("deviceKey : " + CSEProfile.dKey);
				System.out.println("requestBody : " + requestBody);
				
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(post);
	
		HttpEntity responseEntity = response.getEntity();
			
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseString;
	}
	
	/**
	 * aeContentInstanceCreate Method
	 * @param uploadThingData
	 * @param CSEProfile
	 * @return responseCode
	 * @throws Exception
	 * Mobius로 oneM2M AE contentInstance resource Create 요청을 위한  Method로서 HTTP POST를 사용함
	 */
	public int aeContentInstanceCreateMessage(ContentInstance uploadThingData, CSEBase CSEProfile) throws Exception {
		
		String requestBody = "";
		
		if (primitiveType) {
			requestBody = 			  
				"<m2m:contentInstance\n" +
						"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
						"<contentInfo>" + uploadThingData.contentInfo + "</contentInfo>\n" + 
						//"<content>" + Base64.encode(uploadThingData.content.getBytes()) + "</content>\n" +
						"<content>" + uploadThingData.content + "</content>\n" +
				"</m2m:contentInstance>";
		}
		else {
			requestBody = 			  
				"<m2m:cin\n" +
						"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
						"<cnf>" + uploadThingData.contentInfo + "</cnf>\n" + 
						//"<con>" + Base64.encode(uploadThingData.content.getBytes()) + "</con>\n" +
						"<con>" + uploadThingData.content + "</con>\n" +
				"</m2m:cin>";
		}
		
		StringEntity entity = new StringEntity(
							new String(requestBody.getBytes()));
		
		URI uri = null;
		
		if (interopType) {
			uri = new URIBuilder()
				.setScheme("http")
				.setHost(INCSEAddress)
				.setPath("/Mobius/" + CSEProfile.resourceName + "/" + uploadThingData.appName + "/" + uploadThingData.containerName)
				.build();
		}
		else {
			uri = new URIBuilder()
			.setScheme("http")
			.setHost(INCSEAddress)
			.setPath("/Mobius/remoteCSE-" + CSEProfile.CSEID + "/AE-" + uploadThingData.appId + "/container-" + uploadThingData.containerName)
			.build();
		}
		
		HttpPost post = new HttpPost(uri);
				post.setHeader("Content-Type", "application/vnd.onem2m-res+xml;ty=4");
				post.setHeader("Accept", "application/xml");
				post.setHeader("locale", "ko");
				post.setHeader("X-M2M-Origin", CSEProfile.from);
				post.setHeader("X-M2M-RI", Integer.toString(requestIndex));
				post.setHeader("dKey", CSEProfile.dKey);
				post.setEntity(entity);
				
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = httpClient.execute(post);
	
		HttpEntity responseEntity = response.getEntity();
			
		String responseString = EntityUtils.toString(responseEntity);
		
		int responseCode = response.getStatusLine().getStatusCode();
		
		System.out.println(uri.getPath());
		
		if (debugPrint) {
			System.out.println("HTTP Response Code : " + responseCode);
			System.out.println("HTTP Response String : " + responseString);
		}
		
		httpClient.close();
		
		return responseCode;
	}
}