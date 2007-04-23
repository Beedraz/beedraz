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

package org.beedraz.semantics_II.path;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.edit.Edit;
import org.beedraz.semantics_II.expression.collection.CollectionBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEvent;
import org.beedraz.semantics_II.topologicalupdate.UpdateSource;
import org.ppeew.annotations_I.vcs.CvsInfo;


/**
 * <p>Return any element from a {@link CollectionBeed}. The result only
 *   changes when that element is removed from the collection, or the
 *   collection changes</p>
 *
 * @author Jan Dockx
 *
 * @invar getBeanBeedPath() != null;
 * @invar getBeanBeed() == getBeanBeedPath().get();
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class CollectionAnyElementPath<_Beed_ extends Beed<?>>
    extends AbstractDependentPath<_Beed_> {

  /*<construction>*/
  //-----------------------------------------------------------------

  /**
   * @pre beanBeedPath != null;
   * @post getBeanBeedPath() == beanBeedPath;
   * @post getBeanBeed() == beanBeedPath.get();
   */
  public CollectionAnyElementPath(Path<? extends CollectionBeed<_Beed_, ?>> collectionBeedPath) {
    assert collectionBeedPath != null;
    $collectionBeedPath = collectionBeedPath;
    addUpdateSource(collectionBeedPath);
    $collectionBeed = $collectionBeedPath.get();
    if ($collectionBeed != null) {
      addUpdateSource($collectionBeed);
    }
    select();
  }

  /*</construction>*/



  /*<section name="dependent">*/
  //-----------------------------------------------------------------

  @Override
  protected PathEvent<_Beed_> filteredUpdate(Map<UpdateSource, Event> events, Edit<?> edit) {
    assert events != null;
    _Beed_ oldElement = $selectedBeed;
    @SuppressWarnings("unchecked")
    SetEvent<_Beed_> setEvent = (SetEvent<_Beed_>)events.get($collectionBeed);
    PathEvent<CollectionBeed<_Beed_, ?>> pathEvent =
        (PathEvent<CollectionBeed<_Beed_, ?>>)events.get($collectionBeedPath);
    if (pathEvent != null) {
      $collectionBeed = pathEvent.getNewValue();
    }
    if ((pathEvent != null) || ((setEvent != null) && setEvent.getRemovedElements().contains($selectedBeed))) {
      select();
    }
    if (oldElement != $selectedBeed) {
      return new PathEvent<_Beed_>(CollectionAnyElementPath.this, oldElement, $selectedBeed, edit);
    }
    else {
      return null;
    }
  }

  /*</section>*/



  /*<property name="collection beed path">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final Path<? extends CollectionBeed<_Beed_, ?>> getCollectionBeedPath() {
    return $collectionBeedPath;
  }

  private final Path<? extends CollectionBeed<_Beed_, ?>> $collectionBeedPath;

  /*</property>*/



  /*<property name="bean beed">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final CollectionBeed<_Beed_, ?> getCollectionBeed() {
    return $collectionBeed;
  }

  private CollectionBeed<_Beed_, ?> $collectionBeed;

  /*</property>*/



  /*<property name="selected">*/
  //-----------------------------------------------------------------

  public final _Beed_ get() {
    return $selectedBeed;
  }

  /**
   * If {@link #$collectionBeed} is null, set selected beed
   * to {@code null}.
   * If {@link #$collectionBeed} is not null, but empty, set
   * selected beed to {@code null}.
   * If {@link #$collectionBeed} is not null and not empty, set
   * selected beed to any element from the collection.
   *
   */
  private void select() {
    if ($collectionBeed == null) {
      $selectedBeed = null;
    }
    else {
      Collection<_Beed_> collection = $collectionBeed.get();
      if (collection.isEmpty()) {
        $selectedBeed = null;
      }
      else {
        Iterator<_Beed_> iter = $collectionBeed.get().iterator();
        assert iter.hasNext();
        $selectedBeed = iter.next();
      }
    }
  }

  private _Beed_ $selectedBeed;

  /*</property>*/

}

