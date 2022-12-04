package desafioPoker.controle;

import java.util.*;

public class Jogo {
    public static int qJogadores; // tem que ter entre 2 e 8 jogadores
    ArrayList<Jogador> participantes = new ArrayList();
    public int valorBlind;
    public int qCartasMesa;
    public int qCartasMao;
    public int valorMesa;
    public int maiorLanceMesa;
    public Jogador dealer;
    public Jogador smallBlind;
    public Jogador bigBlind;
    int jogadoresNaPartida;
    public Carta[] cartasComunitarias = new Carta[5];

    public Jogo(int qJogadores) {
        this.qJogadores = qJogadores;
        this.qCartasMesa = 5;
        this.qCartasMao = 2;
        this.valorBlind = 100;
        this.valorMesa=0;
        this.maiorLanceMesa=0;
    }

    public void cadastrarJogadores(String novoJogador){
            participantes.add(new Jogador(novoJogador));
    }

    public void statusJogadores(){
        for(int i=0; i<qJogadores;i++) {
            System.out.println(participantes.get(i).toString());
            System.out.print("Cartas do jogador: ");
            participantes.get(i).getCarta1().converterNumeroCarta();
            participantes.get(i).getCarta2().converterNumeroCarta();
            System.out.println("\n");
            System.out.println("------------------------------");

        }
        System.out.println("Cartas comunitária: ");
        for(int j=0; j<cartasComunitarias.length; j++){
                cartasComunitarias[j].converterNumeroCarta();
        }

    }

    public void apresentacao(){
        System.out.print("\n");
        System.out.println("Apresentação dos jogadores: ");
        for(int i=0; i<qJogadores;i++) {
            System.out.print(participantes.get(i).apresentarJogador());
            if (participantes.get(i) == dealer) {
                System.out.println("    Dealer");
            } else if (participantes.get(i) == smallBlind) {
                System.out.println("    Smallblind");
            } else if (participantes.get(i) == bigBlind) {
                System.out.println("    BigBlind");
            }else{
                System.out.println(" ");
            }


        }
    }

    public void definirDealerInicial(){
        Random aleatorio= new Random();
        int dealerInicial = aleatorio.nextInt(participantes.size() - 1);
        this.dealer = participantes.get(dealerInicial);
        this.smallBlind=participantes.get((dealerInicial+1>qJogadores-1)?(((qJogadores-1)-(dealerInicial))):(dealerInicial+1));
        this.bigBlind=participantes.get((dealerInicial+2>qJogadores-1)?(1-((qJogadores-1)-(dealerInicial))):(dealerInicial+2));
    }

    public void darCartas(){
        Baralho baralho = new Baralho();
        baralho.embaralhar();

        for(int i=0; i<cartasComunitarias.length; i++){
            cartasComunitarias[i]= baralho.getOrdemCartas()[i];
            baralho.getOrdemCartas()[i]=new Carta(0,0);
        }

        // dar as cartas para os jogadores
        int posicaoBaralho=4;
        for (int j=0; j<qJogadores; j++){
            participantes.get(j).setCarta1(baralho.getOrdemCartas()[posicaoBaralho+1]);
            participantes.get(j).setCarta2(baralho.getOrdemCartas()[posicaoBaralho+2]);
            posicaoBaralho+=2;
        }
    }

    public void preFlop(){
        System.out.println("Começo de partida: ");

        System.out.println(this.smallBlind.nome + " aposta obrigatóriamente:  " + (this.valorBlind/2));
        this.smallBlind.apostar(this.valorBlind/2);
        this.valorMesa+=this.valorBlind/2;

        System.out.println(this.bigBlind.nome + " aposta obrigatóriamente:  " + (this.valorBlind));
        this.bigBlind.apostar(this.valorBlind);
        this.valorMesa+=this.valorBlind;

        this.maiorLanceMesa = this.valorBlind;
        Jogador jogadorAnterior = this.bigBlind;
        jogadoresNaPartida = participantes.size();
        Scanner s1 = new Scanner(System.in);

        boolean ok = false;
        apostas: while (ok==false){

           int jogadorDaVez=((participantes.indexOf(jogadorAnterior)+1)>(participantes.size()-1)?(0):(participantes.indexOf(jogadorAnterior)+1));

            if(participantes.get(jogadorDaVez).jogando && participantes.get(jogadorDaVez).getAllwin() == false ) {
                System.out.println("----------------------------------------");
                System.out.print (participantes.get(jogadorDaVez).nome + "  Aposta corrente " + participantes.get(jogadorDaVez).getApostaCorrente()+  "  Mão do jogador: ");
                participantes.get(jogadorDaVez).getCarta1().converterNumeroCarta();
                participantes.get(jogadorDaVez).getCarta2().converterNumeroCarta();
                System.out.println(" ");

                perguntarJogador();
                int opcao = s1.nextInt();
                switch (opcao) {
                    case 1:
                        this.valorMesa += this.maiorLanceMesa-participantes.get(jogadorDaVez).getApostaCorrente();
                        participantes.get(jogadorDaVez).cobrir(this.maiorLanceMesa);
                        break;
                    case 2:
                        System.out.println("Digite o valor da aposta: ");
                        int valorAposta = s1.nextInt();
                        while (valorAposta>participantes.get(jogadorDaVez).getMontanteFichas() || valorAposta<(this.maiorLanceMesa-participantes.get(jogadorDaVez).getApostaCorrente())){
                            if(valorAposta>participantes.get(jogadorDaVez).getMontanteFichas()) {
                                System.out.println(" Valor insuficiante! Digite uma quantidade menor ou igual a " + participantes.get(jogadorDaVez).getMontanteFichas());
                            }else{
                                System.out.println(" Valor menor que o mínimo, digite pelo menos " + (this.maiorLanceMesa -participantes.get(jogadorDaVez).getApostaCorrente()));
                            }
                            valorAposta = s1.nextInt();
                        }

                        participantes.get(jogadorDaVez).apostar(valorAposta);
                        this.maiorLanceMesa = participantes.get(jogadorDaVez).getApostaCorrente();
                        this.valorMesa += valorAposta;
                        break;
                    case 3:
                        participantes.get(jogadorDaVez).correr();
                        jogadoresNaPartida--;
                        break;
                }
            }
           jogadorAnterior = participantes.get(jogadorDaVez);

           int quantidadeOk=0;
            for(int i=0; i<participantes.size();i++){
                if (participantes.get(i).getApostaCorrente() == maiorLanceMesa && participantes.get(i).getJogando()==true){
                    quantidadeOk++;
                };
            }
           if(quantidadeOk == jogadoresNaPartida){
               System.out.println("Fim do pre-flop");
               System.out.print("\n");
               break apostas;
           }

        }

    }

    public Jogador getDealer() {
        return dealer;
    }

    public void setDealer(Jogador dealer) {
        this.dealer = dealer;
    }

    protected void perguntarJogador(){

        System.out.println("Digite para: \n 1- Cobrir \n 2- Apostar \n 3- Correr ");
    }

    public void perguntarVencedor(){
        System.out.print("\n");
        System.out.println("Quem venceu a partida? ");

        for(int i=0; i<qJogadores;i++){
            System.out.print((i+1) + "- " + participantes.get(i).nome);
            if (participantes.get(i).jogando == false) {
                System.out.println("  (desistente)");
            } else {
                System.out.println(" ");
            }
        }
        Scanner entrada = new Scanner(System.in);
        int vencedor= entrada.nextInt();
        System.out.println("Vencedor: " + participantes.get(vencedor-1).nome + " recebeu "+ this.valorMesa + "!");
    }

    public void determinarVencedor(){

        List  <Carta> cartasJogadoresComunitarias = new ArrayList<>();
        for (int i = 0; i<participantes.size(); i++){
            if(participantes.get(i).getJogando()) {
                //junta as cartas
                for (int k = 0; k < cartasComunitarias.length; k++) {
                    cartasJogadoresComunitarias.add(cartasComunitarias[k]);
                }
                cartasJogadoresComunitarias.add(participantes.get(i).getCarta1());
                cartasJogadoresComunitarias.add(participantes.get(i).getCarta2());
                Collections.sort(cartasJogadoresComunitarias);

                int qCopas=0;
                int qPaus=0;
                int qEspadas=0;
                int qOuros=0;

                for(int y=0; y<cartasJogadoresComunitarias.size(); y++){
                    switch (cartasJogadoresComunitarias.get(y).getNaipe()){
                        case 1:
                            qCopas++;
                        case 2:
                            qEspadas++;
                        case 3:
                            qPaus++;
                        case 4:
                            qOuros++;
                    }


                }

                if(qCopas >= 5|| qPaus>= 5|| qEspadas >= 5|| qOuros >= 5){

                }
            }
        }
    }

    public int getMaiorLanceMesa() {
        return maiorLanceMesa;
    }

    public void setMaiorLanceMesa(int maiorLanceMesa) {
        this.maiorLanceMesa = maiorLanceMesa;
    }


}
