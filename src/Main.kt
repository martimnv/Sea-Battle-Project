import kotlin.random.Random

var numLinhas = -1
var numColunas = -1

var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()

fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int> {

    when {
        numLinhas == 4 -> return arrayOf(2,0,0,0)               //RETORNA O NÚMERO DE BARCOS PARA CADA TIPO DE TABULEIRO
        numLinhas == 5 -> return arrayOf(1,1,1,0)
        numLinhas == 7 -> return arrayOf(2,1,1,1)
        numLinhas == 8 -> return arrayOf(2,2,1,1)
        numLinhas == 10 -> return arrayOf(3,2,1,1)
        else -> return arrayOf()
    }
}

fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>> {

    return Array(numLinhas) {arrayOfNulls<Char?>(numColunas)}           //CRIAÇÃO DE UM TABULEIRO VAZIO
}

fun coordenadaContida(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {

    val numLinhas = tabuleiro.size                  //INDICA QUE O NUMERO DE LINHAS É MESMO QUE O TAMANHO DO TABULEIRO
    val numColunas = tabuleiro[1].size              //COMEÇA NA SEGUNDA LINHA DO TABULEIRO

    return !(linha == 1 && coluna == 1) &&
            !(linha == 1 && coluna == numColunas) &&
            !(linha == numLinhas && coluna == 1) &&
            !(linha == numLinhas && coluna == numColunas) &&
            linha > 0 && linha <= numLinhas &&
            coluna > 0 && coluna <= numColunas
}

fun limparCoordenadasVazias(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int,Int>> {

    var count = 0
    // INICIA UM CONTADOR PARA ACOMPANHAR O NÚMERO DE COORDENADAS NÃO NULAS
    for (numeros in coordenadas) {              //SE OS NUMEROS NO ARRAY NAO FOREM NULL ENTAO ADICIONA 1 NO COUNT
        if (numeros != Pair(0,0)) {
            count++
        }
    }

    val coordenadasValidas = Array(count) {Pair(0,0)}           // CRIA UM NOVO ARRAY DE TAMANHO IGUAL AO CONTADOR, INICIALIZADO COM Pair(0,0)
    var idx = 0

    // PREENCHE O NOVO ARRAY APENAS COM AS COORDENADAS NÃO NULAS (DIFERENTES DE Pair(0,0))
    for (numeros in coordenadas) {
        if (numeros != Pair(0,0)) {
            coordenadasValidas[idx++] = numeros
        }
    }
    return coordenadasValidas                // RETORNA O ARRAY RESULTANTE CONTENDO APENAS AS COORDENADAS NÃO NULAS
}

fun juntarCoordenadas(coordenadas1: Array<Pair<Int, Int>>, coordenadas2: Array<Pair<Int, Int>>): Array<Pair<Int,Int>> {

    val somarCoordenadas = coordenadas1 + coordenadas2              //JUNÇAO DAS DUAS COORDENADAS
    return somarCoordenadas                                         // RETORNA A NOVA LISTA COM A JUNÇAO DAS COORDENDAS
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int,Int>> {

    val coordenadas = Array(dimensao) {Pair(0,0)}                   // INICIALIZA UM ARRAY DE COORDENADAS COM A DIMENSÃO ESPECIFICADA

    if (!coordenadaContida(tabuleiro,linha,coluna)) {               // VERIFICA SE A COORDENADA INICIAL ESTÁ CONTIDA NO TABULEIRO
        return emptyArray()                                         // SE NÃO, RETORNA UMA LISTA VAZIA
    }

    for (i in 0 until dimensao) {                             // LOOP PARA GERAR AS COORDENADAS COM BASE NA ORIENTAÇÃO E DIMENSÃO
        val linhaNova: Int
        val colunaNova: Int

        when (orientacao) {                                         // CALCULA AS NOVAS COORDENADAS COM BASE NA ORIENTAÇÃO
            "N" -> {linhaNova = linha - i; colunaNova = coluna}
            "S" -> {linhaNova = linha + i; colunaNova = coluna}
            "E" -> {linhaNova = linha; colunaNova = coluna + i}
            "O" -> {linhaNova = linha; colunaNova = coluna - i}
            else -> {return emptyArray()}                           // SE A ORIENTAÇÃO NÃO É VÁLIDA, RETORNA UMA LISTA VAZIA
        }

        if (!coordenadaContida(tabuleiro,linha,coluna)) {           // VERIFICA SE AS NOVAS COORDENADAS ESTÃO CONTIDAS NO TABULEIRO
            return emptyArray()                                     // SE NÃO, RETORNA UMA LISTA VAZIA
        }

        coordenadas[i] = Pair(linhaNova,colunaNova)                 // ATRIBUI AS NOVAS COORDENADAS AO ARRAY
    }
    return coordenadas                                               // RETORNA O ARRAY DE COORDENADAS GERADO PARA O NAVIO
}

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int,Int>> {

    if (!coordenadaContida(tabuleiro,linha,coluna)){                // VERIFICA SE A COORDENADA INICIAL ESTÁ CONTIDA NO TABULEIRO
        return emptyArray()                                         // SE NÃO, RETORNA UMA LISTA VAZIA
    }

    var coordenadasAVoltaDoBarco = emptyArray<Pair<Int,Int>>()      // INICIALIZA UM ARRAY DE COORDENADAS VAZIO

    for (i in 0 until dimensao) {                 // LOOP PARA GERAR AS COORDENADAS NA FRONTEIRA COM BASE NA ORIENTAÇÃO E DIMENSÃO DO BARCO
        val linhaNova: Int
        val colunaNova: Int

        when (orientacao) {                             // CALCULA AS NOVAS COORDENADAS COM BASE NA ORIENTAÇÃO
            "N" -> {
                linhaNova = linha - i; colunaNova = coluna
            }

            "S" -> {
                linhaNova = linha + i; colunaNova = coluna
            }

            "E" -> {
                linhaNova = linha; colunaNova = coluna + i
            }

            "O" -> {
                linhaNova = linha; colunaNova = coluna - i
            }

            else -> {
                return emptyArray()                     // SE A ORIENTAÇÃO NÃO É VÁLIDA, RETORNA UMA LISTA VAZIA
            }
        }

        if (coordenadaContida(tabuleiro, linhaNova, colunaNova)) {              // VERIFICA SE AS NOVAS COORDENADAS ESTÃO CONTIDAS NO TABULEIRO
            for (a in -1..1) {                                     // LOOP PARA GERAR COORDENADAS AO REDOR DAS NOVAS COORDENADAS DO BARCO
                for (b in -1..1) {
                    val linhaAoRedor = linhaNova + a
                    val colunaAoRedor = colunaNova + b

                    // VERIFICA SE AS COORDENADAS AO REDOR ESTÃO CONTIDAS E NÃO SÃO AS MESMAS DO BARCO
                    if (coordenadaContida(tabuleiro,linhaAoRedor,colunaAoRedor) &&
                        (linhaAoRedor != linhaNova || colunaAoRedor != colunaNova)) {
                        coordenadasAVoltaDoBarco += Pair(linhaAoRedor,colunaAoRedor)
                    }
                }
            }
        } else {
            return emptyArray()                                         // SE AS NOVAS COORDENADAS NÃO ESTÃO CONTIDAS, RETORNA UMA LISTA VAZIA
        }
    }
    return coordenadasAVoltaDoBarco                                     // RETORNA O ARRAY DE COORDENADAS NA FRONTEIRA DO BARCO
}

fun estaLivre(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int,Int>>): Boolean {

    for (linha in tabuleiro) {                                              // PERCORRE CADA LINHA DO TABULEIRO
        for (coordenadaAtual in linha) {                                    // PERCORRE CADA COLUNA DO TABULEIRO
            if (coordenadaAtual != null && coordenadaAtual != ' ') {    // VERIFICA SE A COORDENADA ATUAL NÃO É NULA E NÃO É UM ESPAÇO EM BRANCO
                return false                                            // SE NÃO ESTIVER VAZIA, RETORNA FALSO
            }
        }
    }
    return true                                                         // SE TODAS AS COORDENADAS ESTIVEREM VAZIAS, RETORNA VERDADEIRO
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {

    if (coordenadaContida(tabuleiro,linha,coluna)) {                // VERIFICA SE A COORDENADA ESTÁ CONTIDA NO TABULEIRO
        if (estaLivre(tabuleiro, arrayOf(Pair(linha,coluna)))) {    // VERIFICA SE A COORDENADA ESTÁ LIVRE
            tabuleiro[linha-1][coluna-1] = '1'                      // SE SIM, INSERE O NAVIO ('1') NA COORDENADA ESPECIFICADA
            return true                                             // RETORNA VERDADEIRO SE A INSERÇÃO FOR BEM-SUCEDIDA
        }
    }
    return false                                                    // RETORNA FALSO SE A COORDENADA NÃO ESTIVER CONTIDA OU NÃO ESTIVER LIVRE
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Boolean {
    // VERIFICA SE A POSIÇÃO INICIAL ESTÁ FORA DOS LIMITES DO TABULEIRO
    if (linha !in 1..tabuleiro.size && coluna !in 1..tabuleiro[0].size) return false

    var novaLinha = linha
    var novaColuna = coluna

    for (i in 0 until dimensao) {     // LOOP PARA VERIFICAR SE AS NOVAS COORDENADAS DO NAVIO ESTÃO FORA DOS LIMITES OU JÁ ESTÃO OCUPADAS
        if (novaLinha !in 1..tabuleiro.size && novaColuna !in 1..tabuleiro[0].size ||
            tabuleiro[novaLinha - 1][novaColuna - 1] != null) {
            return false                     // RETORNA FALSO SE AS COORDENADAS NÃO ESTIVEREM DISPONÍVEIS
        }
        when (orientacao) {                  // ATUALIZA AS NOVAS COORDENADAS COM BASE NA ORIENTAÇÃO
            "N" -> novaLinha--
            "S" -> novaLinha++
            "O" -> novaColuna--
            "E" -> novaColuna++
        }
    }

    for (i in -1..1) {                   // LOOP PARA VERIFICAR SE AS COORDENADAS AO REDOR DO NAVIO ESTÃO LIVRES
        for (j in -1..1) {
            val linhaSeguinte = linha + i
            val colunaSeguinte = coluna + j

            if (linhaSeguinte in 1..tabuleiro.size && colunaSeguinte in 1..tabuleiro[0].size &&
                tabuleiro[linhaSeguinte - 1][colunaSeguinte- 1] != null) {
                return false          // VERIFICA SE AS COORDENADAS AO REDOR ESTÃO FORA DOS LIMITES OU JÁ ESTÃO OCUPADAS E SE SIM RETORNA FALSO
            }
        }
    }

    novaLinha = linha           // RESETA AS COORDENADAS INICIAIS
    novaColuna = coluna
    for (i in 0 until dimensao) {           // LOOP PARA INSERIR O NAVIO NO TABULEIRO
        tabuleiro[novaLinha - 1][novaColuna - 1] = when (dimensao) {        // ATRIBUI O CARACTERE DO NAVIO DE ACORDO COM A DIMENSÃO
            1 -> '1'
            2 -> '2'
            3 -> '3'
            4 -> '4'
            else -> return false
        }
        when (orientacao) {             // ATUALIZA AS NOVAS COORDENADAS COM BASE NA ORIENTAÇÃO
            "N" -> novaLinha--
            "S" -> novaLinha++
            "O" -> novaColuna--
            "E" -> novaColuna++
        }
    }
    return true                     // RETORNA VERDADEIRO SE O NAVIO FOI INSERIDO COM SUCESSO
}

fun navioCompleto(tabuleiroPalpites: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {

    if (!coordenadaContida(tabuleiroPalpites, linha, coluna)) {     // VERIFICA SE A COORDENADA ESTÁ CONTIDA NO TABULEIRO DE PALPITES
        return false                // RETORNA FALSO SE A COORDENADA NÃO ESTIVER CONTIDA
    }
    return tabuleiroPalpites[linha - 1][coluna - 1] == '1'      // RETORNA VERDADEIRO SE A COORDENADA CONTÉM UM NAVIO ('1')
}

fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, navios: Array<Int>) {

    var submarinos = 0
    val tipoDeTabuleiro = tabuleiro.size

    while (submarinos < 2) {                // LOOP PARA COLOCAR 2 SUBMARINOS NO TABULEIRO

        val linha = (0 until tipoDeTabuleiro).random()              // GERA COORDENADAS ALEATÓRIAS NO TABULEIRO
        val coluna = (0 until tipoDeTabuleiro).random()

        if (tabuleiro[linha][coluna] == null) {                     // VERIFICA SE A COORDENADA ESTÁ VAZIA (NULA)
            var ocupado = false

            for (linhamovel in -1..1) {          // LOOP PARA VERIFICAR SE AS COORDENADAS AO REDOR JÁ ESTÃO OCUPADAS POR OUTROS SUBMARINOS
                for (colunamovel in -1..1) {
                    val verificarEmCimaEBaixo = linha + linhamovel
                    val verificarAEsquerdaEDireita = coluna + colunamovel
                    if (verificarEmCimaEBaixo in 0 until tipoDeTabuleiro && verificarAEsquerdaEDireita in 0 until tipoDeTabuleiro &&
                        tabuleiro[verificarEmCimaEBaixo][verificarAEsquerdaEDireita] == '1') { // VERIFICA SE AS COORDENADAS AO REDOR CONTÊM UM SUBMARINO ('1')
                        ocupado = true
                    }
                }
            }
            if (!ocupado) {                             // SE NÃO HÁ SUBMARINOS NAS COORDENADAS AO REDOR, INSERE UM SUBMARINO
                tabuleiro[linha][coluna] = '1'
                submarinos++
            }
        }
    }
}

fun obtemMapa(tabuleiro: Array<Array<Char?>>, verificacao: Boolean): Array<String?> {

    val legenda = criaLegendaHorizontal(tabuleiro[0].size)          // CRIA A LEGENDA HORIZONTAL
    var terreno : Array<String?> = arrayOf("| $legenda |")
    var linhas = 1

    if (tabuleiro[0].size !in 1..26) {                      // VERIFICA SE O NÚMERO DE COLUNAS ESTÁ NO INTERVALO DE 1 A 26
        return arrayOf("")                              // RETORNA UM ARRAY VAZIO SE O NÚMERO DE COLUNAS NÃO ESTIVER NO INTERVALO VÁLIDO
    } else {
        while (linhas <= tabuleiro.size) {              // LOOP PARA PERCORRER CADA LINHA DO TABULEIRO
            var texto = ""
            var repeticoesColuna = 0
            while (repeticoesColuna <= tabuleiro[0].size){              // LOOP PARA PERCORRER CADA COLUNA DA LINHA
                repeticoesColuna++
                texto += if (repeticoesColuna != tabuleiro[linhas - 1].size + 1) {
                    if (tabuleiro[linhas - 1][repeticoesColuna - 1] != null) {
                        "| ${tabuleiro[linhas - 1][repeticoesColuna - 1]} "
                    } else {                 // SE A COORDENADA ESTIVER VAZIA, INSERE "~" OU "?" CONFORME A OPÇÃO DE VERIFICAÇÃO
                        if (verificacao) {
                            "| ~ "
                        } else {
                            "| ? "
                        }
                    }
                } else {
                    "| $linhas"                 // INSERE O NÚMERO DA LINHA NO FINAL DA LINHA
                }
            }
            terreno += texto
            linhas++
        }
    }
    return terreno
}


fun lancarTiro(tabuleiroReal: Array<Array<Char?>>, tabuleiroPalpites: Array<Array<Char?>>, coordenadas: Pair<Int, Int>): String {

    //
    val linha = coordenadas.first                // OBTÉM AS COORDENADAS DO TIRO
    val coluna = coordenadas.second

    if (!coordenadaContida(tabuleiroReal, linha, coluna)) {        // VERIFICA SE AS COORDENADAS ESTÃO DENTRO DOS LIMITES DO TABULEIRO REAL
        return "Coordenada inválida"
    }

    when (tabuleiroReal[linha - 1][coluna - 1]) {           // VERIFICA O QUE EXISTE NA COORDENADA DO TABULEIRO REA
        '1' -> {
            tabuleiroPalpites[linha - 1][coluna - 1] = '1'      // SE HÁ UM SUBMARINO NA COORDENADA, ATUALIZA O TABULEIRO DE PALPITES COM '1'
            return "Tiro num submarino."
        }
        else -> {
            tabuleiroPalpites[linha - 1][coluna - 1] = 'X'     // SE NÃO HÁ SUBMARINO NA COORDENADA, ATUALIZA O TABULEIRO DE PALPITES COM 'X'
            return "Agua."
        }
    }
}

fun geraTiroComputador(tabuleiroPalpites: Array<Array<Char?>>): Pair<Int, Int> {

    var coordenadasEscolhidas = emptyArray<Pair<Int,Int>>()         // INICIALIZA UM ARRAY DE COORDENADAS VAZIO

    for (i in 0 until tabuleiroPalpites.size) {                 // PERCORRE CADA LINHA DO TABULEIRO DE PALPITES
        for (j in 0 until tabuleiroPalpites[0].size) {          // PERCORRE CADA COLUNA DA LINHA
            if (tabuleiroPalpites[i][j] == null) {                   // VERIFICA SE A COORDENADA DO TABULEIRO DE PALPITES ESTÁ VAZIA (NULA)
                coordenadasEscolhidas += Pair(i+1,j+1)               // ADICIONA AS COORDENADAS AO ARRAY DE COORDENADAS ESCOLHIDAS
            }
        }
    }
    return coordenadasEscolhidas.random()                           // RETORNA UMA COORDENADA ALEATÓRIA DENTRO DAS COORDENADAS ESCOLHIDAS
}

fun contarNaviosDeDimensao(tabuleiroPalpites: Array<Array<Char?>>, dimensao: Int): Int {

    var count = 0               // INICIALIZA O CONTADOR

    for (linha in 0 until tabuleiroPalpites.size) {         // PERCORRE CADA LINHA DO TABULEIRO DE PALPITES
        for (coluna in 0 until tabuleiroPalpites[linha].size) {         // PERCORRE CADA COLUNA DA LINHA
            if (tabuleiroPalpites[linha][coluna] != null) {        // VERIFICA SE A COORDENADA DO TABULEIRO DE PALPITES NÃO É NULA
                val podeInserir = when (tabuleiroPalpites[linha][coluna]) {  // VERIFICA SE A DIMENSÃO DO NAVIO NA COORDENADA CORRESPONDE À DIMENSÃO ESPECIFICADA
                    '1' -> dimensao == 1
                    '2' -> dimensao == 2
                    '3' -> dimensao == 3
                    '4' -> dimensao == 4
                    else -> false
                }

                if (podeInserir) {                  // SE A DIMENSÃO DO NAVIO NA COORDENADA CORRESPONDE, INCREMENTA O CONTADOR
                    count++
                }
            }
        }
    }
    return count                            // RETORNA O NÚMERO DE NAVIOS DE DETERMINADA DIMENSÃO NO TABULEIRO DE PALPITES
}

fun venceu(tabuleiroPalpites: Array<Array<Char?>>): Boolean {
    // CALCULA O NÚMERO TOTAL DE NAVIOS NO TABULEIRO
    val navios = calculaNumNavios(tabuleiroPalpites.size, tabuleiroPalpites[0].size)

    // VERIFICA SE O NÚMERO DE NAVIOS DE CADA DIMENSÃO CORRESPONDE AO NÚMERO TOTAL DE NAVIOS
    return (contarNaviosDeDimensao(tabuleiroPalpites,1) == navios[0])
}

fun lerJogo(nomeFicheiro: String, tipoTabuleiro: Int): Array<Array<Char?>> {
    return arrayOf()
}

fun gravarJogo(nomeFicheiro: String,
               tabuleiroRealHumano: Array<Array<Char?>>,
               tabuleiroPalpitesHumano: Array<Array<Char?>>,
               tabuleiroRealComputador: Array<Array<Char?>>,
               tabuleiroPalpitesComputador: Array<Array<Char?>>) {

}

fun tamanhoTabuleiroValido(numLinhas: Int?, numColunas: Int?): Boolean {
    // VERIFICA SE O NÚMERO DE LINHAS ESTÁ ENTRE OS VALORES PERMITIDOS
    if (!((numLinhas == 5) || (numLinhas == 7) || (numLinhas == 8) || (numLinhas == 10) || (numLinhas == 4))) {
        return false
    }

    // VERIFICA SE O NÚMERO DE COLUNAS É IGUAL AO NÚMERO DE LINHAS
    if (numColunas != numLinhas) {
        return false
    }
    // RETORNA VERDADEIRO SE AMBAS AS CONDIÇÕES FOREM SATISFEITAS
    return true
}

fun processaCoordenadas(coordenadas: String, numLinhas: Int, numColunas: Int): Pair<Int,Int>? {

    var linhaReal = -1          // INICIALIZA AS VARIÁVEIS DE LINHA E COLUNA
    var colunaReal = -1

    val letrasCoordenadas= 'A'..'J'      // DEFINE AS LETRAS PERMITIDAS PARA AS COORDENADAS

    if ((coordenadas == "") || (coordenadas.length != 4 && coordenadas.length != 3)) {  // VERIFICAÇÕES DE VALIDAÇÃO DA STRING DE COORDENADAS
        return null
    }
    // PROCESSAMENTO DAS COORDENADAS QUANDO A STRING TEM 3 CARACTERES
    if (coordenadas.length == 3) {
        if (coordenadas[1] != ',') {
            return null
        }

        val numstr = "${coordenadas[0]}"
        val linha = numstr.toIntOrNull()
        if (linha == null || linha !in 1..numLinhas) {      // VERIFICA SE A LINHA É UM NÚMERO VÁLIDO E ESTÁ DENTRO DOS LIMITES DO TABULEIRO
            return null
        }

        linhaReal = linha

        val valorLetra = (coordenadas[2]).code          // OBTÉM O VALOR NUMÉRICO DA LETRA E VERIFICA SE ESTÁ DENTRO DOS LIMITES DO TABULEIRO
        val coluna = valorLetra - 64
        if  (coluna !in 1..numColunas) {
            return null
        }

        colunaReal = coluna
    }
    // PROCESSAMENTO DAS COORDENADAS QUANDO A STRING TEM 4 CARACTERES
    if (coordenadas.length == 4) {
        if (coordenadas[2] != ',') {
            return null
        }

        val numstr = "${coordenadas[0]}${coordenadas[1]}"
        val linha = numstr.toIntOrNull()
        if (linha == null || linha !in 1..numLinhas){   // VERIFICA SE A LINHA É UM NÚMERO VÁLIDO E ESTÁ DENTRO DOS LIMITES DO TABULEIRO
            return null
        }

        linhaReal = linha
        // OBTÉM O VALOR NUMÉRICO DA LETRA E VERIFICA SE ESTÁ DENTRO DOS LIMITES DO TABULEIRO
        val valorLetra = (coordenadas[3]).code
        val coluna = valorLetra - 64
        if (valorLetra - 64 !in 1..numColunas) {
            return null
        }

        colunaReal = coluna
    }
    return Pair(linhaReal,colunaReal)        // RETORNA AS COORDENADAS PROCESSADAS COMO UM PAR DE INTEIROS
}

fun criaLegendaHorizontal(numColunas: Int?): String {

    if (numColunas == null) {       // VERIFICA SE O NÚMERO DE COLUNAS É NULO
        return ""
    }
    // DEFINE O PRIMEIRO E O ÚLTIMO CARACTERE DA LEGENDA
    val primeiroCaracter = 'A'
    val ultimoCaracter = (primeiroCaracter + numColunas - 1).toChar()
    // INICIALIZA A STRING DA LEGENDA
    var tabela = ""
    var letra = primeiroCaracter
    while (letra <= ultimoCaracter) {                   // GERA A LEGENDA CONCATENANDO OS CARACTERES SEPARADOS POR " | "
        tabela += if (letra == primeiroCaracter) "$letra" else " | $letra"
        letra++
    }
    return tabela                   // RETORNA A LEGENDA HORIZONTAL
}

const val MENU_PRINCIPAL = -1
const val MENU_DEFINIR_TABULEIRO_NAVIOS = 1
const val MENU_JOGAR = 2
const val MENU_LER_FICHEIRO = 3
const val MENU_GRAVAR_FICHEIRO = 4
const val SAIR = 0

fun main() {
    var menu :Int?
    val invalido = "!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente"
    fun menuPrincipal(): Int {
        println()
        println(
            "> > Batalha Naval < <\n\n" +
                    "1 - Definir Tabuleiro e Navios\n" +
                    "2 - Jogar\n" +
                    "3 - Gravar\n" +
                    "4 - Ler\n" +
                    "0 - Sair\n"
        )
        do {
            val menu = readln().toIntOrNull()
            when (menu) {
                1 -> return MENU_DEFINIR_TABULEIRO_NAVIOS
                2 -> return MENU_JOGAR
                3 -> return MENU_GRAVAR_FICHEIRO
                4 -> return MENU_LER_FICHEIRO
                0 -> return 0
                9 -> return 0
                null -> println("!!! Opcao invalida, tente novamente")
                else -> println("!!! Opcao invalida, tente novamente")
            }
        } while (menu != 0)
        return menuPrincipal()
    }
    fun contaSubmarinos(tabuleiro: Array<Array<Char?>>): Int {
        var contador = 0
        for (linha in tabuleiro) {
            for (celula in linha) {
                if (celula == '1') {
                    contador++
                }
            }
        }
        return contador
    }

    fun menuDefinirTabuleiroENavios(): Int {
        if (numLinhas == 4) {
            println("Tabuleiros ja foram definidos")
            return MENU_PRINCIPAL
        }
        println("\n> > Batalha Naval < <\n\nDefina o tamanho do tabuleiro:\nQuantas linhas?")
        var quantasLinhas: Int?
        do {
            quantasLinhas = readln().toIntOrNull()
            if (quantasLinhas == -1) {
                return MENU_PRINCIPAL
            }
        } while (quantasLinhas == null)
        var quantasColunas: Int?
        do {
            println("Quantas colunas?")
            quantasColunas = readln().toIntOrNull()
            if (quantasColunas == -1) {
                return MENU_PRINCIPAL
            }
        } while (quantasColunas == null)
        if (!tamanhoTabuleiroValido(quantasLinhas, quantasColunas)) {
            println("Tamanho tabuleiro inválido")
            return MENU_PRINCIPAL
        }
        numLinhas = quantasLinhas
        numColunas = quantasColunas
        tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)
        tabuleiroHumano = criaTabuleiroVazio(quantasLinhas, quantasColunas)
        tabuleiroPalpitesDoComputador = criaTabuleiroVazio(numLinhas, numColunas)
        tabuleiroPalpitesDoHumano = criaTabuleiroVazio(numLinhas, numColunas)
        for (linha in obtemMapa(tabuleiroHumano, true)) {
            println(linha)
        }
        for (i in 1..2) {
            println("Insira as coordenadas de um submarino:\nCoordenadas? (ex: 6,G)")
            var coordenadas: Pair<Int, Int>? = null
            while (coordenadas == null) {
                val coordenadasInseridas = readlnOrNull() ?: ""
                val coordenadasValidas =
                    processaCoordenadas(coordenadasInseridas, quantasLinhas, quantasColunas)
                if (coordenadasValidas != null) {
                    coordenadas = coordenadasValidas
                    if (tabuleiroHumano[coordenadas.first - 1][coordenadas.second - 1] != null) {
                        println("!!! Posicionamento invalido, tente novamente")
                        coordenadas = null
                    } else {
                        val coordenadaBemInserida =
                            insereNavio(tabuleiroHumano, coordenadas.first, coordenadas.second, "N", 1)
                        if (!coordenadaBemInserida) {
                            println("!!! Posicionamento invalido, tente novamente")
                            coordenadas = null
                        } else {
                            for (linha in obtemMapa(tabuleiroHumano, true)) {
                                println(linha)
                            }
                        }
                    }
                } else {
                    println("Coordenadas invalidas, tente novamente")
                }
            }
        }
        println("Pretende ver o mapa gerado para o Computador? (S/N)")
        var verOMapa: String?
        do {
            val numeroNavios = calculaNumNavios(quantasLinhas, quantasColunas)
            tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)
            val preenche = preencheTabuleiroComputador(tabuleiroComputador, numeroNavios)
            verOMapa = readln()
            when (verOMapa) {
                "-1" -> return MENU_PRINCIPAL
                "S" -> {
                    preencheTabuleiroComputador(tabuleiroComputador, numeroNavios)
                    val mapaComputador = obtemMapa(tabuleiroComputador, true)
                    for (linha in mapaComputador) {
                        println(linha)
                    }
                    val submarinosNoComputador = contaSubmarinos(tabuleiroComputador)
                    numeroNavios[0] = 0 + submarinosNoComputador
                    return MENU_PRINCIPAL
                }

                "N" -> return MENU_PRINCIPAL
                else -> println("!!! Opcao invalida\nPretende ver o mapa gerado para o Computador? (S/N)")
            }
        } while (verOMapa != "S" || verOMapa != "N" || verOMapa != "-1")
        return MENU_PRINCIPAL
    }

    fun aguardarEnter() {
        println("Prima enter para continuar...")
        readLine() // Aguarda a entrada do usuário (pode ser Enter ou qualquer outra coisa)
    }

    fun menuJogar(): Int {
        if (numLinhas == -1) {
            println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
            return MENU_PRINCIPAL
        }

        var jogoContinua = true
        var rodada = 1

        while (jogoContinua) {

            val mapaHumano = obtemMapa(tabuleiroPalpitesDoHumano, false)
            for (linha in mapaHumano) {
                println(linha)
            }

            println("Indique a posição que pretende atingir\nCoordenadas? (ex: 6,G)")
            val coordenadasHumano = processaCoordenadas(readLine() ?: "", numLinhas, numColunas)

            if (coordenadasHumano != null) {
                val resultadoHumano =
                    lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadasHumano)
                if (resultadoHumano == "Tiro num submarino.") {
                    println(">>> HUMANO >>>$resultadoHumano Navio ao fundo!")
                } else {
                    println(">>> HUMANO >>>$resultadoHumano")
                }
            } else {
                println("Coordenadas inválidas. Tente novamente.")
            }

            // Verifica se o humano venceu
            if (venceu(tabuleiroPalpitesDoHumano)) {
                println("PARABENS! Venceu o jogo!")
                jogoContinua = false
            } else {
                // Jogada do computador
                val coordenadasComputador = geraTiroComputador(tabuleiroPalpitesDoComputador)
                val resultadoComputador =
                    lancarTiro(tabuleiroHumano, tabuleiroPalpitesDoComputador, coordenadasComputador)
                println("Computador lancou tiro para a posicao $coordenadasComputador")
                println(">>> COMPUTADOR >>>$resultadoComputador")
                var enterPressionado1 = false
                while (!enterPressionado1) {
                    println("Prima enter para continuar")
                    if (readLine().isNullOrEmpty()) {
                        enterPressionado1 = true
                    }
                }

                // Verifica se o computador venceu
                if (venceu(tabuleiroPalpitesDoComputador)) {
                    println("O computador venceu! Melhor sorte da próxima vez.")
                    jogoContinua = false
                }
            }

            // Próxima rodada
            rodada++
        }

        var enterPressionado = false
        while (!enterPressionado) {
            println("Prima enter para voltar ao menu principal")
            if (readLine().isNullOrEmpty()) {
                enterPressionado = true
            }
        }

        return MENU_PRINCIPAL
    }


    fun menuLerFicheiro(): Int {
        if (numLinhas == -1) {
            println(invalido)
            return MENU_PRINCIPAL
        }
        return menuPrincipal()
    }

    fun menuGravarFicheiro(): Int {
        if (numLinhas == -1) {
            println(invalido)
            return MENU_PRINCIPAL
        }
        return menuPrincipal()
    }

    var menuAtual = MENU_PRINCIPAL

    while (true) {
        menuAtual = when (menuAtual) {
            MENU_PRINCIPAL -> menuPrincipal()
            MENU_DEFINIR_TABULEIRO_NAVIOS -> menuDefinirTabuleiroENavios()
            MENU_JOGAR -> menuJogar()
            MENU_LER_FICHEIRO -> menuLerFicheiro()
            MENU_GRAVAR_FICHEIRO -> menuGravarFicheiro()
            SAIR -> {
                println("Vou sair\n")
                return
            }
            else -> return
        }
    }
}