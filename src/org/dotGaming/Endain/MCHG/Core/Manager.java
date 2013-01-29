package org.dotGaming.Endain.MCHG.Core;

// Force all managers to implement these functions for easy management
public interface Manager {
	// This function should always be called after instantiating a manger.
	// It should return TRUE on success and if FALSE is returned the plugin
	// should shutdown gracefully.
	boolean load();
	
	// This function will be called upon the initialization of a new game.
	void reset();
	
	// This function will be called when a command is received to stop the
	// plugin.
	void kill();
}
