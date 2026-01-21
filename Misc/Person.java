package base;

public class Person{
    private String name;
    private int age;


    public Person(){
        this("",0,YearInSchool.FRESHMAN);
    }

    public Person(String name, int age, YearInSchool year){
        setName(name);
        setAge(age);
        setYear(year);
    }


    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    //... this can be done by going into code menu, and using generage... how?

    @Override
    public String toString(){//allows me to use this instead of hte original tostring method(function
        return String.format("%s: name=%s, age=%d", this.getClass().getName(), name, age);
        //getclass is important? returns class object for "this", i.e. current class
    }
    @Override
    public boolean equals(Object o){
        if (o == null){
            return false;
        }
        if (o = this){
            return true;
        }
        if (this.getClass() != 0.getClass()){
            return false;
        }
        Person p = (Person)o; // casting object since .age, .year, .name not part of default equals class?
        return (name.equals(o.name) && age == 0.age);
    }
    @Override
    public int hashCode(){
        int h = ((name.hashCode() * age) ^ year.hashCode()); // just using String's built in hashCode method to practice overriding the default one for our class. The ^ and * are just for flavor
        return h
    }


    public final int getPriority(){// use final to prevent subclasses from overriding. works on the entire class to prevent subclasses
        return agePriority() + categoryPriority();
    }
    protected abstract int agePriority(){// use abstract for class that only acts as a base to pass on methods to subclasses. Can't be used on its own, only by subclasses?
    }
    protected int categoryPriority(){
        return 1;
    }


}

//Inheretance = class that requires you implement certain methods to use
//i.e:
//public class Person implements Moveable, Comparable, Runnable{
//    public void go(){
//        //code to make person or moveable or comparable go
//    }
//}