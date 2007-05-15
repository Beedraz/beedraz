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


import static org.ppeew.annotations_I.License.Type.APACHE_V2;
import static org.ppeew.smallfries_I.MultiLineToStringUtil.objectToString;

import java.util.Set;

import org.beedraz.semantics_II.topologicalupdate.AbstractUpdateSource;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;
import org.ppeew.collection_I.WeakHashSet;


/**
 * Support for implementations of {@link Beed}.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractBeed<_Event_ extends Event>
    extends AbstractUpdateSource
    implements Beed<_Event_> {

  public final boolean isListener(Listener<? super _Event_> listener) {
    return $changeListeners.contains(listener);
  }

  public final void addListener(Listener<? super _Event_> listener) {
    assert listener != null;
    $changeListeners.add(listener);
  }

  public final void removeListener(Listener<? super _Event_> listener) {
    $changeListeners.remove(listener);
  }

  @Override
  protected final void fireEvent(Event event) {
    @SuppressWarnings("unchecked")
    _Event_ eEvent = (_Event_)event;
    @SuppressWarnings("unchecked")
    Set<Listener<? super _Event_>> listeners = $changeListeners.strongClone();
    for (Listener<? super _Event_> listener : listeners) {
      listener.beedChanged(eEvent);
      // same event, because is immutable
      // mudo unlock event
    }
  }

  /**
   * @invar $changeListeners != null;
   * @invar Collections.noNull($changeListeners);
   */
  private final WeakHashSet<Listener<? super _Event_>> $changeListeners =
      new WeakHashSet<Listener<? super _Event_>>();

  @Override
  public final String toString() {
    return simpleClassName(getClass()) + "@" + Integer.toHexString(hashCode()) +
           "[" + otherToStringInformation() + "]";
  }

  private String simpleClassName(Class<?> c) {
    String simpleName = c.getSimpleName();
    if (simpleName.equals("")) {
      String fullName = c.getName();
      String[] parts = fullName.split("\\.");
      simpleName = simpleClassName(c.getSuperclass()) + "<" + parts[parts.length - 1];
    }
    return simpleName;
  }

  protected String otherToStringInformation() {
    return "";
  }

  public void toString(StringBuffer sb, int level) {
    assert sb != null;
    objectToString(this, sb, level);
  }

}
