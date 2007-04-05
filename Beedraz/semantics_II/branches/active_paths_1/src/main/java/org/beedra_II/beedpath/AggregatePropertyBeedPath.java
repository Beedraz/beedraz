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

package org.beedra_II.beedpath;


import org.beedra_II.BeedMapping;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.property.PropertyBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>{@link BeedPath} that selects a {@link PropertyBeed} from an
 *   {@link AggregateBeed}. The {@link #get() returned} {@link PropertyBeed}
 *   has the {@link #getFrom() from aggregate beed} as
 *   {@link PropertyBeed#getOwner() owner}.</p>
 * <p>When the {@link #getFrom() from beed} is {@code null}, the
 *   {@link #get() resulting} {@link PropertyBeed} is {@code null}
 *   too. When the {@link #getFrom() owner} changes, the
 *   {@link #get() resulting} {@link PropertyBeed} can change to.
 *   When the {@link #get() resulting} {@link PropertyBeed}
 *   changes, dependents and listeners are warned.</p>
 *
 * @author Jan Dockx
 *
 * @invar getFrom() == null ? get() == null;
 * @invar get() != null ? get().getOwner() == getOwner();
 *
 * @note instead of inhering from {@link BeedMapping}, we could use it as a strategy
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class AggregatePropertyBeedPath<_Owner_ extends AggregateBeed,
                                                _PropertyBeed_ extends PropertyBeed<?>>
    extends AbstractIndependentBeedPath<_PropertyBeed_>
    implements BeedMapping<_Owner_, _PropertyBeed_> {

  /**
   * @basic
   */
  public final _Owner_ getFrom() {
    return $from;
  }

  public final void setFrom(_Owner_ owner) {
    $from = owner; // we are listening to no-one
    _PropertyBeed_ oldResult = $result;
    if ($from == null) {
      $result = null;
    }
    else {
      $result = map($from);
      assert $result.getOwner() == $from : "owner of selected beed must be $from";
    }
    if ($result != oldResult) {
      updateDependents(new BeedPathEvent<_PropertyBeed_>(this, oldResult, $result, null));
    }
  }

  private _Owner_ $from;

  public final _PropertyBeed_ get() {
    return $result;
  }

  private _PropertyBeed_ $result;

}

