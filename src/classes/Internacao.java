// DAR UMA OLHADA PRA TESTAR
package classes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public class Internacao {

    private Paciente paciente;
    private Medico medicoResponsavel;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private String quarto;
    private double custoDiaria;
    private double custoTotal;
    private boolean ativa;

    // Construtor para iniciar uma internação
    public Internacao(Paciente paciente, Medico medicoResponsavel, LocalDate dataEntrada, String quarto, double custoDiaria) {
        this.paciente = paciente;
        this.medicoResponsavel = medicoResponsavel;
        this.dataEntrada = dataEntrada;
        this.dataSaida = null;
        this.quarto = quarto;
        this.custoDiaria = custoDiaria;
        this.ativa = true;
        this.custoTotal = 0.0;
    }

    public void finalizarInternacao(LocalDate dataSaida) {
        if (this.ativa) {
            this.dataSaida = dataSaida;
            this.ativa = false;
            this.custoTotal = calcularCustoTotal();
            
            paciente.adicionarInternacaoAoHistorico(this);
        }
    }

    public void cancelarInternacao() {
        if (this.ativa) {
            this.ativa = false;
            this.dataSaida = LocalDate.now();
            this.custoTotal = 0.0; // Cancelada = sem custo
        }
    }

    //Calcula o numero de dias de internação
    public long calcularDiasInternacao() {
        LocalDate dataFim = (dataSaida != null) ? dataSaida : LocalDate.now();
        return ChronoUnit.DAYS.between(dataEntrada, dataFim);
    }

    private double calcularCustoTotal() {
        long dias = calcularDiasInternacao();
        double custoBase = dias * custoDiaria;
        
        //Aplica lógica do plano de saúde
        if (paciente instanceof PacienteEspecial) {
            PacienteEspecial pacienteEspecial = (PacienteEspecial) paciente;
            
            //Se tem direito a internação gratuita e ficou menos de 7 dias
            if (pacienteEspecial.temDireitoInternacaoGratuita() && dias < 7) {
                return 0.0; // Internação gratuita
            }
        }
        
        return custoBase;
    }

    public void recalcularCusto() {
        if (!ativa) {
            this.custoTotal = calcularCustoTotal();
        }
    }

    //Verifica se o quarto está ocupado
    public boolean ocupaQuartoNaData(String outroQuarto, LocalDate data) {
        if (!this.quarto.equalsIgnoreCase(outroQuarto)) {
            return false; // Quarto diferente
        }
        LocalDate dataFim = (dataSaida != null) ? dataSaida : LocalDate.now().plusYears(10);
        return !data.isBefore(dataEntrada) && !data.isAfter(dataFim);
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedicoResponsavel() {
        return medicoResponsavel;
    }

    public void setMedicoResponsavel(Medico medicoResponsavel) {
        this.medicoResponsavel = medicoResponsavel;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public String getQuarto() {
        return quarto;
    }

    public void setQuarto(String quarto) {
        this.quarto = quarto;
    }

    public double getCustoDiaria() {
        return custoDiaria;
    }

    public void setCustoDiaria(double custoDiaria) {
        this.custoDiaria = custoDiaria;
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public boolean isAtiva() {
        return ativa;
    }

    //logs de td
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String saida = (dataSaida != null) ? dataSaida.format(formatter) : "Em andamento";
        String statusStr = ativa ? "ATIVA" : "FINALIZADA";
        
        return "Internacao{" +
                "paciente=" + paciente.getNome() +
                ", medico=" + medicoResponsavel.getNome() +
                ", dataEntrada=" + dataEntrada.format(formatter) +
                ", dataSaida=" + saida +
                ", quarto='" + quarto + '\'' +
                ", dias=" + calcularDiasInternacao() +
                ", custoTotal=R$ " + String.format("%.2f", custoTotal) +
                ", status=" + statusStr +
                '}';
    }

    public String getInformacoesCompletas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder info = new StringBuilder();
        
        info.append("=== INTERNAÇÃO ===\n");
        info.append("Paciente: ").append(paciente.getNome()).append("\n");
        info.append("Médico Responsável: Dr(a). ").append(medicoResponsavel.getNome()).append("\n");
        info.append("Quarto: ").append(quarto).append("\n");
        info.append("Data Entrada: ").append(dataEntrada.format(formatter)).append("\n");
        
        if (dataSaida != null) {
            info.append("Data Saída: ").append(dataSaida.format(formatter)).append("\n");
        } else {
            info.append("Data Saída: Em andamento\n");
        }
        
        info.append("Dias de Internação: ").append(calcularDiasInternacao()).append("\n");
        info.append("Custo Diária: R$ ").append(String.format("%.2f", custoDiaria)).append("\n");
        info.append("Custo Total: R$ ").append(String.format("%.2f", custoTotal)).append("\n");
        info.append("Status: ").append(ativa ? "ATIVA" : "FINALIZADA").append("\n");
        
        return info.toString();
    }
}