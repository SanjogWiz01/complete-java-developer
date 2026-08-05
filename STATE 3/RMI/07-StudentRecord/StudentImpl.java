import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class StudentImpl extends UnicastRemoteObject implements StudentInterface {

    private List<Student> students;

    public StudentImpl() throws RemoteException {
        super();
        students = new ArrayList<>();
        students.add(new Student(1, "Alice", 21));
        students.add(new Student(2, "Bob", 22));
        students.add(new Student(3, "Charlie", 20));
        students.add(new Student(4, "Diana", 23));
    }

    @Override
    public Student findStudent(int id) throws RemoteException {
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    @Override
    public List<Student> getAllStudents() throws RemoteException {
        return students;
    }
}