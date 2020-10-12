package Presentation.Records;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class NotificationsListener implements Runnable {

    private Socket notificationsSocket;

    public NotificationsListener()
    {


    }

    @Override
    public void run()
    {

            String res = "-1";

            try
            {
                DataInputStream inputStream = new DataInputStream(notificationsSocket.getInputStream());
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(inputStream));

                res = clientReader.readLine();

                String operator = res.split("\\|")[0];

                System.out.println("Client receive from server on notifications socket: " + res + "\n");


            }

            catch (Exception e)
            {
                e.printStackTrace();
            }



    }
}
