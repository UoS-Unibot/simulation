package org.evors.rs.ui.sandpit;

/**
 * The Sandpit package provides a full visualiser for the simulation. The
 * SandPitRenderer renders the simulation on an abstract SandPitCanvas; this
 * provides a controllable orthogonal camera with gridlines. SandPitControls
 * provides a simple (Netbeans IDE generated) form for controlling the
 * simulation playback via a VisualiserListener.
 *
 * The abstract SandPitCanvas is implemented in two canvases: the WorldViewer,
 * which simply displays a world e.g. for previewing, whilst the TrialViewer
 * actually controls and renders a full simulation. The SimulationViewer is
 * essentially a JPanel wrapper for it; use this class for custom GUI forms.
 * Look at the JFrame VisualiserTest for an example of how this can be done.
 *
 */
