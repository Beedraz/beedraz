package org.beedra_II.beed;


import java.util.HashSet;
import java.util.Set;

import org.beedra_II.BeedraBean;
import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractBeed<_BeedraBean_ extends BeedraBean, _Value_>
    implements Beed<_BeedraBean_, _Value_> {

  /**
   * @pre bean != null;
   */
  protected AbstractBeed(_BeedraBean_ bean) {
    assert bean != null;
    $bean = bean;
  }

  public final _BeedraBean_ getBean() {
    return $bean;
  }

  /**
   * @invar $bean != null;
   */
  private final _BeedraBean_ $bean;

  public final boolean isChangeListener(BeedChangeListener<BeedChangeEvent<_Value_>> listener) {
    return $changeListeners.contains(listener);
  }

  // MUDO ? extends Beed<_Value_> is THIS TYPE

  public final void addChangeListener(BeedChangeListener<BeedChangeEvent<_Value_>> listener) {
    assert listener != null;
    $changeListeners.add(listener);
  }

  public final void removeChangeListener(BeedChangeListener<BeedChangeEvent<_Value_>> listener) {
    $changeListeners.remove(listener);
  }

  protected final void fireChangeEvent(BeedChangeEvent<_Value_> event) {
    for (BeedChangeListener<BeedChangeEvent<_Value_>> listener : $changeListeners) {
      listener.propertyChange(event);
      // same event, because is immutable
    }
  }

  /**
   * @invar $changeListeners != null;
   * @invar Collections.noNull($changeListeners);
   */
  private final Set<BeedChangeListener<BeedChangeEvent<_Value_>>> $changeListeners =
      new HashSet<BeedChangeListener<BeedChangeEvent<_Value_>>>();

}
