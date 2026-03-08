package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class PrincipalComBusca {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Criando Scanner
        Scanner in = new Scanner(System.in);

        // Recebendo valores
        System.out.println("Qual filme deseja buscar?");
        String nome = in.nextLine().replace(' ','+');
        System.out.println("Qual o ano do filme que deseja buscar (deixe nulo para não especificar)?");
        String ano = in.nextLine();

        // Protocolo HTTP
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.omdbapi.com/?t="+nome+(ano.isBlank()?"":("&y="+ano))+"&apikey=6585022c")) // Especificar ano é opcional
                .build();
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body()); // Imprime a resposta da API

        Gson gson = new GsonBuilder() // Biblioteca do google para tratamendo de JSON
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();

        TituloOmdb meuTituloOmdb = gson.fromJson(response.body(), TituloOmdb.class);
        System.out.println(meuTituloOmdb);

        Titulo meuTitulo = new Titulo(meuTituloOmdb);
        System.out.println(meuTitulo);
    }
}
