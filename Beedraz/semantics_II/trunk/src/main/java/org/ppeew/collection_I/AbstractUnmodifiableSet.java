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


import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;


/**
 * Unmodifiable {@link OrderedSet} backed by an {@link OrderedSet}.
 *
 * @author Jan Dockx
 */
public abstract class AbstractUnmodifiableSet<E>
    extends AbstractSet<E> {

  protected abstract class AbstractUnmodifiableIterator implements Iterator<E> {

    /**
     * @result false;
     * @throws UnsupportedOperationException
     *         true;
     */
    public final void remove() {
      throw new UnsupportedOperationException();
    }

  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  @Override
  public final boolean add(E object) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  @Override
  public final boolean addAll(Collection<? extends E> c) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  @Override
  public final boolean removeAll(Collection<?> c) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @result false;
   * @throws UnsupportedOperationException
   *         true;
   */
  @Override
  public final boolean remove(Object object) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

}