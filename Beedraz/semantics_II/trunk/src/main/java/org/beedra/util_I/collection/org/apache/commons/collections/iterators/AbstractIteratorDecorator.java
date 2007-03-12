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
package org.beedra.util_I.collection.org.apache.commons.collections.iterators;

import java.util.Iterator;

/**
 * Provides basic behaviour for decorating an iterator with extra functionality.
 * <p>
 * All methods are forwarded to the decorated iterator.
 *
 * <p><strong>copied from <a href="http://svn.apache.org/viewvc/jakarta/commons/proper/collections/branches/collections_jdk5_branch/src/java/org/apache/commons/collections/iterators/AbstractIteratorDecorator.java?view=markup">Apache Jakarta Commons SVN repository d.d. 2007/3/12</a></strong>
 *   and adapted to generics</p>
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author James Strachan
 * @author Stephen Colebourne
 *
 * @author Jan Dockx
 */
public class AbstractIteratorDecorator<E> implements Iterator<E> {

    /** The iterator being decorated */
    protected final Iterator<E> iterator;

    //-----------------------------------------------------------------------
    /**
     * Constructor that decorates the specified iterator.
     *
     * @param iterator  the iterator to decorate, must not be null
     * @throws IllegalArgumentException if the collection is null
     */
    public AbstractIteratorDecorator(Iterator<E> iter) {
        super();
        if (iter == null) {
            throw new IllegalArgumentException("Iterator must not be null");
        }
        this.iterator = iter;
    }

    /**
     * Gets the iterator being decorated.
     *
     * @return the decorated iterator
     */
    protected Iterator<E> getIterator() {
        return iterator;
    }

    //-----------------------------------------------------------------------
    public boolean hasNext() {
        return iterator.hasNext();
    }

    public E next() {
        return iterator.next();
    }

    public void remove() {
        iterator.remove();
    }

}