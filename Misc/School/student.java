package school;

import base.Person;

public class Student extends Person{ // Student is an extension, or subclass of Person
    private YearInSchool year;
    private float gpa;

    public Student(){
        super();
        setYear(YearInSchool.FRESHMAN);
        setGPA(0.0f);

    }

    public student(String name, int age, YearInSchool year, float gpa){
        super(name, age);
        setYear(year);
        setGpa(gpa);

    }

    public YearInSchool getYear() {
        return year;
    }

    public void setYear(YearInSchool year) {
        this.year = year;
    }
    public float getGpa(){
        return gpa;
    }
    public void setGpa(){
        this.gpa = gpa;
    }

    @Override
    public String toString(){
        return super.toString() + String.format(", year=%s, gpa=%f", year, gpa);
    }
    @override
    public boolean equals(Object o){
        boolean b = super.equals(o);
        if (!b){
            return false;
        }
        Student s = (Student)o;
        return (year == s.year && gpa == s.gpa);
        // if the parent "equals" method checks what it knows (name, age) and thinks
        // that it's equal, we can then check our subclass specific values like year andn gpa
    }

    @Override
    public int hashCode(){
        return (super.hashCode() * (int)getAge()) ^ year.hashCode();
    }

    @Override
    protected int agePriority(){
        return this.getAge() / 10;
    }

    @Override
    protected int categoryPriority(){
        return 5;
    }


}