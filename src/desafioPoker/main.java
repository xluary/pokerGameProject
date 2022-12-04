package desafioPoker;

import desafioPoker.controle.Jogo;

import java.util.InputMismatchException;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {

       try {
            System.out.println("--* Início de partida *-- ");


       //     System.out.println("Digite a quantidade de jogadores: ");
           Scanner entrada = new Scanner(System.in);
       //     int qJogadores = entrada.nextInt();
                int qJogadores =5;

            while (qJogadores<2 || qJogadores>=23){
                System.out.println("Digite uma quantidade válida de jogadores (maior do que 1 e menor do que 23): ");
                qJogadores = entrada.nextInt();
            }
            Jogo partida01 = new Jogo(qJogadores); //inicia a partida

           for (int i=0; i<qJogadores; i++){
           //     System.out.println("Nome do jogador " + (i+1) + ": ");
           //     String novoJogador = entrada.next();
                String novoJogador= ("Jogador" + (i+1));
                partida01.cadastrarJogadores(novoJogador);
           }
           partida01.definirDealerInicial(); //define quem será o dealer o SB e o BB
           partida01.apresentacao();



           partida01.darCartas(); //embaralha e distribui as cartas comunitárias e para os jogadores
           partida01.preFlop();

           partida01.statusJogadores();
           partida01.determinarVencedor();
           partida01.perguntarVencedor();


        }catch (InputMismatchException e){
            System.out.println("Digite um número válido");
        } catch (Exception e){
            System.out.println("Digite um número válido");
        } finally {
       }


    }


}
