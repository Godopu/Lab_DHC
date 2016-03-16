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

import java.util.ArrayList;

/**
 * Data class for oneM2M container resource.
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 */
public class Container {

	public String resourceType = null;
	public String resourceID = null;
	public String resourceName = null;
	public String parentID = null;
	public String parentName = null;
	public String creationTime = null;
	public String lastModifiedTime = null;
	public String labels = null;
	public String expirationTime = null;
	public String announceTo = null;
	public String announcedAttribute = null;
	public String stateTag = null;
	public String creator = null;
	public String maxNrOfInstances = null;
	public String maxByteSize = null;
	public String maxInstanceAge = null;
	public String currentNrOfInstances = null;
	public String currentByteSize = null;
	public String uploadCondition = null;
	public String uploadConditionValue = null;
	public String containerType = null;
	public String heartbeatPeriod = null;
	
	public ArrayList<Object> contentInstance = new ArrayList<Object>();
}