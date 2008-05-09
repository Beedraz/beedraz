/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedraz.semantics_II.demo;


import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.expression.association.set.BidirToManyBeed;
import org.beedraz.semantics_II.expression.association.set.ordered.OrderedBidirToManyBeed;
import org.beedraz.semantics_II.expression.number.real.double64.EditableDoubleBeed;
import org.beedraz.semantics_II.expression.string.EditableStringBeed;


/**
 */
public class Person extends AbstractBeanBeed {


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


  /*<property name="length">*/
  //------------------------------------------------------------------

  /**
   * The length.
   */
  public final EditableDoubleBeed length = new EditableDoubleBeed(this);

  /*</property>*/


  /*<section name="pets">*/
  //-----------------------------------------------------------------

  /**
   * The pets of the person.
   */
  public final BidirToManyBeed<Person, Animal> pets =
    new BidirToManyBeed<Person, Animal>(this);

  /*</section>*/


  /*<section name="orderedPets">*/
  //-----------------------------------------------------------------

  /**
   * The pets of the person.
   */
  public final OrderedBidirToManyBeed<Person, Animal> orderedPets =
    new OrderedBidirToManyBeed<Person, Animal>(this);

  /*</section>*/

}

