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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.re.keti.ncube.CSEBase;

/**
 * (Next version ���� ����) oneM2M AE ���� resource �� P2P ����� �����ϱ� ���� �淮 HTTP server Class
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 */
public class HttpServerKeti extends Thread {
	Selector clientSelector;
	private BlockingQueue<ArrayList<Object>> resourceManagerQueue;
	private BlockingQueue<ArrayList<Object>> interactionManagerQueue;
	private BlockingQueue<ArrayList<Object>> httpServerQueue;
	private int serverPort;
	private int serverThreads;
	
	public HttpServerKeti(
			BlockingQueue<ArrayList<Object>> resourceQueue,
			BlockingQueue<ArrayList<Object>> interactionQueue,
			BlockingQueue<ArrayList<Object>> myQueue,
			int port, int threads) {
		
		this.resourceManagerQueue = resourceQueue;
		this.interactionManagerQueue = interactionQueue;
		this.httpServerQueue = myQueue;
		this.serverPort = port;
		this.serverThreads = threads;
	}
	
	public void run(){
		try {
			serverStart(serverPort, serverThreads);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * serverStart Method
	 * @param serverPort
	 * @param serverThreads
	 * @throws IOException
	 * HTTP Server ������ ���� �ʱ�ȭ�� �����ϴ� Method
	 */
	public void serverStart(int serverPort, int serverThreads) throws IOException {
		clientSelector = Selector.open();
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		InetSocketAddress sa = new InetSocketAddress(InetAddress.getLocalHost(), serverPort);
		ssc.socket().bind(sa);
		ssc.register(clientSelector, SelectionKey.OP_ACCEPT);

		Executor executor = Executors.newFixedThreadPool(serverThreads);

		while (true)
			try {
				while (clientSelector.select(100) == 0);
				Set<SelectionKey> readySet = clientSelector.selectedKeys();
				for (Iterator<SelectionKey> it = readySet.iterator(); it.hasNext();) {
					final SelectionKey key = it.next();
					it.remove();
					if (key.isAcceptable())
						acceptClient(ssc);
					else {
						key.interestOps(0);
						executor.execute(new Runnable() {
							public void run() {
								try {
									handleClient(key);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			}
	}

	void acceptClient(ServerSocketChannel ssc) throws IOException {
		SocketChannel clientSocket = ssc.accept();
		clientSocket.configureBlocking(false);
		SelectionKey key = clientSocket.register(clientSelector, SelectionKey.OP_READ);
		HttpdConnection client = new HttpdConnection(
									clientSocket, resourceManagerQueue, 
									interactionManagerQueue, httpServerQueue);
		key.attach(client);
	}

	void handleClient(SelectionKey key) throws Exception {
		HttpdConnection client = (HttpdConnection) key.attachment();
		if (key.isReadable())
			client.read(key);
		else
			client.write(key);
		clientSelector.wakeup();
	}
}

/**
 * (Next version ���� ����) oneM2M AE ���� resource �� P2P ����� �����ϱ� ���� �淮 HTTP server daemon Class
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 *
 */
class HttpdConnection {

	static Charset charset = Charset.forName("8859_1");
	static Pattern httpGetPattern = Pattern.compile("(?s)GET /?(\\S*).*");
	static Pattern httpPostPattern = Pattern.compile("(?s)POST /?(\\S*).*");
	
	final String oneM2MXMLStartTag = "<m2m:AE\n" +
										"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
										"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n";
	final String oneM2MXMLEndTag = "</m2m:AE>";
	
	BlockingQueue<ArrayList<Object>> resourceManagerQueue;
	BlockingQueue<ArrayList<Object>> interactionManagerQueue;
	BlockingQueue<ArrayList<Object>> httpServerQueue;
	SocketChannel clientSocket;
	ByteBuffer buff = ByteBuffer.allocateDirect(64 * 1024);
	String request;
	String response;
	FileChannel file;
	int filePosition;

	HttpdConnection(
			SocketChannel clientSocket,
			BlockingQueue<ArrayList<Object>> resourceQueue,
			BlockingQueue<ArrayList<Object>> interactionQueue,
			BlockingQueue<ArrayList<Object>> myQueue) {
		
		this.clientSocket = clientSocket;
		this.resourceManagerQueue = resourceQueue;
		this.interactionManagerQueue = interactionQueue;
		this.httpServerQueue = myQueue;
	}

	/**
	 * read Method
	 * @param key
	 * @throws Exception
	 * HTTP request�� �о���� Method
	 */
	void read(SelectionKey key) throws Exception {
		if (request == null && (clientSocket.read(buff) == -1 
				|| buff.get(buff.position() - 1) == '\n' 
				|| buff.get(buff.position() - 1) == '>'))
			
			processRequest(key);
		else
			key.interestOps(SelectionKey.OP_READ);
	}

	void processRequest(SelectionKey key) throws Exception {
		String url = null;
		
		buff.flip();
		request = charset.decode(buff).toString();
		
		System.out.println("http request : \n" + request);
		
		Matcher get = httpGetPattern.matcher(request);
		Matcher post = httpPostPattern.matcher(request);
		
		if (get.matches()) {
			url = get.group(1);
			if (url.matches("nCube.*")) {
				ArrayList<Object> result = HttpGetParser.httpParse(request);
				if (!result.isEmpty()) {
					String resultString = (String) result.get(0);
					if (resultString.contains("request")) {
						resourceManagerQueue.put(result);
						ArrayList<Object> httpServerArrayList = httpServerQueue.take();
						String responseHttp = (String) httpServerArrayList.get(0);
						
						System.out.println("[HttpServer] resource response : " + responseHttp);
						
						if (responseHttp.equals("requestCSEBaseRetrieve")) {
							response = httpResponse(HttponeM2MRSC.ACCEPTED) + 
									HttpResponseGenerator.responseCSEBase((CSEBase) httpServerArrayList.get(1));
						}
					}
					else {
						response = httpResponse(resultString);
					}
				}
				else {
					response = httpResponse(HttponeM2MRSC.NOT_FOUND);
				}
			}
			else {
				response = httpResponse(HttponeM2MRSC.NOT_FOUND);
			}
		}
		
		else if (post.matches()) {
			url = post.group(1);
			if (url.matches("nCube.*")) {
				ArrayList<Object> result = HttpPostParser.httpParse(request);
				if (!result.isEmpty()) {
					String resultString = (String) result.get(0);
					if (resultString.contains("request")) {
						resourceManagerQueue.put(result);
						ArrayList<Object> httpServerArrayList = httpServerQueue.take();
						String responseHttp = (String) httpServerArrayList.get(0);
						
						System.out.println("[HttpServer] resource response : " + responseHttp);
						
						if (responseHttp.equals("create")) {
							response = httpResponse(HttponeM2MRSC.CREATED);
						}
						else if (responseHttp.equals("registered")) {
							response = httpResponse(HttponeM2MRSC.ALREADY_EXISTS);
						}
						else if (responseHttp.equals("Found")) {
							String mgmtCmdRequest = (String) httpServerArrayList.get(1);
							mgmtCmdRequest = mgmtCmdRequest + "," + result.get(2);
							System.out.println(mgmtCmdRequest);
							
							ArrayList<Object> interactionSendArrayList = new ArrayList<Object>();
							interactionSendArrayList.add("requestMgmtCmdControl");
							interactionSendArrayList.add(mgmtCmdRequest);
							interactionManagerQueue.put(interactionSendArrayList);
							
							response = httpResponse(HttponeM2MRSC.CREATED);
						}
						else if (responseHttp.equals("not found")) {
							response = httpResponse(HttponeM2MRSC.NOT_FOUND);
						}
						else {
							response = httpResponse(HttponeM2MRSC.NOT_FOUND);
						}
					}
					else {
						response = httpResponse(resultString);
					}
				}
				else {
					response = httpResponse(HttponeM2MRSC.BAD_REQUEST);
				}
			}
			else {
				response = httpResponse(HttponeM2MRSC.BAD_REQUEST);
			}
		}
		
		else
			response = httpResponse(HttponeM2MRSC.BAD_REQUEST);

		if (response != null) {
			buff.clear();
			charset.newEncoder().encode(CharBuffer.wrap(response), buff, true);
			buff.flip();
		}
		key.interestOps(SelectionKey.OP_WRITE);
	}

	/**
	 * write Method
	 * @param key
	 * @throws IOException
	 * HTTP response�� ���� Method
	 */
	void write(SelectionKey key) throws IOException {
		if (response != null) {
			clientSocket.write(buff);
			if (buff.remaining() == 0)
				response = null;
		} else if (file != null) {
			int remaining = (int) file.size() - filePosition;
			long sent = file.transferTo(filePosition, remaining, clientSocket);
			if (sent >= remaining || remaining <= 0) {
				file.close();
				file = null;
			} else
				filePosition += sent;
		}
		if (response == null && file == null) {
			clientSocket.close();
			key.cancel();
		} else
			key.interestOps(SelectionKey.OP_WRITE);
	}
	
	/**
	 * httpResponse Method
	 * @param code
	 * @return responseString
	 * HTTP response code�� ���� ������ �����ϴ� Method
	 */
	String httpResponse(String oneM2MRSC) {

		String responseString = null;
		
		@SuppressWarnings("unused")
		String ok = "HTTP/1.1 200 OK\n" + 
				"Connection: close\n" + 
				"Server: nCube\n" + 
				"Content-Type: application/vnd.onem2m-res+xml\n" +
				"X-M2M-RSC: 2000\n\n";
		
		String accepted = "HTTP/1.1 202 Accepted\n" + 
				"Connection: close\n" + 
				"Server: nCube\n" + 
				"Content-Type: application/vnd.onem2m-res+xml\n" +
				"X-M2M-RSC: " + HttponeM2MRSC.ACCEPTED + "\n\n";
	
		String created = "HTTP/1.1 201 Created\n" + 
					"Connection: close\n" + 
					"Server: nCube\n" + 
					"Content-Type: application/vnd.onem2m-res+xml\n" +
					"X-M2M-RSC: " + HttponeM2MRSC.CREATED + "\n\n";
		
		String badRequest = "HTTP/1.1 400 Bad Request\n" + 
							"Connection: close\n" + 
							"Server: nCube\n";
		
		String forbidden = "HTTP/1.1 403 Forbidden\n" + 
							"Connection: close\n" + 
							"Server: nCube\n\n";
		
		String notFound = "HTTP/1.1 404 Not Found\n" + 
							"Connection: close\n" + 
							"Server: nCube\n" +
							"X-M2M-RSC: " + HttponeM2MRSC.NOT_FOUND + "\n\n";
		
		String alreadyExists = "HTTP/1.1 403 Forbidden\n" + 
							"Connection: close\n" + 
							"Server: nCube\n";
		
		String notImplemented = "HTTP/1.1 501 Not Implemented\n" + 
							"Connection: close\n" + 
							"Server: nCube\n" +
							"X-M2M-RSC: " + HttponeM2MRSC.NOT_IMPLEMENTED + "\n\n";
		
		if (oneM2MRSC.equals(HttponeM2MRSC.ACCEPTED)) {
			responseString = accepted;
		}
		
		switch(oneM2MRSC) {
		
		case HttponeM2MRSC.ACCEPTED:
			responseString = accepted;
			break;
			
		case HttponeM2MRSC.CREATED:
			responseString = created;
			break;
		
		case HttponeM2MRSC.BAD_REQUEST:
			responseString = badRequest + "X-M2M-RSC: " + HttponeM2MRSC.BAD_REQUEST + "\n\n";
			break;
			
		case HttponeM2MRSC.CONTENTS_UNACCEPTABLE:
			responseString = badRequest + "X-M2M-RSC: " + HttponeM2MRSC.CONTENTS_UNACCEPTABLE + "\n\n";
			break;
			
		case HttponeM2MRSC.SUBSCRIPTION_CREATOR_HAS_NO_PRIVILEGE:
			responseString = forbidden;
			break;
			
		case HttponeM2MRSC.NOT_FOUND:
			responseString = notFound;
			break;
			
		case HttponeM2MRSC.ALREADY_EXISTS:
			responseString = alreadyExists + "X-M2M-RSC: " + HttponeM2MRSC.ALREADY_EXISTS + "\n\n";
			break;
			
		case HttponeM2MRSC.NOT_IMPLEMENTED:
			responseString = notImplemented;
			break;
		}
		
		return responseString;
	}
}
