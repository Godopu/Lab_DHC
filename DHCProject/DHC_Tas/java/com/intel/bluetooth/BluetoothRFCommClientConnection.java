package com.intel.bluetooth;

import java.io.IOException;

public class BluetoothRFCommClientConnection extends BluetoothRFCommConnection {
	public BluetoothRFCommClientConnection(BluetoothStack bluetoothStack, BluetoothConnectionParams params)
			throws IOException {
		super(bluetoothStack, bluetoothStack.connectionRfOpenClientConnection(params));
		boolean initOK = false;
		try {
			this.securityOpt = bluetoothStack.rfGetSecurityOpt(this.handle,
					Utils.securityOpt(params.authenticate, params.encrypt));
			RemoteDeviceHelper.connected(this);
			initOK = true;
		} finally {
			if (!initOK) {
				try {
					bluetoothStack.connectionRfCloseClientConnection(this.handle);
				} catch (IOException e) {
					DebugLog.error("close error", e);
				}
			}
		}
	}

	void closeConnectionHandle(long handle) throws IOException {
		RemoteDeviceHelper.disconnected(this);
		bluetoothStack.connectionRfCloseClientConnection(handle);
	}
}
