package com.tomas.wiimote;

import wiiusej.Wiimote;

public interface WiimoteLifeCycleEvents {
	void connected(Wiimote[] wiimotes);

	void disconnected();
}