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

package org.beedraz.semantics_II;


import static org.beedraz.semantics_II.Edit.State.DONE;
import static org.beedraz.semantics_II.Edit.State.UNDONE;
import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.indent;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.objectToString;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * @author Jan Dockx
 *
 * @invar getSource() != null;
 * @invar (getEdit() != null) ? getEdit().getState() == DONE || getEdit().getState() == UNDONE;
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractEvent implements Event {

  public static int countAbstractEvent = 0;
//  public static Set<AbstractEvent> instancesAbstractEvent = new HashSet<AbstractEvent>();

  /**
   * @pre  source != null;
   * @pre  (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE);
   *
   * @post getSource() == source;
   * @post getEdit() == edit;
   * @post (edit != null) ? getEditState() == edit.getState() : getEditState() == null;
   */
  protected AbstractEvent(Beed<?> source, Edit<?> edit) {
    assert source != null;
    assert (edit != null) ? (edit.getState() == DONE) || (edit.getState() == UNDONE) : true;
    $source = source;
    $edit = edit;
    $editState = (edit != null) ? edit.getState() : null;
    countAbstractEvent++;
//    instancesAbstractEvent.add(this);
  }

  public final Beed<?> getSource() {
    return $source;
  }

  /**
   * @invar $source != null;
   */
  private final Beed<?> $source;

  public final Edit<?> getEdit() {
    return $edit;
  }

  private final Edit<?> $edit;

  public Edit.State getEditState() {
    return $editState;
  }

  private final Edit.State $editState;

  @Override
  public final String toString() {
    return getClass().getSimpleName() + //"@" + hashCode() +
           "[" + otherToStringInformation() + "]";
  }

  protected String otherToStringInformation() {
    return "source: " + getSource() +
           ", edit: " + getEdit() +
           ", edit state: " + getEditState();
  }

  public void toString(StringBuffer sb, int level) {
    assert sb != null;
    assert level >= 0;
    objectToString(this, sb, level);
    sb.append(indent(level + 1) + "source:\n");
    getSource().toString(sb, level + 2);
    sb.append(indent(level + 1) + "edit:\n");
    getEdit().toString(sb, level + 2);
    sb.append(indent(level + 1) + "edit state: " + getEditState() + "\n");
  }

}

