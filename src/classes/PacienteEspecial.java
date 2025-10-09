package classes;

public class PacienteEspecial extends Paciente {

    public PacienteEspecial(String nome, String cpf, int idade, PlanoDeSaude planoDeSaude) {
        super(nome, cpf, idade);
        
        if (planoDeSaude == null) {
            throw new IllegalArgumentException("PacienteEspecial deve ter um plano de saúde!");
        }
        
        this.setPlanoDeSaude(planoDeSaude);
    }

    // Calcula o valor final da consulta usando a lógica do plano
    public double calcularValorFinalConsulta(Especialidade especialidade, double valorOriginal) {
        return this.getPlanoDeSaude().calcularValorComDesconto(
            especialidade, 
            valorOriginal, 
            // Passa a idade para o plano aplicar desconto de idoso
            this.getIdade()  
        );
    }

    // Calcula apenas o desconto (sem aplicar no valor)
    public double calcularDescontoTotal(Especialidade especialidade, double valorOriginal) {
        return this.getPlanoDeSaude().calcularDesconto(
            especialidade, 
            valorOriginal, 
            this.getIdade()
        );
    }

    // Verifica se tem direito a internação gratuita
    public boolean temDireitoInternacaoGratuita() {
        return this.getPlanoDeSaude().isPagaInternacao();
    }

    // Sobrescreve o método isEspecial para retornar true
    @Override
    public boolean isEspecial() {
        return true;
    }

    //log paciente especial
    @Override
    public String toString() {
        return "PacienteEspecial{" +
                "nome='" + getNome() + '\'' +
                ", cpf='" + getCpf() + '\'' +
                ", idade=" + getIdade() +
                ", plano=" + getPlanoDeSaude().getNome() +
                '}';
    }
}