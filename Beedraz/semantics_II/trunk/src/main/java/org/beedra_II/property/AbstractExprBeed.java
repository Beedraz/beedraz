/*<license>
Copyright 2007 - $Date$ by the authors mentioned below.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</license>*/

package org.beedra_II.property;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.beedra_II.AbstractBeed;
import org.beedra_II.Event;
import org.beedra_II.edit.Edit;
import org.beedra_II.topologicalupdate.AbstractUpdateSourceDependentDelegate;
import org.beedra_II.topologicalupdate.Dependent;
import org.beedra_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Abstract implementation of expression beeds.
 *
 * @invar isEffective() ?? get() != null;
 *
 * @mudo Isn't the clue of this class originally that is works for expressions of primitive type?
 *       Where we use a isEffective method? Shouldn't the name reflect this?
 *       And now the clue is also that the beed is dependent! Name should reflect this too.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AbstractExprBeed<_Result_ extends Object,
                                       _ResultEvent_ extends Event>
    extends AbstractBeed<_ResultEvent_> {

  /**
   * Return the value of this beed, as an object.
   *
   * @basic
   *
   * @mudo Isn't this a {@link SimpleBeed} then?
   */
  public abstract _Result_ get();

  protected abstract _ResultEvent_ createNewEvent(_Result_ oldValue, _Result_ newValue, Edit<?> edit);

  /**
   * Whether {@code r1} and {@code r2} should be considered semantically equivalent.
   *
   * @todo Why don't we use r1.equals(r2)?
   */
  protected abstract boolean equalValue(_Result_ r1, _Result_ r2);

  /**
   * This method is called by the dependent when it is asked to update.
   * Subtypes should implement with code that deals with events from any update source
   * they have registered with the dependent.
   */
  protected abstract _ResultEvent_ filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit);



  /*<property name="effective">*/
  //------------------------------------------------------------------

  /**
   * @basic
   * @init false;
   */
  public final boolean isEffective() {
    return $effective;
  }

  /**
   * @post isEffective() == effective;
   */
  protected void assignEffective(boolean effective) {
    $effective = effective;
  }

  private boolean $effective = false;

  /*</property>*/



  /*<section name="dependent">*/
  //------------------------------------------------------------------

  /**
   * @mudo must be private, but protected now to force errors in subtypes
   */
  private final Dependent $dependent = new AbstractUpdateSourceDependentDelegate(this) {

    @Override
    protected _ResultEvent_ filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
      return AbstractExprBeed.this.filteredUpdate(events, edit);
    }

  };

  protected final void addUpdateSource(UpdateSource us) {
    $dependent.addUpdateSource(us);
  }

  protected final void removeUpdateSource(UpdateSource us) {
    $dependent.removeUpdateSource(us);
  }


  /*<section name="update source">*/
  //------------------------------------------------------------------

  public final int getMaximumRootUpdateSourceDistance() {
    return $dependent.getMaximumRootUpdateSourceDistance();
  }

  public final Set<? extends UpdateSource> getUpdateSources() {
    return $dependent.getUpdateSources();
  }

  private final static Set<? extends UpdateSource> PHI = Collections.emptySet();

  public final Set<? extends UpdateSource> getUpdateSourcesTransitiveClosure() {
    /* fixed to make it possible to use this method during construction,
     * before $dependent is initialized. But that is bad code, and should be
     * fixed.
     */
    return $dependent == null ? PHI : $dependent.getUpdateSourcesTransitiveClosure();
  }

  /*</section>*/



  @Override
  protected final String otherToStringInformation() {
    return get() == null ? "null" : get().toString();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "value:" + get() + "\n");
  }

}

