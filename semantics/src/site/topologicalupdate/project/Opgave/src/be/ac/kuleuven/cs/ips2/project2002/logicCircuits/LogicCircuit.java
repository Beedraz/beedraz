package be.ac.kuleuven.cs.ips2.project2002.logicCircuits;


import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;


/**
 * <p>LogicCircuit instances represent electronic logic circuits. Their
 *   main property is that they have a boolen value ({@link #getValue()}).
 *   When the value changes, instances of this type send
 *   {@link ValueChangedEvent}s to registered
 *   {@link ValueChangedListener}s.</p>
 * <p>To make it easier to identify logic circuit instances in a user
 *   interface, all logic circuit instances have a String name
 *   {@link #getName()}.</p>
 * <p>2 kinds of logic circuits are discerned: <def>autonomous</def>
 *   logic circuits and derived logic circuits, or <def>logic gates</def>.
 *   Autonomous logic circuits get their boolean value from some external
 *   source. The value of gates depend on the value of other
 *   logic circuits. As a consequence, a change in the boolean value of
 *   a given logic circuit might result in a change of the boolean
 *   value of its dependent logic gates, recursively. In general, any
 *   logic circuit thus needs to notify its dependents of a change in
 *   its boolean value, so they can calculate the effect of that change on
 *   their own boolean value, and change it if necessary. The dependency
 *   graph is acyclic. This is guaranteed by a precondition of
 *   {@link #addDependentGate(LogicGate)}.</p>
 * <p>This class offers final support for the name property, the boolean
 *   value property, and administration and notification of the value
 *   changed listeners and dependents.<br>
 *   {@link #setValue(boolean)} changes the actual value of this logic
 *   circuit instance, taking care of all necessary notifications.
 *   This method is protected, because some subtypes, more specifically
 *   gates, cannot offer this functionality publicly. Their boolean value
 *   is under their control, and should not be mutable by a user.</p>
 * <p>For demonstration purposes, instances of this type are required to
 *   notify interested {@link DemoUpdateListener}s when they start contemplating
 *   a change in the local boolean value, and when this process is completed
 *   (whether the value was actually changed or not). This class offers final
 *   support for the administration of those listeners.</p>
 *
 * @invar | getName() != null;
 * @invar | ! getName().equals("");
 * @invar | getValueChangedListeners() != null;
 * @invar | for each Object o:
 *            getValueChangedListeners().contains(o) ==> o != null;
 * @invar | for each Object o:
 *            getValueChangedListeners().contains(o) ==>
 *               o instanceof ValueChangedListener;
 * @invar | getDemoUpdateListeners() != null;
 * @invar | for each Object o:
 *            getDemoUpdateListeners().contains(o) ==> o != null;
 * @invar | for each Object o:
 *            getDemoUpdateListeners().contains(o) ==>
 *               o instanceof DemoUpdateListener;
 * @invar | getDependentGates() != null;
 * @invar | for each Object o:
 *            getDependentGates().contains(o) ==> o != null;
 * @invar | for each Object o:
 *            getDependentGates().contains(o) ==>
 *               o instanceof LogicGate;
 * @invar The graph of autonumous logic circuits and there dependents
 *        is acyclic.
 *        | ! this.isDependent(this);
 * @invar When the boolean value of <this>this</this> changes, registered
 *        ValueChangedListeners need to be notified.
 * @invar When <this>this</this> is starting a change of its boolean value,
 *        registered DemoUpdateListeners need to be notified. 
 * @invar When <this>this</this> contemplates a change of its boolean value,
 *        registered DemoUpdateListeners need to be notified.
 * @invar When <this>this</this> finishes contemplating a change of its boolean value,
 *        registered DemoUpdateListeners need to be notified.
 *
 * @version $Revision$
 * @author  Jan Dockx
 */
abstract public class LogicCircuit {
  
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


  /*<construction>*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  /**
   * @param name
   *        A string used to identify this logic circuit.
   * @param initialValue
   *        The initial value for the boolean value of the new instance.
   *
   * @pre | name != null;
   * @pre | ! name.equals("");
   * @post | new.getName().equals(name);
   * @post | new.getValue() == initialValue;
   * @post | new.getValueChangedListeners().isEmpty();
   * @post | new.getDemoUpdateListeners().isEmpty();
   * @post | new.getDependentGates().isEmpty();
   */
  public LogicCircuit(String name, boolean initialValue) {
    $name = name;
    $value = initialValue;
  }

  /*</construction>*/



  /*<property name="Name">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------
  
  final public String getName() {
    return $name;
  }

  private String $name;
  
  /*</property>*/



  /*<property name="Value">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------
  
  final public boolean getValue() {
    return $value;
  }

  /**
   * This logic circuit has the same value as <formal-arg>other</formal-arg>.
   * We explicitly do not use {@link java.lang.Object#equals(Object)} for
   * this. Instances of this class have to work with referential semantics
   * in different places, such as collections. Logic circuits with the
   * same boolean value are not necessarily equal.
   *
   * @result | getValue() == other.getValue();
   */
  final public boolean hasSameValue(LogicCircuit other) {
    return getValue() == other.getValue();
  }

  /**
   * <p>Set the boolean value of <this>this</this> and do necessary notifications.</p>
   * <p>If <formal-arg>value</formal-arg> is not different from the current
   *   value of {@link #getValue()}, nothing happens.</p>
   *
   * @param value
   *        The new value of this logic circuit.
   *
   * @post | getValue() == value;
   * @post listeners are warned ...
   * @post dependents are warned ...
   */
  final protected void setValue(boolean value) {
    if ($value != value) {
      $value = value;
      notifyValueChangedListeners(new ValueChangedEvent(this, $value));
    }
  }
  
  private boolean $value;


  /*<section name="Events">*/
  //-------------------------------------------------------------------

  /**
   * Returns the set of registered ValueChangedListeners.
   */
  final public Set getValueChangedListeners() {
    return (Set)$valueChangedListeners.clone();
  }
  
  /**
   * <formal-arg>listener</formal-arg> is added to the set of objects to
   * be notified when the boolean value of <this>this</this> changes.
   *
   * @param listener
   *        The object to be notified of a change of our boolean value.
   * @post  <formal-arg>listener</formal-arg> is in the set of objects to
   *        be notified when the boolean value of this object changes.
   *      | (listener != null) ==>
   *            new.getValueChangedListeners().contains(listener);
   */
  final public void addValueChangedListener(ValueChangedListener listener) {
    if (listener != null) {
      $valueChangedListeners.add(listener);
    }
  }
  
  /**
   * <formal-arg>listener</formal-arg> is removed from the set of objects to
   * be notified when the boolean value of <this>this</this> changes.
   *
   * @param listener
   *        The object no longer to be notified of a change of our boolean value.
   * @post  <formal-arg>listener</formal-arg> is not in the set of objects to
   *        be notified when the boolean value of this object changes.
   *      | ! new.getValueChangedListeners().contains(listener);
   */
  final public void removeValueChangedListener(ValueChangedListener listener) {
    $valueChangedListeners.remove(listener);
  }
  
  /**
   * Notify all registered ValueChangedListeners about a boolean value change,
   * described in <formal-arg>event</formal-arg>.
   *
   * @param event
   *        The event object describing the change that occured to
   *        the <property>.
   * @pre | event != null;
   * @post The method boolean valueChanged is called with
   *       <formal-arg>event</formal-arg> on all registered
   *       ValueChangedListeners.
   *     | for each ValueChangedListener l:
   *         $valueChangedListeners.contains(l) ==>
   *            (new l).valueChanged(event) is called;
   */
  private void notifyValueChangedListeners(ValueChangedEvent event) {
    Iterator iter = $valueChangedListeners.iterator();
    while(iter.hasNext()) {
      ((ValueChangedListener)iter.next()).valueChanged(event);
    }  
  }
  
  /**
   * The set of boolean value changed listeners.
   *
   * @invar This variable references an effective set.
   *      | $valueChangedListeners != null;
   * @invar All elements are effective.
   *      | for each Object o:
   *          $valueChangedListeners.contains(o) ==> o != null;
   * @invar All elements are ValueChangedListeners.
   *      | for each Object o:
   *          $valueChangedListeners.contains(o) ==>
   *            o instanceof ValueChangedListener;
   */
  private HashSet $valueChangedListeners = new HashSet();
  
  /*</section>*/



  /*<section name="Demo Update Listeners">*/
  //-------------------------------------------------------------------
  // for demonstration purposes only
 
  /**
   * Returns the set of registered DemoUpdateListeners.
   */
  final public Set getDemoUpdateListeners() {
    return (Set)$demoUpdateListeners.clone();
  }
  
  /**
   * <formal-arg>listener</formal-arg> is added to the set of objects to
   * be notified about the update progress.
   *
   * @param listener
   *        The object to be notified about the update progress.
   * @post  <formal-arg>listener</formal-arg> is in the set of objects to
   *        be notified about the update progress.
   *      | (listener != null) ==>
   *            new.getDemoUpdateListeners().contains(listener);
   */
  final public void addDemoUpdateListener(DemoUpdateListener listener) {
    if (listener != null) {
      $demoUpdateListeners.add(listener);
    }
  }
  
  /**
   * <formal-arg>listener</formal-arg> is removed from the set of objects to
   * be notified about the update progress.
   *
   * @param listener
   *        The object no longer to be notified about the update progress.
   * @post  <formal-arg>listener</formal-arg> is not in the set of objects to
   *        be notified about the update progress.
   *      | ! new.getDemoUpdateListeners().contains(listener);
   */
  final public void removeDemoUpdateListener(DemoUpdateListener listener) {
    $demoUpdateListeners.remove(listener);
  }
  
  /**
   * Notify all registered DemoUpdateListeners about the start of a
   * value update.
   *
   * @post The method valueUpdateStarted is called with
   *       <this>this</this> on all registered DemoUpdateListeners.
   *     | for each DemoUpdateListener l:
   *         $demoUpdateListeners.contains(l) ==>
   *            (new l).valueUpdateStarted(this) is called;
   */
  final protected void valueUpdateStarts() {
    Iterator iter = $demoUpdateListeners.iterator();
    while(iter.hasNext()) {
      ((DemoUpdateListener)iter.next()).valueUpdateStarted(this);
    }  
  }
  
  /**
   * Notify all registered DemoUpdateListeners about the completion of a
   * value update.
   *
   * @post The method valueUpdateCompleted is called with
   *       <this>this</this> on all registered DemoUpdateListeners.
   *     | for each DemoUpdateListener l:
   *         $demoUpdateListeners.contains(l) ==>
   *            (new l).valueUpdateCompleted(this) is called;
   */
  final protected void valueUpdateCompleted() {
    Iterator iter = $demoUpdateListeners.iterator();
    while(iter.hasNext()) {
      ((DemoUpdateListener)iter.next()).valueUpdateCompleted(this);
    }  
  }
  
  /**
   * The set of demo update listeners.
   *
   * @invar This variable references an effective set.
   *      | $demoUpdateListeners != null;
   * @invar All elements are effective.
   *      | for each Object o:
   *          $demoUpdateListeners.contains(o) ==> o != null;
   * @invar All elements are DemoUpdateListeners.
   *      | for each Object o:
   *          $demoUpdateListeners.contains(o) ==>
   *            o instanceof DemoUpdateListener;
   */
  private HashSet $demoUpdateListeners = new HashSet();
  
  /*</section>*/

  /*</property>*/



  /*<property name="Dependent Gates">*/
  //-------------------------------------------------------------------
  //-------------------------------------------------------------------

  /**
   * Returns the set of registered dependents.
   */
  final public Set getDependentGates() {
    return (Set)$dependents.clone();
  }
  
  /**
   * The number of dependent gates.
   *
   * @result | getDependentGates().size();
   */
  final public int getFanOut() {
    return $dependents.size();
  }
  
  /**
   * @result | exists LogicGate lg2 in getDependentGates():
   *             (lg == lg2) || lg2.isDependent(lg);
   */
  final public boolean isDependent(LogicGate lg) {
    Iterator iter = getDependentGates().iterator();
    while (iter.hasNext()) {
      LogicGate testGate = (LogicGate)iter.next();
      if ((testGate == lg) || (testGate.isDependent(lg))) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * <formal-arg>dependent</formal-arg> is added to the set of dependents.
   *
   * @param dependent
   *        The object to be notified of a change of our boolean value.
   * @pre   Don't create loops.
   *      | ! dependent.isDependent(this);
   * @post  <formal-arg>dependent</formal-arg> is in the set of objects to
   *        be notified when the boolean value of this object changes.
   *      | (dependent != null) ==>
   *            new.getDependentGates().contains(dependent);
   */
  final public void addDependentGate(LogicGate dependent) {
    if (dependent != null) {
      $dependents.add(dependent);
    }
  }
  
  /**
   * <formal-arg>dependent</formal-arg> is removed from the set of dependents.
   *
   * @param dependent
   *        The object no longer to be notified of a change of our boolean value.
   * @post  <formal-arg>dependent</formal-arg> is not in the set of objects to
   *        be notified when the boolean value of this object changes.
   *      | ! new.getDependentGates().contains(dependent);
   */
  final public void removeDependentGate(LogicGate dependent) {
    $dependents.remove(dependent);
  }
    
  /**
   * Notify all registered dependents about a boolean value change, so that
   * they can update themselves.
   *
   * @post The method boolean updateValue is called with
   *       <this>this</this> on all registered depentent Gates.
   *     | for each LogicGate l:
   *         $dependents.contains(l) ==>
   *            (new l).updateValue(this) is called;
   */
  final protected void updateDependentGates() {
    Iterator iter = getDependentGates().iterator();
    while (iter.hasNext()) {
      ((LogicGate)iter.next()).updateValue();
    }  
  }
  
  /**
   * The set of dependents.
   *
   * @invar This variable references an effective set.
   *      | $dependents != null;
   * @invar All elements are effective.
   *      | for each Object o:
   *          $dependents.contains(o) ==> o != null;
   * @invar All elements are LogicGates.
   *      | for each Object o:
   *          $dependents.contains(o) ==>
   *            o instanceof LogicGate;
   */
  private HashSet $dependents = new HashSet();

  /*</property>*/



  /**
   * This method is not really overwritten (we only call the super
   * implementation). It is listed here to make it final, so that
   * referential semantics is guaranteed for instances of this type.
   *
   * @result | this == other;
   */
  final public boolean equals(Object other) {
    return super.equals(other);
  }
  
  /**
   * A string representation of this.
   *
   * @return | result != null;
   * @return | ! result.equals("");
   */
  public String toString() {
    return getName() + ": " + new Boolean(getValue()).toString();
  }
  
}