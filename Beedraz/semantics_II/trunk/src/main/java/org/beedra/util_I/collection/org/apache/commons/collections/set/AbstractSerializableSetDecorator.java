/*
 *  Copyright 2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.beedra.util_I.collection.org.apache.commons.collections.set;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Serializable subclass of AbstractSetDecorator.
 * <p><strong>This class is copied from Apache Jakarta Commons Collections 3.2, and made
 *   generic.</strong></p>
 *
 * <p><strong>copied from <a href="http://svn.apache.org/viewvc/jakarta/commons/proper/collections/branches/collections_jdk5_branch/src/java/org/apache/commons/collections/set/AbstractSerializableSetDecorator.java?view=markup">Apache Jakarta Commons SVN repository d.d. 2007/3/12</a></strong>
 *   and adapted to generics</p>
 *
 * @author Stephen Colebourne
 * @since Commons Collections 3.1
 *
 * @author Jan Dockx
 */
public abstract class AbstractSerializableSetDecorator<E>
        extends AbstractSetDecorator<E>
        implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 1229469966212206107L;

    /**
     * Constructor.
     */
    protected AbstractSerializableSetDecorator(Set<E> set) {
        super(set);
    }

    //-----------------------------------------------------------------------
    /**
     * Write the set out using a custom routine.
     *
     * @param out  the output stream
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(collection);
    }

    /**
     * Read the set in using a custom routine.
     *
     * @param in  the input stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        collection = (Collection)in.readObject();
    }

}
