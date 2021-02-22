import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;

class Servidor{
 public static void main(String argv[]) throws Exception
 {    
    System.out.println("Servidor ligado");
    
    DatagramSocket s = new DatagramSocket(4255);
    
    byte[] receiveData = new byte[16*1024];
    byte[] sendData = new byte[16*1024];
    
    while(true) {
        DatagramPacket p = new DatagramPacket(receiveData, receiveData.length);
        
        s.receive(p);
        
        int opt = ByteBuffer.wrap(receiveData).getInt();
        receiveData = new byte[16*1024];
        p = new DatagramPacket(receiveData, receiveData.length);
        s.receive(p);
          
        if(opt == 1){
            OutputStream out = new FileOutputStream(new String(receiveData).trim());
            receiveData = new byte[16*1024];
            
            p = new DatagramPacket(receiveData, receiveData.length);
            s.receive(p);
            
            out.write(receiveData, 0, receiveData.length);
            
            out.close();
            System.out.println("Arquivo recebido");
                
        }else if(opt == 2){ 
            File f = new File(new String(receiveData).trim());
            
            sendData = Files.readAllBytes(f.toPath());
            
            InetAddress addrs = p.getAddress();
            int port = p.getPort();
            p = new DatagramPacket(sendData, sendData.length, addrs, port);
            s.send(p);
            
            System.out.println("Arquivo enviado");
        }   
    }
 }
}