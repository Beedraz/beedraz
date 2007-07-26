/*<license>
  Copyright 2007, PeopleWare n.v.
  NO RIGHTS ARE GRANTED FOR THE USE OF THIS SOFTWARE, EXCEPT, IN WRITING,
  TO SELECTED PARTIES.
</license>*/

package org.beedraz.semantics_II;


/**
 * When we asked {@link #getFirstEvent()} to create a merged event with
 * {@link #getSecondEvent()}, {@link #getFirstEvent()} signals it cannot.
 * This means {@link #getFirstEvent()} and {@link #getSecondEvent()} are of
 * different type ({@link #getReason()} {@code ==} {@link Reason#DIFFERENT_TYPE}),
 * come from a different source ({@link #getReason()} {@code ==}
 * {@link Reason#DIFFERENT_SOURCE}), or that the new state reported
 * by {@link #getFirstEvent()} is not the old state reported by
 * {@link #getSecondEvent()} ({@link #getReason()} {@code ==}
 * {@link Reason#INCOMPATIBLE_STATES}).
 *
 * @invar getFirstEvent() != null;
 * @invar getSecondEvent() != null;
 * @invar getReason() != null;
 * @invar getMessage().equals(getReason().toString());
 * @invar getReason() == Reason.DIFFERENT_TYPE ? getFirstEvent().getClass() != getSecondEvent.getClass();
 * @invar getReason() == Reason.DIFFERENT_SOURCE ?
 *          (getFirstEvent().getClass() == getSecondEvent.getClass()) &&
 *          (getFirstEvent().getSource() != getSecondEvent().getSource());
 * @invar getReason() == Reason.INCOMPATIBLE_STATES ?
 *          (getFirstEvent().getClass() == getSecondEvent.getClass()) &&
 *          (getFirstEvent().getSource() == getSecondEvent().getSource());
 *          but the first event new state is different from the second event
 *          old state.
 */
public class CannotCombineEventsException extends Exception {

  public enum Reason {

    /**
     * The {@link #getFirstEvent()} and {@link #getSecondEvent()}
     * are not of the same type.
     */
    DIFFERENT_TYPE,

    /**
     * The {@link #getFirstEvent()} and {@link #getSecondEvent()}
     * are of the same type, but have a different source.
     */
    DIFFERENT_SOURCE,

    /**
     * The {@link #getFirstEvent()} and {@link #getSecondEvent()}
     * are of the same type, and have the same source, but
     * the new state of {@link #getFirstEvent()} is different
     * from the old state of {@link #getSecondEvent()}.
     */
    INCOMPATIBLE_STATES
  }

  /**
   * @pre firstEvent != null;
   * @pre secondEvent != null;
   * @pre reason != null;
   * @post getFirstEvent() == firstEvent;
   * @post getSecondEvent() == secondEvent;
   * @post getReason() == reason;
   * @post getCause() == null;
   * @post getMessage().equals(reason.toString());
   */
  public CannotCombineEventsException(final Event firstEvent, final Event secondEvent, final Reason reason) {
    this(firstEvent, secondEvent, reason, null);
  }

  /**
   * @pre firstEvent != null;
   * @pre secondEvent != null;
   * @pre reason != null;
   * @post getFirstEvent() == firstEvent;
   * @post getSecondEvent() == secondEvent;
   * @post getReason() == reason;
   * @post getCause() == cause;
   * @post getMessage().equals(reason.toString());
   */
  public CannotCombineEventsException(final Event firstEvent, final Event secondEvent, Reason reason, Throwable cause) {
    super(reason.toString(), cause);
    assert firstEvent != null;
    assert secondEvent != null;
    // assert reason != null;
    $firstEvent = firstEvent;
    $secondEvent = secondEvent;
    $reason = reason;
  }



  /*<property name="second edit">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  public final Event getFirstEvent() {
    return $firstEvent;
  }

  /**
   * @invar $firstEvent != null;
   */
  private final Event $firstEvent;

  /*</property>*/



  /*<property name="second edit">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  public final Event getSecondEvent() {
    return $secondEvent;
  }

  /**
   * @invar $secondEvent != null;
   */
  private final Event $secondEvent;

  /*</property>*/



  /*<property name="reason">*/
  //-------------------------------------------------------

  /**
   * @basic
   */
  public final Reason getReason() {
    return $reason;
  }

  private final Reason $reason;

  /*</property>*/



  @Override
  public final String toString() {
    return super.toString() + "[" +
           getFirstEvent().toString() + ", " +
           getSecondEvent().toString() + "]";
  }

}

