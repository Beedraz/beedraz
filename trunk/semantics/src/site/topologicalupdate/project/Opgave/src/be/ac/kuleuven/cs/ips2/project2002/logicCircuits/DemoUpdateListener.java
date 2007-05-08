package be.ac.kuleuven.cs.ips2.project2002.logicCircuits;


import java.util.EventListener;


/**
 * Instances of this type are warned by the {@link LogicCircuit} they
 * are registered with when it begins the proces of changing its boolean
 * value, and when it ends that process. This is for demonstration purposes.
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
public interface DemoUpdateListener extends EventListener {

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


  /**
   * This method will be called by the {@link LogicCircuit} <this>this</this>
   * is registered with when it begins the proces of changing its boolean
   * value.
   *
   * @pre | updatingCircuit != null;
   */
  public void valueUpdateStarted(LogicCircuit updatingCircuit);

  /**
   * This method will be called by the {@link LogicCircuit} <this>this</this>
   * is registered with when it completes the proces of changing its boolean
   * value.
   *
   * @pre | updatingCircuit != null;
   */
  public void valueUpdateCompleted(LogicCircuit updatedCircuit);

}