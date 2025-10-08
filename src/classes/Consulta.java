package classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consulta {

    // status da consulta
    public enum Status {
        AGENDADA,
        CONCLUIDA,
        CANCELADA
    }

    private Paciente paciente;
    private Medico medico;
    private LocalDateTime dataHora;
    private String local;
    private Status status;
    private String diagnostico;
    private String prescricao;
    private double valorOriginal;
    private double valorFinal;

    public Consulta(Paciente paciente, Medico medico, LocalDateTime dataHora, String local) {
        this.paciente = paciente;
        this.medico = medico;
        this.dataHora = dataHora;
        this.local = local;
        this.status = Status.AGENDADA;
        this.diagnostico = null;
        this.prescricao = null;
        this.valorOriginal = medico.getCustoConsulta();
        this.valorFinal = calcularValorFinal();
    }

    // Calcula o valor final com desconto
    private double calcularValorFinal() {
        if (paciente instanceof PacienteEspecial) {
            PacienteEspecial pacienteEspecial = (PacienteEspecial) paciente;
            return pacienteEspecial.calcularValorFinalConsulta(
                medico.getEspecialidade(), 
                valorOriginal
            );
        }
        return valorOriginal;
    }

    public void recalcularValor() {
        this.valorOriginal = medico.getCustoConsulta();
        this.valorFinal = calcularValorFinal();
    }

    public void concluirConsulta(String diagnostico, String prescricao) {
        if (this.status == Status.AGENDADA) {
            this.diagnostico = diagnostico;
            this.prescricao = prescricao;
            this.status = Status.CONCLUIDA;
            
            paciente.adicionarConsultaAoHistorico(this);
            medico.registrarConsultaRealizada();
        }
    }

    //Cancela a consulta
    public void cancelarConsulta() {
        if (this.status == Status.AGENDADA) {
            this.status = Status.CANCELADA;
            medico.removerConsultaAgenda(this);
        }
    }

    public boolean conflitaComHorario(LocalDateTime outroHorario) {
        return this.dataHora.equals(outroHorario);
    }

    public boolean conflitaComLocal(String outroLocal, LocalDateTime outroHorario) {
        return this.local.equalsIgnoreCase(outroLocal) && 
               this.dataHora.equals(outroHorario);
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
        recalcularValor();
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
        recalcularValor();
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Status getStatus() {
        return status;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getPrescricao() {
        return prescricao;
    }

    public double getValorOriginal() {
        return valorOriginal;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public double getDesconto() {
        return valorOriginal - valorFinal;
    }

    // Log da consulta
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Consulta{" +
                "paciente=" + paciente.getNome() +
                ", medico=" + medico.getNome() +
                ", dataHora=" + dataHora.format(formatter) +
                ", local='" + local + '\'' +
                ", status=" + status +
                ", valorFinal=R$ " + String.format("%.2f", valorFinal) +
                '}';
    }
}