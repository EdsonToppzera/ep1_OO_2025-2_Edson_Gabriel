package classes;

import java.util.List;
import java.util.ArrayList;

public class Paciente {

    private String nome;
    private String cpf;
    private int idade;
    private PlanoDeSaude planoDeSaude;
    private List<Consulta> historicoConsultas; 
    private List<Internacao> historicoInternacoes;

    // Construtor
    public Paciente(String nome, String cpf, int idade) {
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.planoDeSaude = null;
        this.historicoConsultas = new ArrayList<>();
        this.historicoInternacoes = new ArrayList<>();
    }

    // Ve se o cliente eh especial
    public boolean isEspecial() {
        return this.planoDeSaude != null;
    }

    // adiciona uma consulta ao histórico
    public void adicionarConsultaAoHistorico(Consulta consulta) {
        if (consulta != null) {
            this.historicoConsultas.add(consulta);
        }
    }

    // adiciona uma internação ao histórico
    public void adicionarInternacaoAoHistorico(Internacao internacao) {
        if (internacao != null) {
            this.historicoInternacoes.add(internacao);
        }
    }

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

    // log do paciente
    @Override
    public String toString() {
        return "Paciente{" +
            "nome='" + nome + '\'' +
            ", cpf='" + cpf + '\'' +
            ", plano=" + (isEspecial() ? planoDeSaude.getNomeDoPlano() : "Nenhum") +
            '}';
    }
}