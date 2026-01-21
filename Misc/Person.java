package base;

public classPerson{
    private String name;
    private int age;
    private YearInSchool year;

    public Person(){
        this("",0,YearInSchool.FRESHMAN);
    }

    public Person(String name, int age, YearInSchool year){
        setName(name);
        setAge(age);
        setYear(year);
    }

    @Override
    public String toString(){//allows me to use this instead of hte original tostring method(function
        return String.format("%s: name=%s, age=%d, year: %s", this.getClass().getName(), name, age, year);
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
        return (name.equals(o.name) && age == 0.age && year == o.year);
    }
    @Override
    public int hashCode(){
        int h = ((name.hashCode() * age) ^ year.hashCode()); // just using String's built in hashCode method to practice overriding the default one for our class. The ^ and * are just for flavor
        return h
    }






    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    //... this can be done by going into code menu, and using generage... how?

}