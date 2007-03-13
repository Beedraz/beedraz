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

package org.beedra_II.property.set;


import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.beedra_II.Beed;
import org.beedra_II.aggregate.AggregateBeed;
import org.beedra_II.event.Event;
import org.beedra_II.event.Listener;
import org.beedra_II.property.AbstractPropertyBeed;
import org.ppeew.annotations.vcs.CvsInfo;
import org.ppeew.smallfries.ComparisonUtil;


/**
 * A {@link SetBeed} that mappes the {@link Beed beeds} in
 * a given {@link SetBeed} to a set of other elements using
 * a {@link Mapping}.
 *
 * @mudo We veronderstellen hier dat een mapping injectief is, dwz dat twee
 *       verschillende waarden nooit gemapt worden op hetzelfde beeld
 *       (zie invariant in {@link Mapping}).
 *       Indien dit niet het geval is, moeten we immers een MappedCollectionBeed
 *       maken, die dubbels toelaat.
 * @mudo De bron van een MappedSetBeed definieren we hier als een SetBeed. Dit
 *       kan echter ook een OrderedSetBeed zijn. Dit betekent dat deze beeds in
 *       een hierarchie moeten geplaatst worden.
 * @remark  The value of a SetBeed is always effective (this is an invariant of
 *          the SetBeed class). So, when the source is null, the resulting
 *          mapped set is an effective, empty set.
 *
 * @author  Nele Smeets
 * @author  Peopleware n.v.
 *
 * @invar  getMapping() != null;
 * @invar  get().size() ==
 *           getSource() != null
 *             ? getSource().get().size()
 *             : 0;
 * @invar  (forAll _From_ from;
 *            getSource().contains(from);
 *            get().contains(getMapping().map(from)));
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class MappedSetBeed<_From_ extends Beed<_FromEvent_>, _FromEvent_ extends Event, _To_>
    extends AbstractPropertyBeed<SetEvent<_To_>>
    implements SetBeed<_To_> {

  /**
   * @pre   owner != null;
   * @pre   mapping != null;
   * @post  getOwner() == owner;
   * @post  getMapping() == mapping;
   * @post  getSource() == null;
   * @post  get() != null;
   * @post  get().isEmpty();
   */
  public MappedSetBeed(Mapping<_From_, _To_> mapping, AggregateBeed owner) {
    super(owner);
    $mapping = mapping;
  }


  /*<property name="mapping">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final Mapping<_From_, _To_> getMapping() {
    return $mapping;
  }

  private Mapping<_From_, _To_> $mapping;

  /*</property>*/


  /*<property name="source">*/
  //------------------------------------------------------------------

  /**
   * @basic
   */
  public final SetBeed<_From_> getSource() {
    return $source;
  }

  /**
   * @param   source
   * @post    getSource() == source;
   * @post    get() == the result of mapping the given source
   * @post    The MappedSetBeed is registered as a listener of the given SetBeed.
   * @post    The MappedSetBeed is registered as a listener of all beeds in
   *          the given source. (The reason is that the MappedSetBeed should be
   *          notified (and then recalculate) when one of the beeds in the source
   *          changes.)
   * @post    The listeners of this beed are notified when the value changes.
   */
  public final void setSource(SetBeed<_From_> source) {
    $source = source;
    if (source != null) {
      // register the MappedSetBeed as listener of the given source
      source.addListener($sourceListener);
      // register the MappedSetBeed as listener of all beeds in the given source
      for (_From_ beed : source.get()) {
        beed.addListener($beedListener);
      }
    }
    // recalculate and notify the listeners if the value has changed
    Set<_To_> oldValue = $mappedSet;
    recalculate();
    if (! ComparisonUtil.equalsWithNull(oldValue, $mappedSet)) {
      fireChangeEvent(
        new SetEvent<_To_>(
          MappedSetBeed.this, $mappedSet,  oldValue, null));
    }
  }

  private SetBeed<_From_> $source;

  /*</property>*/


  /**
   * A listener that will be registered as listener of the {@link #getSource()}.
   */
  private final Listener<SetEvent<_From_>> $sourceListener =
        new Listener<SetEvent<_From_>>() {

    /**
     * @post    The MappedSetBeed is registered as a listener of all beeds
     *          that are added to the source by the given event. (The reason is that
     *          the MappedSetBeed should be notified (and then recalculate) when one
     *          of the beeds changes.)
     * @post    The MappedSetBeed is removed as listener of all beeds
     *          that are removed from the source by the given event.
     * @post    get() == the result of mapping the elements of the given source
     * @post    The listeners of this beed are notified when the set changes.
     */
    public void beedChanged(SetEvent<_From_> event) {
      // add the MappedSetBeed as listener of all beeds that are added to the source by the given event
      Set<_From_> added = event.getAddedElements();
      Set<_To_> addedMapped = new HashSet<_To_>();
      for (_From_ beed : added) {
        beed.addListener($beedListener);
        addedMapped.add(getMapping().map(beed));
      }
      // remove the MappedSetBeed as listener from all beeds that are removed from the source by the given event
      Set<_From_> removed = event.getRemovedElements();
      Set<_To_> removedMapped = new HashSet<_To_>();
      for (_From_ beed : removed) {
        beed.removeListener($beedListener);
        removedMapped.add(getMapping().map(beed));
      }

      // recalculate and notify the listeners if the value has changed
      Set<_To_> oldValue = $mappedSet;
      recalculate();
      if (! ComparisonUtil.equalsWithNull(oldValue, $mappedSet)) {
        fireChangeEvent(
          new SetEvent<_To_>(
            MappedSetBeed.this, addedMapped, removedMapped, event.getEdit()));
      }
    }

  };


  private final Listener<_FromEvent_> $beedListener = new Listener<_FromEvent_>() {

    /**
     * @post    get() == the result of mapping the elements of the given source
     * @post    The listeners of this beed are notified.
     */
    public void beedChanged(_FromEvent_ event) {
      // recalculate and notify the listeners if the value has changed
      Set<_To_> oldValue = $mappedSet;
      recalculate();
      if (! ComparisonUtil.equalsWithNull(oldValue, $mappedSet)) {
        fireChangeEvent(new SetEvent<_To_>(MappedSetBeed.this, null, null, event.getEdit()));
      }
    }

  };

  /**
   * @invar  $mappedSet != null;
   */
  private Set<_To_> $mappedSet = new HashSet<_To_>();

  /**
   * @basic
   */
  public final Set<_To_> get() {
    return new AbstractSet<_To_>() {

      @Override
      public Iterator<_To_> iterator() {
        return $mappedSet.iterator();
      }

      /**
       * The size of the source set, if the source is effective.
       * Zero, if the source set is not effective.
       */
      @Override
      public int size() {
        return $mappedSet.size();
      }

    };
  }


  /**
   * The value of $mappedSet is recalculated. This is done by iterating over the beeds
   * in the source set beed.
   * When the source is null, the result is an empty set.
   * When the source contains zero beeds, the result is an empty set.
   * Otherwise, the resulting set contains the maps of all beeds in the given set.
   */
  public void recalculate() {
    $mappedSet = new HashSet<_To_>();
    Set<_From_> fromSet = getSource() != null? getSource().get(): new HashSet<_From_>();
    for (_From_ from : fromSet) {
      $mappedSet.add(getMapping().map(from));
    }
  }


  /**
   * @post  result != null;
   * @post  result.getAddedElements().equals(get());
   * @post  result.getRemovedElements().isEmpty();
   * @post  result.getEdit() == null;
   * @post  result.getEditState() == null;
   */
  @Override
  protected SetEvent<_To_> createInitialEvent() {
    return new SetEvent<_To_>(this, get(), null, null);
  }

}

