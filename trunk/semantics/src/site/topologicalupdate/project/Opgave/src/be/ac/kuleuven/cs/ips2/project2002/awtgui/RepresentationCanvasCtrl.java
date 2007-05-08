package be.ac.kuleuven.cs.ips2.project2002.awtgui;


import be.ac.kuleuven.cs.ips2.project2002.awtgui.RepresentationCanvas;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;


/**
 * A JPanel that features controls for the demo mode of a
 * {@link RepresentationCanvas}.
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
public class RepresentationCanvasCtrl extends JPanel {

	/*<section name="Meta Information">*/
  //------------------------------------------------------------------
  //------------------------------------------------------------------

	static final public String CVS_SOURCE =
	    /*<indoc name="CVS Source">*/"$Source$"/*</indoc>*/;
	static final public String CVS_REVISION =
	    /*<indoc name="CVS Revision">*/"$Revision$"/*</indoc>*/;
	static final public String CVS_DATE =
	    /*<indoc name="CVS Date">*/"$Date$"/*</indoc>*/;
	static final public String CVS_STATE =
	    /*<indoc name="CVS State">*/"$State$"/*</indoc>*/;
	static final public String CVS_TAG =
	    /*<indoc name="CVS Tag">*/"$Name$"/*</indoc>*/;
	static final public String CVS_AUTHOR =
	    /*<indoc name="CVS Revision Author">*/"$Author: jand $"/*</indoc>*/;

  /*</section>*/


  public RepresentationCanvasCtrl(RepresentationCanvas rc) {
    $representationCanvas = rc;
    $representationCanvas.setDemoModeDelay(DELAY_INITIAL);
    
    $label = new JLabel("Demo Mode");
    $label.setHorizontalAlignment(JLabel.LEFT);

    $enabled = new JCheckBox("Enabled", false);
    $enabled.addChangeListener($enabledChangedListener);

    $delayPanel =
        new JPanel() {
              public Dimension getMinimumSize() {
                return new Dimension(100, 400);
              }
              public Dimension getPreferredSize() {
                return new Dimension(100, 400);
              }
            };
    $delayPanel.setLayout(new BorderLayout());
    $delayLabel = new JLabel("Delay");
    $delayLabel.setHorizontalAlignment(JLabel.LEFT);
    $delay = new JSlider(JSlider.VERTICAL, 0, DELAY_MAX, DELAY_INITIAL);
    $delay.setMajorTickSpacing(DELAY_MAJOR_TICKS);
    $delay.setMinorTickSpacing(DELAY_MINOR_TICKS);
    $delay.setSnapToTicks(false);
    $delay.setPaintTicks(true);
    $delay.setExtent(DELAY_EXTENT);
    $delay.setLabelTable(
        $delay.createStandardLabels(DELAY_LABEL_UNIT));
    $delay.setPaintLabels(true);
    $delay.setPaintTrack(true);
    $delay.addChangeListener($delayChangedListener);
    $delayPanel.setEnabled(false);
    $delayLabel.setEnabled(false);
    $delay.setEnabled(false);
	  $delayPanel.add($delayLabel, BorderLayout.NORTH);
	  $delayPanel.add($delay, BorderLayout.CENTER);

    setLayout(new BorderLayout());
	  add($label, BorderLayout.NORTH);
	  add($enabled, BorderLayout.CENTER);
	  add($delayPanel, BorderLayout.SOUTH);
	  
	  setBorder(new CompoundBorder(
	                    new EmptyBorder(20, 20, 20, 20),
                      new CompoundBorder(
                              new EtchedBorder(),
                              new EmptyBorder(10, 10, 10, 10))));
  }
  
  private RepresentationCanvas $representationCanvas;

  private JLabel $label;
  
  private JCheckBox $enabled;
  private ChangeListener $enabledChangedListener =
      new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
              $delayPanel.setEnabled($enabled.isSelected());
              $delayLabel.setEnabled($enabled.isSelected());
              $delay.setEnabled($enabled.isSelected());
              $representationCanvas.setInDemoMode($enabled.isSelected());
            }
          };


  private JPanel $delayPanel;
  private JLabel $delayLabel;
  private JSlider $delay;
  private ChangeListener $delayChangedListener =
      new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
              $representationCanvas.setDemoModeDelay($delay.getValue());
            }
          };

  static private int DELAY_MAX = 5500;
    // JDJDJD swing bug? seems to be necessary to get real max of 5000
  static private int DELAY_INITIAL = 250;
  static private int DELAY_MAJOR_TICKS = 1000;
  static private int DELAY_MINOR_TICKS = 500;
  static private int DELAY_EXTENT = DELAY_INITIAL;
    // JDJDJD swing bug? 500 acts as 550
  static private int DELAY_LABEL_UNIT = 1000;
  
}