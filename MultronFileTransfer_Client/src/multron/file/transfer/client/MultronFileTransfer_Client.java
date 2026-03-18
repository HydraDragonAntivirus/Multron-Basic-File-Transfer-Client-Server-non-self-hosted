/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package multron.file.transfer.client;

/**
 *
 * @author winball501
 */
public class MultronFileTransfer_Client {

    /**
     * @param args the command line arguments
     */
    static String ip = "";
    static int port = 0;
    static String filetosend = "";
    static String savedir = "";
    static int action = 0;
    public static void connect() {
  
        switch(action) {
            
            case 1 -> {
                    try {
                      
                      
                 
                      while(true) {
                          java.net.Socket socket = new java.net.Socket(ip, port);
                          System.out.println("Waiting for server...");
                         
                          java.io.DataOutputStream output = new java.io.DataOutputStream(socket.getOutputStream());
                          java.io.File file = new java.io.File(filetosend);
                          output.writeUTF(file.getName());
                          output.writeLong(file.length());
                          System.out.println("Receiving: " + file.getName() + " " + file.length() + " bytes");  
                          java.io.FileInputStream fis = new java.io.FileInputStream(file);
                          int bytesRead;
                            long fileSize = file.length();
                            long sent = 0;
                            int bufferSize = 8192;   
                            byte[] buffer = new byte[bufferSize];
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                output.write(buffer, 0, bytesRead);
                                sent += bytesRead;

                               
                                int percent = (int) ((sent * 100) / fileSize);
                                System.out.print("\rSending: " + percent + "% (" + sent + "/" + fileSize + ")");
                            }

                                socket.close();
                                fis.close();
                                System.out.println("Done! Waiting for next transfer...\n");
                                System.out.print("Send again? (y/n): ");
                                java.util.Scanner sc = new java.util.Scanner(System.in);
                                if (!sc.nextLine().equalsIgnoreCase("y")) break;
                       
                      }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
            
            case 2 -> {
                try {
                  
                      while(true) {
                          java.net.Socket socket = new java.net.Socket(ip, port);
                          System.out.println("Waiting for server...");
                         
                          java.io.DataInputStream input = new java.io.DataInputStream(socket.getInputStream());
                          String filename = input.readUTF();
                          long sizefile = input.readLong();
                          System.out.println("Receiving: " + filename + " " + sizefile + " bytes");
                          java.io.FileOutputStream fos = new java.io.FileOutputStream(savedir + "/" + filename);
                          java.io.File file = new java.io.File(savedir + "/" + filename);
                          file.createNewFile();
                              byte[] buffer = new byte[8192];
                              long received = 0;
                              int bytesRead;

                              while (received < sizefile) {
                                    bytesRead = input.read(buffer, 0, (int) Math.min(buffer.length, sizefile - received));
                                    if (bytesRead == -1) break;

                                    fos.write(buffer, 0, bytesRead);
                                    received += bytesRead;

                                    int percent = (int) ((received * 100) / sizefile);
                                    System.out.print("\rReceiving: " + percent + "% (" + received + "/" + sizefile + ")");
                              }
                                socket.close();
                                fos.close();
                                System.out.println("Done! Waiting for next transfer...\n");
                                System.out.print("Get again? (y/n): ");
                                java.util.Scanner sc = new java.util.Scanner(System.in);
                                if (!sc.nextLine().equalsIgnoreCase("y")) break;
                          
                      }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
             
                
                break;
            }
        }
    }
    
    public static void main(String[] args) {
            try {
                if(args.length > 0) {
                   for(int x = 0; x < args.length; x++) {
                         switch(args[x]) {
                             case "-ip:port" -> {
                                 String ip_port =  args[x + 1];
                                 java.util.StringTokenizer tk = new java.util.StringTokenizer(ip_port, ":");
                                 ip = tk.nextToken();
                                 port = Integer.parseInt(tk.nextToken());
                                 x++;
                                 System.out.println("ip > " + ip);
                                 System.out.println("port > " + port);
                                 break;
                          }
                             case "-send" -> {
                                 filetosend =  args[x + 1];
                                 action = 1;
                                 connect();
                                 x++;
                                 break;
                          }
                             case "-get" -> {
                                 savedir =  args[x + 1];
                                 action = 2;
                                 connect();
                                 x++;
                                 break;
                          }
                         }
                  }
                } else {
                    System.out.println("multron basic file transfer client commands > -ip:port <ip:port> -send <yourfilelocation> -get <savedirectory>");
                }
              
                
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }
    
}
