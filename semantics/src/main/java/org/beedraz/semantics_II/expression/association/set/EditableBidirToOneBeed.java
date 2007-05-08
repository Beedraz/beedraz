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

package org.beedraz.semantics_II.expression.association.set;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Map;

import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.bean.BeanBeed;
import org.beedraz.semantics_II.edit.Edit;
import org.beedraz.semantics_II.expression.EditableSimpleExpressionBeed;
import org.beedraz.semantics_II.topologicalupdate.AbstractUpdateSource;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * @mudo description
 *
 * @invar  getOwner() instanceof _Many_;
 *
 * @mudo implement event propagation to ONE
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class EditableBidirToOneBeed<_One_ extends BeanBeed,
                                    _Many_ extends BeanBeed>
    extends EditableSimpleExpressionBeed<BidirToManyBeed<_One_, _Many_>, BidirToOneEvent<_One_, _Many_>> {

  /**
   * @pre  owner != null;
   * @post getOwner() == owner;
   */
  public EditableBidirToOneBeed(_Many_ owner) {
    super(owner);
  }

  @Override
  @SuppressWarnings("unchecked")
  public final _Many_ getOwner() {
    return (_Many_)super.getOwner();
  }

  public final _One_ getOne() {
    return get() == null ? null : get().getOwner();
  }

  static void packageUpdateDependents(Map<AbstractUpdateSource, Event> events, Edit<?> edit) {
    AbstractUpdateSource.updateDependents(events, edit);
  }

}

