class Animal { 
    // Method 
    public void sound() { 
        System.out.println("Animal makes a sound"); 
    } 
}
 
// Child Class 1 
class Dog extends Animal { 
    // Method Overriding (Polymorphism) 
    @Override 
    public void sound() { 
        System.out.println("Dog barks"); 
    } 
} 
 
// Child Class 2 
class Cat extends Animal { 
 
    // Method Overriding 
    @Override 
    public void sound() { 
        System.out.println("Cat meows"); 
    } 
} 
 
// Encapsulation Example 
class Student { 
 
    // Private variables 
    private String name; 
    private int age; 
 
    // Setter methods 
    public void setName(String name) { 
        this.name = name; 
    } 
 
    public void setAge(int age) { 
        this.age = age; 
    } 
    // Getter methods 
    public String getName() { 
        return name; 
    } 
    public int getAge() { 
        return age; 
    } 
} 
// Main Class 
public class OOPDemo { 
    public static void main(String[] args) { 
        // Encapsulation 
        Student s1 = new Student(); 
        s1.setName("Ramesh"); 
        s1.setAge(22); 
        System.out.println("Student Name: " + s1.getName()); 
        System.out.println("Student Age: " + s1.getAge()); 
        System.out.println(); 
        // Inheritance + Polymorphism 
        Animal a; 
        a = new Dog(); 
        a.sound(); 
        a = new Cat(); 
        a.sound(); 
    } 
}
