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

package kr.re.keti.ncube;

/**
 * Data class for oneM2M node/deviceInfo resource.
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 */
public class DeviceInfo {
	
	public String resourceType = null;
	public String resourceID = null;
	public String resourceName = null;
	public String parentID = null;
	public String creationTime = null;
	public String lastModifiedTime = null;
	public String expirationTime = null;
	public String mgmtDefinition = null;
	public String labels = null;
	public String deviceLabel = null;
	public String description = null;
	public String manufacturer = null;
	public String model = null;
	public String deviceType = null;
	public String fwVersion = null;
	public String swVersion = null;
	public String hwVersion = null;
	
	public String objectIDs = null;
	public String objectPaths = null;
}