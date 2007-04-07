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

package org.beedra_II.path;


import org.beedra_II.ActualOldNewEvent;
import org.beedra_II.Beed;
import org.beedra_II.Event;
import org.beedra_II.edit.Edit;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * {@link Event} that describes a change in a {@link Path}. A {@link Path}
 * is only considered changed when the beed that is finally returned has changed.
 * {@code null} is a likely value for the old or new state.
 *
 * @author Jan Dockx
 *
 * @invar getOldBeed() != getNewBeed();
 *
 * @note If {@link Path} would become a property beed, we could use
 *       {@link ActualOldNewEvent}. Or, we could loosen
 *       {@link ActualOldNewEvent} to accept any beed in the constructor.
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class PathEvent<_SelectedBeed_ extends Beed<?>>
    extends ActualOldNewEvent<_SelectedBeed_> {

  /**
   * @pre  source != null;
   * @pre  (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
   * @pre  (oldBeed != null) && (newBeed != null)
   *          ? ! oldBeed.equals(newBeed)
   *          : true;
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post (edit != null) ? getEditState() == edit.getState() : getEditState() == null;
   * @post getOldBeed() == oldBeed;
   * @post getNewBeed() == newBeed;
   */
  public PathEvent(Path<_SelectedBeed_> source,
                        _SelectedBeed_ oldBeed,
                        _SelectedBeed_ newBeed,
                        Edit<?> edit) {
    super(source, oldBeed, newBeed, edit);
  }

}

