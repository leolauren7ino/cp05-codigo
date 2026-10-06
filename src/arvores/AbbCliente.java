package arvores;

// ABB de clientes, baseada na AbbInt: o nó guarda um objeto Cliente
public class AbbCliente {

    private class No {
        Cliente dado;
        No esq, dir;
    }

    public No root = null;

    /* ---------- 1) Inserção por CPF (ABB de cadastro) ---------- */

    // Menor CPF vai para a esquerda, maior ou igual para a direita.
    // O menu avisa e não chama a inserção se o CPF já existe.
    public No inserirPorCpf(No p, Cliente c) {
        if (p == null) {
            p = new No();
            p.dado = c;
            p.esq = null;
            p.dir = null;
        } else if (c.getCpf().compareTo(p.dado.getCpf()) < 0) {
            p.esq = inserirPorCpf(p.esq, c);
        } else {
            p.dir = inserirPorCpf(p.dir, c);
        }
        return p;
    }

    // Retorna false (e não insere) se o CPF já está cadastrado
    public boolean inserirPorCpf(Cliente c) {
        if (consultar(c.getCpf()) != null) {
            return false;
        }
        root = inserirPorCpf(root, c);
        return true;
    }

    /* ---------- 2) Inserção por total gasto (ABB de oferta) ---------- */

    // Menor gasto vai para a esquerda, igual ou maior para a direita (igual não se perde)
    public No inserirPorGasto(No p, Cliente c) {
        if (p == null) {
            p = new No();
            p.dado = c;
            p.esq = null;
            p.dir = null;
        } else if (c.getTotalGasto() < p.dado.getTotalGasto()) {
            p.esq = inserirPorGasto(p.esq, c);
        } else {
            p.dir = inserirPorGasto(p.dir, c);
        }
        return p;
    }

    public void inserirPorGasto(Cliente c) {
        root = inserirPorGasto(root, c);
    }

    /* ---------- 3) Consulta por CPF ---------- */

    // Devolve o cliente encontrado ou null
    public Cliente consultar(No p, String cpf) {
        if (p != null) {
            int comp = cpf.compareTo(p.dado.getCpf());
            if (comp == 0) {
                return p.dado;
            } else if (comp < 0) {
                return consultar(p.esq, cpf);
            } else {
                return consultar(p.dir, cpf);
            }
        } else {
            return null;
        }
    }

    public Cliente consultar(String cpf) {
        return consultar(root, cpf);
    }

    // Consulta e apresenta todos os dados do cliente
    public boolean consultarEApresentar(String cpf) {
        Cliente c = consultar(root, cpf);
        if (c == null) {
            return false;
        }
        c.apresentar();
        return true;
    }

    /* ---------- 4) Somatório dos gastos ---------- */

    public double somaGastos(No p, double soma) {
        if (p != null) {
            soma = soma + p.dado.getTotalGasto();
            soma = somaGastos(p.esq, soma);
            soma = somaGastos(p.dir, soma);
        }
        return soma;
    }

    public double somaGastos() {
        return somaGastos(root, 0);
    }

    /* ---------- 5) Quantidade de clientes com gasto acima de um limite ---------- */

    // A ABB é organizada por CPF, então é preciso percorrer todos os nós
    public int contaAcima(No p, double limite, int cont) {
        if (p != null) {
            if (p.dado.getTotalGasto() > limite) {
                cont++;
            }
            cont = contaAcima(p.esq, limite, cont);
            cont = contaAcima(p.dir, limite, cont);
        }
        return cont;
    }

    public int contaAcima(double limite) {
        return contaAcima(root, limite, 0);
    }

    /* ---------- 6) Marcar cliente como não apto (busca por CPF) ---------- */

    public boolean marcarNaoApto(No p, String cpf) {
        if (p != null) {
            int comp = cpf.compareTo(p.dado.getCpf());
            if (comp == 0) {
                p.dado.setAptoOferta(false);
                return true;
            } else if (comp < 0) {
                return marcarNaoApto(p.esq, cpf);
            } else {
                return marcarNaoApto(p.dir, cpf);
            }
        } else {
            return false;
        }
    }

    public boolean marcarNaoApto(String cpf) {
        return marcarNaoApto(root, cpf);
    }

    /* ---------- 7) Percorre o cadastro e gera a ABB de oferta ---------- */

    // Percorre esta ABB (cadastro) e insere na ABB oferta, organizada por gasto,
    // só os clientes com gasto >= minimo e aptos para a oferta
    public void gerarAbbOferta(No p, AbbCliente oferta, double minimo) {
        if (p != null) {
            gerarAbbOferta(p.esq, oferta, minimo);
            if (p.dado.getTotalGasto() >= minimo && p.dado.isAptoOferta()) {
                oferta.root = oferta.inserirPorGasto(oferta.root, p.dado);
            }
            gerarAbbOferta(p.dir, oferta, minimo);
        }
    }

    public void gerarAbbOferta(AbbCliente oferta, double minimo) {
        gerarAbbOferta(root, oferta, minimo);
    }

    /* ---------- 8) Percorre a ABB de oferta e gera a fila ---------- */

    // Mesmo percurso do mostrarEmOrdem da AbbInt (direita, nó, esquerda):
    // o maior gasto entra primeiro na fila, sem precisar ordenar depois
    public void gerarFila(No p, FilaCliente fila) {
        if (p != null) {
            gerarFila(p.dir, fila);
            fila.enfileirar(p.dado);
            gerarFila(p.esq, fila);
        }
    }

    public void gerarFila(FilaCliente fila) {
        gerarFila(root, fila);
    }

    /* ---------- Extras ---------- */

    // Remoção por CPF: mesmos 3 casos do removeValor da AbbInt
    public No removerPorCpf(No p, String cpf) {
        if (p != null) {
            if (cpf.equals(p.dado.getCpf())) {
                if (p.esq == null && p.dir == null) {   // nó a ser removido é nó folha
                    return null;
                }
                if (p.esq == null) {   // sem sub-árvore esquerda: o ponteiro passa a apontar para a direita
                    return p.dir;
                } else {
                    if (p.dir == null) {   // sem sub-árvore direita: o ponteiro passa a apontar para a esquerda
                        return p.esq;
                    } else {
                        // dois filhos: o menor nó da sub-árvore direita recebe a sub-árvore esquerda
                        No aux, ref;
                        ref = p.dir;
                        aux = p.dir;
                        while (aux.esq != null) {
                            aux = aux.esq;
                        }
                        aux.esq = p.esq;
                        return ref;
                    }
                }
            } else {   // procura o CPF a ser removido na ABB
                if (cpf.compareTo(p.dado.getCpf()) < 0) {
                    p.esq = removerPorCpf(p.esq, cpf);
                } else {
                    p.dir = removerPorCpf(p.dir, cpf);
                }
            }
        }
        return p;
    }

    // Retorna false se o CPF não existe
    public boolean removerPorCpf(String cpf) {
        if (consultar(cpf) == null) {
            return false;
        }
        root = removerPorCpf(root, cpf);
        return true;
    }

    // Esvazia a árvore (usado na ABB de oferta depois de gerar a fila)
    public void esvaziar() {
        root = null;
    }

    public boolean estaVazia() {
        return root == null;
    }

    // Apresenta (em ordem de CPF) os clientes ainda aptos; devolve quantos são
    public int listarAptos(No p, int cont) {
        if (p != null) {
            cont = listarAptos(p.esq, cont);
            if (p.dado.isAptoOferta()) {
                p.dado.apresentar();
                System.out.println();
                cont++;
            }
            cont = listarAptos(p.dir, cont);
        }
        return cont;
    }

    public int listarAptos() {
        return listarAptos(root, 0);
    }
}
