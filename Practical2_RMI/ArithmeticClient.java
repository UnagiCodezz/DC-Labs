import java.rmi.*;
import java.util.Scanner;

public class ArithmeticClient {
    public static void main(String[] args) {
        try {
            // Look up the remote object
            ArithmeticInterface server = (ArithmeticInterface) Naming.lookup("rmi://localhost:1099/ArithmeticService");

            Scanner sc = new Scanner(System.in);

            System.out.print("Enter first number: ");
            double num1 = sc.nextDouble();
            System.out.print("Enter second number: ");
            double num2 = sc.nextDouble();

            System.out.println("\n--- Results from RMI Server ---");
            System.out.println("Addition       : " + num1 + " + " + num2 + " = " + server.add(num1, num2));
            System.out.println("Subtraction    : " + num1 + " - " + num2 + " = " + server.subtract(num1, num2));
            System.out.println("Multiplication : " + num1 + " * " + num2 + " = " + server.multiply(num1, num2));

            try {
                System.out.println("Division       : " + num1 + " / " + num2 + " = " + server.divide(num1, num2));
            } catch (RemoteException e) {
                System.out.println("Division       : Error - " + e.getMessage());
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Error: Make sure ArithmeticServer is running.");
            e.printStackTrace();
        }
    }
}
