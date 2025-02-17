// [Template no Kotlin Playground](https://pl.kotl.in/WcteahpyN)

enum class Nivel(val proporcaoXp: Double) {
    BASICO(0.5),
    INTERMEDIARIO(1.0),
    DIFICIL(2.0)
}

data class Usuario(
    val nome: String,
    var conteudosInscritos: Set<ConteudoEducacional> = mutableSetOf<ConteudoEducacional>(),
    var conteudosCompletos: Set<ConteudoEducacional> = mutableSetOf<ConteudoEducacional>()
) {

    fun adicionarConteudo(conteudo: ConteudoEducacional) {
        conteudosInscritos = conteudosInscritos.plus(conteudo)
    }

    fun adicionarConteudos(conteudos: List<ConteudoEducacional>) {
        conteudosInscritos = conteudosInscritos.plus(conteudos)
    }

    fun completarConteudo(conteudo: ConteudoEducacional) {
        if (conteudosInscritos.contains(conteudo)) {
            conteudosCompletos = conteudosInscritos + conteudo
            conteudosInscritos = conteudosInscritos.minus(conteudo)
        } else {
            throw MatchException("Usuário não está inscrito neste conteudo: ${conteudo}", IllegalArgumentException())
        }
    }
    fun calcularXp(): Double {
        return conteudosCompletos.sumOf { it.xpConteudo() }
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
        usuario.adicionarConteudos(conteudos)
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

    val conteudosDiversos = setOf(conteudoBasico[0], conteudoIntermediario[0], conteudoAvancado[0])

    val formacaoCompleta = Formacao("Formação Fullstack", conteudoBasico + conteudoIntermediario + conteudoAvancado)

    val usuarioJoao = Usuario("João Pedro", conteudoBasico.toSet())
    val usuarioMaria = Usuario("Maria do Socorro", conteudosDiversos)
    val usuarioRafaela = Usuario("Rafaela Paz", conteudoAvancado.toSet())
    val usuarioMiguel = Usuario("Miguel Alcantara")

    formacaoCompleta.matricular(usuarioMiguel)
    assert(usuarioMiguel.conteudosInscritos == formacaoCompleta.conteudos)
    with(formacaoCompleta) {
        println("Formação completa ${nome} com XP total de ${xpFormacao}XP")
    }

    usuarioJoao.run {
        try {
            completarConteudo(conteudoBasico[0])
            completarConteudo(conteudoBasico[1])
        } catch (e: MatchException) {
            print(e.message)
        }
    }

    assert(usuarioJoao.conteudosCompletos == setOf(conteudoBasico.slice(0..1)))
    assert(usuarioJoao.conteudosInscritos == setOf(conteudoBasico[2]))

    usuarioMaria.run {
        try {
            conteudosDiversos.map { completarConteudo(it) }
        } catch (e: MatchException) {
            print(e.message)
        }
    }
    assert(usuarioMaria.conteudosInscritos.isEmpty())

    usuarioRafaela.run {
        adicionarConteudos(conteudoIntermediario)
        conteudoAvancado.map { completarConteudo(it) }
        println("XP Total do usuário ${usuarioRafaela.nome}: ${calcularXp()}XP")
    }
    assert(usuarioRafaela.conteudosCompletos == conteudoAvancado)
    assert(usuarioRafaela.conteudosInscritos == conteudoIntermediario)
}
