package kr.re.keti.ncube.httpserver;

import kr.re.keti.ncube.CSEBase;

public class HttpResponseGenerator {
	
//	public static void responseCSEBase(AE ae1) {
//		try {
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document document = builder.newDocument();
//			
//			Element rootElement = document.createElement("AE");
//			document.appendChild(rootElement);
//			rootElement.appendChild(document.createTextNode(ae1.AEID));
//			
//			TransformerFactory tFact = TransformerFactory.newInstance();
//			Transformer trans = tFact.newTransformer();
//			
//			StringWriter writer = new StringWriter();
//			StreamResult result = new StreamResult(writer);
//			
//			DOMSource source = new DOMSource(document);
//			trans.transform(source, result);
//			
//			System.out.println(writer.toString());
//		} catch (ParserConfigurationException | TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static String responseCSEBase(CSEBase cseBase) {
		String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
							+ "<m2m:CSEBase"
							+ " xmlns:m2m=\"http://www.onem2m.org/xml/protocols\""
							+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n";
		String xmlBody = "<resourceType>" + cseBase.resourceType + "</resourceType>\n"
						+ "<resourceID>" + cseBase.resourceID + "</resourceID>\n"
						+ "<resourceName>" + cseBase.resourceName + "</resourceName>\n"
						+ "<parentID>" + cseBase.parentID + "</parentID>\n"
						+ "<creationTime>" + cseBase.creationTime + "</creationTime>\n"
						+ "<lastModifiedTime>" + cseBase.lastModifiedTime + "</lastModifiedTime>\n"
						+ "<labels>" + cseBase.labels + "</labels>\n"
						+ "<accessControlPolicyIDs>" + cseBase.accessControlPolicyIDs + "</accessControlPolicyIDs>\n"
						+ "<supportedResourceType></supportedResourceType>\n"
						+ "<cseType>" + cseBase.CSEType + "</cseType>\n"
						+ "<CSE-ID>" + cseBase.CSEID + "</CSE-ID>\n"
						+ "<pointOfAccess>" + cseBase.pointOfAccess + "</pointOfAccess>\n"
						+ "<nodeLink>" + cseBase.nodeLink + "</nodeLink>\n"
						+ "<notificationCongestionPolicy></notificationCongestionPolicy>\n";
		String xmlHeaderEnd = "</m2m:CSEBase>";
		
		String xmlString = xmlHeader + xmlBody + xmlHeaderEnd;
		
		System.out.println(xmlString);
		
		return xmlString;
	}
}