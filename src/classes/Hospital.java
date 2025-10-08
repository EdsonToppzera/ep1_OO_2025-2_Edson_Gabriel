package classes;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class Hospital {

    private String nome;
    private List<Paciente> pacientes;
    private List<Medico> medicos;
    private List<PlanoDeSaude> planosDeSaude;
    private List<Consulta> consultas;
    private List<Internacao> internacoes;
    private Map<String, Boolean> quartosOcupados;

    public Hospital(String nome) {
        this.nome = nome;
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.planosDeSaude = new ArrayList<>();
        this.consultas = new ArrayList<>();
        this.internacoes = new ArrayList<>();
        this.quartosOcupados = new HashMap<>();
    }

    //CADASTROS PARA O USUARIO
    //
    public boolean cadastrarPaciente(Paciente paciente) {
        if (buscarPacientePorCpf(paciente.getCpf()) != null) {
            return false;
        }
        return pacientes.add(paciente);
    }

    public boolean cadastrarMedico(Medico medico) {
        if (buscarMedicoPorCrm(medico.getCrm()) != null) {
            return false;
        }
        return medicos.add(medico);
    }

    public boolean cadastrarPlanoDeSaude(PlanoDeSaude plano) {
        if (buscarPlanoPorNome(plano.getNome()) != null) {
            return false;
        }
        return planosDeSaude.add(plano);
    }

    //PARTE DO AGENDAMENTO
    //
    public Consulta agendarConsulta(Paciente paciente, Medico medico, 
                                    LocalDateTime dataHora, String local) {
        //ve se tem horario
        if (!medico.estaDisponivelEm(dataHora)) {
            System.out.println("O medico ja tem uma consulta nesse horario.");
            return null;
        }

        //Ve se o local ta ok
        if (localOcupado(local, dataHora)) {
            System.out.println("Este local ja esta ocupado nesse horario.");
            return null;
        }

        //Cria a consulta
        Consulta consulta = new Consulta(paciente, medico, dataHora, local);
        consultas.add(consulta);
        medico.adicionarConsultaAgenda(consulta);

        return consulta;
    }

    //Verifica se um local está ocupado
    private boolean localOcupado(String local, LocalDateTime dataHora) {
        for (Consulta c : consultas) {
            if (c.getStatus() == Consulta.Status.AGENDADA && 
                c.conflitaComLocal(local, dataHora)) {
                return true;
            }
        }
        return false;
    }

    //LOGICA DAS INTERNACAO
    //
    public Internacao registrarInternacao(Paciente paciente, Medico medicoResponsavel, LocalDate dataEntrada, String quarto, double custoDiaria) {
        //Ve se o quarto ta ok
        if (quartoOcupado(quarto)) {
            System.out.println("Erro: Quarto já está ocupado.");
            return null;
        }

        //Cria a internação
        Internacao internacao = new Internacao(paciente, medicoResponsavel, dataEntrada, quarto, custoDiaria);
        internacoes.add(internacao);
        quartosOcupados.put(quarto, true);

        return internacao;
    }

    //Finaliza uma internação
    public boolean finalizarInternacao(Internacao internacao, LocalDate dataSaida) {
        if (internacao.isAtiva()) {
            internacao.finalizarInternacao(dataSaida);
            quartosOcupados.put(internacao.getQuarto(), false); // Libera o quarto
            return true;
        }
        return false;
    }

    //Cancela uma internação
    public boolean cancelarInternacao(Internacao internacao) {
        if (internacao.isAtiva()) {
            internacao.cancelarInternacao();
            quartosOcupados.put(internacao.getQuarto(), false); // Libera o quarto
            return true;
        }
        return false;
    }

    //Verifica se um quarto está ocupado
    private boolean quartoOcupado(String quarto) {
        for (Internacao i : internacoes) {
            if (i.isAtiva() && i.getQuarto().equalsIgnoreCase(quarto)) {
                return true;
            }
        }
        return false;
    }

    //PARTE DE BUSCAR PELAS INFORMACOES
    //
    
    // Busca paciente por CPF
    public Paciente buscarPacientePorCpf(String cpf) {
        for (Paciente p : pacientes) {
            if (p.getCpf().equals(cpf)) {
                return p;
            }
        }
        return null;
    }

    // Busca médico por CRM
    public Medico buscarMedicoPorCrm(String crm) {
        for (Medico m : medicos) {
            if (m.getCrm().equals(crm)) {
                return m;
            }
        }
        return null;
    }

    // Busca plano por nome
    public PlanoDeSaude buscarPlanoPorNome(String nome) {
        for (PlanoDeSaude p : planosDeSaude) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

    // Busca médicos por especialidade
    public List<Medico> buscarMedicosPorEspecialidade(Especialidade especialidade) {
        List<Medico> resultado = new ArrayList<>();
        for (Medico m : medicos) {
            if (m.getEspecialidade() == especialidade) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    //Busca consultas de um paciente
    public List<Consulta> buscarConsultasPorPaciente(Paciente paciente) {
        List<Consulta> resultado = new ArrayList<>();
        for (Consulta c : consultas) {
            if (c.getPaciente().equals(paciente)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    //Busca internações
    public List<Internacao> buscarInternacoesAtivas() {
        List<Internacao> resultado = new ArrayList<>();
        for (Internacao i : internacoes) {
            if (i.isAtiva()) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    //ESTATISTICAS GERAIS
    //

    // Médico que mais atendeu
    public Medico medicoQueMaisAtendeu() {
        if (medicos.isEmpty()) return null;
        
        Medico campeao = medicos.get(0);
        for (Medico m : medicos) {
            if (m.getNumeroConsultasRealizadas() > campeao.getNumeroConsultasRealizadas()) {
                campeao = m;
            }
        }
        return campeao;
    }

    // Especialidade mais procurada
    public Especialidade especialidadeMaisProcurada() {
        Map<Especialidade, Integer> contagem = new HashMap<>();
        
        for (Consulta c : consultas) {
            Especialidade esp = c.getMedico().getEspecialidade();
            contagem.put(esp, contagem.getOrDefault(esp, 0) + 1);
        }
        Especialidade maisComum = null;
        int maxContagem = 0;
        for (Map.Entry<Especialidade, Integer> entry : contagem.entrySet()) {
            if (entry.getValue() > maxContagem) {
                maxContagem = entry.getValue();
                maisComum = entry.getKey();
            }
        }
        return maisComum;
    }

    //Total de pacientes com determinado plano
    public int totalPacientesComPlano(PlanoDeSaude plano) {
        int count = 0;
        for (Paciente p : pacientes) {
            if (p.getPlanoDeSaude() != null && 
                p.getPlanoDeSaude().equals(plano)) {
                count++;
            }
        }
        return count;
    }

    //os getters
    public String getNome() {
        return nome;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<PlanoDeSaude> getPlanosDeSaude() {
        return planosDeSaude;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public List<Internacao> getInternacoes() {
        return internacoes;
    }

    //log
    @Override
    public String toString() {
        return "Hospital{" +
                "nome='" + nome + '\'' +
                ", pacientes=" + pacientes.size() +
                ", medicos=" + medicos.size() +
                ", consultas=" + consultas.size() +
                ", internacoes=" + internacoes.size() +
                '}';
    }
}
