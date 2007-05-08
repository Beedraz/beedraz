/*<license>
Copyright 2007 - $Date$ by PeopleWare n.v..

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

import java.lang.ref.WeakReference;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A set that stores elements in {@link WeakReference weak references}.
 * This means that membership in the set is nevere a reason not to be garbage
 * collected.
 *
 * @author Jan Dockx
 * @author PeopleWare n.v.
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class WeakHashSet<E> extends AbstractSet<E> implements Cloneable {

  /**
   * We only use keys in the backing weak hash $wrapped. This dummy
   * object is used as value.
   */
  private final static Object DUMMY = new Object();

  @Override
  public final Iterator<E> iterator() {
    return $wrapped.keySet().iterator();
  }

  @Override
  public final int size() {
    return $wrapped.size();
  }

  @Override
  public final boolean isEmpty() {
    return $wrapped.isEmpty();
  }

  @Override
  public final boolean contains(Object o) {
    return $wrapped.containsKey(o);
  }

  @Override
  public final boolean add(E e) {
    Object dummy = $wrapped.put(e, DUMMY);
    return dummy == null; // e was not yet in the map
  }

  @Override
  public final boolean remove(Object o) {
    Object dummy = $wrapped.remove(o);
    return dummy != null; // there actually was an o in the map
  }

  @Override
  public final void clear() {
    $wrapped.clear();
  }

  @Override
  public final WeakHashSet<E> clone() {
    try {
      @SuppressWarnings("unchecked")
      WeakHashSet<E> result = (WeakHashSet<E>)super.clone();
      Map<E, Object> resultMap = new WeakHashMap<E, Object>($wrapped); // WeakHashMap is not Cloneable
      result.$wrapped = resultMap;
      return result;
    }
    catch (CloneNotSupportedException cnsExc) {
      assert false : "should not happen";
      return null; // keep compiler happy
    }
  }

  public final HashSet<E> strongClone() {
    return new HashSet<E>(this);
  }

  /**
   * Cannot be made final for clone.
   */
  private Map<E, Object> $wrapped = new WeakHashMap<E, Object>();

}

