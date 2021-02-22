import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Scanner;

class Cliente {
 public static void main(String argv[]) throws Exception
 {
    Scanner scan = new Scanner(System.in);
    
    System.out.println("Selecione a opcao desejada:\n0 - Sair \n1 - Enviar arquivo \n2 - Receber arquivo\n");
    int op = scan.nextInt();
    
    String fileName;
    long startTime, elapsedTime;
    
    while(op != 0){
        switch(op){
            case 1:
                System.out.println("Digite o nome do arquivo com extensao que voce deseja enviar (deve estar no mesmo diretorio do programa): ");
                scan.nextLine();
                fileName = scan.nextLine();
                startTime = System.nanoTime();
                sendFile(fileName);
                elapsedTime = System.nanoTime() - startTime;
                System.out.println(elapsedTime/1000000);
            break;
            case 2:
                System.out.println("Digite o nome do arquivo com extensao que voce deseja receber: ");
                scan.nextLine();
                fileName = scan.nextLine();
                startTime = System.nanoTime();
                receiveFile(fileName);
                elapsedTime = System.nanoTime() - startTime;
                System.out.println(elapsedTime/1000000);
            break;
        }
        System.out.println("Selecione a opcao desejada:\n0 - Sair \n1 - Enviar arquivo \n2 - Receber arquivo\n");
        op = scan.nextInt();
    }
    
 }

    private static void receiveFile(String fileName) throws IOException {
        byte[] receiveData = new byte[16*1024];
        
        DatagramSocket s = new DatagramSocket();
        InetAddress host = InetAddress.getByName("127.0.0.1");
        
        receiveData = ByteBuffer.allocate(Integer.BYTES).putInt(2).array();
        
        DatagramPacket p = new DatagramPacket(receiveData, receiveData.length, host, 4255);
        
        s.send(p);
        receiveData = new byte[16*1024];
        receiveData = fileName.getBytes();
        
        p = new DatagramPacket(receiveData, receiveData.length, host, 4255);
        s.send(p);
        receiveData = new byte[16*1024];
        
        p = new DatagramPacket(receiveData, receiveData.length, host, 4255);
        s.receive(p);
        
        OutputStream out = new FileOutputStream(fileName);
        
        out.write(receiveData, 0, receiveData.length);
            
        out.close();
        
        s.close();
    }

    private static void sendFile(String fileName) throws IOException {
        File f = new File(fileName);
        byte[] bytes = Files.readAllBytes(f.toPath());
        byte[] sendData = new byte[16*1024];
        
        DatagramSocket s = new DatagramSocket();
        InetAddress host = InetAddress.getByName("127.0.0.1");

        sendData = ByteBuffer.allocate(Integer.BYTES).putInt(1).array();
        
        DatagramPacket p = new DatagramPacket(sendData, sendData.length, host, 4255);
        s.send(p);
        sendData = new byte[16*1024];
        sendData = fileName.getBytes();
        
        p = new DatagramPacket(sendData, sendData.length, host, 4255);
        s.send(p);
        
        sendData = new byte[16*1024];
        p = new DatagramPacket(bytes, bytes.length, host, 4255);
        s.send(p);

        s.close();
    }
}