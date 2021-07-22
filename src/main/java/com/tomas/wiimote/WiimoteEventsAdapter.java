package com.tomas.wiimote;

import wiiusej.wiiusejevents.physicalevents.ExpansionEvent;
import wiiusej.wiiusejevents.physicalevents.IREvent;
import wiiusej.wiiusejevents.physicalevents.MotionSensingEvent;
import wiiusej.wiiusejevents.physicalevents.WiimoteButtonsEvent;
import wiiusej.wiiusejevents.utils.WiimoteListener;
import wiiusej.wiiusejevents.wiiuseapievents.*;

public interface WiimoteEventsAdapter extends WiimoteListener {
	@Override
	default void onButtonsEvent(WiimoteButtonsEvent wiimoteButtonsEvent) {
	}

	@Override
	default void onIrEvent(IREvent irEvent) {
	}

	@Override
	default void onMotionSensingEvent(MotionSensingEvent motionSensingEvent) {
	}

	@Override
	default void onExpansionEvent(ExpansionEvent expansionEvent) {
	}

	@Override
	default void onStatusEvent(StatusEvent statusEvent) {
	}

	@Override
	default void onDisconnectionEvent(DisconnectionEvent disconnectionEvent) {
	}

	@Override
	default void onNunchukInsertedEvent(NunchukInsertedEvent nunchukInsertedEvent) {
	}

	@Override
	default void onNunchukRemovedEvent(NunchukRemovedEvent nunchukRemovedEvent) {
	}

	@Override
	default void onGuitarHeroInsertedEvent(GuitarHeroInsertedEvent guitarHeroInsertedEvent) {
	}

	@Override
	default void onGuitarHeroRemovedEvent(GuitarHeroRemovedEvent guitarHeroRemovedEvent) {
	}

	@Override
	default void onClassicControllerInsertedEvent(ClassicControllerInsertedEvent classicControllerInsertedEvent) {
	}

	@Override
	default void onClassicControllerRemovedEvent(ClassicControllerRemovedEvent classicControllerRemovedEvent) {
	}
}
