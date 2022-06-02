package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import communication.RMI;

public class TestClient {

    private static RMI node;

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Usage: java TestClient <node_ap> <operation> [<opnd>]");
            return;
        }

        TestClient client = new TestClient();

        try {
            Registry registry = LocateRegistry.getRegistry();
            node = (RMI) registry.lookup(args[0]);

            String op = args[1];

            switch (op.toUpperCase()) {
            case "JOIN":
                break;
            case "LEAVE":
                break;
            case "PUT":
                break;
            case "GET":
                break;
            case "DELETE":
                break;
            default:
                System.out.println("Unknown operation: " + op);
            }

        } catch (RemoteException e) {
            System.out.println("Error executing remote function: " + e.getMessage());
            e.printStackTrace();
        } catch (NotBoundException e) {
            System.out.println("RMI is not bound to any object" + e.getMessage());
            e.printStackTrace();
        }
    }
}
