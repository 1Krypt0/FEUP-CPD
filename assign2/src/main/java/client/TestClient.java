package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import communication.RMI;

public class TestClient {

    private static final String CRLF = "\r\n";
    private static RMI node;

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Usage: java TestClient <node_ap> <operation> [<opnd>]");
            return;
        }

        TestClient client = new TestClient();

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
            case "GET":
            case "DELETE":
                operand = args[2];
                res = client.sendTCPMessage(op, operand, host, Integer.parseInt(nodeID));
                break;
            case "PUT":
                String fileValue = client.getFileValue(args[2]);
                res = client.sendTCPMessage(op, fileValue, host, Integer.parseInt(nodeID));
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

    public String sendTCPMessage(String op, String operand, String destinationIP, int destinationPort) {

        try {
            final InetAddress address = InetAddress.getByName(destinationIP);
            final Socket socket = new Socket(address, destinationPort);

            final OutputStream stream = socket.getOutputStream();
            final PrintWriter writer = new PrintWriter(stream, true);

            final String ip = socket.getLocalAddress().getHostAddress();
            final int port = socket.getLocalPort();

            String message;

            switch (op) {
            case "PUT":
                message = composePutMessage(operand, ip, port);
                break;
            case "GET":
                message = composeGetMessage(operand, ip, port);
                break;
            case "DELETE":
                message = composeDeleteMessage(operand, ip, port);
                break;
            default:
                message = "";
                break;
            }

            writer.println(message);

            final String res = this.getTCPResponse(socket);

            socket.close();

            return res;

        } catch (final UnknownHostException e) {
            System.out.println("Could not find remote machine");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error sending TCP message: " + e.getMessage());
            e.printStackTrace();
        }

        return "";
    }

    public String getTCPResponse(Socket socket) {
        try {

            final InputStream stream = socket.getInputStream();

            final byte[] msg = stream.readAllBytes();

            return new String(msg).trim();

        } catch (final IOException e) {
            System.out.println("Error getting response from server");
        }

        return "";
    }

    public String getFileValue(String path) {
        final File file = new File(path);
        String value = "";

        try {
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            String buf;
            while ((buf = reader.readLine()) != null) {
                value += buf;
                value += "\n";
            }
            reader.close();
            return value;
        } catch (final FileNotFoundException e) {
            System.out.println("Error reading log File: " + e.getMessage());
            e.printStackTrace();
        } catch (final IOException e) {
            System.out.println("Error closing log File: " + e.getMessage());
            e.printStackTrace();
        }

        return "";
    }

    public String composePutMessage(String body, String ip, int port) throws UnknownHostException {
        return "PUT" + " ip:" + ip + " port:" + Integer.toString(port) + CRLF + CRLF + body;
    }

    public String composeGetMessage(String body, String ip, int port) {
        return "GET" + " ip:" + ip + " port:" + Integer.toString(port) + CRLF + CRLF + body;
    }

    public String composeDeleteMessage(String body, String ip, int port) {
        return "DELETE" + " ip:" + ip + " port:" + Integer.toString(port) + CRLF + CRLF + body;
    }
}
