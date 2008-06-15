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

package org.ppeew.collection_I;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Collection;
import java.util.Set;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * Unmodifiable {@link Set} general code.
 *
 * @author Jan Dockx
 * @todo start using ppwcode util collections_N asap
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public abstract class AbstractUnmodifiableSet<E>
    extends AbstractUnmodifiableCollection<E>
    implements Set<E> {

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    try {
      @SuppressWarnings("unchecked")
      Collection<E> cOther = (Collection<E>)other;
      if (size() != cOther.size()) {
        return false;
      }
      for (E e : cOther) {
        if (! contains(e)) {
          return false;
        }
      }
      for (E e : this) {
        if (! cOther.contains(e)) {
          return false;
        }
      }
      return true;
    }
    catch (ClassCastException ccExc) {
      return false;
    }
  }

  @Override
  public final int hashCode() {
    // sum of element hash codes
    int acc = 0;
    for (E e : this) {
      if (e != null) {
        acc += e.hashCode();
      }
    }
    return acc;
  }

}