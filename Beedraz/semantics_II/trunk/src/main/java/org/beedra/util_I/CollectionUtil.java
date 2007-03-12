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

package org.beedra.util_I;


import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

import org.toryt.util_I.annotations.vcs.CvsInfo;


@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class CollectionUtil {

  /**
   * @pre s1 != null;
   * @pre s2 != null;
   * @result s1.containsAll(result) && s2.containsAll(s2)
   *         && foreach (E e) {s1.contains(e) && s2.contains(e) ? result.contains(e)};
   */
  public static <E> Set<E> intersection(Set<E> s1, Set<E> s2) {
    assert s1 != null;
    assert s2 != null;
    HashSet<E> intersection = new HashSet<E>(s1);
    intersection.retainAll(s2);
    return intersection;
  }


  /**
   * Raw type empty sorted set. Use {@link #emptySortedSet()} for a generic, typed
   * equivalent.
   */
  @SuppressWarnings("unchecked")
  public final static SortedSet EMPTY_SORTED_SET = new EmptySortedSet();

  @SuppressWarnings("unchecked")
  public static final <T> SortedSet<T> emptySortedSet() {
    return EMPTY_SORTED_SET;
  }

  /**
   * Based on java.util.Collections.EmptySet.
   */
  private static class EmptySortedSet
      extends AbstractSet<Object>
      implements Serializable, SortedSet<Object> {

    @Override
    public final Iterator<Object> iterator() {
      return new Iterator<Object>() {
        public boolean hasNext() {
          return false;
        }
        public Object next() {
          throw new NoSuchElementException();
        }
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }

    @Override
    public final int size() {
      return 0;
    }

    @Override
    public boolean contains(Object obj) {
      return false;
    }

    // Preserves singleton property
    private Object readResolve() {
      return EMPTY_SORTED_SET;
    }

    public Comparator<? super Object> comparator() {
      return null;
    }

    public Object first() throws NoSuchElementException {
      throw new NoSuchElementException();
    }

    public Object last() throws NoSuchElementException {
      throw new NoSuchElementException();
    }

    @SuppressWarnings("unchecked")
    public SortedSet<Object> subSet(Object fromElement, Object toElement) {
      return EMPTY_SORTED_SET;
    }

    @SuppressWarnings("unchecked")
    public SortedSet<Object> headSet(Object toElement) {
      return EMPTY_SORTED_SET;
    }

    @SuppressWarnings("unchecked")
    public SortedSet<Object> tailSet(Object fromElement) {
      return EMPTY_SORTED_SET;
    }

  }

}

