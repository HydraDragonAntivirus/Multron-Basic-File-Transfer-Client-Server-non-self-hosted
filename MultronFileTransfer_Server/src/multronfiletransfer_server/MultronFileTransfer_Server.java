/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package multronfiletransfer_server;

/**
 *
 * @author winball501
 */
public class MultronFileTransfer_Server {

    /**
     * @param args the command line arguments
     */
    static int port = 0;
    static String filetosend = "";
    static String savedir = "";
    static int action = 0;
    public static void connect() {
  
        switch(action) {
            
            case 1 -> {
                    try {
                      java.net.ServerSocket serversocket = new java.net.ServerSocket(port);
                      while(true) {
                          System.out.println("Waiting for client...");
                          java.net.Socket socket = serversocket.accept();
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
                              
                            output.flush();
                           
                            fis.close();
                            System.out.println("\nDone!");
                          
                      }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
            
            case 2 -> {
                try {
                      java.net.ServerSocket serversocket = new java.net.ServerSocket(port);
                      while(true) {
                          System.out.println("Waiting for client...");
                          java.net.Socket socket = serversocket.accept();
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
                              fos.flush();
                              fos.close();
                              System.out.println("\nDone!");
                               
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
                             case "-port" -> {
                                 port = Integer.parseInt(args[x + 1]);
                                 x++;
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
                    System.out.println("multron basic file transfer server commands > -port <yourport> -send <yourfilelocation> -get <savedirectory>");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }
    
}
