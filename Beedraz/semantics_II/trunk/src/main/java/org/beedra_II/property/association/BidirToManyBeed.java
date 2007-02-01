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

package org.beedra_II.property.association;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.beedra_II.bean.BeanBeed;
import org.beedra_II.property.AbstractPropertyBeed;
import org.beedra_II.property.set.FinalSetEvent;
import org.beedra_II.property.set.SetBeed;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * @invar getProject() != null ? getProject.getTasks().contains(this);
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class BidirToManyBeed<_One_ extends BeanBeed,
                              _Many_ extends BeanBeed>
    extends AbstractPropertyBeed<FinalSetEvent<_Many_>>
    implements SetBeed<_Many_, FinalSetEvent<_Many_>> {

  public BidirToManyBeed(_One_ bean) {
    super(bean);
  }

  @SuppressWarnings("unchecked")
  public final _One_ getOwner() {
    return (_One_)super.getOwner();
  }

  public final Set<_Many_> get() {
    return Collections.unmodifiableSet($many);
  }

  void add(_Many_ many) {
    assert many != null;
    $many.add(many);
  }

  void fireAddedEvent(_Many_ many) {
    Set<_Many_> added = new HashSet<_Many_>();
    added.add(many);
    fireChangeEvent(new FinalSetEvent<_Many_>(this, added, null));
  }

  void remove(_Many_ many) {
    $many.remove(many);
  }

  void fireRemovedEvent(_Many_ many) {
    Set<_Many_> removed = new HashSet<_Many_>();
    removed.add(many);
    fireChangeEvent(new FinalSetEvent<_Many_>(this, null, removed));
  }

  private final Set<_Many_> $many = new HashSet<_Many_>();

  @Override
  protected final FinalSetEvent<_Many_> createInitialEvent() {
    return new FinalSetEvent<_Many_>(this, null, $many); // event constructor copies set
  }

}

