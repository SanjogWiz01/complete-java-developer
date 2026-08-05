import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NumberImpl extends UnicastRemoteObject implements NumberInterface {

    public NumberImpl() throws RemoteException {
        super();
    }

    @Override
    public long factorial(int n) throws RemoteException {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    @Override
    public boolean isPrime(int n) throws RemoteException {
        if (n < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int reverse(int n) throws RemoteException {
        int reversed = 0;
        while (n != 0) {
            reversed = reversed * 10 + n % 10;
            n /= 10;
        }
        return reversed;
    }
}