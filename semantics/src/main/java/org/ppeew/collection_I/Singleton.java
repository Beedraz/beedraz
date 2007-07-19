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

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * A {@link Set} with exactly one element. The implementation is unmodifiable.
 *
 * @deprecated Use {@link Collections#singleton(Object)}
 */
@Deprecated
@Copyright("2007 - $Date$, PeopleWare n.v.")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class Singleton<_Element_> extends AbstractUnmodifiableSet<_Element_> {

  /**
   * @post getElement() == element;
   */
  public Singleton(_Element_ element) {
    $element = element;
  }

  /**
   * @basic
   */
  public final _Element_ getElement() {
    return $element;
  }

  private _Element_ $element;

  @Override
  public Iterator<_Element_> iterator() {
    return new Iterator<_Element_>() {

      private boolean $elementReturned = false;

      public final boolean hasNext() {
        return ! $elementReturned;
      }

      public _Element_ next() throws NoSuchElementException {
        if ($elementReturned) {
          throw new NoSuchElementException();
        }
        $elementReturned = true;
        return $element;
      }

      public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
      }

    };
  }

  @Override
  public final int size() {
    return 1;
  }

}

