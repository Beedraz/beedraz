/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.beedra.util_I.collection.org.apache.commons.collections.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.Unmodifiable;
import org.beedra.util_I.collection.org.apache.commons.collections.iterators.UnmodifiableIterator;
import org.beedra.util_I.collection.org.apache.commons.collections.iterators.UnmodifiableListIterator;

/**
 * Decorates another <code>List</code> to ensure it can't be altered.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * <p><strong>copied from <a href="http://svn.apache.org/viewvc/jakarta/commons/proper/collections/branches/collections_jdk5_branch/src/java/org/apache/commons/collections/list/UnmodifiableList.java?view=markup">Apache Jakarta Commons SVN repository d.d. 2007/3/12</a></strong>
 *   and adapted to generics</p>
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 *
 * @author Jan Dockx
 */
public final class UnmodifiableList<E>
        extends AbstractSerializableListDecorator<E>
        implements Unmodifiable {

    /** Serialization version */
    private static final long serialVersionUID = 6595182819922443652L;

    /**
     * Factory method to create an unmodifiable list.
     *
     * @param list  the list to decorate, must not be null
     * @throws IllegalArgumentException if list is null
     */
    public static <E> List<E> decorate(List<E> list) {
        if (list instanceof Unmodifiable) {
            return list;
        }
        return new UnmodifiableList<E>(list);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     *
     * @param list  the list to decorate, must not be null
     * @throws IllegalArgumentException if list is null
     */
    private UnmodifiableList(List<E> list) {
        super(list);
    }

    //-----------------------------------------------------------------------
    @Override
    public Iterator<E> iterator() {
        return UnmodifiableIterator.decorate(decorated().iterator());
    }

    @Override
    public boolean add(E object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    //-----------------------------------------------------------------------
    @Override
    public ListIterator<E> listIterator() {
        return UnmodifiableListIterator.decorate(decorated().listIterator());
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return UnmodifiableListIterator.decorate(decorated().listIterator(index));
    }

    @Override
    public void add(int index, E object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E set(int index, E object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        List<E> sub = decorated().subList(fromIndex, toIndex);
        return new UnmodifiableList<E>(sub);
    }

}