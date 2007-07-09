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

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;
import org.ppeew.smallfries_I.ComparisonUtil;


/**
 * {@link OrderedSet} backed by a {@link LinkedList}.
 *
 * @author Jan Dockx
 */
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class LinkedListOrderedSet<E>
    extends AbstractSet<E>
    implements OrderedSet<E> {

  public LinkedListOrderedSet() {
    $backingList = new LinkedList<E>();
  }

  public LinkedListOrderedSet(Collection<? extends E> coll) {
    if ((coll != null) && (coll instanceof LinkedListOrderedSet)) {
      LinkedListOrderedSet<E> argL = (LinkedListOrderedSet<E>)coll;
      LinkedList<E> argBl = argL.$backingList;
      @SuppressWarnings("unchecked")
      LinkedList<E> l = (LinkedList<E>)argBl.clone();
      $backingList = l;
    }
    else {
      $backingList = new LinkedList<E>();
      if (coll != null) {
        for (E e: coll) {
          add(e);
        }
      }
    }
  }

  @Override
  public LinkedListOrderedSet<E> clone() {
    try {
      @SuppressWarnings("unchecked")
      LinkedListOrderedSet<E> result = (LinkedListOrderedSet<E>)super.clone();
      @SuppressWarnings("unchecked")
      LinkedList<E> cloneBl = (LinkedList<E>)$backingList.clone();
      result.$backingList = cloneBl;
      return result;
    }
    catch (CloneNotSupportedException exc) {
      assert false : "CloneNotSupportedException should not happen: " + exc;
      return null; // keep compiler happy
    }
  }

  /**
   * Can't be final, because of clone();
   *
   * @invar $backingList != null;
   */
  private LinkedList<E> $backingList;

  /**
   * @basic
   */
  @Override
  public final int size() {
    return $backingList.size();
  }

  /**
   * @throws IndexOutOfBoundsException
   *         index < 0;
   * @throws IndexOutOfBoundsException
   *         index >= size();
   */
  public final E get(int index) throws IndexOutOfBoundsException {
    return $backingList.get(index); // IndexOutOfBoundsException
  }

  /**
   * @result result >= 0 ?? get(result).equals(object);
   * @result result == -1 ?? ! contains(object);
   */
  public final int indexOf(Object object) {
    return $backingList.indexOf(object);
  }

  @Override
  public final Iterator<E> iterator() {
    return $backingList.iterator();
  }

  /**
   * @result result != null;
   * @result equals(result);
   */
  public final List<E> asList() {
    return Collections.unmodifiableList($backingList);
  }

  /**
   * Puts {@code object} at the end of this ordered set. If {@code 'contains(object)},
   * moves {@code object} to the end of this ordered set, and moves all elements
   * inbetween down.
   *
   * @result contains(object);
   * @result get(size() - 1) == object;
   * @result ! 'contains(object) ? size() == 'size() + 1;
   * @result 'contains(object) ? size() == 'size();
   * @result ! 'contains(object) ? for (E e : this') {indexOf(e) == 'indexOf(e)};
   * @result 'contains(object) ? for (E e : this') {'indexOf(e) < 'indexOf(object) ? indexOf(e) == 'indexOf(e)};
   * @result 'contains(object) ? for (E e : this') {'indexOf(e) >= 'indexOf(object) ? indexOf(e) == 'indexOf(e) - 1};
   * @throws IndexOutOfBoundsException
   *         index < 0;
   * @throws IndexOutOfBoundsException
   *         index > size();
   */
  @Override
  public final boolean add(E object) {
    ListIterator<E> iter = $backingList.listIterator();
    while (iter.hasNext()) {
      E e = iter.next();
      if (ComparisonUtil.equalsWithNull(e, object)) {
        iter.remove();
      }
    }
    iter.add(object); // add at the end
    return true;
  }

  /**
   * Puts {@code object} at {@code index} oin this ordered set. If {@code 'contains(object)},
   * moves {@code object} to {@code index} in this ordered set, and moves all elements
   * inbetween.
   *
   * @result contains(object);
   * @result get(index) == object;
   * @result ! 'contains(object) ? size() == 'size() + 1;
   * @result 'contains(object) ? size() == 'size();
   * @result ! 'contains(object) ? for (E e : this') {'indexOf(e) < index ? indexOf(e) == 'indexOf(e)};
   * @result ! 'contains(object) ? for (E e : this') {'indexOf(e) >= index ? indexOf(e) == 'indexOf(e) + 1};
   * @result 'contains(object) && 'indexOf(object) < index ? for (E e : this') {'indexOf(e) < 'indexOf(object) ? indexOf(e) == 'indexOf(e)};
   * @result 'contains(object) && 'indexOf(object) < index ? for (E e : this') {'indexOf(e) > 'indexOf(object) && 'indexOf(e) < index ? indexOf(e) == 'indexOf(e) - 1};
   * @result 'contains(object) && 'indexOf(object) < index ? for (E e : this') {'indexOf(e) > index ? indexOf(e) == 'indexOf(e)};
   * @result 'contains(object) && 'indexOf(object) >= index ? for (E e : this') {'indexOf(e) < index ? indexOf(e) == 'indexOf(e)};
   * @result 'contains(object) && 'indexOf(object) >= index ? for (E e : this') {'indexOf(e) > index && 'indexOf(e) < 'indexOf(object) ? indexOf(e) == 'indexOf(e) + 1};
   * @result 'contains(object) && 'indexOf(object) >= index ? for (E e : this') {'indexOf(e) > 'indexOf(object) ? indexOf(e) == 'indexOf(e)};
   * @throws IndexOutOfBoundsException
   *         index < 0;
   * @throws IndexOutOfBoundsException
   *         index > size();
   */
  public final boolean add(int index, E object) throws IndexOutOfBoundsException {
    if ((index < 0) || (index > size())) {
      throw new IndexOutOfBoundsException();
    }
    boolean added = false;
    int removed = -1;
    ListIterator<E> iter = $backingList.listIterator();
    if (index == 0) {
      iter.add(object);
      added = true;
    }
    while (iter.hasNext() && (! (added && (removed >= 0)))) {
      E e = iter.next();
      if (ComparisonUtil.equalsWithNull(e, object)) {
        removed = iter.previousIndex();
        iter.remove();
      }
      if (iter.nextIndex() == index) {
        iter.add(object);
        added = true;
      }
    }
    return removed != index + 1; // object was in the set at position index
  }

//  /**
//   * Puts all elements of {@code coll} at {@code index} in this ordered set. Elements of {@code coll}
//   * that are already a member are moved.
//   *
//   * @todo contract (too difficult to do now)
//   *
//   * @throws NullPointerException
//   *         coll == null;
//   * @throws IndexOutOfBoundsException
//   *         index < 0;
//   * @throws IndexOutOfBoundsException
//   *         index > size();
//   */
//  public final boolean addAll(int index, Collection<? extends E> coll) throws NullPointerException, IndexOutOfBoundsException {
//    if (coll == null) {
//      throw new NullPointerException();
//    }
//    if ((index < 0) || (index > size())) {
//      throw new IndexOutOfBoundsException();
//    }
//    Set<E> beforeIndex = new HashSet<E>($backingList.subList(0, index));
//    beforeIndex.retainAll(coll);
//    int removedBeforeIndex = beforeIndex.size();
//    $backingList.removeAll(coll);
//    $backingList.addAll(index, coll);
//    return ! coll.isEmpty();
//  }

  /**
   * Removes the element at position {@code index}. Elements later in the order are moved down.
   *
   * @result ! contains(object);
   * @result size() == 'size() - 1;
   * @result for (E e : this') {'indexOf(e) < index ? indexOf(e) == 'indexOf(e)};
   * @result for (E e : this') {'indexOf(e) > index ? indexOf(e) == 'indexOf(e) - 1};
   * @throws IndexOutOfBoundsException
   *         index < 0;
   * @throws IndexOutOfBoundsException
   *         index > size();
   */
  public final E remove(int index) throws IndexOutOfBoundsException {
    return $backingList.remove(index); // IndexOutOfBoundsException
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    try {
      @SuppressWarnings("unchecked")
      OrderedSet<E> cOther = (OrderedSet<E>)other;
      if (size() != cOther.size()) {
        return false;
      }
      Iterator<E> myIter = iterator();
      Iterator<E> otherIter = cOther.iterator();
      while (myIter.hasNext()) {
        if (! ComparisonUtil.equalsWithNull(myIter.next(), otherIter.next())) {
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
  public final String toString() {
    return $backingList.toString();
  }

}