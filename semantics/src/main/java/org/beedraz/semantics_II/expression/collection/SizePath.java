/*<license>
Copyright 2007 - $Date: 2007-09-17 10:29:37 +0200 (ma, 17 sep 2007) $ by the authors mentioned below.

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

import java.util.Map;

import org.beedraz.semantics_II.Beed;
import org.beedraz.semantics_II.Edit;
import org.beedraz.semantics_II.Event;
import org.beedraz.semantics_II.expression.number.integer.IntegerBeed;
import org.beedraz.semantics_II.path.AbstractDependentPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathEvent;
import org.ppeew.annotations_I.Copyright;
import org.ppeew.annotations_I.License;
import org.ppeew.annotations_I.vcs.SvnInfo;


/**
 * <p>Returns the size of a {@link CollectionBeed} that is given by a {@link Path}.
 *   The result changes when the size of the collection changes, or when the path
 *   references another collecion.</p>
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getCollectionBeedPath() != null;
 * @invar  getCollectionBeed() == getCollectionBeedPath().get();
 * @invar  getCollectionBeedPath() = old.getCollectionBeedPath();
 * @invar  get() == getCollectionBeed() != null
 *                    ? getCollectionBeed().getSize()
 *                    : null;
 */
@Copyright("2007 - $Date: 2007-09-17 10:29:37 +0200 (ma, 17 sep 2007) $, Beedraz authors")
@License(APACHE_V2)
@SvnInfo(revision = "$Revision: 1070 $",
         date     = "$Date: 2007-09-17 10:29:37 +0200 (ma, 17 sep 2007) $")
public class SizePath extends AbstractDependentPath<IntegerBeed<?>> {


  /*<construction>*/
  //-----------------------------------------------------------------

  /**
   * @pre  collectionBeedPath != null;
   * @post getCollectionBeedPath() == collectionBeedPath;
   * @post getCollectionBeed() == collectionBeedPath.get();
   */
  public SizePath(Path<? extends CollectionBeed<?, ?>> collectionBeedPath) {
    assert collectionBeedPath != null;
    $collectionBeedPath = collectionBeedPath;
    addUpdateSource(collectionBeedPath);
    $collectionBeed = $collectionBeedPath.get();
    if ($collectionBeed != null) {
      addUpdateSource($collectionBeed);
    }
    computeSize();
  }

  /*</construction>*/


  /*<section name="dependent">*/
  //-----------------------------------------------------------------

  @Override
  protected PathEvent<IntegerBeed<?>> filteredUpdate(Map<Beed<?>, Event> events, Edit<?> edit) {
    assert events != null;
    IntegerBeed<?> oldElement = $size;
    /*
     * Events can come from:
     * - collection beed path: replace the collection beed and recalculate the size
     * - collection beed: recalculate the size
     */
    @SuppressWarnings("unchecked")
    PathEvent<CollectionBeed<IntegerBeed<?>, ?>> pathEvent =
        (PathEvent<CollectionBeed<IntegerBeed<?>, ?>>)events.get($collectionBeedPath);
    if (pathEvent != null) {
      $collectionBeed = pathEvent.getNewValue();
    }
    computeSize();
    if (oldElement != $size) {
      return new PathEvent<IntegerBeed<?>>(SizePath.this, oldElement, $size, edit);
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
  public final Path<? extends CollectionBeed<?, ?>> getCollectionBeedPath() {
    return $collectionBeedPath;
  }

  private final Path<? extends CollectionBeed<?, ?>> $collectionBeedPath;

  /*</property>*/



  /*<property name="bean beed">*/
  //-----------------------------------------------------------------

  /**
   * @basic
   */
  public final CollectionBeed<?, ?> getCollectionBeed() {
    return $collectionBeed;
  }

  private CollectionBeed<?, ?> $collectionBeed;

  /*</property>*/



  /*<property name="selected">*/
  //-----------------------------------------------------------------

  public final IntegerBeed<?> get() {
    return $size;
  }

  /**
   * If {@link #$collectionBeed} is null, set the size
   * to {@code null}.
   * If {@link #$collectionBeed} is not null, set
   * the size to the size of the collection.
   *
   */
  private void computeSize() {
    if ($collectionBeed == null) {
      $size = null;
    }
    else {
      $size = $collectionBeed.getSize();
    }
  }

  private IntegerBeed<?> $size;

  /*</property>*/

}

