package kr.re.keti.ncube.interactionmanager;

public class InteractionMQTTRequest {
	
	@SuppressWarnings("unused")
	private String INCSEAddress = null;
	@SuppressWarnings("unused")
	private boolean interopType = false;
	@SuppressWarnings("unused")
	private boolean debugPrint = false;
	
	public InteractionMQTTRequest (String inCSEIP, boolean interopType, boolean debugPrint) {
		this.INCSEAddress = inCSEIP;
		this.interopType = interopType;
		this.debugPrint = debugPrint;
	}
}