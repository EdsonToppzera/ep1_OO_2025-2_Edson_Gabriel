package classes;
import java.util.HashMap;
import java.util.Map;

//FALTA ADD BGL DE INTERNACAO E PRA +60 (paciente especial)
public class Plano {
    private String nome;
    private Map<String, Double> tabelaDeDescontos;

    //construtores
    public Plano(String nome) {
        this.nome = nome;
        this.tabelaDeDescontos = new HashMap<>();
    }

    public void adicionarOuAtualizarDesconto(String especialidade, double percentual) {
        if (especialidade != null && percentual >= 0 && percentual <= 1) {
            this.tabelaDeDescontos.put(especialidade, percentual);
        } else {
            System.out.println("Especialidade ou desconto invalido.");
        }
    }
    
    public double contaDesconto(double valorInicial, String especialidade) {
        //caso nn tenha especiliade, nn tem desconto
        double percentualDesconto = this.tabelaDeDescontos.getOrDefault(especialidade, 0.0);
        double valorDesconto = valorInicial*percentualDesconto;
        return valorInicial-valorDesconto;
    }

    //getset
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    //log
    public String toString() {
        return "PlanoDeSaude: ["+"nome: '"+nome+']';
    }


}
