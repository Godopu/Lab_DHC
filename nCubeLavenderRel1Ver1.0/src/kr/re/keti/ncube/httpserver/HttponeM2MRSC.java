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

/**
 * Data class for oneM2M HTTP response status codes.
 * @author NakMyoung Sung (nmsung@keti.re.kr)
 */
public class HttponeM2MRSC {
	
	public static final String ACCEPTED = "1000";
	public static final String CREATED = "2001";
	public static final String CONFLICT = "2101";
	public static final String BAD_REQUEST = "4000";
	public static final String NOT_FOUND = "4004";
	public static final String OPERATION_NOT_ALLOWED = "4005";
	public static final String REQUEST_TIMEOUT = "4008";
	public static final String SUBSCRIPTION_CREATOR_HAS_NO_PRIVILEGE = "4101";
	public static final String CONTENTS_UNACCEPTABLE = "4102";
	public static final String ACCESS_DENIED = "4103";
	public static final String GROUP_REQUEST_IDENTIFIER_EXISTS = "4104";
	public static final String INTERNAL_SERVER_ERROR = "5000";
	public static final String NOT_IMPLEMENTED = "5001";
	public static final String TARGET_NOT_REACHABLE = "5103";
	public static final String NO_PRIVILEGE = "5105";
	public static final String ALREADY_EXISTS = "5106";
	public static final String TARGET_NOT_SUBSCRIBABLE = "5203";
	public static final String SUBSCRIPTION_VERIFICATION_INITIATION_FAILED = "5204";
	public static final String SUBSCRIPTION_HOST_HAS_NO_PRIVILEGE = "5205";
	public static final String NON_BLOCKING_REQUEST_NOT_SUPPORTED = "5206";
	public static final String EXTERNAL_OBJECT_NOT_REACHABLE = "6003";
	public static final String EXTERNAL_OBJECT_NOT_FOUND = "6005";
	public static final String MAX_NUMBER_OF_MEMBER_EXCEEDED = "6010";
	public static final String MEMBER_TYPE_INCONSISTENT = "6011";
	public static final String MANAGEMENT_SESSION_CANNOT_BE_ESTABLISHED = "6020";
	public static final String MANAGEMENT_SESSION_ESTABLISHMENT_TIMEOUT = "6021";
	public static final String INVALID_CMDTYPE = "6022";
	public static final String INVALID_ARGUMENTS = "6023";
	public static final String INSUFFICIENT_ARGUMENT = "6024";
	public static final String MGMT_CONVERSION_ERROR = "6025";
	public static final String CANCELLATION_FAILED = "6026";
	public static final String ALREADY_COMPLETE = "6028";
	public static final String COMMAND_NOT_CANCELLABLE = "6029";
}