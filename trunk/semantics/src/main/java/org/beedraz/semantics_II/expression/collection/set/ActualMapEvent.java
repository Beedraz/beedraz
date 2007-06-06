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

package org.beedraz.semantics_II.expression.collection.set;


import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.beedraz.semantics_II.AbstractEvent;
import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.path.Path;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * Event that notifies of changes in an actual {@link MapBeed}.
 *
 * @author Nele Smeets
 * @author Peopleware n.v.
 *
 * @mudo move to collection package; map is not a set
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State: Exp $",
         tag      = "$Name:  $")
public final class ActualMapEvent<_Key_, _Value_ extends Beed<?>>
    extends AbstractEvent
    implements MapEvent<_Key_, _Value_> {

  /**
   * @pre  source != null;
   * @pre  edit != null;
   * @pre  (edit.getState() == DONE) || (edit.getState() == UNDONE);
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post getEditState() == edit.getState();
   * @post addedElements != null
   *           ? getAddedElements().equals(addedElements)
   *           : getAddedElements().isEmpty();
   * @post removedElements != null
   *           ? getRemovedElements().equals(removedElements)
   *           : getRemovedElements().isEmpty();
   */
  public ActualMapEvent(MapBeed<_Key_, _Value_, ?> source,
                        Map<_Key_, Path<_Value_>> addedElements,
                        Map<_Key_, Path<_Value_>> removedElements,
                        Edit<?> edit) {
    super(source, edit);
    $addedElements = addedElements == null
                       ? Collections.<_Key_, Path<_Value_>>emptyMap()
                       : new HashMap<_Key_, Path<_Value_>>(addedElements);
    $removedElements = removedElements == null
                         ? Collections.<_Key_, Path<_Value_>>emptyMap()
                         : new HashMap<_Key_, Path<_Value_>>(removedElements);
  }

  /**
   * @basic
   */
  public Map<_Key_, Path<_Value_>> getAddedElements() {
    return Collections.unmodifiableMap($addedElements);
  }

 /**
   * @invar $addedElements != null;
   */
  private final Map<_Key_, Path<_Value_>> $addedElements;

  /**
   * @basic
   */
  public Map<_Key_, Path<_Value_>> getRemovedElements() {
    return Collections.unmodifiableMap($removedElements);
  }

  /**
   * @invar $removedElements != null;
   */
  private final Map<_Key_, Path<_Value_>> $removedElements;

  @Override
  protected String otherToStringInformation() {
    return super.otherToStringInformation() +
           ", added elements: " + getAddedElements() +
           ", removed elements: " + getRemovedElements();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "added elements: " + getAddedElements() + "\n");
    sb.append(indent(level + 1) + "removed elements: " + getRemovedElements() + "\n");
  }

}

