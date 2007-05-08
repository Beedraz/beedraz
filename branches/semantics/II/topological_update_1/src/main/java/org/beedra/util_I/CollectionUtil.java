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


import java.util.HashSet;
import java.util.Set;

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

}

