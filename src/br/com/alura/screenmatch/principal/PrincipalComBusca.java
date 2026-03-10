package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.excecao.ErroDeConversaoDeAnoException;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrincipalComBusca {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Criando Scanner
        Scanner scan = new Scanner(System.in);
        List<Titulo> titulos = new ArrayList<>();

        Gson gson = new GsonBuilder() // Biblioteca do google para tratamendo de JSON
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        do {

            // Recebendo valores
            System.out.println("Qual filme deseja buscar (digite \"sair\" para finalizar)?");
            String nome = scan.nextLine().replaceAll(" ", "+");
            if (nome.equalsIgnoreCase("sair")){
                break;
            }
            System.out.println("Qual o ano do filme que deseja buscar (deixe nulo para não especificar)?");
            String ano = scan.nextLine();
            if (ano.equalsIgnoreCase("sair")){
                break;
            }

            try {

                // Protocolo HTTP
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://www.omdbapi.com/?t=" + nome + (ano.isBlank() ? "" : ("&y=" + ano)) + "&apikey=6585022c")) // Especificar ano é opcional
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println(response.body()); // Imprime a resposta da API


                TituloOmdb meuTituloOmdb = gson.fromJson(response.body(), TituloOmdb.class);
                System.out.println(meuTituloOmdb);

                Titulo meuTitulo = new Titulo(meuTituloOmdb);
                System.out.println("Título convertido de JSON para objeto Java");
                System.out.println(meuTitulo);

                titulos.add(meuTitulo);
            } catch (NumberFormatException e) {
                System.out.println("Aconteceu um erro: ");
                System.out.println(e.getMessage());
            } catch (ErroDeConversaoDeAnoException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Algo deu errado");
            }

        } while (true);
        System.out.println("Programa Finalizado!");
        System.out.println("Lista de Títulos: "+titulos);

        FileWriter writer = new FileWriter("filmes.json");
        writer.write(gson.toJson(titulos));
        writer.close();
        System.out.println();
    }
}
