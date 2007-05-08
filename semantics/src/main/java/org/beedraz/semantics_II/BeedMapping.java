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

package org.beedraz.semantics_II;


import static org.ppeew.annotations_I.License.Type.APACHE_V2;

import org.beedraz.semantics_II.bean.BeanBeed;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;
import org.ppeew.smallfries_I.Mapping;


/**
 * A mapping maps a given beed of type _From_ to an
 * element of type _To_.
 *
 * @author  Jan Dockx
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public interface BeedMapping<_From_ extends Beed<?>,
                             _To_>
    extends Mapping<_From_, _To_> {

  /**
   * Returns true when the result of the mapping can change when the
   * source beed changes. Returns false otherwise.
   *
   * example1: Suppose we have the following {@link BeanBeed}:
   *
   *           public class Person extends AbstractBeanBeed {
   *             public final EditableStringBeed name = new EditableStringBeed(this);
   *           }
   *
   *           and suppose we have the following mapping:
   *
   *           BeedMapping<Person, StringBeed> mapping =
   *             new BeedMapping<Person, StringBeed>() {
   *               public StringBeed map(Person person) {
   *                 return person.name;
   *               }
   *               public boolean dependsOnBeeds() {
   *                 return false;
   *               }
   *             };
   *
   *          The result of the mapping does not change when the Person beed
   *          changes. The StringBeed representing the name is always the same.
   *
   * example2: Suppose we have the following {@link BeanBeed}:
   *
   *           public class Person extends AbstractBeanBeed {
   *             public final EditableStringBeed name = new EditableStringBeed(this);
   *           }
   *
   *           and suppose we have the following mapping:
   *
   *           BeedMapping<Person, String> mapping =
   *             new BeedMapping<Person, String>() {
   *               public String map(Person person) {
   *                 return person.name.get();
   *               }
   *               public boolean dependsOnBeeds() {
   *                 return true;
   *               }
   *             };
   *
   *          The result of the mapping can change when the Person beed changes:
   *          when the name of the person changes from "John" to "Paul",
   *          the result of the mapping changes accordingly.
   *
   */
  boolean dependsOnBeed();

}

