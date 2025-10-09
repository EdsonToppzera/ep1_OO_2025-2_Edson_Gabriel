package classes;

import java.io.*;
//import java.time.LocalDateTime;
//import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivos {

    private static final String PASTA_DADOS = "dados/";
    private static final String ARQUIVO_PACIENTES = PASTA_DADOS + "pacientes.csv";
    private static final String ARQUIVO_MEDICOS = PASTA_DADOS + "medicos.csv";
    private static final String ARQUIVO_PLANOS = PASTA_DADOS + "planos.csv";
    private static final String ARQUIVO_CONSULTAS = PASTA_DADOS + "consultas.csv";
    private static final String ARQUIVO_INTERNACOES = PASTA_DADOS + "internacoes.csv";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public GerenciadorArquivos() {
        File pasta = new File(PASTA_DADOS);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }

    //SALVAR DADOS
    public void salvarPacientes(List<Paciente> pacientes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_PACIENTES))) {
            writer.write("nome;cpf;idade;plano;tipo\n");
            
            for (Paciente p : pacientes) {
                String planoNome = (p.getPlanoDeSaude() != null) ? p.getPlanoDeSaude().getNome() : "NENHUM";
                String tipo = (p instanceof PacienteEspecial) ? "ESPECIAL" : "COMUM";
                
                writer.write(String.format("%s;%s;%d;%s;%s\n",
                    p.getNome(), p.getCpf(), p.getIdade(), planoNome, tipo));
            }
            System.out.println("Pacientes salvos com sucesso");
        } catch (IOException e) {
            System.err.println("Erro ao salvar pacientes: " + e.getMessage());
        }
    }

    public void salvarMedicos(List<Medico> medicos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_MEDICOS))) {
            writer.write("nome;crm;especialidade;custoConsulta;consultasRealizadas\n");
            
            for (Medico m : medicos) {
                writer.write(String.format("%s;%s;%s;%.2f;%d\n",
                    m.getNome(), m.getCrm(), m.getEspecialidade(), 
                    m.getCustoConsulta(), m.getNumeroConsultasRealizadas()));
            }
            System.out.println("Médicos salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar médicos: " + e.getMessage());
        }
    }

    public void salvarPlanos(List<PlanoDeSaude> planos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_PLANOS))) {
            writer.write("nome;pagaInternacao;descontos\n");
            
            for (PlanoDeSaude p : planos) {
                StringBuilder descontos = new StringBuilder();
                for (Especialidade esp : p.getDescontos().keySet()) {
                    descontos.append(esp).append(":").append(p.getDescontos().get(esp)).append("|");
                }
                
                writer.write(String.format("%s;%s;%s\n",
                    p.getNome(), p.isPagaInternacao(), descontos.toString()));
            }
            System.out.println("Planos salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar planos: " + e.getMessage());
        }
    }

    public void salvarConsultas(List<Consulta> consultas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CONSULTAS))) {
            writer.write("cpfPaciente;crmMedico;dataHora;local;status;diagnostico;prescricao;valorOriginal;valorFinal\n");
            
            for (Consulta c : consultas) {
                String diagnostico = (c.getDiagnostico() != null) ? c.getDiagnostico() : "";
                String prescricao = (c.getPrescricao() != null) ? c.getPrescricao() : "";
                
                writer.write(String.format("%s;%s;%s;%s;%s;%s;%s;%.2f;%.2f\n",
                    c.getPaciente().getCpf(),
                    c.getMedico().getCrm(),
                    c.getDataHora().format(DATE_TIME_FORMATTER),
                    c.getLocal(),
                    c.getStatus(),
                    diagnostico,
                    prescricao,
                    c.getValorOriginal(),
                    c.getValorFinal()));
            }
            System.out.println("✓ Consultas salvas com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar consultas: " + e.getMessage());
        }
    }

    public void salvarInternacoes(List<Internacao> internacoes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_INTERNACOES))) {
            writer.write("cpfPaciente;crmMedico;dataEntrada;dataSaida;quarto;custoDiaria;custoTotal;ativa\n");
            
            for (Internacao i : internacoes) {
                String dataSaida = (i.getDataSaida() != null) ? i.getDataSaida().format(DATE_FORMATTER) : "";
                
                writer.write(String.format("%s;%s;%s;%s;%s;%.2f;%.2f;%s\n",
                    i.getPaciente().getCpf(),
                    i.getMedicoResponsavel().getCrm(),
                    i.getDataEntrada().format(DATE_FORMATTER),
                    dataSaida,
                    i.getQuarto(),
                    i.getCustoDiaria(),
                    i.getCustoTotal(),
                    i.isAtiva()));
            }
            System.out.println("✓ Internações salvas com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar internações: " + e.getMessage());
        }
    }

    public void salvarTodosDados(Hospital hospital) {
        salvarPacientes(hospital.getPacientes());
        salvarMedicos(hospital.getMedicos());
        salvarPlanos(hospital.getPlanosDeSaude());
        salvarConsultas(hospital.getConsultas());
        salvarInternacoes(hospital.getInternacoes());
    }

    public List<PlanoDeSaude> carregarPlanos() {
        List<PlanoDeSaude> planos = new ArrayList<>();
        File arquivo = new File(ARQUIVO_PLANOS);
        
        if (!arquivo.exists()) {
            return planos;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PLANOS))) {
            String linha = reader.readLine();
            
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                String nome = dados[0];
                boolean pagaInternacao = Boolean.parseBoolean(dados[1]);
                
                PlanoDeSaude plano = new PlanoDeSaude(nome, pagaInternacao);
                
                // Carrega descontos
                if (dados.length > 2 && !dados[2].isEmpty()) {
                    String[] descontos = dados[2].split("\\|");
                    for (String desc : descontos) {
                        String[] partes = desc.split(":");
                        if (partes.length == 2) {
                            Especialidade esp = Especialidade.valueOf(partes[0]);
                            double valor = Double.parseDouble(partes[1]);
                            plano.adicionarDesconto(esp, valor);
                        }
                    }
                }
                
                planos.add(plano);
            }
            System.out.println("Planos carregados com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao carregar planos: " + e.getMessage());
        }
        
        return planos;
    }

    public List<Paciente> carregarPacientes(List<PlanoDeSaude> planos) {
        List<Paciente> pacientes = new ArrayList<>();
        File arquivo = new File(ARQUIVO_PACIENTES);
        
        if (!arquivo.exists()) {
            return pacientes;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PACIENTES))) {
            String linha = reader.readLine(); // Pula cabeçalho
            
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                String nome = dados[0];
                String cpf = dados[1];
                int idade = Integer.parseInt(dados[2]);
                String planoNome = dados[3];
                String tipo = dados[4];
                
                Paciente paciente;
                
                if (tipo.equals("ESPECIAL") && !planoNome.equals("NENHUM")) {
                    PlanoDeSaude plano = buscarPlanoPorNome(planos, planoNome);
                    paciente = new PacienteEspecial(nome, cpf, idade, plano);
                } else {
                    paciente = new Paciente(nome, cpf, idade);
                }
                
                pacientes.add(paciente);
            }
            System.out.println("Pacientes carregados com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao carregar pacientes: " + e.getMessage());
        }
        
        return pacientes;
    }

    public List<Medico> carregarMedicos() {
        List<Medico> medicos = new ArrayList<>();
        File arquivo = new File(ARQUIVO_MEDICOS);
        
        if (!arquivo.exists()) {
            return medicos;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_MEDICOS))) {
            String linha = reader.readLine();
            
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                String nome = dados[0];
                String crm = dados[1];
                Especialidade especialidade = Especialidade.valueOf(dados[2]);
                double custoConsulta = Double.parseDouble(dados[3].replace(",", "."));
                int consultasRealizadas = Integer.parseInt(dados[4]);
                
                Medico medico = new Medico(nome, crm, especialidade, custoConsulta);
                medico.setNumeroConsultasRealizadas(consultasRealizadas);
                
                medicos.add(medico);
            }
            System.out.println("Médicos carregados com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao carregar médicos: " + e.getMessage());
        }
        
        return medicos;
    }

    private PlanoDeSaude buscarPlanoPorNome(List<PlanoDeSaude> planos, String nome) {
        for (PlanoDeSaude p : planos) {
            if (p.getNome().equals(nome)) {
                return p;
            }
        }
        return null;
    }
}