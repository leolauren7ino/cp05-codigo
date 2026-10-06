# CLAUDE.md — Checkpoint 5 (Códigos de Alta Performance, FIAP)

Projeto Java da Profª Patrícia Magna. Sistema que prioriza o contato de uma loja de varejo com clientes para ofertas especiais, usando Árvore Binária de Busca (ABB) e fila.
**Entrega: 08/10/2026**, upload do projeto zipado no portal da FIAP. Grupo de até 5 alunos.

## Materiais de referência (leia antes de codar)
- `docs/checkpoint5.pdf`: enunciado e critérios de avaliação.
- `docs/aulas/`: materiais de aula (ABB, filas, percursos). Siga o estilo deles: recursão sobre `No p`, métodos públicos recebendo o nó, nomes em português.

## Estrutura do projeto base (NÃO alterar a estrutura de packages)
```
src/
  aplicacao/DivulgaOferta.java   # menu (0-5) já montado; pontos de integração marcados em comentário
  arquivos/backupClientes.txt    # formato: nome,cpf,whatsapp,gasto (um cliente por linha)
  arvores/AbbInt.java            # ABB de inteiros vista em aula; BASE OBRIGATÓRIA da nova ABB
```
- Packages existentes: `aplicacao`, `arquivos`, `arvores`. Novas classes (`Cliente`, `AbbCliente`, fila) entram em packages coerentes, sem renomear nada existente.
- A classe de aplicação se chama `DivulgaOferta` (o PDF escreve "DivulgaOfertas"). **Não renomear.**
- Os nomes e RMs do grupo vão no comentário no topo de `DivulgaOferta`. Deixar o placeholder `NOME - RM`. **Nunca inventar nomes.**

## Regras obrigatórias do enunciado
1. A ABB de clientes é baseada em `AbbInt` (mesma estrutura `No`, `esq`, `dir`, mesmo padrão recursivo), guardando um objeto `Cliente` no nó.
2. A ABB de cadastro é organizada por **CPF** (comparar como `String`). A ABB de oferta é organizada por **total gasto**.
3. A ABB de oferta só contém clientes com `gasto >= valor mínimo` **e** `aptoOferta == true`.
4. A fila é gerada **pelo percurso** da ABB de oferta, do maior para o menor gasto. **Proibido ordenar depois.** Usar o percurso in-ordem invertido (direita, nó, esquerda), igual ao `mostrarEmOrdem` do `AbbInt`.
5. Depois de gerar a fila, a ABB de oferta é **esvaziada**.
6. Retirar **um cliente por vez** da fila, lendo a resposta pelo teclado. Aceitou: mensagem + `aptoOferta = false` na ABB de cadastro (busca por CPF). Recusou: mensagem e cadastro inalterado. Só volta ao menu quando a fila esvaziar.
7. Ao encerrar (opção 0), listar os clientes da ABB de cadastro que ainda estão aptos (não aceitaram nenhuma oferta).
8. Somente biblioteca padrão do Java. A implementação de fila é livre; preferir uma fila própria simples (lista encadeada), fácil de explicar.

## Classe `Cliente`
Atributos: `nome`, `cpf`, `whatsapp`, `totalGasto` (double), `aptoOferta` (boolean, `true` ao cadastrar). Construtor, getters/setters necessários e método que apresenta os dados na tela.

## Métodos obrigatórios da `AbbCliente` (valem pontos no critério 2)
1. Inserir por CPF.
2. Inserir por total gasto (aceitar valores iguais sem perder cliente).
3. Consultar por CPF e apresentar todos os dados.
4. Somatório de todos os gastos (percurso).
5. Contar clientes com gasto acima de um limite.
6. Marcar cliente como não apto (busca por CPF).
7. Percorrer o cadastro e gerar outra ABB organizada por gasto (com o filtro da regra 3).
8. Percorrer a ABB de oferta e gerar a fila em ordem decrescente de gasto.

Extras exigidos pelo menu/enunciado: remover por CPF (opção 5), esvaziar a ABB de oferta, listar clientes ainda aptos (opção 0).

## Menu (já existe em `DivulgaOferta`; apenas ligar aos métodos)
- 0: lista não-aceitantes e encerra · 1: carrega `backupClientes.txt` (só uma vez, flag `aptoLer`) · 2: inscreve um cliente · 3: oferta (fluxo da regra 6) · 4: submenu (consulta CPF, somatório, contagem acima de valor, voltar) · 5: remove por CPF.
- Tratar bordas: CPF não encontrado, árvore vazia, nenhum cliente elegível, CPF duplicado (avisar e não duplicar), entrada inválida no `Scanner` (cuidado ao misturar `nextInt`/`nextDouble` com `nextLine`).

## Compilar e rodar (sempre a partir da raiz do projeto; o caminho do arquivo de dados é relativo)
```
javac -d out $(find src -name '*.java')
java -cp out aplicacao.DivulgaOferta
```
Testar com roteiro de entrada via stdin cobrindo as opções 1, 2, 3 (aceite e recusa), 4, 5 e 0. Conferir que a fila sai em ordem decrescente de gasto e que quem aceitou não reaparece na oferta seguinte.

## Como trabalhar
1. Ler PDF, aulas e código base; apresentar um **plano curto** (classes, métodos, assinaturas) e **esperar confirmação** antes de codar.
2. Implementar em etapas: `Cliente`, `AbbCliente`, fila, menu. Compilar a cada etapa.
3. Comentar o código em português, de forma curta. O grupo precisa defender o trabalho, então prefira código simples e legível.
4. Ao final, conferir item por item os critérios de avaliação do PDF e informar qual parte do código cobre cada um.

## Restrições de segurança do fluxo
- **Não fazer `git push`** nem mexer em remotos. O repositório base (`leolauren7ino/cp05-codigo`) é da professora e é somente leitura para o grupo. Commits locais só se o usuário pedir.
- Não incluir `.git/` nem `out/` no zip de entrega.
