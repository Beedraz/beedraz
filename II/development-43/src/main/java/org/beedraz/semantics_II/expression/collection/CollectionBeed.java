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

package org.beedraz.semantics_II.expression.collection;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import java.util.Collection;

import org.beedraz.semantics_II.expression.SimpleExpressionBeed;
import org.beedraz.semantics_II.expression.number.integer.IntegerBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * @author  Jan Dockx
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface CollectionBeed<_Element_,
                                _Event_ extends CollectionEvent<_Element_>>
    extends SimpleExpressionBeed<Collection<_Element_>, _Event_> {

  /**
   * @basic
   */
  Collection<_Element_> get();

  /**
   * The number of elements in the collection.
   * @result  result != null;
   * @result  result.get() == get().size();
   */
  IntegerBeed<?> getSize();

  /**
   * The number of distinct elements in the collection.
   * @result  result != null;
   * @result  result.get() == # {element : get().contains(element)};
   */
  IntegerBeed<?> getCardinality();

}

