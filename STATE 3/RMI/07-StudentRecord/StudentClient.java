import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StudentClient {

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            StudentInterface service = (StudentInterface) registry.lookup("StudentService");

            System.out.println("--- All students ---");
            for (Student s : service.getAllStudents()) {
                System.out.println(s);
            }

            System.out.println("--- Search for id 3 ---");
            Student found = service.findStudent(3);
            System.out.println(found != null ? found : "No student found.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}