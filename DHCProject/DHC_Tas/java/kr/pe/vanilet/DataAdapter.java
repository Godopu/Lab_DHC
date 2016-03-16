package kr.pe.vanilet;

import java.io.IOException;

import kr.pe.vanilet.dim.SimpleDimBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.keti.tas.soft.AdaptorInfomation;
import com.keti.tas.soft.CubeTasClient;
import com.keti.tas.soft.CubeTasServer;
import com.keti.tas.soft.MsgReceiveEvent;
import com.keti.tas.soft.MsgReceiveListener;
import com.keti.tas.soft.ThingData;
import com.keti.tas.soft.ThingInformation;
import com.keti.tas.soft.ThingTasConnector;
import com.keti.tas.soft.ThingsManagement;

public class DataAdapter {
    private static final Log log = LogFactory.getLog(DataAdapter.class);    
    
    public static void main(String argString[]) throws IOException, InterruptedException{
        
        final CubeTasServer cubeServer = new CubeTasServer();
        final ThingTasConnector thingServer = new ThingTasConnector();
        
        cubeServer.start();
        thingServer.start();
  
        AdaptorInfomation adaptor = new AdaptorInfomation(cubeServer.getServerPort());
        ThingsManagement manager = new ThingsManagement(adaptor);
            
        manager.addThing("HR", "sensoractor", "HR");
        log.info("The thing \"HR\" added.");
        
        manager.addThing("STRESS", "sensoractor", "STRESS");
        log.info("The thing \"STRESS\" added.");
        
        // Thing -> nCube
        thingServer.addReceiveListener(new MsgReceiveListener() {
        	
            public void receiveMsgEvent(MsgReceiveEvent event) {
            	String strData = event.getMessage();
            	
        		Gson gson = new Gson();
        		
        		SensorResource resource = gson.fromJson(strData, SensorResource.class);
        		System.out.println("resource data : " + resource.getStress() + " , " + resource.getHR());
        		log.info("Message [Thing -> TAS] received.\n" + resource);
        		
        		if(resource != null) {
        			ThingInformation thing = ThingsManagement.getThingIDByName("HR");
        			if(thing != null && thing.getRegistStatus()) {
        				ThingData thingData = new ThingData();
        				
        				try {
        					SimpleDimBuilder dimBuilder = new SimpleDimBuilder();
        					dimBuilder.createEntryElement("544", "HR", "Basic-Nu-Observed-Value", "float", "" + resource.getHR());
//        					
        					new CubeTasClient().upload(thingData.makeThingDataMsg(thing.getThingName(), "HR", dimBuilder.saveToBase64String()));
						} catch (IOException e) {
							log.error("uploading humidity to Mobius failed.", e);
						}
        			}
        			
        			thing = ThingsManagement.getThingIDByName("STRESS");
        			if(thing != null && thing.getRegistStatus()) {
        				ThingData thingData = new ThingData();
        				
        				try {
        					SimpleDimBuilder dimBuilder = new SimpleDimBuilder();
        					dimBuilder.createEntryElement("544", "%", "Basic-Nu-Observed-Value", "float", "" + resource.getStress());
        					
        					new CubeTasClient().upload(thingData.makeThingDataMsg(thing.getThingName(), "STRESS", dimBuilder.saveToBase64String()));
						} catch (IOException e) {
							log.error("uploading humidity to Mobius failed.", e);
						}
        			}
        		}
        		else
        			log.warn("Resource is null");
            }
        });
        
//     // Thing -> nCube
//        thingServer.addReceiveListener(new MsgReceiveListener() {
//        	
//            @Override
//            public void receiveMsgEvent(MsgReceiveEvent event) {
//            	String strData = event.getMessage();
//            	
//        		Gson gson = new Gson();
//        		
//        		SensorResource resource = gson.fromJson(strData, SensorResource.class);
//        		
//        		log.info("Message [Thing -> TAS] received.\n" + resource);
//        		
//        		if(resource != null) {
//        			ThingInformation thing = ThingsManagement.getThingIDByName("temperature");
//        			if(thing != null && thing.getRegistStatus()) {
//        				ThingData thingData = new ThingData();
//        				
//        				try {
//        					SimpleDimBuilder dimBuilder = new SimpleDimBuilder();
//        					dimBuilder.createEntryElement("544", "℃", "Basic-Nu-Observed-Value", "float", "" + resource.getTemperature());
//        					
//							new CubeTasClient().upload(thingData.makeThingDataMsg(thing.getThingName(), "temperature", dimBuilder.saveToBase64String()));
//						} catch (IOException e) {
//							log.error("uploading temperature to Mobius failed.", e);
//						}
//        			}
//        			
//        			thing = ThingsManagement.getThingIDByName("humidity");
//        			if(thing != null && thing.getRegistStatus()) {
//        				ThingData thingData = new ThingData();
//        				
//        				try {
//        					SimpleDimBuilder dimBuilder = new SimpleDimBuilder();
//        					dimBuilder.createEntryElement("544", "%", "Basic-Nu-Observed-Value", "float", "" + resource.getHumidity());
//        					
//        					new CubeTasClient().upload(thingData.makeThingDataMsg(thing.getThingName(), "humidity", dimBuilder.saveToBase64String()));
//						} catch (IOException e) {
//							log.error("uploading humidity to Mobius failed.", e);
//						}
//        			}
//        		}
//        		else
//        			log.warn("Resource is null");
//            }
//        });
        //Add a receive listener to CubeServer
        // nCube -> Thing
        cubeServer.addReceiveListener(new MsgReceiveListener() { 

            public void receiveMsgEvent(MsgReceiveEvent event) {
            	log.info("Control command[nCube -> TAS] received.\nMSg [" + event.getMessage() + "]");
            }
        });
    }
}
