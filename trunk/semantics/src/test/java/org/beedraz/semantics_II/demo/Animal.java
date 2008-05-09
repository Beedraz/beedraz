/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedraz.semantics_II.demo;


import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.expression.association.set.EditableBidirToOneBeed;
import org.beedraz.semantics_II.expression.association.set.ordered.EditableOrderedBidirToOneBeed;
import org.beedraz.semantics_II.expression.string.EditableStringBeed;


/**
 */
public class Animal extends AbstractBeanBeed {


  /*<property name="name">*/
  //------------------------------------------------------------------

  /**
   * The name.
   */
  public final EditableStringBeed name = new EditableStringBeed(this) {

    /**
     * The name should be effective.
     * The name should contain at most 256 characters.
     */
    @Override
    public boolean isAcceptable(String goal) {
      return  goal != null &&
              checkNameLength(goal);
    }

  };

  public final boolean checkNameLength(final String goal) {
    return goal.length() <= 256;
  }

  /*</property>*/


  /*<property name="person">*/
  //------------------------------------------------------------------

  /**
   * The owner of the animal.
   */
  public final EditableBidirToOneBeed<Person, Animal> person =
    new EditableBidirToOneBeed<Person, Animal>(this);

  /*</property>*/


  /*<property name="orderedPerson">*/
  //------------------------------------------------------------------

  /**
   * The owner of the animal.
   */
  public final EditableOrderedBidirToOneBeed<Person, Animal> orderedPerson =
    new EditableOrderedBidirToOneBeed<Person, Animal>(this);

  /*</property>*/

}

