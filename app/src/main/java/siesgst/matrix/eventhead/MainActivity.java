package siesgst.matrix.eventhead;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    Button setIp,resetButton;
    EditText ipAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setIp = findViewById(R.id.setIp);
        resetButton = findViewById(R.id.resetButton);
        ipAddress = findViewById(R.id.ipAddress);

        setIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ipAddress.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,"Please enter some IP Address",Toast.LENGTH_SHORT).show();
                }
                else {
                    setIp.setEnabled(false);
                    ipAddress.setEnabled(false);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setIp.isEnabled()) {
                    Toast.makeText(MainActivity.this,"Please set some IP Address",Toast.LENGTH_SHORT).show();
                }
                else {
                    new Thread(new UdpSender(5,ipAddress.getText().toString())).start();
                }
            }
        });

    }

    class UdpSender implements Runnable {

        int message;
        String ipAdd;

        UdpSender(int message , String ipAdd) {
            this.message = message;
            this.ipAdd = ipAdd;
        }

        @Override
        public void run() {
            try {
                DatagramSocket udpSocket = new DatagramSocket(1111);
                udpSocket.setReuseAddress(true);
                InetAddress serverAddress = InetAddress.getByName(ipAdd);
                //InetAddress serverAddress = InetAddress.getByName(ipAdd);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream pout = new PrintStream(baos);
                pout.print(message);
                byte[] buffer = baos.toByteArray();
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length,serverAddress,1111);
                udpSocket.send(packet);
                udpSocket.close();
            } catch (IOException e) {
                Log.d("ExceptionHua",e+"");
            }
        }
    }
}
