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


import org.beedra_II.bean.AbstractBeanBeed;
import org.beedra_II.property.PropertyBeedSelector;
import org.beedra_II.property.association.BidirToManyBeed;
import org.beedra_II.property.integer.EditableIntegerBeed;
import org.beedra_II.property.integer.IntegerSumBeed;
import org.beedra_II.property.string.EditableStringBeed;


/**
 * To be added
 *
 * validation
 * civilization
 * propagation
 */
public class Project extends AbstractBeanBeed {

  public final EditableStringBeed name = new EditableStringBeed(this);

  {
    registerProperty(name); // MUDO this should be done in the constructor of the SEPB
  }

  public final EditableIntegerBeed numberOfDaysAnalysis = new EditableIntegerBeed(this);

  {
    registerProperty(numberOfDaysAnalysis);
  }

  public final EditableIntegerBeed numberOfDaysDevelopment = new EditableIntegerBeed(this);

  {
    registerProperty(numberOfDaysDevelopment);
  }

  public final IntegerSumBeed numberOfDays = new IntegerSumBeed(this);

  {
    numberOfDays.addTerm(numberOfDaysAnalysis);
    numberOfDays.addTerm(numberOfDaysDevelopment);
    registerProperty(numberOfDays); // problem: bean event must be compound, not 2 different events
  }


  public final BidirToManyBeed<Project, Task> tasks = new BidirToManyBeed<Project, Task>(this);

  public final static PropertyBeedSelector<Project, BidirToManyBeed<Project, Task>> tasksSelector =
    new PropertyBeedSelector<Project, BidirToManyBeed<Project, Task>>() {

            public BidirToManyBeed<Project, Task> getPropertyBeed(Project owner) {
              assert owner != null;
              return owner.tasks;
            }
        };

  {
    registerProperty(tasks);
  }

}

