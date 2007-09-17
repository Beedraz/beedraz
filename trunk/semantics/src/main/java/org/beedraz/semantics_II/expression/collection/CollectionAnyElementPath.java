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
import java.util.Iterator;
import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.path.AbstractDependentPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Return any element from a {@link CollectionBeed}. The result only
 *   changes when that element is removed from the collection, or the
 *   collection changes</p>
 *
 * @author Jan Dockx
 *
 * @invar getCollectionBeedPath() != null;
 * @invar getCollectionBeed() == getCollectionBeedPath().get();
 * @invar getCollectionBeedPath() = old.getCollectionBeedPath();
 */
@Copyright("2007 - $Date$, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision$",
         date     = "$Date$")
public class CollectionAnyElementPath<_Beed_ extends Beed<?>>
    extends AbstractDependentPath<_Beed_> {

  /*<construction>*/
  //-----------------------------------------------------------------

  /**
   * @pre  collectionBeedPath != null;
   * @post getCollectionBeedPath() == collectionBeedPath;
   * @post getCollectionBeed() == collectionBeedPath.get();
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
  protected PathEvent<_Beed_> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    assert events != null;
    _Beed_ oldElement = $selectedBeed;
    @SuppressWarnings("unchecked")
    /*
     * Events can come from:
     * - collection beed path: replace the collection beed and recalculate the selected beed
     * - collection beed: recalculate the selected beed when:
     *       1. the currently selected beed has been removed
     *       2. the collection beed was empty, and some elements have been added
     *          (so the selected beed changes from null to an effective beed)
     */
    CollectionEvent<_Beed_> collectionEvent = (CollectionEvent<_Beed_>)events.get($collectionBeed);
    @SuppressWarnings("unchecked")
    PathEvent<CollectionBeed<_Beed_, ?>> pathEvent =
        (PathEvent<CollectionBeed<_Beed_, ?>>)events.get($collectionBeedPath);
    if (pathEvent != null) {
      $collectionBeed = pathEvent.getNewValue();
    }
    if (( pathEvent != null) ||
        ( (collectionEvent != null) &&
          (collectionEvent.getRemovedElements().contains($selectedBeed) || // the currently selected beed is removed
           $selectedBeed == null && !collectionEvent.getAddedElements().isEmpty()))) { // the collection is no longer empty
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

