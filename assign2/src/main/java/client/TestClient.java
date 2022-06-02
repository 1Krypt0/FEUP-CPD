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

        try {

            String[] accessPoint = args[0].split(":");
            String host = accessPoint[0];
            String nodeID = accessPoint[1];

            Registry registry = LocateRegistry.getRegistry(host);
            node = (RMI) registry.lookup(nodeID);

            String op = args[1];
            String res;
            String operand;

            switch (op.toUpperCase()) {
            case "JOIN":
                res = node.join();
                break;
            case "LEAVE":
                res = node.leave();
                break;
            case "PUT":
                operand = args[2];
                break;
            case "GET":
                operand = args[2];
                break;
            case "DELETE":
                operand = args[2];
                break;
            default:
                System.out.println("Unknown operation: " + op);
                return;
            }

            System.out.println(res);

        } catch (RemoteException e) {
            System.out.println("Error executing remote function: " + e.getMessage());
            e.printStackTrace();
        } catch (NotBoundException e) {
            System.out.println("RMI is not bound to any object" + e.getMessage());
            e.printStackTrace();
        }
    }
}
