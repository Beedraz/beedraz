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

package org.beedra.util_I.collection;


import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @mudo doc
 *
 * <p>Based on <a href="http://svn.apache.org/viewvc/jakarta/commons/proper/collections/branches/collections_jdk5_branch/src/java/org/apache/commons/collections/set/AbstractSerializableSetDecorator.java?view=markup">Apache Jakarta Commons SVN repository d.d. 2007/3/12</a>.</p>
 *
 * @author Stephen Colebourne
 * @author Henning P. Schmiedehausen
 *
 * @author Jan Dockx
 */
public interface OrderedSet<E> extends Set<E> {

    /**
     * Gets an unmodifiable view of the order of the Set.
     *
     * @return an unmodifiable list view
     */
    List<E> asList();

    E get(int index);

    int indexOf(E object);

    void add(int index, E object);

    boolean addAll(int index, Collection<?> coll);

    E remove(int index);

}