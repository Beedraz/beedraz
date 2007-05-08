package be.ac.kuleuven.cs.ips2.project2002.logicCircuits;


import java.util.EventListener;


/**
 * ValueChangedListener instances can be registered with a
 * {@link LogicCircuit}. They will be notified when the boolean value
 * ({@link LogicCircuit#getValue()}) of the logic circuit instance
 * changes with a {@link ValueChangedEvent}.
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
public interface ValueChangedListener extends EventListener {

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
	    /*<indoc name="CVS Revision Author">*/"$Author$"/*</indoc>*/;

  /*</section>*/


  /**
   * @param vce
   *        The {@link ValueChangedEvent} carrying information about the
   *        value change <this>this</this> is being notified about.
   *
   * @pre | vce != null;
   */    
  public void valueChanged(ValueChangedEvent vce);

}