package classes;

import java.util.Map;
import java.util.HashMap;

public class PlanoDeSaude {

    private String nome;
    private boolean pagaInternacao;
    private Map<Especialidade, Double> descontos;

    public PlanoDeSaude(String nome, boolean pagaInternacao) {
        this.nome = nome;
        this.pagaInternacao = pagaInternacao;
        this.descontos = new HashMap<>();
    }

    //caso nn pague internacao, eh so chamar o nome
    public PlanoDeSaude(String nome) {
        this(nome, false);
    }

    //add desconto para uma especialidade
    public void adicionarDesconto(Especialidade especialidade, double percentualDesconto) {
        if (percentualDesconto >= 0 && percentualDesconto <= 100) {
            this.descontos.put(especialidade, percentualDesconto);
        }
    }

    //calcula o desconto
    public double calcularDesconto(Especialidade especialidade, double valorOriginal) {
        if (descontos.containsKey(especialidade)) {
            double percentualDesconto = descontos.get(especialidade);
            return valorOriginal * (percentualDesconto / 100);
        }
        return 0.0;
    }

    // Calcula o valor final com desconto
    public double calcularValorComDesconto(Especialidade especialidade, double valorOriginal) {
        double desconto = calcularDesconto(especialidade, valorOriginal);
        return valorOriginal - desconto;
    }

    // Verifica se tem desconto para uma especialidade
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

    // log do plano
    @Override
    public String toString() {
        return "PlanoDeSaude{" +
                "nome='" + nome + '\'' +
                ", pagaInternacao=" + pagaInternacao +
                ", descontos=" + descontos +
                '}';
    }
}
