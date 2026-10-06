package arvores;

// Fila simples de clientes, feita com lista encadeada
public class FilaCliente {

    private class NoFila {
        Cliente dado;
        NoFila prox;
    }

    private NoFila inicio = null;
    private NoFila fim = null;

    // Coloca o cliente no fim da fila
    public void enfileirar(Cliente c) {
        NoFila novo = new NoFila();
        novo.dado = c;
        novo.prox = null;
        if (fim == null) {
            inicio = novo;
        } else {
            fim.prox = novo;
        }
        fim = novo;
    }

    // Retira o cliente do início da fila (null se a fila estiver vazia)
    public Cliente desenfileirar() {
        if (inicio == null) {
            return null;
        }
        Cliente c = inicio.dado;
        inicio = inicio.prox;
        if (inicio == null) {
            fim = null;
        }
        return c;
    }

    public boolean estaVazia() {
        return inicio == null;
    }
}
