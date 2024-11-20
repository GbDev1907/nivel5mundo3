package cadastroclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Produto;

public class CadastroClient {

    public static void main(String[] args) {
        String serverAddress = "localhost"; 
        int serverPort = 4321;               
        Socket socket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            socket = new Socket(serverAddress, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            String login = "op1";
            String senha = "op1";   

            out.writeObject(login); 
            out.writeObject(senha);  

            String loginResponse = (String) in.readObject();
            System.out.println(loginResponse);

            if (loginResponse.equals("Usuário inválido.") || loginResponse.equals("Usuário conectado com sucesso.")) {
                return;
            }

            out.writeObject("L"); 

            List<Produto> produtos = (List<Produto>) in.readObject();

            System.out.println("Produtos disponíveis:");
            if (produtos != null && !produtos.isEmpty()) {
                for (Produto produto : produtos) {
                    System.out.println("Nome: " + produto.getNome());
                    System.out.println("Quantidade: " + produto.getQuantidade());
                    System.out.println("Preço de venda: " + produto.getPrecoVenda());
                    System.out.println("------------");
                }
            } else {
                System.out.println("Nenhum produto encontrado.");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}