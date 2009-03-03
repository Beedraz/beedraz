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

package org.beedra.example.z_beedra;


import static org.ppwcode.util.smallfries_I.MultiLineToStringUtil.indent;

import java.util.Set;

import org.beedraz.semantics_II.bean.AbstractBeanBeed;
import org.beedraz.semantics_II.expression.association.set.BidirToManyBeed;
import org.beedraz.semantics_II.expression.association.set.EditableBidirToOneBeed;
import org.beedraz.semantics_II.expression.string.EditableStringBeed;
import org.ppwcode.util.smallfries_I.ComparisonUtil;


/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 */
public class Task extends AbstractBeanBeed {

  private final static String SPACE = " ";

  public final EditableBidirToOneBeed<Project, Task> project = new EditableBidirToOneBeed<Project, Task>(this) {
    @Override
    public boolean isAcceptable(BidirToManyBeed<Project, Task> goal) {
      return uniqueNameInProject(name.get(), goal == null ? null : goal.getOwner());
    }
  };

  public final EditableStringBeed name = new EditableStringBeed(this) {
    @Override
    public boolean isAcceptable(String goal) {
      return ! goal.endsWith(SPACE) && uniqueNameInProject(goal, project.getOne());
    }
  };

  private boolean uniqueNameInProject(String nameToVerify, Project p) {
    if (p == null) {
      return true;
    }
    Set<Task> tasks = p.tasks.get();
    for (Task task : tasks) {
      if (ComparisonUtil.equalsWithNull(task.name.get(), nameToVerify)) {
        if (task != this) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  protected String otherToStringInformation() {
    return ", name: " + name.get();
  }

  @Override
  public void toString(StringBuffer sb, int level) {
    super.toString(sb, level);
    sb.append(indent(level + 1) + "name: " + name.get() + "\n");
  }

}

