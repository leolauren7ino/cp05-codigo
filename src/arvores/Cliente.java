package arvores;

public class Cliente {

    private String nome;
    private String cpf;
    private String whatsapp;
    private double totalGasto;
    private boolean aptoOferta;

    // Todo cliente é cadastrado apto para receber oferta
    public Cliente(String nome, String cpf, String whatsapp, double totalGasto) {
        this.nome = nome;
        this.cpf = cpf;
        this.whatsapp = whatsapp;
        this.totalGasto = totalGasto;
        this.aptoOferta = true;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public double getTotalGasto() {
        return totalGasto;
    }

    public boolean isAptoOferta() {
        return aptoOferta;
    }

    public void setAptoOferta(boolean aptoOferta) {
        this.aptoOferta = aptoOferta;
    }

    // Apresenta todos os dados do cliente na tela
    public void apresentar() {
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("Whatsapp: " + whatsapp);
        System.out.printf("Valor Total em Compras: R$ %.2f%n", totalGasto);
        System.out.println("Apto para Oferta: " + (aptoOferta ? "sim" : "não"));
    }
}
