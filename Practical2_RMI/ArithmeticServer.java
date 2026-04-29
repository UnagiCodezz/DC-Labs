import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

// Server class that implements the remote interface
public class ArithmeticServer extends UnicastRemoteObject implements ArithmeticInterface {

    public ArithmeticServer() throws RemoteException {
        super();
    }

    public double add(double a, double b) throws RemoteException {
        System.out.println("add(" + a + ", " + b + ") called");
        return a + b;
    }

    public double subtract(double a, double b) throws RemoteException {
        System.out.println("subtract(" + a + ", " + b + ") called");
        return a - b;
    }

    public double multiply(double a, double b) throws RemoteException {
        System.out.println("multiply(" + a + ", " + b + ") called");
        return a * b;
    }

    public double divide(double a, double b) throws RemoteException {
        System.out.println("divide(" + a + ", " + b + ") called");
        if (b == 0) {
            throw new RemoteException("Cannot divide by zero!");
        }
        return a / b;
    }

    public static void main(String[] args) {
        try {
            ArithmeticServer server = new ArithmeticServer();

            // Create registry on port 1099
            LocateRegistry.createRegistry(1099);

            // Bind the server object with a name
            Naming.rebind("rmi://localhost:1099/ArithmeticService", server);

            System.out.println("Arithmetic RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
