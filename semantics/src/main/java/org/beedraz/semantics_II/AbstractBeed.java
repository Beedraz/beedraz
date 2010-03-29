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


import static org.ppwcode.metainfo_I.License.Type.APACHE_V2;
import static org.ppwcode.util.smallfries_I.MultiLineToStringUtil.objectToString;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.ppwcode.metainfo_I.Copyright;
import org.ppwcode.metainfo_I.License;
import org.ppwcode.metainfo_I.vcs.SvnInfo;
import org.ppwcode.util.collection_I.ArraySet;
import org.ppwcode.util.collection_I.WeakArraySet;


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
    implements Beed<_Event_> {

  /*<section name="listeners">*/
  //------------------------------------------------------------------

  public final boolean isListener(Listener<? super _Event_> listener) {
    return ($changeListeners != null) && $changeListeners.contains(listener);
  }

  public final void addListener(Listener<? super _Event_> listener) {
    assert listener != null;
    if ($changeListeners == null) {
      $changeListeners = new HashSet<Listener<? super _Event_>>();
    }
    $changeListeners.add(listener);
  }

  public final void removeListener(Listener<? super _Event_> listener) {
    if ($changeListeners != null) {
      $changeListeners.remove(listener);
      if ($changeListeners.isEmpty()) {
        $changeListeners = null;
      }
    }
  }

  /**
   * @pre event != null;
   * @pre event instanceof _Event_
   * @post for (Listener<? super _Event_> l : isListener(l)) {
   *         l.beedChanged(event)
   *       }
   *
   * @mudo use _Event_???
   */
  protected final void fireEvent(Event event) {
    assert event != null;
    if ($changeListeners != null) {
      @SuppressWarnings("unchecked")
      _Event_ eEvent = (_Event_)event;
      @SuppressWarnings("unchecked")
      Set<Listener<? super _Event_>> listeners = (Set<Listener<? super _Event_>>)$changeListeners.clone();
      for (Listener<? super _Event_> listener : listeners) {
        listener.beedChanged(eEvent);
        // same event, because is immutable
        // mudo unlock event
      }
    }
  }

  /**
   * If the set is empty, it is discarded to save memory.
   */
  private HashSet<Listener<? super _Event_>> $changeListeners = null;

  /*</section>*/




  /*<section name="dependents">*/
  //------------------------------------------------------------------

  public final Set<Dependent> getDependents() {
    return ($dependents == null) ? EMPTY_DEPENDENTS : $dependents.clone();
  }

  @Deprecated
  public final boolean isDependent(Dependent dependent) {
    return getDependents().contains(dependent);
  }

  public final boolean isTransitiveDependent(Dependent dependent) {
    if ($dependents == null) {
      return false;
    }
    if ($dependents.contains(dependent)) {
      return true;
    }
    else {
      for (Dependent d : $dependents) {
        if (d.getDependentUpdateSource().isTransitiveDependent(dependent)) {
          return true;
        }
      }
      return false;
    }
  }

  private final static Set<Dependent> EMPTY_DEPENDENTS = Collections.emptySet();

  /**
   * @pre dependent != null;
   * @pre dependent.getDependentUpdateSource() != this;
   * @pre ! getUpdateSourcesTransitiveClosure().contains(dependent.getDependentUpdateSource());
   */
  public final void addDependent(Dependent dependent) {
    assert dependent != null;
    assert dependent.getDependentUpdateSource() != this;
    /* MUDO incredible slowdown, and -da doesn't work ???
    assert ! getUpdateSourcesTransitiveClosure().contains(dependent.getDependentUpdateSource());
    */
    assert dependent.getMaximumRootUpdateSourceDistance() > getMaximumRootUpdateSourceDistance();
    if ($dependents == null) {
      $dependents = new ArraySet<Dependent>();
    }
    $dependents.add(dependent);
  }

  public final void removeDependent(Dependent dependent) {
    if ($dependents != null) {
      $dependents.remove(dependent);
      if ($dependents.isEmpty()) {
        $dependents = null;
      }
    }
  }

  /**
   * If the set is empty, it is set to null, to save memory.
   */
  private ArraySet<Dependent> $dependents = null;

  /*</section>*/



  /*<section name="update sources">*/
  //------------------------------------------------------------------

  public final Set<? extends Beed<?>> getRootUpdateSources() {
    Set<? extends Beed<?>> uss = getUpdateSourcesTransitiveClosure();
    HashSet<Beed<?>> result = new HashSet<Beed<?>>();
    for (Beed<?> us : uss) {
      if (us.getMaximumRootUpdateSourceDistance() == 0) {
        result.add(us);
      }
    }
    return result;
  }

  /*</section>*/



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
