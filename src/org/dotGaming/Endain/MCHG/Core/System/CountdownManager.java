package org.dotGaming.Endain.MCHG.Core.System;

import org.dotGaming.Endain.MCHG.Core.Game;
import org.dotGaming.Endain.MCHG.Core.Manager;

public class CountdownManager implements Manager{
	private Game g;
	private int retries;
	private int announcements;
	private int countdown;
	
	public CountdownManager(Game g) {
		this.g = g;
	}
	
	@Override
	public boolean load() {
		// Set default values
		this.retries = 4;
		this.announcements = 2;
		this.countdown = 5;
		return true;
	}
	
	@Override
	public void reset() {
		// Restore default values
		this.retries = 4;
		this.announcements = 2;
		this.countdown = 5;
	}
	
	@Override
	public void kill() {
		// Nothing to do for now
	}
	
	class Announce implements Runnable {
		@Override
		public void run() {
			// TODO
		}
	}
	
	class Countdown implements Runnable {
		@Override
		public void run() {
			// TODO
		}
	}
}
