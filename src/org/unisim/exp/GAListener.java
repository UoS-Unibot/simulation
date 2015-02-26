package org.unisim.exp;

import org.unisim.genesis.Stats;

/**
 * Is notified by the GA of certain events, such as updating output with stats
 * or finishing.
 *
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public interface GAListener {

    public void GAupdateSummary(Stats stats);

    public void GAFinished();
}
