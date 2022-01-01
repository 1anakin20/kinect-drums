package com.tomas.wiimote;

import com.github.awvalenti.wiiusej.WiiusejNativeLibraryLoadingException;
import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WiimoteManager extends WiimoteEventsAdapter {
	private final List<WiimoteLifeCycleEvents> listeners = new ArrayList<>();
	private WiiUseApiManager wiiUseApiManager;
	private ExecutorService executorService;
	private Future<?> lookForWiimotes;

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
		executorService = Executors.newSingleThreadExecutor();
		Runnable lookForWiimotesRunnable = () -> {
			Wiimote[] wiimotes;
//			do {
//				wiimotes = wiiUseApiManager.getWiimotes(number);
//				System.out.println("connectedWiimotes = " + Arrays.toString(wiimotes));
//				if (wiimotes.length != number) {
//					wiiUseApiManager.shutdown();
//				} else {
//					break;
//				}
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			} while (true);

			boolean foundWiimotes = false;
			while (!foundWiimotes) {
				System.out.println("Looking for wiimotes to connect");
				wiimotes = wiiUseApiManager.getWiimotes(2);
				if (wiimotes.length == number) {
					System.out.println("Got " + number);
					connected(wiimotes);
					foundWiimotes = true;
				} else {
					wiiUseApiManager.shutdown();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		};

		lookForWiimotes = executorService.submit(lookForWiimotesRunnable);
	}

	public void shutdown() {
		lookForWiimotes.cancel(true);
		executorService.shutdown();
		wiiUseApiManager.definitiveShutdown();
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
}
