package kr.re.keti.ncube.interactionmanager;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import kr.re.keti.ncube.AE;
import kr.re.keti.ncube.CSEBase;
import kr.re.keti.ncube.Container;
import kr.re.keti.ncube.DeviceInfo;
import kr.re.keti.ncube.ExecInstance;
import kr.re.keti.ncube.Firmware;
import kr.re.keti.ncube.MgmtCmd;
import kr.re.keti.ncube.Software;

/**
 * Mobius와 Interaction을 통해 수신한 메시지를 파싱하기 위한 Method를 모아놓은 Class로서 각 Method는 Dom Parser로 구현됨
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 */
public class InteractionResponseParser {
	
	private boolean primitiveType = false;
	
	public InteractionResponseParser (boolean primitiveType) {
		this.primitiveType = primitiveType;
	}
	
	/**
	 * CSERegistration Method
	 * @param updateProfile
	 * @param responseString
	 * @return CSERegistProfile
	 * @throws Exception
	 * CSE 등록에 대한 리턴메시지를 파싱하기 위한 Method
	 */
	public CSEBase CSERegistration(CSEBase updateProfile, String[] httpResponse) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(httpResponse[1]));
		Document document = builder.parse(xmlSource);
		
		CSEBase CSERegistProfile = updateProfile;
		
		if (primitiveType) {
			NodeList resourceTypeNodeList = document.getElementsByTagName("resourceType");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("resourceID");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("resourceName");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("parentID");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("creationTime");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lastModifiedTime");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("labels");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.labels = labelsNode.getNodeValue();
			}
			
			NodeList accessControlPolicyIDsNodeList = document.getElementsByTagName("accessControlPolicyIDs");
			if (accessControlPolicyIDsNodeList.getLength() > 0 && accessControlPolicyIDsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node accessControlPolicyIDsNode = accessControlPolicyIDsNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.accessControlPolicyIDs = accessControlPolicyIDsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("expirationTime");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.expirationTime = expirationTimeNode.getNodeValue();
			}
			
			NodeList announceToNodeList = document.getElementsByTagName("announceTo");
			if (announceToNodeList.getLength() > 0 && announceToNodeList.item(0).getChildNodes().getLength() > 0) {
				Node announceToNode = announceToNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.announceTo = announceToNode.getNodeValue();
			}
			
			NodeList announcedAttributeNodeList = document.getElementsByTagName("announcedAttribute");
			if (announcedAttributeNodeList.getLength() > 0 && announcedAttributeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node announcedAttributeNode = announcedAttributeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.announcedAttribute = announcedAttributeNode.getNodeValue();
			}
			
			NodeList pointOfAccessNodeList = document.getElementsByTagName("pointOfAccess");
			if (pointOfAccessNodeList.getLength() > 0 && pointOfAccessNodeList.item(0).getChildNodes().getLength() > 0) {
				Node pointOfAccessNode = pointOfAccessNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.pointOfAccess = pointOfAccessNode.getNodeValue();
			}
			
			NodeList CSEBaseNodeList = document.getElementsByTagName("CSEBase");
			if (CSEBaseNodeList.getLength() > 0 && CSEBaseNodeList.item(0).getChildNodes().getLength() > 0) {
				Node CSEBaseNode = CSEBaseNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.CSEBase = CSEBaseNode.getNodeValue();
			}
			
			NodeList CSEIDNodeList = document.getElementsByTagName("CSE-ID");
			if (CSEIDNodeList.getLength() > 0 && CSEIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node CSEIDNode = CSEIDNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.CSEID = CSEIDNode.getNodeValue();
			}
			
			NodeList CSETypeNodeList = document.getElementsByTagName("cseType");
			if (CSETypeNodeList.getLength() > 0 && CSETypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node CSETypeNode = CSETypeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.CSEType = CSETypeNode.getNodeValue();
			}
			
			NodeList nodeLinkNodeList = document.getElementsByTagName("nodeLink");
			if (nodeLinkNodeList.getLength() > 0 && nodeLinkNodeList.item(0).getChildNodes().getLength() > 0) {
				Node nodeLinkNode = nodeLinkNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.nodeLink = nodeLinkNode.getNodeValue();
			}
		}
		else {
			NodeList resourceTypeNodeList = document.getElementsByTagName("rty");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("ri");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("rn");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("pi");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("ct");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lt");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("lbl");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.labels = labelsNode.getNodeValue();
			}
			
			NodeList accessControlPolicyIDsNodeList = document.getElementsByTagName("acpi");
			if (accessControlPolicyIDsNodeList.getLength() > 0 && accessControlPolicyIDsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node accessControlPolicyIDsNode = accessControlPolicyIDsNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.accessControlPolicyIDs = accessControlPolicyIDsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("et");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.expirationTime = expirationTimeNode.getNodeValue();
			}
			
			NodeList announceToNodeList = document.getElementsByTagName("at");
			if (announceToNodeList.getLength() > 0 && announceToNodeList.item(0).getChildNodes().getLength() > 0) {
				Node announceToNode = announceToNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.announceTo = announceToNode.getNodeValue();
			}
			
			NodeList announcedAttributeNodeList = document.getElementsByTagName("aa");
			if (announcedAttributeNodeList.getLength() > 0 && announcedAttributeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node announcedAttributeNode = announcedAttributeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.announcedAttribute = announcedAttributeNode.getNodeValue();
			}
			
			NodeList pointOfAccessNodeList = document.getElementsByTagName("poa");
			if (pointOfAccessNodeList.getLength() > 0 && pointOfAccessNodeList.item(0).getChildNodes().getLength() > 0) {
				Node pointOfAccessNode = pointOfAccessNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.pointOfAccess = pointOfAccessNode.getNodeValue();
			}
			
			NodeList CSEBaseNodeList = document.getElementsByTagName("cb");
			if (CSEBaseNodeList.getLength() > 0 && CSEBaseNodeList.item(0).getChildNodes().getLength() > 0) {
				Node CSEBaseNode = CSEBaseNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.CSEBase = CSEBaseNode.getNodeValue();
			}
			
			NodeList CSEIDNodeList = document.getElementsByTagName("csi");
			if (CSEIDNodeList.getLength() > 0 && CSEIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node CSEIDNode = CSEIDNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.CSEID = CSEIDNode.getNodeValue();
			}
			
			NodeList CSETypeNodeList = document.getElementsByTagName("cst");
			if (CSETypeNodeList.getLength() > 0 && CSETypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node CSETypeNode = CSETypeNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.CSEType = CSETypeNode.getNodeValue();
			}
			
			NodeList nodeLinkNodeList = document.getElementsByTagName("nl");
			if (nodeLinkNodeList.getLength() > 0 && nodeLinkNodeList.item(0).getChildNodes().getLength() > 0) {
				Node nodeLinkNode = nodeLinkNodeList.item(0).getChildNodes().item(0);
				CSERegistProfile.nodeLink = nodeLinkNode.getNodeValue();
			}
		}
		
		if (httpResponse[0].length() > 3) {
			CSERegistProfile.dKey = httpResponse[0];
		}
		
		return CSERegistProfile;
	}
	
	/**
	 * firmwareCreateParse Method
	 * @param updateInfo
	 * @param responseString
	 * @return firmwareCreateInfo
	 * @throws Exception
	 * firmware create에 대한 리턴메시지를 파싱하기 위한 Method
	 */
	public Firmware firmwareCreateParse(Firmware updateInfo, String responseString) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(responseString));
		Document document = builder.parse(xmlSource);
		
		Firmware firmwareCreateInfo = updateInfo;
		
		if (primitiveType) {
			NodeList resourceTypeNodeList = document.getElementsByTagName("resourceType");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("resourceID");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("resourceName");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("parentID");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("creationTime");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lastModifiedTime");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("labels");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.labels = labelsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("expirationTime");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.expirationTime = expirationTimeNode.getNodeValue();
			}
			
			NodeList mgmtDefinitionNodeList = document.getElementsByTagName("mgmtDefinition");
			if (mgmtDefinitionNodeList.getLength() > 0 && mgmtDefinitionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node mgmtDefinitionNode = mgmtDefinitionNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.mgmtDefinition = mgmtDefinitionNode.getNodeValue();
			}
			
			NodeList objectIDsNodeList = document.getElementsByTagName("objectIDs");
			if (objectIDsNodeList.getLength() > 0 && objectIDsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node objectIDsNode = objectIDsNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.objectIDs = objectIDsNode.getNodeValue();
			}
			
			NodeList objectPathsNodeList = document.getElementsByTagName("objectPaths");
			if (objectPathsNodeList.getLength() > 0 && objectPathsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node objectPathsNode = objectPathsNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.objectPaths = objectPathsNode.getNodeValue();
			}
			
			NodeList versionNodeList = document.getElementsByTagName("version");
			if (versionNodeList.getLength() > 0 && versionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node versionNode = versionNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.version = versionNode.getNodeValue();
			}
			
			NodeList nameNodeList = document.getElementsByTagName("name");
			if (nameNodeList.getLength() > 0 && nameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node nameNode = nameNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.name = nameNode.getNodeValue();
			}
			
			NodeList URLNodeList = document.getElementsByTagName("URL");
			if (URLNodeList.getLength() > 0 && URLNodeList.item(0).getChildNodes().getLength() > 0) {
				Node URLNode = URLNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.url = URLNode.getNodeValue();
			}
			
			NodeList updateNodeList = document.getElementsByTagName("update");
			if (updateNodeList.getLength() > 0 && updateNodeList.item(0).getChildNodes().getLength() > 0) {
				Node updateNode = updateNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.update = updateNode.getNodeValue();
			}
			
			NodeList actionNodeList = document.getElementsByTagName("action");
			if (actionNodeList.getLength() > 0 && actionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node actionNode = actionNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.action = actionNode.getNodeValue();
			}
		
			NodeList statusNodeList = document.getElementsByTagName("status");
			if (statusNodeList.getLength() > 0 && statusNodeList.item(0).getChildNodes().getLength() > 0) {
				Node statusNode = statusNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.status = statusNode.getNodeValue();
			}
		}
		else {
			NodeList resourceTypeNodeList = document.getElementsByTagName("rty");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("ri");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("rn");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("pi");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("ct");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lt");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("lbl");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.labels = labelsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("et");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.expirationTime = expirationTimeNode.getNodeValue();
			}
			
			NodeList mgmtDefinitionNodeList = document.getElementsByTagName("mgd");
			if (mgmtDefinitionNodeList.getLength() > 0 && mgmtDefinitionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node mgmtDefinitionNode = mgmtDefinitionNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.mgmtDefinition = mgmtDefinitionNode.getNodeValue();
			}
			
			NodeList objectIDsNodeList = document.getElementsByTagName("obis");
			if (objectIDsNodeList.getLength() > 0 && objectIDsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node objectIDsNode = objectIDsNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.objectIDs = objectIDsNode.getNodeValue();
			}
			
			NodeList objectPathsNodeList = document.getElementsByTagName("obps");
			if (objectPathsNodeList.getLength() > 0 && objectPathsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node objectPathsNode = objectPathsNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.objectPaths = objectPathsNode.getNodeValue();
			}
			
			NodeList versionNodeList = document.getElementsByTagName("vr");
			if (versionNodeList.getLength() > 0 && versionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node versionNode = versionNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.version = versionNode.getNodeValue();
			}
			
			NodeList nameNodeList = document.getElementsByTagName("nm");
			if (nameNodeList.getLength() > 0 && nameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node nameNode = nameNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.name = nameNode.getNodeValue();
			}
			
			NodeList URLNodeList = document.getElementsByTagName("url");
			if (URLNodeList.getLength() > 0 && URLNodeList.item(0).getChildNodes().getLength() > 0) {
				Node URLNode = URLNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.url = URLNode.getNodeValue();
			}
			
			NodeList updateNodeList = document.getElementsByTagName("ud");
			if (updateNodeList.getLength() > 0 && updateNodeList.item(0).getChildNodes().getLength() > 0) {
				Node updateNode = updateNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.update = updateNode.getNodeValue();
			}
			
			NodeList actionNodeList = document.getElementsByTagName("acn");
			if (actionNodeList.getLength() > 0 && actionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node actionNode = actionNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.action = actionNode.getNodeValue();
			}
		
			NodeList statusNodeList = document.getElementsByTagName("sus");
			if (statusNodeList.getLength() > 0 && statusNodeList.item(0).getChildNodes().getLength() > 0) {
				Node statusNode = statusNodeList.item(0).getChildNodes().item(0);
				firmwareCreateInfo.status = statusNode.getNodeValue();
			}
		}
		
		return firmwareCreateInfo;
	}
	
	/**
	 * deviceInfoCreateParse Method
	 * @param updateInfo
	 * @param responseString
	 * @return deviceInfoCreateInfo
	 * @throws Exception
	 * deviceInfo Create에 대한 리턴메시지를 파싱하기 위한 Method
	 */
	public DeviceInfo deviceInfoCreateParse(DeviceInfo updateInfo, String responseString) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(responseString));
		Document document = builder.parse(xmlSource);
		
		DeviceInfo deviceInfoCreateInfo = updateInfo;

		if (primitiveType) {
			NodeList resourceTypeNodeList = document.getElementsByTagName("resourceType");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("resourceID");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("resourceName");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("parentID");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("creationTime");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lastModifiedTime");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("labels");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.labels = labelsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("expirationTime");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.expirationTime = expirationTimeNode.getNodeValue();
			}
			
			NodeList mgmtDefinitionNodeList = document.getElementsByTagName("mgmtDefinition");
			if (mgmtDefinitionNodeList.getLength() > 0 && mgmtDefinitionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node mgmtDefinitionNode = mgmtDefinitionNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.mgmtDefinition = mgmtDefinitionNode.getNodeValue();
			}
			
			NodeList objectIDsNodeList = document.getElementsByTagName("objectIDs");
			if (objectIDsNodeList.getLength() > 0 && objectIDsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node objectIDsNode = objectIDsNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.objectIDs = objectIDsNode.getNodeValue();
			}
			
			NodeList objectPathsNodeList = document.getElementsByTagName("objectPaths");
			if (objectPathsNodeList.getLength() > 0 && objectPathsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node objectPathsNode = objectPathsNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.objectPaths = objectPathsNode.getNodeValue();
			}
			
			NodeList deviceLabelNodeList = document.getElementsByTagName("deviceLabel");
			if (deviceLabelNodeList.getLength() > 0 && deviceLabelNodeList.item(0).getChildNodes().getLength() > 0) {
				Node deviceLabelNode = deviceLabelNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.deviceLabel = deviceLabelNode.getNodeValue();
			}
			
			NodeList manufacturerNodeList = document.getElementsByTagName("manufacturer");
			if (manufacturerNodeList.getLength() > 0 && manufacturerNodeList.item(0).getChildNodes().getLength() > 0) {
				Node manufacturerNode = manufacturerNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.manufacturer = manufacturerNode.getNodeValue();
			}
			
			NodeList modelNodeList = document.getElementsByTagName("model");
			if (modelNodeList.getLength() > 0 && modelNodeList.item(0).getChildNodes().getLength() > 0) {
				Node modelNode = modelNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.model = modelNode.getNodeValue();
			}
			
			NodeList deviceTypeNodeList = document.getElementsByTagName("deviceType");
			if (deviceTypeNodeList.getLength() > 0 && deviceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node deviceTypeNode = deviceTypeNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.deviceType = deviceTypeNode.getNodeValue();
			}
			
			NodeList fwVersionNodeList = document.getElementsByTagName("fwVersion");
			if (fwVersionNodeList.getLength() > 0 && fwVersionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node fwVersionNode = fwVersionNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.fwVersion = fwVersionNode.getNodeValue();
			}
			
			NodeList swVersionNodeList = document.getElementsByTagName("swVersion");
			if (swVersionNodeList.getLength() > 0 && swVersionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node swVersionNode = swVersionNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.swVersion = swVersionNode.getNodeValue();
			}
			
			NodeList hwVersionNodeList = document.getElementsByTagName("hwVersion");
			if (hwVersionNodeList.getLength() > 0 && hwVersionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node hwVersionNode = hwVersionNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.hwVersion = hwVersionNode.getNodeValue();
			}
		}
		
		else {
			NodeList resourceTypeNodeList = document.getElementsByTagName("rty");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("ri");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("rn");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("pi");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("ct");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lt");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("lbl");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.labels = labelsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("et");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.expirationTime = expirationTimeNode.getNodeValue();
			}
			
			NodeList mgmtDefinitionNodeList = document.getElementsByTagName("mgd");
			if (mgmtDefinitionNodeList.getLength() > 0 && mgmtDefinitionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node mgmtDefinitionNode = mgmtDefinitionNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.mgmtDefinition = mgmtDefinitionNode.getNodeValue();
			}
			
			NodeList objectIDsNodeList = document.getElementsByTagName("obis");
			if (objectIDsNodeList.getLength() > 0 && objectIDsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node objectIDsNode = objectIDsNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.objectIDs = objectIDsNode.getNodeValue();
			}
			
			NodeList objectPathsNodeList = document.getElementsByTagName("obps");
			if (objectPathsNodeList.getLength() > 0 && objectPathsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node objectPathsNode = objectPathsNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.objectPaths = objectPathsNode.getNodeValue();
			}
			
			NodeList deviceLabelNodeList = document.getElementsByTagName("dlb");
			if (deviceLabelNodeList.getLength() > 0 && deviceLabelNodeList.item(0).getChildNodes().getLength() > 0) {
				Node deviceLabelNode = deviceLabelNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.deviceLabel = deviceLabelNode.getNodeValue();
			}
			
			NodeList manufacturerNodeList = document.getElementsByTagName("man");
			if (manufacturerNodeList.getLength() > 0 && manufacturerNodeList.item(0).getChildNodes().getLength() > 0) {
				Node manufacturerNode = manufacturerNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.manufacturer = manufacturerNode.getNodeValue();
			}
			
			NodeList modelNodeList = document.getElementsByTagName("mod");
			if (modelNodeList.getLength() > 0 && modelNodeList.item(0).getChildNodes().getLength() > 0) {
				Node modelNode = modelNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.model = modelNode.getNodeValue();
			}
			
			NodeList deviceTypeNodeList = document.getElementsByTagName("dty");
			if (deviceTypeNodeList.getLength() > 0 && deviceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node deviceTypeNode = deviceTypeNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.deviceType = deviceTypeNode.getNodeValue();
			}
			
			NodeList fwVersionNodeList = document.getElementsByTagName("fwv");
			if (fwVersionNodeList.getLength() > 0 && fwVersionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node fwVersionNode = fwVersionNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.fwVersion = fwVersionNode.getNodeValue();
			}
			
			NodeList swVersionNodeList = document.getElementsByTagName("swv");
			if (swVersionNodeList.getLength() > 0 && swVersionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node swVersionNode = swVersionNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.swVersion = swVersionNode.getNodeValue();
			}
			
			NodeList hwVersionNodeList = document.getElementsByTagName("hwv");
			if (hwVersionNodeList.getLength() > 0 && hwVersionNodeList.item(0).getChildNodes().getLength() > 0) {
				Node hwVersionNode = hwVersionNodeList.item(0).getChildNodes().item(0);
				deviceInfoCreateInfo.hwVersion = hwVersionNode.getNodeValue();
			}
		}
		
		return deviceInfoCreateInfo;
	}
	
	/**
	 * containerCreateParse Method
	 * @param responseString
	 * @return containerRegistProfile
	 * @throws Exception
	 * Mobius Mashup에 Container 생성 후 받은 리턴메시지를 파싱하기 위한 Method 
	 */
	public Container containerCreateParse(String responseString) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(responseString));
		Document document = builder.parse(xmlSource);
		
		Container containerRegistProfile = new Container();
		
		if (primitiveType) {
			NodeList resourceTypeNodeList = document.getElementsByTagName("resourceType");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("resourceID");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("parentID");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("resourceName");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("creationTime");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lastModifiedTime");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("labels");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.labels = labelsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("expirationTime");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.expirationTime = expirationTimeNode.getNodeValue();
			}
			
			NodeList announceToNodeList = document.getElementsByTagName("announceTo");
			if (announceToNodeList.getLength() > 0 && announceToNodeList.item(0).getChildNodes().getLength() > 0) {
				Node announceToNode = announceToNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.announceTo = announceToNode.getNodeValue();
			}
			
			NodeList announcedAttributeNodeList = document.getElementsByTagName("announcedAttribute");
			if (announcedAttributeNodeList.getLength() > 0 && announcedAttributeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node announcedAttributeNode = announcedAttributeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.announcedAttribute = announcedAttributeNode.getNodeValue();
			}
			
			NodeList stateTagNodeList = document.getElementsByTagName("stateTag");
			if (stateTagNodeList.getLength() > 0 && stateTagNodeList.item(0).getChildNodes().getLength() > 0) {
				Node stateTagNode = stateTagNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.stateTag = stateTagNode.getNodeValue();
			}
			
			NodeList creatorNodeList = document.getElementsByTagName("creator");
			if (creatorNodeList.getLength() > 0 && creatorNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creatorNode = creatorNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.creator = creatorNode.getNodeValue();
			}
			
			NodeList maxNrOfInstancesNodeList = document.getElementsByTagName("maxNrOfInstances");
			if (maxNrOfInstancesNodeList.getLength() > 0 && maxNrOfInstancesNodeList.item(0).getChildNodes().getLength() > 0) {
				Node maxNrOfInstancesNode = maxNrOfInstancesNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.maxNrOfInstances = maxNrOfInstancesNode.getNodeValue();
			}
			
			NodeList maxByteSizeNodeList = document.getElementsByTagName("maxByteSize");
			if (maxByteSizeNodeList.getLength() > 0 && maxByteSizeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node maxByteSizeNode = maxByteSizeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.maxByteSize = maxByteSizeNode.getNodeValue();
			}
			
			NodeList maxInstanceAgeNodeList = document.getElementsByTagName("maxInstanceAge");
			if (maxInstanceAgeNodeList.getLength() > 0 && maxInstanceAgeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node maxInstanceAgeNode = maxInstanceAgeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.maxInstanceAge = maxInstanceAgeNode.getNodeValue();
			}
			
			NodeList currentNrOfInstancesNodeList = document.getElementsByTagName("currentNrOfInstances");
			if (currentNrOfInstancesNodeList.getLength() > 0 && currentNrOfInstancesNodeList.item(0).getChildNodes().getLength() > 0) {
				Node currentNrOfInstancesNode = currentNrOfInstancesNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.currentNrOfInstances = currentNrOfInstancesNode.getNodeValue();
			}
			
			NodeList currentByteSizeNodeList = document.getElementsByTagName("currentByteSize");
			if (currentByteSizeNodeList.getLength() > 0 && currentByteSizeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node currentByteSizeNode = currentByteSizeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.currentByteSize = currentByteSizeNode.getNodeValue();
			}
		}
		else {
			NodeList resourceTypeNodeList = document.getElementsByTagName("rty");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("ri");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("pi");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("rn");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("ct");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lt");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("lbl");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.labels = labelsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("et");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.expirationTime = expirationTimeNode.getNodeValue();
			}
			
			NodeList announceToNodeList = document.getElementsByTagName("at");
			if (announceToNodeList.getLength() > 0 && announceToNodeList.item(0).getChildNodes().getLength() > 0) {
				Node announceToNode = announceToNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.announceTo = announceToNode.getNodeValue();
			}
			
			NodeList announcedAttributeNodeList = document.getElementsByTagName("aa");
			if (announcedAttributeNodeList.getLength() > 0 && announcedAttributeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node announcedAttributeNode = announcedAttributeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.announcedAttribute = announcedAttributeNode.getNodeValue();
			}
			
			NodeList stateTagNodeList = document.getElementsByTagName("st");
			if (stateTagNodeList.getLength() > 0 && stateTagNodeList.item(0).getChildNodes().getLength() > 0) {
				Node stateTagNode = stateTagNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.stateTag = stateTagNode.getNodeValue();
			}
			
			NodeList creatorNodeList = document.getElementsByTagName("cr");
			if (creatorNodeList.getLength() > 0 && creatorNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creatorNode = creatorNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.creator = creatorNode.getNodeValue();
			}
			
			NodeList maxNrOfInstancesNodeList = document.getElementsByTagName("mni");
			if (maxNrOfInstancesNodeList.getLength() > 0 && maxNrOfInstancesNodeList.item(0).getChildNodes().getLength() > 0) {
				Node maxNrOfInstancesNode = maxNrOfInstancesNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.maxNrOfInstances = maxNrOfInstancesNode.getNodeValue();
			}
			
			NodeList maxByteSizeNodeList = document.getElementsByTagName("mbs");
			if (maxByteSizeNodeList.getLength() > 0 && maxByteSizeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node maxByteSizeNode = maxByteSizeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.maxByteSize = maxByteSizeNode.getNodeValue();
			}
			
			NodeList maxInstanceAgeNodeList = document.getElementsByTagName("mia");
			if (maxInstanceAgeNodeList.getLength() > 0 && maxInstanceAgeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node maxInstanceAgeNode = maxInstanceAgeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.maxInstanceAge = maxInstanceAgeNode.getNodeValue();
			}
			
			NodeList currentNrOfInstancesNodeList = document.getElementsByTagName("cni");
			if (currentNrOfInstancesNodeList.getLength() > 0 && currentNrOfInstancesNodeList.item(0).getChildNodes().getLength() > 0) {
				Node currentNrOfInstancesNode = currentNrOfInstancesNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.currentNrOfInstances = currentNrOfInstancesNode.getNodeValue();
			}
			
			NodeList currentByteSizeNodeList = document.getElementsByTagName("cbs");
			if (currentByteSizeNodeList.getLength() > 0 && currentByteSizeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node currentByteSizeNode = currentByteSizeNodeList.item(0).getChildNodes().item(0);
				containerRegistProfile.currentByteSize = currentByteSizeNode.getNodeValue();
			}
		}

		return containerRegistProfile;
	}
	
	/**
	 * mgmtCmdCreateParse Method
	 * @param responseString
	 * @return mgmtCmdRegistProfile
	 * @throws Exception
	 * Mobius Mashup에 MgmtCmd 생성 후 받은 리턴메시지를 파싱하기 위한 Method 
	 */
	public MgmtCmd mgmtCmdCreateParse(String responseString) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(responseString));
		Document document = builder.parse(xmlSource);
		
		MgmtCmd mgmtCmdRegistProfile = new MgmtCmd();
		
		if (primitiveType) {
			NodeList resourceTypeNodeList = document.getElementsByTagName("resourceType");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("resourceID");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("parentID");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("resourceName");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("creationTime");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lastModifiedTime");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("labels");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.labels = labelsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("expirationTime");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.expirationTime = expirationTimeNode.getNodeValue();
			}
			
			NodeList cmdTypeNodeList = document.getElementsByTagName("cmdType");
			if (cmdTypeNodeList.getLength() > 0 && cmdTypeNodeList.item(0).getChildNodes().getLength() > 0) {
					Node cmdTypeNode = cmdTypeNodeList.item(0).getChildNodes().item(0);
					mgmtCmdRegistProfile.cmdType = cmdTypeNode.getNodeValue();
			}
			
			NodeList execEnableNodeList = document.getElementsByTagName("execEnable");
			if (execEnableNodeList.getLength() > 0 && execEnableNodeList.item(0).getChildNodes().getLength() > 0) {
				Node execEnableNode = execEnableNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.execEnable = execEnableNode.getNodeValue();
			}
			
			NodeList execTargetNodeList = document.getElementsByTagName("execTarget");
			if (execTargetNodeList.getLength() > 0 && execTargetNodeList.item(0).getChildNodes().getLength() > 0) {
				Node execTargetNode = execTargetNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.execTarget = execTargetNode.getNodeValue();
			}
		}
		else {
			NodeList resourceTypeNodeList = document.getElementsByTagName("rty");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("ri");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("pi");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("rn");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("ct");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lt");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("lbl");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.labels = labelsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("et");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.expirationTime = expirationTimeNode.getNodeValue();
			}
			NodeList cmdTypeNodeList = document.getElementsByTagName("cmt");
			if (cmdTypeNodeList.getLength() > 0 && cmdTypeNodeList.item(0).getChildNodes().getLength() > 0) {
					Node cmdTypeNode = cmdTypeNodeList.item(0).getChildNodes().item(0);
					mgmtCmdRegistProfile.cmdType = cmdTypeNode.getNodeValue();
			}
			
			NodeList execEnableNodeList = document.getElementsByTagName("exe");
			if (execEnableNodeList.getLength() > 0 && execEnableNodeList.item(0).getChildNodes().getLength() > 0) {
				Node execEnableNode = execEnableNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.execEnable = execEnableNode.getNodeValue();
			}
			
			NodeList execTargetNodeList = document.getElementsByTagName("ext");
			if (execTargetNodeList.getLength() > 0 && execTargetNodeList.item(0).getChildNodes().getLength() > 0) {
				Node execTargetNode = execTargetNodeList.item(0).getChildNodes().item(0);
				mgmtCmdRegistProfile.execTarget = execTargetNode.getNodeValue();
			}
		}
		
		return mgmtCmdRegistProfile;
	}
	
	/**
	 * execInstanceParse Method
	 * @param responseString
	 * @return mgmtCmdRegistProfile
	 * @throws Exception
	 * Mobius Mashup에 MgmtCmd 생성 후 받은 리턴메시지를 파싱하기 위한 Method 
	 */
	public ExecInstance execInstanceParse(String responseString) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(responseString));
		Document document = builder.parse(xmlSource);
		
		ExecInstance execInstanceProfile = new ExecInstance();
		
		if (primitiveType) {
			NodeList resourceTypeNodeList = document.getElementsByTagName("resourceType");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("resourceID");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("parentID");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("resourceName");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("creationTime");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lastModifiedTime");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("labels");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.labels = labelsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("expirationTime");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.expirationTime = expirationTimeNode.getNodeValue();
			}
			
			NodeList execStatusNodeList = document.getElementsByTagName("execStatus");
			if (execStatusNodeList.getLength() > 0 && execStatusNodeList.item(0).getChildNodes().getLength() > 0) {
				Node execStatusNode = execStatusNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.execStatus = execStatusNode.getNodeValue();
			}
			
			NodeList execTargetNodeList = document.getElementsByTagName("execTarget");
			if (execTargetNodeList.getLength() > 0 && execTargetNodeList.item(0).getChildNodes().getLength() > 0) {
				Node execTargetNode = execTargetNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.execTarget = execTargetNode.getNodeValue();
			}
			
			NodeList execReqArgsNodeList = document.getElementsByTagName("execReqArgs");
			if (execReqArgsNodeList.getLength() > 0 && execReqArgsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node execReqArgsNode = execReqArgsNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.execReqArgs = execReqArgsNode.getNodeValue();
			}
		}
		else {
			NodeList resourceTypeNodeList = document.getElementsByTagName("rty");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList resourceIDNodeList = document.getElementsByTagName("ri");
			if (resourceIDNodeList.getLength() > 0 && resourceIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.resourceID = resourceIDNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("pi");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList resourceNameNodeList = document.getElementsByTagName("rn");
			if (resourceNameNodeList.getLength() > 0 && resourceNameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceNameNode = resourceNameNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.resourceName = resourceNameNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("ct");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lt");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("lbl");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.labels = labelsNode.getNodeValue();
			}
			
			NodeList expirationTimeNodeList = document.getElementsByTagName("et");
			if (expirationTimeNodeList.getLength() > 0 && expirationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node expirationTimeNode = expirationTimeNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.expirationTime = expirationTimeNode.getNodeValue();
			}
			NodeList execStatusNodeList = document.getElementsByTagName("exs");
			if (execStatusNodeList.getLength() > 0 && execStatusNodeList.item(0).getChildNodes().getLength() > 0) {
				Node execStatusNode = execStatusNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.execStatus = execStatusNode.getNodeValue();
			}
			
			NodeList execTargetNodeList = document.getElementsByTagName("ext");
			if (execTargetNodeList.getLength() > 0 && execTargetNodeList.item(0).getChildNodes().getLength() > 0) {
				Node execTargetNode = execTargetNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.execTarget = execTargetNode.getNodeValue();
			}
			
			NodeList execReqArgsNodeList = document.getElementsByTagName("exra");
			if (execReqArgsNodeList.getLength() > 0 && execReqArgsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node execReqArgsNode = execReqArgsNodeList.item(0).getChildNodes().item(0);
				execInstanceProfile.execReqArgs = execReqArgsNode.getNodeValue();
			}
		}
		
		
		return execInstanceProfile;
	}
	
	/**
	 * mgmtCmdRequestParse Method
	 * @param responseString
	 * @return mgmtCmdRegistProfile
	 * @throws Exception
	 * Mobius Mashup에서 MgmtCmd 제어 요청 수신메시지를 파싱하기 위한 Method 
	 */
	public MgmtCmd mgmtCmdRequestParse(String responseString) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(responseString));
		Document document = builder.parse(xmlSource);
		
		MgmtCmd mgmtCmdRegistProfile = new MgmtCmd();
		
		NodeList resourceIDNodeList = document.getElementsByTagName("resourceID");
		Node resourceIDNode = resourceIDNodeList.item(0).getChildNodes().item(0);
		mgmtCmdRegistProfile.resourceID = resourceIDNode.getNodeValue();
		
		NodeList parentIDNodeList = document.getElementsByTagName("parentID");
		Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
		mgmtCmdRegistProfile.parentID = parentIDNode.getNodeValue();
		
		NodeList execTargetNodeList = document.getElementsByTagName("execTarget");
		Node execTargetNode = execTargetNodeList.item(0).getChildNodes().item(0);
		mgmtCmdRegistProfile.execTarget = execTargetNode.getNodeValue();

		NodeList execReqArgsNodeList = document.getElementsByTagName("execReqArgs");
		Node execReqArgsNode = execReqArgsNodeList.item(0).getChildNodes().item(0);
		mgmtCmdRegistProfile.execReqArgs = execReqArgsNode.getNodeValue();
				
		return mgmtCmdRegistProfile;
	}
	
	
	/**
	 * aeCreateParse Method
	 * @param responseString
	 * @return responseString
	 * @throws Exception
	 * Mobius로 oneM2M AE resource Create 요청을 파싱하기 위한  Method
	 */
	public AE aeCreateParse(String responseString) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(responseString));
		Document document = builder.parse(xmlSource);
		
		AE aeRegistProfile = new AE();
		
		if (primitiveType) {
			NodeList resourceTypeNodeList = document.getElementsByTagName("resourceType");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("parentID");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("creationTime");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lastModifiedTime");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("labels");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.labels = labelsNode.getNodeValue();
			}
			
			NodeList nameNodeList = document.getElementsByTagName("appName");
			if (nameNodeList.getLength() > 0 && nameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node nameNode = nameNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.name = nameNode.getNodeValue();
			}
			
			NodeList appIdNodeList = document.getElementsByTagName("App-ID");
			if (appIdNodeList.getLength() > 0 && appIdNodeList.item(0).getChildNodes().getLength() > 0) {
				Node appIdNode = appIdNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.AppID = appIdNode.getNodeValue();
			}
	
			NodeList aeIDNodeList = document.getElementsByTagName("AE-ID");
			if (aeIDNodeList.getLength() > 0 && aeIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node aeIDNode = aeIDNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.AEID = aeIDNode.getNodeValue();
			}
			
			NodeList pointOfAccessNodeList = document.getElementsByTagName("pointOfAccess");
			if (pointOfAccessNodeList.getLength() > 0 && pointOfAccessNodeList.item(0).getChildNodes().getLength() > 0) {
				Node pointOfAccessNode = pointOfAccessNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.pointOfAccess = pointOfAccessNode.getNodeValue();
			}
		}
		else {
			NodeList resourceTypeNodeList = document.getElementsByTagName("rty");
			if (resourceTypeNodeList.getLength() > 0 && resourceTypeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.resourceType = resourceTypeNode.getNodeValue();
			}
			
			NodeList parentIDNodeList = document.getElementsByTagName("pi");
			if (parentIDNodeList.getLength() > 0 && parentIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.parentID = parentIDNode.getNodeValue();
			}
			
			NodeList creationTimeNodeList = document.getElementsByTagName("ct");
			if (creationTimeNodeList.getLength() > 0 && creationTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.creationTime = creationTimeNode.getNodeValue();
			}
			
			NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lt");
			if (lastModifiedTimeNodeList.getLength() > 0 && lastModifiedTimeNodeList.item(0).getChildNodes().getLength() > 0) {
				Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
			}
			
			NodeList labelsNodeList = document.getElementsByTagName("lbl");
			if (labelsNodeList.getLength() > 0 && labelsNodeList.item(0).getChildNodes().getLength() > 0) {
				Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.labels = labelsNode.getNodeValue();
			}
			
			NodeList nameNodeList = document.getElementsByTagName("apn");
			if (nameNodeList.getLength() > 0 && nameNodeList.item(0).getChildNodes().getLength() > 0) {
				Node nameNode = nameNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.name = nameNode.getNodeValue();
			}
			
			NodeList appIdNodeList = document.getElementsByTagName("api");
			if (appIdNodeList.getLength() > 0 && appIdNodeList.item(0).getChildNodes().getLength() > 0) {
				Node appIdNode = appIdNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.AppID = appIdNode.getNodeValue();
			}
	
			NodeList aeIDNodeList = document.getElementsByTagName("aei");
			if (aeIDNodeList.getLength() > 0 && aeIDNodeList.item(0).getChildNodes().getLength() > 0) {
				Node aeIDNode = aeIDNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.AEID = aeIDNode.getNodeValue();
			}
			
			NodeList pointOfAccessNodeList = document.getElementsByTagName("poa");
			if (pointOfAccessNodeList.getLength() > 0 && pointOfAccessNodeList.item(0).getChildNodes().getLength() > 0) {
				Node pointOfAccessNode = pointOfAccessNodeList.item(0).getChildNodes().item(0);
				aeRegistProfile.pointOfAccess = pointOfAccessNode.getNodeValue();
			}
		}
		
		return aeRegistProfile;
	}
	
	/**
	 * applicationDownloadParse Method
	 * @param responseString
	 * @return responseString
	 * @throws Exception
	 * Mobius로부터 수신한 Device Application (AE) Download 요청을 파싱하기 위한  Method
	 */
	public Software applicationDownloadParse(String responseString) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(responseString));
		Document document = builder.parse(xmlSource);
		
		Software softwareDownloadInfo = new Software();
		
		NodeList resourceTypeNodeList = document.getElementsByTagName("resourceType");
		Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.resourceType = resourceTypeNode.getNodeValue();
		
		NodeList parentIDNodeList = document.getElementsByTagName("parentID");
		Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.parentID = parentIDNode.getNodeValue();
		
		NodeList creationTimeNodeList = document.getElementsByTagName("creationTime");
		Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.creationTime = creationTimeNode.getNodeValue();
		
		NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lastModifiedTime");
		Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
		
		NodeList labelsNodeList = document.getElementsByTagName("labels");
		Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.labels = labelsNode.getNodeValue();
		
		NodeList versionNodeList = document.getElementsByTagName("version");
		Node versionNode = versionNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.version = versionNode.getNodeValue();
		
		NodeList nameNodeList = document.getElementsByTagName("name");
		Node nameNode = nameNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.name = nameNode.getNodeValue();
		
		NodeList URLNodeList = document.getElementsByTagName("URL");
		Node URLNode = URLNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.url = URLNode.getNodeValue();
		
		NodeList installNodeList = document.getElementsByTagName("install");
		Node installNode = installNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.install = installNode.getNodeValue();
		
		NodeList uninstallNodeList = document.getElementsByTagName("uninstall");
		Node uninstallNode = uninstallNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.uninstall = uninstallNode.getNodeValue();
		
		return softwareDownloadInfo;
	}
	
	/**
	 * aeCreateParse Method
	 * @param responseString
	 * @return responseString
	 * @throws Exception
	 * Mobius로 oneM2M Software resource Create 요청에 대한 응답을 파싱하기 위한  Method
	 */
	public Software softwareCreateParse(String responseString) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource xmlSource = new InputSource();
		xmlSource.setCharacterStream(new StringReader(responseString));
		Document document = builder.parse(xmlSource);
		
		Software softwareDownloadInfo = new Software();
		
		NodeList resourceTypeNodeList = document.getElementsByTagName("resourceType");
		Node resourceTypeNode = resourceTypeNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.resourceType = resourceTypeNode.getNodeValue();
		
		NodeList resourceIdNodeList = document.getElementsByTagName("resourceID");
		Node resourceIdNode = resourceIdNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.resourceID = resourceIdNode.getNodeValue();
		
		NodeList parentIDNodeList = document.getElementsByTagName("parentID");
		Node parentIDNode = parentIDNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.parentID = parentIDNode.getNodeValue();
		
		NodeList creationTimeNodeList = document.getElementsByTagName("creationTime");
		Node creationTimeNode = creationTimeNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.creationTime = creationTimeNode.getNodeValue();
		
		NodeList lastModifiedTimeNodeList = document.getElementsByTagName("lastModifiedTime");
		Node lastModifiedTimeNode = lastModifiedTimeNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.lastModifiedTime = lastModifiedTimeNode.getNodeValue();
		
		NodeList labelsNodeList = document.getElementsByTagName("labels");
		Node labelsNode = labelsNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.labels = labelsNode.getNodeValue();
		
		NodeList versionNodeList = document.getElementsByTagName("version");
		Node versionNode = versionNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.version = versionNode.getNodeValue();
		
		NodeList nameNodeList = document.getElementsByTagName("name");
		Node nameNode = nameNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.name = nameNode.getNodeValue();
		
		NodeList installNodeList = document.getElementsByTagName("install");
		Node installNode = installNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.install = installNode.getNodeValue();
		
		NodeList uninstallNodeList = document.getElementsByTagName("uninstall");
		Node uninstallNode = uninstallNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.uninstall = uninstallNode.getNodeValue();
		
		NodeList installStatusNodeList = document.getElementsByTagName("installStatus");
		Node installStatusNode = installStatusNodeList.item(0).getChildNodes().item(0);
		softwareDownloadInfo.installStatus = installStatusNode.getNodeValue();
		
		return softwareDownloadInfo;
	}
}