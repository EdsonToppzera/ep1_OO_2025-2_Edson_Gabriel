package classes;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Medico {

    private String nome;
    private String crm;
    private Especialidade especialidade;
    private double custoConsulta;
    private List<Consulta> agendaConsultas;
    private int numeroConsultasRealizadas;

    // Construtor
    public Medico(String nome, String crm, Especialidade especialidade, double custoConsulta) {
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade;
        this.custoConsulta = custoConsulta;
        this.agendaConsultas = new ArrayList<>();
    }

    public void adicionarConsultaAgenda(Consulta consulta) {
        if (consulta != null) {
            this.agendaConsultas.add(consulta);
        }
    }
    public void registrarConsultaRealizada() {
        this.numeroConsultasRealizadas++;
    }

    public boolean removerConsultaAgenda(Consulta consulta) {
        return this.agendaConsultas.remove(consulta);
    }

    //verifica se o medico ta disponivel
    public boolean estaDisponivelEm(LocalDateTime dataHora) {
        for (Consulta consulta : agendaConsultas) {
            // Verifica se ja ha consulta nesse horario
            if (consulta.getStatus() == Consulta.Status.AGENDADA && 
                consulta.conflitaComHorario(dataHora)) {
                return false; //Médico ocupado
            }
        }
        return true; //Médico disponível
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public double getCustoConsulta() {
        return custoConsulta;
    }

    public void setCustoConsulta(double custoConsulta) {
        if (custoConsulta >= 0) {
            this.custoConsulta = custoConsulta;
        }
    }

    public List<Consulta> getAgendaConsultas() {
        return agendaConsultas;
    }

    public int getNumeroConsultasRealizadas() {
        return numeroConsultasRealizadas;
    }

    public void setNumeroConsultasRealizadas(int numeroConsultasRealizadas) {
        this.numeroConsultasRealizadas = numeroConsultasRealizadas;
    }

    //log medico
    @Override
    public String toString() {
        return "Medico{" +
                "nome='" + nome + '\'' +
                ", crm='" + crm + '\'' +
                ", especialidade=" + especialidade +
                ", custoConsulta=R$ " + String.format("%.2f", custoConsulta) +
                ", consultasRealizadas=" + numeroConsultasRealizadas +
                '}';
    }

    // log da agenda do medico
    public String getInformacoesAgenda() {
        return String.format("Dr. %s - %s consultas agendadas", nome, agendaConsultas.size());
    }
}