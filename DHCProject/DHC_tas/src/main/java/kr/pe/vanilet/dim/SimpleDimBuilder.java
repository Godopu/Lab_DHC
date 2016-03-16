package kr.pe.vanilet.dim;

import java.io.File;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SimpleDimBuilder
{
	private Document document;
	private Element rootElement;
	private List<Element> entryElementList;
	private boolean initialized;
	
	public SimpleDimBuilder() {
		super();
		
		this.initialized = false;
	}
	
	public void createEntryElement(String unitCode, String unit, String dataName,
			String dataType, String dataValue) {
		initialize();

		Element entryElement = document.createElement("entry");
		
		entryElement.appendChild(createMetaDataElement(unitCode, unit));
		entryElement.appendChild(createSimpleElement(dataName, dataType, dataValue));
		
		entryElementList.add(entryElement);
	}
	
	public void saveToFile(String path) {		
		try {
			if(this.initialized) {
				updateTree();
				
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(path));
	
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");			
				transformer.transform(source, result);
			}
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String saveToString() {
		try {			
			if(this.initialized) {
				updateTree();
				
				DOMSource source = new DOMSource(document);
				StringWriter writer = new StringWriter();
				StreamResult result = new StreamResult(writer);
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");			
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				transformer.transform(source, result);
				
				return writer.toString();
			}
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String saveToCDATAString() {
		try {			
			if(this.initialized) {
				updateTree();
				
				DOMSource source = new DOMSource(document);
				StringWriter writer = new StringWriter();
				
				writer.append("<![CDATA[");
				
				StreamResult result = new StreamResult(writer);
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");			
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				transformer.transform(source, result);
				
				writer.append("]]>");
				
				return writer.toString();
			}
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String saveToBase64String() {
		try {			
			if(this.initialized) {
				updateTree();
				
				DOMSource source = new DOMSource(document);
				StringWriter writer = new StringWriter();
				
				StreamResult result = new StreamResult(writer);
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");			
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				transformer.transform(source, result);

				String stringResult = new String(Base64.getEncoder().encode(writer.toString().getBytes("UTF-8")), "UTF-8");
				
				return stringResult;
			}
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void initialize() {
		try
		{			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			document = docBuilder.newDocument();
			
			// root elements
			rootElement = document.createElement("data-list");
			document.appendChild(rootElement);
	
			entryElementList = new ArrayList<Element>();
			
			this.initialized = true;
		} catch (ParserConfigurationException pce) {
			this.initialized = false;
			pce.printStackTrace();
		}
	}
	
	private Element createMetaDataElement(String unitCode, String unit) {
		Element metaDataElement = document.createElement("meta-data");
		Element metaElement = document.createElement("meta");
		
		metaElement.setAttribute("name", "unit-code");
		metaElement.appendChild(document.createTextNode(unitCode));
		
		metaDataElement.appendChild(metaElement);
		
		metaElement = document.createElement("meta");
		metaElement.setAttribute("name", "unit");
		metaElement.appendChild(document.createTextNode(unit));
		
		metaDataElement.appendChild(metaElement);
		
		return metaDataElement;
	}
	
	private Element createSimpleElement(String dataName, String dataType, String dataValue) {
		Element simpleElement = document.createElement("simple");
		Element nameElement = document.createElement("name");
		Element typeElement = document.createElement("type");
		Element valueElement = document.createElement("value");
		
		nameElement.appendChild(document.createTextNode(dataName));
		typeElement.appendChild(document.createTextNode(dataType));
		valueElement.appendChild(document.createTextNode(dataValue));
		
		simpleElement.appendChild(nameElement);
		simpleElement.appendChild(typeElement);
		simpleElement.appendChild(valueElement);
		
		return simpleElement;
	}

	private void updateTree() {
		for(Element element : entryElementList)
			rootElement.appendChild(element);
	}
	
	@Override
	public String toString() {
		return saveToString();
	}
}

