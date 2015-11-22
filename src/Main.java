import java.io.*;
import java.net.*;
public class Main{
    ServerSocket server;
    Socket client = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;

    void run()
    {
        try{
            server = new ServerSocket(9666, 10);
            System.out.println("Waiting for client");
            client = server.accept();
            System.out.println("Connection received from " + client.getInetAddress().getHostName());
            out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            in = new ObjectInputStream(client.getInputStream());
            sendMessage("Connection successful");
            int suma = 0, i = 0;
            do{
                try{
                    message = (String)in.readObject();
                    System.out.println("client: " + message);
                    if (message.equals("bye"))
                        sendMessage("bye");
                    else{
                    	suma+= Integer.parseInt(message);
                    	i++;
                    	if(i==2){
                    		sendMessage(suma % 2 == 0 ? "Parzysta" : "Nieparzysta");
                    		suma = 0;
                    	}
                    }
                }
                catch(ClassNotFoundException classnot){
                    classnot.printStackTrace();
                }
            }while(!message.equals("bye"));
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            try{
                in.close();
                out.close();
                server.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    
    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("server: " + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    public static void main(String args[])
    {
        Main server = new Main();
        while(true){
            server.run();
        }
    }
}