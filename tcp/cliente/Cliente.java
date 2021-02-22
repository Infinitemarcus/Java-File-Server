import java.io.*;
import java.net.*;
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
        Socket s = new Socket("127.0.0.1", 4255);
        
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        OutputStream out2 = new FileOutputStream(fileName);
        DataInputStream in = new DataInputStream(s.getInputStream());
        
        out.writeInt(2);
        out.writeUTF(fileName);
        
        byte[] bytes = new byte[16*1024];
        
        int count;
        while ((count = in.read(bytes)) > 0) {
           out2.write(bytes, 0, count);
        }
        
        out.close();
        out2.close();
        in.close();
        s.close();
    }

    private static void sendFile(String fileName) throws IOException {
        File f = new File(fileName);
        byte[] bytes = new byte[16 * 1024];
        
        Socket s = new Socket("127.0.0.1", 4255);
        
        InputStream in = new FileInputStream(f);
        DataOutputStream out = new DataOutputStream(s.getOutputStream());

        out.writeInt(1);
        out.writeUTF(fileName);
        
        int count;
        while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
        }

        out.close();
        in.close();
        s.close();
    }
}