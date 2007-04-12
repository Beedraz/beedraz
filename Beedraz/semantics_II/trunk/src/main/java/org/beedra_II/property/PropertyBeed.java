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

package org.beedra_II.property;


import org.beedra_II.Beed;
import org.beedra_II.Event;
import org.beedra_II.aggregate.AggregateBeed;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>{@link Beed Beeds} <em>of
 *   another beed</em>. They are <em>owned</em>
 *   (see {@link #getOwner()}), they do not exist on their own.</p>
 * <p>{@code PropertyBeeds} are like properties
 *   of JavaBeans. The JavaBean is the owner of the
 *   property.</p>
 * <p>Events send by {@code PropertyBeeds} are just
 *   {@link Event BeedEvents}.</p>
 *
 * @author Jan Dockx
 *
 * @invar getOwner() == this'getOwner();
 *
 * @mudo since the owner os not always a Bean, other name? owned beed? weak beed?
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public interface PropertyBeed<_Event_ extends Event>
    extends Beed<_Event_> {

  /**
   * @basic
   */
  AggregateBeed getOwner();

}

