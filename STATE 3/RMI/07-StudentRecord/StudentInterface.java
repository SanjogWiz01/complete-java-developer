import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface StudentInterface extends Remote {

    public Student findStudent(int id) throws RemoteException;

    public List<Student> getAllStudents() throws RemoteException;
}