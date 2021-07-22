package com.tomas.wiimote;

import com.github.awvalenti.wiiusej.WiiusejNativeLibraryLoadingException;
import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;
import wiiusej.wiiusejevents.wiiuseapievents.DisconnectionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WiimoteManager implements WiimoteEventsAdapter {
	private final List<WiimoteLifeCycleEvents> listeners = new ArrayList<>();
	private WiiUseApiManager wiiUseApiManager;

	public WiimoteManager() {
		try {
			wiiUseApiManager = new WiiUseApiManager();
		} catch (WiiusejNativeLibraryLoadingException e) {
			System.err.println("Wii API manager could not be loaded");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void startLookingForWiimotes(int number) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Runnable lookForWiimotes = () -> {
			Wiimote[] wiimotes;
			do {
				wiimotes = wiiUseApiManager.getWiimotes(number);
				System.out.println("connectedWiimotes = " + Arrays.toString(wiimotes));
				if (wiimotes.length != number) {
					wiiUseApiManager.shutdown();
				} else {
					break;
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (true);
			connected(wiimotes);
		};

		executorService.execute(lookForWiimotes);
	}

	public void registerListener(WiimoteLifeCycleEvents listener) {
		listeners.add(listener);
	}

	public void removeListener(WiimoteLifeCycleEvents listener) {
		listeners.remove(listener);
	}

	public void connected(Wiimote[] wiimotes) {
		for (WiimoteLifeCycleEvents listener : listeners) {
			listener.connected(wiimotes);
		}
	}

	// TODO do I need this? make sure it is a wiimote I care about disconnecting?
	@Override
	public void onDisconnectionEvent(DisconnectionEvent disconnectionEvent) {
		for (WiimoteLifeCycleEvents listener : listeners) {
			listener.disconnected();
		}
	}
}
