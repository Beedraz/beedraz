package org.toryt.util_I.collections;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.toryt.util_I.annotations.vcs.CvsInfo;


/**
 * <p>General array utility methods.</p>
 *
 * @author Jan Dockx
 */
@CvsInfo(revision = "$Revision$",
         date     = "$Date$",
         state    = "$State$",
         tag      = "$Name$")
public abstract class ArrayUtils {

  public static <_Base_> _Base_[] subArray(_Base_[] array, int fromInclusive, int toExclusive) {
    assert fromInclusive >= 0;
    assert toExclusive >= 0;
    assert fromInclusive <= toExclusive;
    int lenght = toExclusive - fromInclusive;
    @SuppressWarnings("unchecked") _Base_[] result =
        (_Base_[])Array.newInstance(array.getClass().getComponentType(), lenght);
    System.arraycopy(array, fromInclusive, result, 0, lenght);
    return result;
  }

  /**
   * @pre resultComponentType != null;
   */
  public static <_InBase_, _ResultBase_>
      _ResultBase_[] flatten2(_InBase_[] array,
                                      Class<_ResultBase_> resultComponentType) {
    assert resultComponentType != null;
    LinkedList<_ResultBase_> resultList = new LinkedList<_ResultBase_>();
    flattenHelper(array, resultList);
    @SuppressWarnings("unchecked") _ResultBase_[] result =
        (_ResultBase_[])Array.newInstance(resultComponentType, resultList.size());
    /* unchecked cast because Java API for array construction is not generic */
    resultList.toArray(result);
    return result;
  }

  /**
   * @pre resultComponentType != null;
   */
  public static <_ResultBase_> void flattenHelper(Object[] array,
                                                           LinkedList<_ResultBase_> acc) {
    for (Object firstLevel : array) {
      if ((firstLevel == null) || (! firstLevel.getClass().isArray())) {
        // firstLevel is the final level; it must me of tye _ResultComponentType_
        @SuppressWarnings("unchecked") _ResultBase_ simple = (_ResultBase_)firstLevel;
        // unchecked cast: it is ok, or pre's are violated
        acc.add(simple);
      }
      else {
        // firstLevel is an array; recursive
        flattenHelper((Object[])firstLevel, acc);
      }
    }
  }

  /**
   * {@link Arrays#asList} returns a dynamic list (changes in the array are seen in the
   * list, and vice versa. This does the same, but returns a new list, uncoupled with
   * the array. The result is unmodifiable.
   *
   * @result result.equals(Arrays.asList(a));
   * @throws NullPointerException
   *         a == null;
   */
  public static <T> List<T> asFreshList(T... a) throws NullPointerException {
    if (a == null) {
      throw new NullPointerException();
    }
    List<T> result = new ArrayList<T>(a.length);
    for (T t : a) {
      result.add(t);
    }
    return Collections.unmodifiableList(result);
  }

}