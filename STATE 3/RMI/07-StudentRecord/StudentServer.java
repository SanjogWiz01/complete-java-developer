import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StudentServer {

    public static void main(String[] args) {

        try {
            StudentImpl studentService = new StudentImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("StudentService", studentService);

            System.out.println("Student Server is ready.");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}