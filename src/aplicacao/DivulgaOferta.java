package aplicacao;


import arvores.AbbCliente;
import arvores.Cliente;
import arvores.FilaCliente;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DivulgaOferta {

    /*
     *
     * NOMES E RM dos alunos que compõem o grupo
     *
     * Felipe Durante Slavic - RM 565687
     * Leonardo Laurentino de Curcio - RM 561455
     *
     */

    public static void main(String[] args) {

        Scanner le = new Scanner(System.in);
        // ABB de cadastro (organizada por CPF) e ABB de oferta (organizada por total gasto)
        AbbCliente cadastro = new AbbCliente();
        AbbCliente oferta = new AbbCliente();
        // Fila de clientes a contactar (lista encadeada)
        FilaCliente filaOferta = new FilaCliente();

        int opcao, op;
        String nome, whatsapp, cpf;
        double totalGasto;
        boolean aptoLer = true;
        do {
            System.out.println(" 0 - Encerrar o programa");
            System.out.println(" 1 - Inscrição de clientes por leitura de arquivo");
            System.out.println(" 2 - Inscrição de um cliente");
            System.out.println(" 3 - Oferta de novo produto/promocacao");
            System.out.println(" 4 - Entrar no Submenu ");
            System.out.println(" 5 - Remove um cliente do cadastro");
            opcao = lerInt(le, "Opção: ");
            switch (opcao) {
                case 0 -> {
                    System.out.println("\n\nClientes que nao aceitaram ou nao estavam adequados para a oferta");
                    // Lista os clientes do cadastro que ainda estão aptos
                    if (cadastro.listarAptos() == 0) {
                        System.out.println("Nenhum cliente nessa situação.");
                    }
                }
                case 1 -> {
                    if (aptoLer) {
                        // só trava a opção se o arquivo foi lido; se não achou, pode tentar de novo
                        if (cadastrarBackupDeClientes(cadastro)) {
                            aptoLer = false;
                        }
                    } else {
                        System.out.println("Arquivo já cadastrado!");
                    }
                }
                case 2 -> {
                    nome = lerTexto(le, "Digite nome: ");
                    cpf = lerTexto(le, "Digite CPF: ");
                    whatsapp = lerTexto(le, "Whatsapp: ");
                    totalGasto = lerDouble(le, "Informe total gasto do cliente R$: ");

                    // Instancia o Cliente e insere na ABB de cadastro (CPF repetido não entra)
                    Cliente novo = new Cliente(nome, cpf, whatsapp, totalGasto);
                    if (cadastro.inserirPorCpf(novo)) {
                        System.out.println("Cliente cadastrado!");
                    } else {
                        System.out.println("CPF já cadastrado. Cliente não inserido.");
                    }
                }
                case 3 -> {
                    totalGasto = lerDouble(le, "Qual o valor de saldo mínimo exigido: R$ ");

                    // Percorre o cadastro e gera a ABB de oferta (organizada por gasto)
                    cadastro.gerarAbbOferta(oferta, totalGasto);
                    if (oferta.estaVazia()) {
                        System.out.println("Nenhum cliente elegível para essa oferta.");
                    } else {
                        // Percurso da ABB de oferta gera a fila: maior gasto primeiro
                        oferta.gerarFila(filaOferta);
                        // Esvazia a ABB de oferta para a próxima oferta
                        oferta.esvaziar();

                        // Simula o contato: um cliente por vez até esvaziar a fila
                        while (!filaOferta.estaVazia()) {
                            Cliente atual = filaOferta.desenfileirar();
                            System.out.println("\nContactar cliente:");
                            atual.apresentar();
                            if (lerSimNao(le, "O cliente aceitou a oferta? (s/n): ")) {
                                System.out.println("Cliente " + atual.getNome() + " ACEITOU a oferta!");
                                // Atualiza o cadastro (busca por CPF): não está mais apto
                                cadastro.marcarNaoApto(atual.getCpf());
                            } else {
                                System.out.println("Cliente " + atual.getNome() + " RECUSOU a oferta.");
                            }
                        }
                        System.out.println("\nFila de contatos finalizada.");
                    }
                }
                case 4 -> {
                    do {
                        System.out.println("\t 1) Consulta cliente buscando pelo CPF ");
                        System.out.println("\t 2) Apresenta o total de gasto de todos os clientes");
                        System.out.println("\t 3) Apresenta a quantidade de clientes com saldo acima de um valor a ser consultado");
                        System.out.println("\t 4) Volta menu principal");
                        op = lerInt(le, "\t Opção: ");
                        switch (op) {
                            case 1:
                                cpf = lerTexto(le, "Informe CPF para consulta: ");
                                if (!cadastro.consultarEApresentar(cpf)) {
                                    System.out.println("CPF não encontrado.");
                                }
                                break;
                            case 2:
                                if (cadastro.estaVazia()) {
                                    System.out.println("Cadastro vazio.");
                                } else {
                                    System.out.printf("Total de gastos de todos os clientes: R$ %.2f%n", cadastro.somaGastos());
                                }
                                break;
                            case 3:
                                totalGasto = lerDouble(le, "Informe o valor: R$ ");
                                System.out.printf("Clientes com gasto acima de R$ %.2f: %d%n",
                                        totalGasto, cadastro.contaAcima(totalGasto));
                                break;
                            case 4:
                                System.out.println("Retornando Menu Principal");
                                break;
                            default:
                                System.out.println("Opção invalida");
                        }
                    } while (op != 4);
                }
                case 5 -> {
                    cpf = lerTexto(le, "Informe CPF do cliente que deseja ser retirado do cadastro: ");
                    if (cadastro.removerPorCpf(cpf)) {
                        System.out.println("Cliente removido do cadastro.");
                    } else {
                        System.out.println("CPF não encontrado.");
                    }
                }
                default -> System.out.println("Opção inválida");
            }

        } while (opcao != 0);

        le.close();

    }

    // Lê o arquivo de clientes e insere cada um na ABB de cadastro
    // Devolve true se o arquivo foi lido e false se não foi encontrado
    public static boolean cadastrarBackupDeClientes(AbbCliente cadastro) {
        String caminhoDoArquivo = "src/arquivos/backupClientes.txt";
        int lidos = 0, repetidos = 0, invalidos = 0;

        try {
            // Criar um objeto File com o caminho do arquivo
            File arquivo = new File(caminhoDoArquivo);

            // Criar um Scanner para ler o arquivo
            Scanner leArq = new Scanner(arquivo);

            // Loop para ler linha por linha até o final do arquivo
            while (leArq.hasNextLine()) {
                // Ler a próxima linha
                String linha = leArq.nextLine();
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] partes = linha.split(",");
                try {
                    double totalGasto = Double.parseDouble(partes[3].trim());
                    Cliente cliente = new Cliente(partes[0].trim(), partes[1].trim(), partes[2].trim(), totalGasto);
                    // insere na Abb cadastro o cliente lido do arquivo
                    if (cadastro.inserirPorCpf(cliente)) {
                        lidos++;
                    } else {
                        repetidos++;
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    invalidos++;
                }
            }
            // Fechar o objeto da classe Scanner leArq
            leArq.close();
            System.out.println(lidos + " cliente(s) cadastrado(s) do arquivo. Repetidos: "
                    + repetidos + ". Linhas inválidas: " + invalidos + ".");
            return true;
        } catch (FileNotFoundException e) {
            // Caso o arquivo não seja encontrado
            System.out.println("Arquivo nao encontrado: " + e.getMessage());
            return false;
        }

    }

    // Leituras pelo teclado: tudo com nextLine para não sobrar quebra de linha no buffer

    public static String lerTexto(Scanner le, String mensagem) {
        String texto;
        do {
            System.out.print(mensagem);
            texto = le.nextLine().trim();
        } while (texto.isEmpty());
        return texto;
    }

    public static int lerInt(Scanner le, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(le.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    public static double lerDouble(Scanner le, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                // aceita vírgula ou ponto como separador decimal
                return Double.parseDouble(le.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um valor numérico.");
            }
        }
    }

    public static boolean lerSimNao(Scanner le, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resp = le.nextLine().trim().toLowerCase();
            if (resp.equals("s")) {
                return true;
            } else if (resp.equals("n")) {
                return false;
            }
            System.out.println("Responda com s ou n.");
        }
    }

}
