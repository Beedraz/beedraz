package org.beedraz.semantics_II.demo;

import static org.beedraz.semantics_II.path.Paths.path;

import org.beedraz.semantics_II.EditStateException;
import org.beedraz.semantics_II.IllegalEditException;
import org.beedraz.semantics_II.aggregate.AbstractAggregateBeed;
import org.beedraz.semantics_II.aggregate.AggregateBeed;
import org.beedraz.semantics_II.expression.association.set.BidirToOneEdit;
import org.beedraz.semantics_II.expression.association.set.ordered.OrderedBidirToOneEdit;
import org.beedraz.semantics_II.expression.bool.BooleanBeed;
import org.beedraz.semantics_II.expression.bool.BooleanBeeds;
import org.beedraz.semantics_II.expression.collection.CollectionAnyElementPath;
import org.beedraz.semantics_II.expression.collection.set.EditableSetBeed;
import org.beedraz.semantics_II.expression.collection.set.FilteredSetBeed;
import org.beedraz.semantics_II.expression.collection.set.MappedSetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetBeed;
import org.beedraz.semantics_II.expression.collection.set.SetEdit;
import org.beedraz.semantics_II.expression.collection.set.UnionBeed;
import org.beedraz.semantics_II.expression.collection.set.UnionSetBeed;
import org.beedraz.semantics_II.expression.enumeration.ConditionalPath;
import org.beedraz.semantics_II.expression.enumeration.EditableEnumBeed;
import org.beedraz.semantics_II.expression.enumeration.EnumBeeds;
import org.beedraz.semantics_II.expression.enumeration.EnumEdit;
import org.beedraz.semantics_II.expression.enumeration.EnumSwitchPath;
import org.beedraz.semantics_II.expression.number.real.RealBeed;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleBeed;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleBeeds;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleConstantBeed;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleEdit;
import org.beedraz.semantics_II.expression.number.real.double64.DoubleSumBeed;
import org.beedraz.semantics_II.expression.number.real.double64.EditableDoubleBeed;
import org.beedraz.semantics_II.expression.number.real.double64.stat.DoubleStatBeeds;
import org.beedraz.semantics_II.expression.string.StringBeed;
import org.beedraz.semantics_II.expression.string.StringEdit;
import org.beedraz.semantics_II.path.ConstantPath;
import org.beedraz.semantics_II.path.Path;
import org.beedraz.semantics_II.path.PathFactory;
import org.beedraz.semantics_II.path.Paths;

public class BeedrazDemo {

  public static void main(String[] args) throws EditStateException, IllegalEditException {
    testSum();
    testSum2();
    testSum3();
    testSum4();
    testNegative();
    testPower();
    testSetBeed();
    testFilteredSetBeed();
    testArithmeticMean();
    testMappedSetBeed();
    testUnionBeed();
    testUnionSetBeed();
    testConditionalPath();
    testSwitch();
    testAssociation();
    testOrderedAssociation();
    testCollectionAnyElementPath();
  }

  private static void testSum() {
    System.out.println("testSum");
    DoubleConstantBeed c1 = new DoubleConstantBeed(2.0);
    DoubleConstantBeed c2 = new DoubleConstantBeed(3.0);
    DoubleSumBeed sumBeed1 = new DoubleSumBeed();
    sumBeed1.setLeftOperandPath(new ConstantPath<DoubleConstantBeed>(c1));
    sumBeed1.setRightOperandPath(new ConstantPath<DoubleConstantBeed>(c2));
    System.out.println(sumBeed1.getdouble());
  }

  private static void testSum2() {
    System.out.println("testSum2");
    DoubleBeed c3 = DoubleBeeds.constant(2.0);
    DoubleBeed c4 = DoubleBeeds.constant(3.0);
    DoubleBeed sumBeed2 = DoubleBeeds.sum(c3, c4);
    System.out.println(sumBeed2.getdouble());
  }

  private static void testSum3() throws EditStateException, IllegalEditException {
    System.out.println("testSum3");
    EditableDoubleBeed c5 =
      new EditableDoubleBeed(new AbstractAggregateBeed(){});
    DoubleEdit edit5 = new DoubleEdit(c5);
    edit5.setGoal(2.0);
    edit5.perform();
    EditableDoubleBeed c6 =
      new EditableDoubleBeed(new AbstractAggregateBeed(){});
    DoubleEdit edit6 = new DoubleEdit(c6);
    edit6.setGoal(3.0);
    edit6.perform();
    DoubleBeed sumBeed3 = DoubleBeeds.sum(c5, c6);
    System.out.println(sumBeed3.getdouble());
    edit5 = new DoubleEdit(c5);
    edit5.setGoal(4.0);
    edit5.perform();
    System.out.println(sumBeed3.getdouble());
  }

  private static void testSum4() throws EditStateException, IllegalEditException {
    System.out.println("testSum4");
    EditableDoubleBeed c5 =
      (EditableDoubleBeed) DoubleBeeds.editableDoubleBeed(
          2.0, new AbstractAggregateBeed(){});
    DoubleBeed c6 =
      DoubleBeeds.editableDoubleBeed(3.0, new AbstractAggregateBeed(){});
    DoubleBeed sumBeed3 = DoubleBeeds.sum(c5, c6);
    System.out.println(sumBeed3.getdouble());
    DoubleEdit edit5 = new DoubleEdit(c5);
    edit5.setGoal(4.0);
    edit5.perform();
    System.out.println(sumBeed3.getdouble());
  }

  private static void testNegative() throws EditStateException, IllegalEditException {
    System.out.println("testNegative");
    AggregateBeed owner = new AbstractAggregateBeed(){};
    EditableDoubleBeed d1 =
      (EditableDoubleBeed) DoubleBeeds.editableDoubleBeed(2.0, owner);
    DoubleBeed negativeBeed = DoubleBeeds.negative(d1);
    System.out.println(negativeBeed.getdouble());
    DoubleEdit edit1 = new DoubleEdit(d1);
    edit1.setGoal(4.0);
    edit1.perform();
    System.out.println(negativeBeed.getdouble());
  }

  private static void testPower() throws EditStateException, IllegalEditException {
    System.out.println("testPower");
    AggregateBeed owner = new AbstractAggregateBeed(){};
    EditableDoubleBeed d1 =
      (EditableDoubleBeed) DoubleBeeds.editableDoubleBeed(2.0, owner);
    DoubleBeed powerBeed = DoubleBeeds.power(d1, 2);
    System.out.println(powerBeed.getdouble());
    DoubleEdit edit1 = new DoubleEdit(d1);
    edit1.setGoal(4.0);
    edit1.perform();
    System.out.println(powerBeed.getdouble());
  }

  public static void testSetBeed() throws EditStateException, IllegalEditException {
    System.out.println("testSetBeed");
    EditableSetBeed<RealBeed<?>> source =
      createEditableSetBeed(1.0, 2.0, 3.0, null);
    printRealBeedSet(source);
  }

  private static void printRealBeedSet(SetBeed<RealBeed<?>, ?> source) {
    System.out.println("size: " + source.getSize().getlong());
    for (RealBeed<?> element : source.get()) {
      System.out.println(element.getDouble());
    }
  }

  private static void printStringBeedSet(SetBeed<StringBeed, ?> source) {
    System.out.println("size: " + source.getSize().getlong());
    for (StringBeed element : source.get()) {
      System.out.println(element.get());
    }
  }

  private static void printAnimalSet(SetBeed<Animal, ?> source) {
    System.out.println("size: " + source.getSize().getlong());
    for (Animal element : source.get()) {
      System.out.println(element.name.get());
    }
  }

  private static void printPersonSet(SetBeed<Person, ?> source) {
    System.out.println("size: " + source.getSize().getlong());
    for (Person element : source.get()) {
      System.out.println(element.name.get());
    }
  }

  private static EditableSetBeed<RealBeed<?>> createEditableSetBeed(
      Double... values)
      throws EditStateException, IllegalEditException {
    AggregateBeed aggregateBeed =
      new AbstractAggregateBeed() {};
    EditableSetBeed<RealBeed<?>> source =
      new EditableSetBeed<RealBeed<?>>(aggregateBeed);
    SetEdit<RealBeed<?>> setEdit =
      new SetEdit<RealBeed<?>>(source);
    for (Double value : values) {
      DoubleBeed doubleBeed;
      if (value == null) {
        doubleBeed = DoubleBeeds.constant_null();
      }
      else {
        doubleBeed = DoubleBeeds.constant(value);
      }
      setEdit.addElementToAdd(doubleBeed);
    }
    setEdit.perform();
    return source;
  }

  public static void testArithmeticMean() throws EditStateException, IllegalEditException {
    System.out.println("testArithmeticMean");
    EditableSetBeed<RealBeed<?>> source =
      createEditableSetBeed(7.0, 8.0, 9.0);
    DoubleBeed arithmeticMean = DoubleStatBeeds.avg(path(source));
    System.out.println(arithmeticMean.getDouble());
  }

  public static void testFilteredSetBeed() throws EditStateException, IllegalEditException {
    System.out.println("testFilteredSetBeed");
    EditableSetBeed<RealBeed<?>> source =
      createEditableSetBeed(1.0, 2.0, 3.0, null);
    FilteredSetBeed<RealBeed<?>> filteredSetBeed =
      new FilteredSetBeed<RealBeed<?>>(
          new PathFactory<RealBeed<?>, BooleanBeed>() {
            /**
             * Return true when the integer value is effective.
             */
            public Path<BooleanBeed> createPath(RealBeed<?> realBeed) {
//              return path(BooleanBeeds.notNullV(realBeed));
              return path(BooleanBeeds.and(
                  BooleanBeeds.notNullV(realBeed),
                  BooleanBeeds.ge(realBeed, DoubleBeeds.constant(2))));
            }
          });
    filteredSetBeed.setSourcePath(Paths.path(source));
    printRealBeedSet(filteredSetBeed);
  }

  public static void testMappedSetBeed() throws EditStateException, IllegalEditException {
    System.out.println("testMappedSetBeed");
    EditableSetBeed<RealBeed<?>> source =
      createEditableSetBeed(1.0, 2.0, 3.0, null);
    MappedSetBeed<RealBeed<?>, RealBeed<?>> mappedSetBeed =
      new MappedSetBeed<RealBeed<?>, RealBeed<?>>(
          new PathFactory<RealBeed<?>, RealBeed<?>>() {
            /**
             * Map real value to the square root.
             */
            public Path<RealBeed<?>> createPath(RealBeed<?> realBeed) {
              return new ConstantPath<RealBeed<?>>(DoubleBeeds.root(realBeed, 2.0));
            }
          });
    mappedSetBeed.setSourcePath(Paths.path(source));
    printRealBeedSet(mappedSetBeed);
    // persons
    AggregateBeed aggregateBeed =
      new AbstractAggregateBeed() {};
    EditableSetBeed<Person> persons =
      new EditableSetBeed<Person>(aggregateBeed);
    SetEdit<Person> setEdit = new SetEdit<Person>(persons);
    Person person1 = createPerson("jan1");
    Person person2 = createPerson("jan2");
    setEdit.addElementToAdd(person1);
    setEdit.addElementToAdd(person2);
    setEdit.perform();
    MappedSetBeed<Person, StringBeed> mappedSetBeed2 =
      new MappedSetBeed<Person, StringBeed>(
          new PathFactory<Person, StringBeed>() {
            /**
             * Map person to its name.
             */
            public Path<StringBeed> createPath(Person person) {
              return new ConstantPath<StringBeed>(person.name);
            }
          });
    mappedSetBeed2.setSourcePath(Paths.path(persons));
    printStringBeedSet(mappedSetBeed2);
  }

  public static void testUnionBeed() throws EditStateException, IllegalEditException {
    System.out.println("testUnionBeed");
    AggregateBeed owner = new AbstractAggregateBeed(){};
    EditableSetBeed<RealBeed<?>> set1 = createEditableSetBeed(1.0, 2.0);
    printRealBeedSet(set1);
    EditableSetBeed<RealBeed<?>> set2 = createEditableSetBeed(3.0, 4.0, 5.0);
    printRealBeedSet(set2);
    EditableSetBeed<SetBeed<RealBeed<?>, ?>> source =
      new EditableSetBeed<SetBeed<RealBeed<?>,?>>(owner);
    SetEdit<SetBeed<RealBeed<?>, ?>> setEdit =
      new SetEdit<SetBeed<RealBeed<?>,?>>(source);
    setEdit.addElementToAdd(set1);
    setEdit.addElementToAdd(set2);
    setEdit.perform();
    UnionBeed<RealBeed<?>> unionBeed = new UnionBeed<RealBeed<?>>();
    unionBeed.setSource(source);
    printRealBeedSet(unionBeed);
  }

  public static void testUnionSetBeed() throws EditStateException, IllegalEditException {
    System.out.println("testUnionSetBeed");
    EditableSetBeed<RealBeed<?>> set1 = createEditableSetBeed(1.0, 2.0);
    printRealBeedSet(set1);
    EditableSetBeed<RealBeed<?>> set2 = createEditableSetBeed(3.0, 4.0, 5.0);
    printRealBeedSet(set2);
    UnionSetBeed<RealBeed<?>> unionSetBeed = new UnionSetBeed<RealBeed<?>>();
    unionSetBeed.addSource(set1);
    unionSetBeed.addSource(set2);
    printRealBeedSet(unionSetBeed);
  }

  private static Person createPerson(String name) throws EditStateException, IllegalEditException {
    Person person = new Person();
    StringEdit stringEdit = new StringEdit(person.name);
    stringEdit.setGoal(name);
    stringEdit.perform();
    return person;
  }

  private static Person createPerson(String name, Double length) throws EditStateException, IllegalEditException {
    Person person = createPerson(name);
    DoubleEdit doubleEdit = new DoubleEdit(person.length);
    doubleEdit.setGoal(length);
    doubleEdit.perform();
    return person;
  }

  private static Animal createAnimal(String name) throws EditStateException, IllegalEditException {
    Animal animal = new Animal();
    StringEdit stringEdit = new StringEdit(animal.name);
    stringEdit.setGoal(name);
    stringEdit.perform();
    return animal;
  }

  public static void testConditionalPath() throws EditStateException, IllegalEditException {
    System.out.println("testConditionalPath");
    Person person = createPerson("jan", 1.88);
    ConditionalPath<RealBeed<?>> conditionalPath =
      new ConditionalPath<RealBeed<?>>();
    conditionalPath.setConditionBeedPath(path(BooleanBeeds.notNullV(person.length)));
    conditionalPath.setFirstPath(path(person.length));
    conditionalPath.setSecondPath(path(DoubleBeeds.constant(1.9)));
    System.out.println(conditionalPath.get().getDouble());
    DoubleEdit doubleEdit = new DoubleEdit(person.length);
    doubleEdit.setGoal(null);
    doubleEdit.perform();
    System.out.println(conditionalPath.get().getDouble());
    doubleEdit = new DoubleEdit(person.length);
    doubleEdit.setGoal(1.5);
    doubleEdit.perform();
    System.out.println(conditionalPath.get().getDouble());
  }

  public static void testSwitch() throws IllegalEditException, EditStateException {
    System.out.println("testSwitch");
    AggregateBeed owner = new AbstractAggregateBeed(){};
    EnumSwitchPath<Color, Person> switchPath =
      new EnumSwitchPath<Color, Person>();
    EditableEnumBeed<Color> key =
      EnumBeeds.editableEnumBeed(Color.BLUE, owner);
    switchPath.setKeyBeedPath(path(key));
    switchPath.addCasePath(Color.BLUE, path(createPerson("blue")));
    switchPath.addCasePath(Color.RED, path(createPerson("red")));
    switchPath.addCasePath(Color.YELLOW, path(createPerson("yellow")));
    System.out.println(switchPath.get().name.get());
    EnumEdit<Color> enumEdit = new EnumEdit<Color>(key);
    enumEdit.setGoal(Color.RED);
    enumEdit.perform();
    System.out.println(switchPath.get().name.get());
    enumEdit = new EnumEdit<Color>(key);
    enumEdit.setGoal(Color.BLUE);
    enumEdit.perform();
    System.out.println(switchPath.get().name.get());
    enumEdit = new EnumEdit<Color>(key);
    enumEdit.setGoal(Color.YELLOW);
    enumEdit.perform();
    System.out.println(switchPath.get().name.get());
  }

  public static void testAssociation() throws EditStateException, IllegalEditException {
    System.out.println("testAssociation");
    Person person = createPerson("jan");
    printAnimalSet(person.pets);
    Animal animal1 = createAnimal("blacky1");
    Animal animal2 = createAnimal("blacky2");
    BidirToOneEdit<Person, Animal> bidirToOneEdit =
      new BidirToOneEdit<Person, Animal>(animal1.person);
    bidirToOneEdit.setGoal(person.pets);
    bidirToOneEdit.perform();
    printAnimalSet(person.pets);
    bidirToOneEdit =
      new BidirToOneEdit<Person, Animal>(animal2.person);
    bidirToOneEdit.setGoal(person.pets);
    bidirToOneEdit.perform();
    printAnimalSet(person.pets);
  }

  public static void testOrderedAssociation() throws EditStateException, IllegalEditException {
    System.out.println("testOrderedAssociation");
    Person person = createPerson("jan");
    printAnimalSet(person.orderedPets);
    Animal animal1 = createAnimal("blacky1");
    Animal animal2 = createAnimal("blacky2");
    OrderedBidirToOneEdit<Person, Animal> orderedBidirToOneEdit =
      new OrderedBidirToOneEdit<Person, Animal>(animal1.orderedPerson);
    orderedBidirToOneEdit.setGoal(person.orderedPets);
    orderedBidirToOneEdit.perform();
    printAnimalSet(person.orderedPets);
    orderedBidirToOneEdit =
      new OrderedBidirToOneEdit<Person, Animal>(animal2.orderedPerson);
    orderedBidirToOneEdit.setGoal(person.orderedPets);
    orderedBidirToOneEdit.perform();
    printAnimalSet(person.orderedPets);
  }

  public static void testCollectionAnyElementPath() throws EditStateException, IllegalEditException {
    System.out.println("testCollectionAnyElementPath");
    AggregateBeed owner = new AbstractAggregateBeed(){};
    Person person1 = createPerson("jan1");
    Person person2 = createPerson("jan2");
    Person person3 = createPerson("jan3");
    EditableSetBeed<Person> source =
      new EditableSetBeed<Person>(owner);
    CollectionAnyElementPath<Person> anyPath =
      new CollectionAnyElementPath<Person>(path(source));
    printPersonSet(source);
    System.out.print("any: ");
    System.out.println(anyPath.get() != null ? anyPath.get().name.get() : "no person in the set");
    SetEdit<Person> setEdit = new SetEdit<Person>(source);
    setEdit.addElementToAdd(person1);
    setEdit.addElementToAdd(person2);
    setEdit.addElementToAdd(person3);
    setEdit.perform();
    printPersonSet(source);
    System.out.print("any: ");
    System.out.println(anyPath.get() != null ? anyPath.get().name.get() : "no person in the set");
    setEdit = new SetEdit<Person>(source);
    setEdit.addElementToRemove(person1);
    setEdit.perform();
    printPersonSet(source);
    System.out.print("any: ");
    System.out.println(anyPath.get() != null ? anyPath.get().name.get() : "no person in the set");
    setEdit = new SetEdit<Person>(source);
    setEdit.addElementToRemove(person2);
    setEdit.perform();
    printPersonSet(source);
    System.out.print("any: ");
    System.out.println(anyPath.get() != null ? anyPath.get().name.get() : "no person in the set");
    setEdit = new SetEdit<Person>(source);
    setEdit.addElementToRemove(person3);
    setEdit.perform();
    printPersonSet(source);
    System.out.print("any: ");
    System.out.println(anyPath.get() != null ? anyPath.get().name.get() : "no person in the set");
  }

}
