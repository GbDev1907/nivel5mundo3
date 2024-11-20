/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroclientv2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Gabriel
 */
public class CadastroClientV2 {

    private static ObjectOutputStream socketOut;
    private static ObjectInputStream socketIn;
    private static ThreadClient threadClient;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        String serverAddress = "localhost"; 
        int serverPort = 4321;
        Socket socket = new Socket(serverAddress, serverPort);
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        SaidaFrame saidaFrame = new SaidaFrame();
        saidaFrame.setVisible(true);
        
        threadClient = new ThreadClient(socketIn, saidaFrame.texto);
        threadClient.start();
        
        socketOut.writeObject("op1");
        
        socketOut.writeObject("op1");

        Character commando = ' ';
        try {
            while (!commando.equals('X')) {
                System.out.println("Escolha uma opção:");
                System.out.println("L - Listar | X - Finalizar | E - Entrada | S - Saída");

                commando = reader.readLine().charAt(0);

                processaComando(reader, commando);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            saidaFrame.dispose();
            socketOut.close();
            socketIn.close();
            socket.close();
            reader.close();
        }
    }

    static void processaComando(BufferedReader reader, Character commando) throws IOException {
  
        socketOut.writeChar(commando);
        socketOut.flush();
                
        switch (commando) {
            case 'L' -> {
            }
            case 'E', 'S' -> {
                socketOut.flush();

                System.out.println("Digite o Id da pessoa:");
                int idPessoa = Integer.parseInt(reader.readLine());
                System.out.println("Digite o Id do produto:");
                int idProduto = Integer.parseInt(reader.readLine());
                System.out.println("Digite a quantidade:");
                int quantidade = Integer.parseInt(reader.readLine());
                System.out.println("Digite o valor unitário:");
                long valorUnitario = Long.parseLong(reader.readLine());

                socketOut.writeInt(idPessoa);
                socketOut.flush();
                socketOut.writeInt(idProduto);
                socketOut.flush();
                socketOut.writeInt(quantidade);
                socketOut.flush();
                socketOut.writeLong(valorUnitario);
                socketOut.flush();
            }
            case 'X' -> threadClient.cancela();
            default -> System.out.println("Opção inválida!");
        }
    }
}