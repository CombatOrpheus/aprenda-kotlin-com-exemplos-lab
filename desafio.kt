// [Template no Kotlin Playground](https://pl.kotl.in/WcteahpyN)

enum class Nivel(val proporcaoXp: Double) {
    BASICO(0.5),
    INTERMEDIARIO(1.0),
    DIFICIL(2.0)
}

data class Usuario(
    val nome: String,
    val conteudosInscritos: Set<ConteudoEducacional> = mutableSetOf<ConteudoEducacional>(),
    val conteudosCompletos: Set<ConteudoEducacional> = mutableSetOf<ConteudoEducacional>()
) {

    fun adicionarConteudo(conteudo: ConteudoEducacional) {
        conteudosInscritos.plus(conteudo)
    }

    fun adicionarConteudos(conteudos: List<ConteudoEducacional>) {
        conteudosInscritos.plus(conteudos)
    }

    fun completarConteudo(conteudo: ConteudoEducacional) {
        if (conteudosInscritos.contains(conteudo)) {
            conteudosCompletos.plus(conteudo)
            conteudosInscritos.minus(conteudo)
        } else {
            throw MatchException("Usuário não está inscrito neste conteudo", IllegalArgumentException())
        }
    }
    fun calcularXp(): Double {
        return conteudosCompletos.map { it.xpConteudo() }.sum()
    }

}

data class ConteudoEducacional(
    var nome: String,
    val duracao: Int = 60,
    val nivel: Nivel = Nivel.BASICO,
) {
    fun xpConteudo(): Double {
        return duracao * nivel.proporcaoXp
    }
}

data class Formacao(val nome: String, var conteudos: List<ConteudoEducacional>) {

    val inscritos = mutableListOf<Usuario>()
    val xpFormacao = conteudos.map { it.duracao }.sum()

    fun matricular(usuario: Usuario) {
        inscritos.add(usuario)
    }
}

fun main() {
    val conteudoBasico = listOf("Introdução à Programação", "SQL Básico", "APIs Web").map { ConteudoEducacional(it) }
    val conteudoIntermediario =
        listOf("Laços de Repetição Java", "Armazenamento de Dados com o Docker", "Projetos Java com Gradle")
            .map { ConteudoEducacional(it, duracao = 80, nivel = Nivel.INTERMEDIARIO) }
    val conteudoAvancado =
        listOf("Adicionando Segurança a uma API REST com Spring Security", "Docker Compose")
            .map { ConteudoEducacional(it, duracao = 120, nivel = Nivel.DIFICIL) }

    val usuarioJoao = Usuario("João Pedro", conteudoBasico.toSet())
    val usuarioMaria = Usuario("Maria do Socorro",
        setOf(conteudoBasico[0], conteudoIntermediario[0], conteudoAvancado[0]))
    val usuarioRafaela = Usuario("Rafaela Paz", conteudoAvancado.toSet())
}
