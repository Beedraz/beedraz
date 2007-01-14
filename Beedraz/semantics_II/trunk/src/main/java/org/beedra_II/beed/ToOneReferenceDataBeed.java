package org.beedra_II.beed;


import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.beedra_II.BeedraBean;
import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 *
 * @invar getProject() != null ? getProject.getTasks().contains(this);
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public class ToOneReferenceDataBeed<_One_ extends BeedraBean,
                                _Many_ extends BeedraBean>
    extends AbstractDataBeed<_Many_, _One_> {

  public ToOneReferenceDataBeed(_Many_ bean, String toManyPropertyName) {
    super(bean);
    assert toManyPropertyName != null;
    $toManyPropertyName = toManyPropertyName;
  }

  public final String getToManyPropertyName() {
    return $toManyPropertyName;
  }

  private String $toManyPropertyName;

  public final _One_ get() {
    return $toManyRef.getBean();
  }

  public final ToManyReferenceDataBeed<_One_, _Many_> getToManyReference() {
    return $toManyRef;
  }

  public final void set(_One_ one) {
    if (get() != one) {
      if ($toManyRef != null) {
        $toManyRef.remove(getBean());
      }
      _One_ oldValue = get();
      $toManyRef = toManyRef(one, getToManyPropertyName());
      if ($toManyRef != null) {
        $toManyRef.add(getBean());
      }
      UndoableBeedChangeEvent<_One_> event =
        new UndoableBeedChangeEvent<_One_>(this, oldValue, get());
      fireChangeEvent(event);
    }
  }

  private ToManyReferenceDataBeed<_One_, _Many_> toManyRef(_One_ one,
                                                       String toManyPropertyName) {
    if (one == null) {
      return null;
    }
    else {
      try {
        BeanUtils.getProperty(one, toManyPropertyName);
      }
      catch (IllegalAccessException e) {
        throw new IllegalArgumentException(); // or something
      }
      catch (InvocationTargetException e) {
        throw new IllegalArgumentException(); // or something
      }
      catch (NoSuchMethodException e) {
        throw new IllegalArgumentException(); // or something
      }
      return null;
    }
  }

  private ToManyReferenceDataBeed<_One_, _Many_> $toManyRef;

}

