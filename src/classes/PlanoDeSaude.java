package classes;

import java.util.Map;
import java.util.HashMap;

public class PlanoDeSaude {

    private String nome;
    private boolean pagaInternacao;
    private Map<Especialidade, Double> descontos;
    private static final int IDADE_IDOSO = 60;
    private static final double DESCONTO_ADICIONAL_IDOSO = 10.0;

    public PlanoDeSaude(String nome, boolean pagaInternacao) {
        this.nome = nome;
        this.pagaInternacao = pagaInternacao;
        this.descontos = new HashMap<>();
    }

    // caso nn tenha plano eh so meter o nome
    public PlanoDeSaude(String nome) {
        this(nome, false);
    }

    // Adiciona desconto para cada especialidade
    public void adicionarDesconto(Especialidade especialidade, double percentualDesconto) {
        if (percentualDesconto >= 0 && percentualDesconto <= 100) {
            this.descontos.put(especialidade, percentualDesconto);
        }
    }

    public double calcularDesconto(Especialidade especialidade, double valorOriginal, int idadePaciente) {
        double descontoTotal = 0.0;
        
        // Desconto por especialidade
        if (descontos.containsKey(especialidade)) {
            double percentualDesconto = descontos.get(especialidade);
            descontoTotal += valorOriginal * (percentualDesconto / 100);
        }
        // Desconto adicional para idoso
        if (idadePaciente >= IDADE_IDOSO) {
            descontoTotal += valorOriginal * (DESCONTO_ADICIONAL_IDOSO / 100);
        }
        
        return descontoTotal;
    }

    // Calcula o valor final
    public double calcularValorComDesconto(Especialidade especialidade, double valorOriginal, int idadePaciente) {
        double desconto = calcularDesconto(especialidade, valorOriginal, idadePaciente);
        return Math.max(valorOriginal - desconto, 0.0);
    }

    // Verifica se tem desconto
    public boolean temDescontoPara(Especialidade especialidade) {
        return descontos.containsKey(especialidade);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeDoPlano() {
        return nome;
    }

    public boolean isPagaInternacao() {
        return pagaInternacao;
    }

    public void setPagaInternacao(boolean pagaInternacao) {
        this.pagaInternacao = pagaInternacao;
    }

    public Map<Especialidade, Double> getDescontos() {
        return descontos;
    }

    public void setDescontos(Map<Especialidade, Double> descontos) {
        this.descontos = descontos;
    }

    //log do plano
    @Override
    public String toString() {
        return "PlanoDeSaude{" +
                "nome='" + nome + '\'' +
                ", pagaInternacao=" + pagaInternacao +
                ", descontos=" + descontos +
                '}';
    }
}
