package classes;
import java.util.List;
import java.util.ArrayList;

public class Paciente {
    private String nome;
    private String cpf;
    private int idade;
    private PlanoDeSaude planoDeSaude;
    private List<Consulta> historicoConsulta;
    private List<Internacao> historioInternacao;

    //construtores
    public Paciente(String nome, String cpf, int idade) {
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.planoDeSaude = null;
        this.historicoConsultas = new ArrayList<>();
        this.historicoInternacoes = new ArrayList<>();
    }

    //Veririfica se o cliente eh especial (tem plano)
    public boolean isEspecial() {
        return this.planoDeSaude != null;
    }

    // TALVEZ TENHA Q MUDAR.. add uma consulta/internacao ao paciente
    public void adicionarConsultaAoHistorico(Consulta consulta) {
        if (consulta != null) {
            this.historicoConsultas.add(consulta);
        }
    }
    public void adicionarInternacaoAoHistorico(Internacao internacao) {
        if (internacao != null) {
            this.historicoInternacoes.add(internacao);
        }
    }

    //getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if (idade > 0) {
            this.idade = idade;
        }
    }

    public PlanoDeSaude getPlanoDeSaude() {
        return planoDeSaude;
    }

    public void setPlanoDeSaude(PlanoDeSaude planoDeSaude) {
        this.planoDeSaude = planoDeSaude;
    }

    public List<Consulta> getHistoricoConsultas() {
        return historicoConsultas;
    }

    public List<Internacao> getHistoricoInternacoes() {
        return historicoInternacoes;
    }

    
    //TALVEZ MUDE.. faz o log de um paciente com seus atributos
    public String toString() {
        return "Paciente{" +
                "nome: '" + nome + '\'' +
                ", cpf: '" + cpf + '\'' +
                ", plano: " + (isEspecial() ? planoDeSaude.getNomeDoPlano() : "Nenhum") +
                '}';
    }
}