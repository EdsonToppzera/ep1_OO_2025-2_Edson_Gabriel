package classes;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;

public class Interface {

    private Hospital hospital;
    private Scanner scanner;
    private GerenciadorArquivos gerenciador;

    private void carregarDados() {
        List<PlanoDeSaude> planos = gerenciador.carregarPlanos();
        for (PlanoDeSaude p : planos) {
            hospital.cadastrarPlanoDeSaude(p);
        }
        
        List<Paciente> pacientes = gerenciador.carregarPacientes(planos);
        for (Paciente p : pacientes) {
            hospital.cadastrarPaciente(p);
        }
        
        List<Medico> medicos = gerenciador.carregarMedicos();
        for (Medico m : medicos) {
            hospital.cadastrarMedico(m);
        }
    }
    
    public Interface() {
        this.gerenciador = new GerenciadorArquivos();
        this.hospital = new Hospital("Hospital Central");
        this.scanner = new Scanner(System.in);
        carregarDados();
    }

    public void iniciar() {
        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = lerOpcao();
            processarOpcao(opcao);
        } while (opcao != 0);
        
        gerenciador.salvarTodosDados(hospital);
        System.out.println("Sistema encerrado.");
        scanner.close();
    }

    private void exibirMenuPrincipal() {
        System.out.println("==========SISTEMA HOSPITALAR==========");
        System.out.println("1. Cadastrar Paciente");
        System.out.println("2. Cadastrar Médico");
        System.out.println("3. Cadastrar Plano de Saúde");
        System.out.println("4. Agendar Consulta");
        System.out.println("5. Registrar Internação");
        System.out.println("6. Concluir Consulta");
        System.out.println("7. Finalizar Internação");
        System.out.println("8. Cancelar Consulta");
        System.out.println("9. Cancelar Internação");
        System.out.println("10. Relatórios");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                cadastrarPaciente();
                break;
            case 2:
                cadastrarMedico();
                break;
            case 3:
                cadastrarPlanoDeSaude();
                break;
            case 4:
                agendarConsulta();
                break;
            case 5:
                registrarInternacao();
                break;
            case 6:
                concluirConsulta();
                break;
            case 7:
                finalizarInternacao();
                break;
            case 8:
                cancelarConsulta();
                break;
            case 9:
                cancelarInternacao();
                break;
            case 10:
                menuRelatorios();
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida");
        }
    }

    // ==============CADASTROS==============

    private void cadastrarPaciente() {
        System.out.println("\n=== CADASTRO DE PACIENTE ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Idade: ");
        int idade = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Tem plano de saúde? (S/N): ");
        String temPlano = scanner.nextLine();
        
        Paciente paciente;
        
        if (temPlano.equalsIgnoreCase("S")) {
            System.out.println("\nPlanos disponíveis:");
            listarPlanosDeSaude();
            System.out.print("Nome do plano: ");
            String nomePlano = scanner.nextLine();
            PlanoDeSaude plano = hospital.buscarPlanoPorNome(nomePlano);
            
            if (plano != null) {
                paciente = new PacienteEspecial(nome, cpf, idade, plano);
            } else {
                System.out.println("Plano não encontrado. Cadastrando como paciente comum.");
                paciente = new Paciente(nome, cpf, idade);
            }
        } else {
            paciente = new Paciente(nome, cpf, idade);
        }
        
        if (hospital.cadastrarPaciente(paciente)) {
            System.out.println("Paciente cadastrado com sucesso!");
        } else {
            System.out.println("CPF já cadastrado.");
        }
    }

    private void cadastrarMedico() {
        System.out.println("\n=== CADASTRO DE MÉDICO ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CRM: ");
        String crm = scanner.nextLine();

        System.out.println("\nEspecialidades disponíveis:");
        for (Especialidade esp : Especialidade.values()) {
            System.out.println("- " + esp);
        }

        Especialidade especialidade = null;
        while (especialidade == null) {
            System.out.print("Especialidade: ");
            String espStr = scanner.nextLine().toUpperCase();
            try {
                especialidade = Especialidade.valueOf(espStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Especialidade inválida, Digite exatamente como aparece na lista.");
            }
        }

        System.out.print("Custo da consulta (R$): ");
        double custo = Double.parseDouble(scanner.nextLine());

        Medico medico = new Medico(nome, crm, especialidade, custo);

        if (hospital.cadastrarMedico(medico)) {
            System.out.println("Médico cadastrado com sucesso!");
        } else {
            System.out.println("Erro: CRM já cadastrado.");
        }
    }

    private void cadastrarPlanoDeSaude() {
        System.out.println("\n=== CADASTRO DE PLANO DE SAÚDE ===");
        System.out.print("Nome do plano: ");
        String nome = scanner.nextLine();
        System.out.print("Paga internação? (S/N): ");
        boolean pagaInternacao = scanner.nextLine().equalsIgnoreCase("S");
        
        PlanoDeSaude plano = new PlanoDeSaude(nome, pagaInternacao);
        
        System.out.print("Deseja adicionar descontos por especialidade? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            for (Especialidade esp : Especialidade.values()) {
                System.out.print("Desconto para " + esp + " (%): ");
                double desconto = Double.parseDouble(scanner.nextLine());
                plano.adicionarDesconto(esp, desconto);
            }
        }
        
        if (hospital.cadastrarPlanoDeSaude(plano)) {
            System.out.println("Plano de saúde cadastrado com sucesso!");
        } else {
            System.out.println("Erro: Plano já cadastrado.");
        }
    }

    // ==============AGENDAMENTOS==============

    private void agendarConsulta() {
        System.out.println("\n=== AGENDAR CONSULTA ===");
        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();
        Paciente paciente = hospital.buscarPacientePorCpf(cpf);
        
        if (paciente == null) {
            System.out.println("Paciente não encontrado.");
            return;
        }
        
        System.out.print("CRM do médico: ");
        String crm = scanner.nextLine();
        Medico medico = hospital.buscarMedicoPorCrm(crm);
        
        if (medico == null) {
            System.out.println("Médico não encontrado.");
            return;
        }
        
        System.out.print("Data e hora (dd/MM/yyyy HH:mm): ");
        String dataHoraStr = scanner.nextLine();
        LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr, 
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        
        System.out.print("Local (ex: Consultório 101): ");
        String local = scanner.nextLine();
        
        Consulta consulta = hospital.agendarConsulta(paciente, medico, dataHora, local);
        
        if (consulta != null) {
            System.out.println("Consulta agendada com sucesso!");
            System.out.println(consulta);
        }
    }

    private void registrarInternacao() {
        System.out.println("\n=== REGISTRAR INTERNAÇÃO ===");
        System.out.print("CPF do paciente: ");
        String cpf = scanner.nextLine();
        Paciente paciente = hospital.buscarPacientePorCpf(cpf);
        
        if (paciente == null) {
            System.out.println("Paciente não encontrado.");
            return;
        }
        
        System.out.print("CRM do médico responsável: ");
        String crm = scanner.nextLine();
        Medico medico = hospital.buscarMedicoPorCrm(crm);
        
        if (medico == null) {
            System.out.println("Médico não encontrado.");
            return;
        }
        
        System.out.print("Data de entrada (dd/MM/yyyy): ");
        String dataStr = scanner.nextLine();
        LocalDate dataEntrada = LocalDate.parse(dataStr, 
            DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        System.out.print("Quarto: ");
        String quarto = scanner.nextLine();
        
        System.out.print("Custo da diária (R$): ");
        double custoDiaria = Double.parseDouble(scanner.nextLine());
        
        Internacao internacao = hospital.registrarInternacao(paciente, medico, 
            dataEntrada, quarto, custoDiaria);
        
        if (internacao != null) {
            System.out.println("Internação registrada com sucesso!");
            System.out.println(internacao);
        }
    }

    //OPERAÇÕES
    //
    private void concluirConsulta() {
        System.out.println("\n=== CONCLUIR CONSULTA ===");
        listarConsultasAgendadas();
        System.out.print("Índice da consulta: ");
        int indice = Integer.parseInt(scanner.nextLine());
        
        if (indice >= 0 && indice < hospital.getConsultas().size()) {
            Consulta consulta = hospital.getConsultas().get(indice);
            
            System.out.print("Diagnóstico: ");
            String diagnostico = scanner.nextLine();
            System.out.print("Prescrição: ");
            String prescricao = scanner.nextLine();
            
            consulta.concluirConsulta(diagnostico, prescricao);
            System.out.println("Consulta concluída!");
        } else {
            System.out.println("Consulta inválida.");
        }
    }

    private void finalizarInternacao() {
        System.out.println("\n=== FINALIZAR INTERNAÇÃO ===");
        listarInternacoesAtivas();
        System.out.print("Índice da internação: ");
        int indice = Integer.parseInt(scanner.nextLine());
        
        if (indice >= 0 && indice < hospital.getInternacoes().size()) {
            Internacao internacao = hospital.getInternacoes().get(indice);
            
            System.out.print("Data de saída (dd/MM/yyyy): ");
            String dataStr = scanner.nextLine();
            LocalDate dataSaida = LocalDate.parse(dataStr, 
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            if (hospital.finalizarInternacao(internacao, dataSaida)) {
                System.out.println("Internação finalizada!");
                System.out.println(internacao.getInformacoesCompletas());
            }
        } else {
            System.out.println("Internação inválida.");
        }
    }

    private void cancelarConsulta() {
        System.out.println("\n=== CANCELAR CONSULTA ===");
        listarConsultasAgendadas();
        System.out.print("Índice da consulta: ");
        int indice = Integer.parseInt(scanner.nextLine());
        
        if (indice >= 0 && indice < hospital.getConsultas().size()) {
            Consulta consulta = hospital.getConsultas().get(indice);
            consulta.cancelarConsulta();
            System.out.println("Consulta cancelada!");
        } else {
            System.out.println("Consulta inválida.");
        }
    }

    private void cancelarInternacao() {
        System.out.println("\n=== CANCELAR INTERNAÇÃO ===");
        listarInternacoesAtivas();
        System.out.print("Índice da internação: ");
        int indice = Integer.parseInt(scanner.nextLine());
        
        if (indice >= 0 && indice < hospital.getInternacoes().size()) {
            Internacao internacao = hospital.getInternacoes().get(indice);
            
            if (hospital.cancelarInternacao(internacao)) {
                System.out.println("Internação cancelada!");
            }
        } else {
            System.out.println("Internação inválida.");
        }
    }

    //RELATÓRIOS
    //
    private void menuRelatorios() {
        System.out.println("\n=== RELATÓRIOS ===");
        System.out.println("1. Listar Pacientes");
        System.out.println("2. Listar Médicos");
        System.out.println("3. Listar Consultas");
        System.out.println("4. Listar Internações Ativas");
        System.out.println("5. Estatísticas Gerais");
        System.out.print("Escolha: ");
        
        int opcao = lerOpcao();
        
        switch (opcao) {
            case 1:
                listarPacientes();
                break;
            case 2:
                listarMedicos();
                break;
            case 3:
                listarConsultas();
                break;
            case 4:
                listarInternacoesAtivas();
                break;
            case 5:
                exibirEstatisticas();
                break;
        }
    }

    private void listarPacientes() {
        System.out.println("\n=== PACIENTES CADASTRADOS ===");
        for (Paciente p : hospital.getPacientes()) {
            System.out.println(p);
        }
    }

    private void listarMedicos() {
        System.out.println("\n=== MÉDICOS CADASTRADOS ===");
        for (Medico m : hospital.getMedicos()) {
            System.out.println(m);
        }
    }

    private void listarPlanosDeSaude() {
        for (PlanoDeSaude p : hospital.getPlanosDeSaude()) {
            System.out.println("- " + p.getNome());
        }
    }

    private void listarConsultas() {
        System.out.println("\n=== CONSULTAS ===");
        for (Consulta c : hospital.getConsultas()) {
            System.out.println(c);
        }
    }

    private void listarConsultasAgendadas() {
        System.out.println("\n=== CONSULTAS AGENDADAS ===");
        int i = 0;
        for (Consulta c : hospital.getConsultas()) {
            if (c.getStatus() == Consulta.Status.AGENDADA) {
                System.out.println(i + ". " + c);
            }
            i++;
        }
    }

    private void listarInternacoesAtivas() {
        System.out.println("\n=== INTERNAÇÕES ATIVAS ===");
        int i = 0;
        for (Internacao intern : hospital.getInternacoes()) {
            if (intern.isAtiva()) {
                System.out.println(i + ". " + intern);
            }
            i++;
        }
    }

    private void exibirEstatisticas() {
        System.out.println("\n=== ESTATÍSTICAS GERAIS ===");
        
        Medico medicoDestaque = hospital.medicoQueMaisAtendeu();
        if (medicoDestaque != null) {
            System.out.println("Médico que mais atendeu: " + medicoDestaque.getNome() + 
                " (" + medicoDestaque.getNumeroConsultasRealizadas() + " consultas)");
        }
        
        Especialidade espDestaque = hospital.especialidadeMaisProcurada();
        if (espDestaque != null) {
            System.out.println("Especialidade mais procurada: " + espDestaque);
        }
        
        System.out.println("\nTotal de pacientes: " + hospital.getPacientes().size());
        System.out.println("Total de médicos: " + hospital.getMedicos().size());
        System.out.println("Total de consultas: " + hospital.getConsultas().size());
        System.out.println("Internações ativas: " + hospital.buscarInternacoesAtivas().size());
    }

    //MAIN
    public static void main(String[] args) {
        Interface sistema = new Interface();
        sistema.iniciar();
    }
}
