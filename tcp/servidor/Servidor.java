import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class Servidor implements Runnable{
    public Socket s;
    
    public Servidor(Socket s){
        this.s = s;
    }
    
    public static void main(String argv[]) throws Exception 
    {    
       System.out.println("Servidor ligado");

       ServerSocket welcomeSocket = new ServerSocket(4255);

       while(true) {
           Socket s = welcomeSocket.accept();
               
           Servidor conexao = new Servidor(s);
           Thread t = new Thread(conexao);
           t.start();
       }
    }

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(s.getInputStream());
            int op = in.readInt();
            String fileName = in.readUTF();

            if(op == 1){
                OutputStream out = new FileOutputStream(fileName);

                byte[] bytes = new byte[16*1024];

                int count;
                while ((count = in.read(bytes)) > 0) {
                    out.write(bytes, 0, count);
                }

                out.close();
                in.close();
                System.out.println("Arquivo recebido");
            }else if(op == 2){                
                File f = new File(fileName);
                byte[] bytes = new byte[16 * 1024];

                InputStream in2 = new FileInputStream(f);
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                int count;
                while ((count = in2.read(bytes)) > 0) {
                    out.write(bytes, 0, count);
                }

                in2.close();
                out.close();
                in.close();
                System.out.println("Arquivo enviado");
            }else{
                s.close();
            }
        }catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}