package com.autobots.automanager.modelo;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.CredencialAcesso;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.enumeracoes.TipoUsuario;
import com.autobots.automanager.repositorios.CredencialAcessoRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EmpresaRepositorio empresaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private VeiculoRepositorio veiculoRepositorio;

    @Autowired
    private MercadoriaRepositorio mercadoriaRepositorio;

    @Autowired
    private ServicoRepositorio servicoRepositorio;

    @Autowired
    private VendaRepositorio vendaRepositorio;

    @Autowired
    private TelefoneRepositorio telefoneRepositorio;

    @Autowired
    private DocumentoRepositorio documentoRepositorio;

    @Autowired
    private EnderecoRepositorio enderecoRepositorio;

    @Autowired
    private CredencialAcessoRepositorio credencialRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Criar dados iniciais para teste
        carregarDados();
    }

    private void carregarDados() {
        try {
            // Endereços
            Endereco endereco1 = new Endereco();
            endereco1.setEstado("SP");
            endereco1.setCidade("São Paulo");
            endereco1.setBairro("Moema");
            endereco1.setRua("Avenida Imigrantes");
            endereco1.setNumero("1000");
            endereco1.setCodigoPostal("04082-000");
            endereco1.setInformacoesAdicionais("Próximo ao metrô Imigrantes");
            endereco1 = enderecoRepositorio.save(endereco1);

            Endereco endereco2 = new Endereco();
            endereco2.setEstado("SP");
            endereco2.setCidade("São Paulo");
            endereco2.setBairro("Vila Mariana");
            endereco2.setRua("Rua Abílio Soares");
            endereco2.setNumero("250");
            endereco2.setCodigoPostal("04005-000");
            endereco2.setInformacoesAdicionais("Apto 1501");
            endereco2 = enderecoRepositorio.save(endereco2);

            // Telefones
            Telefone tel1 = new Telefone();
            tel1.setDdd("11");
            tel1.setNumero("3030-0000");
            tel1 = telefoneRepositorio.save(tel1);

            Telefone tel2 = new Telefone();
            tel2.setDdd("11");
            tel2.setNumero("99999-8888");
            tel2 = telefoneRepositorio.save(tel2);

            // Mercadorias (save independently)
            Mercadoria mercadoria1 = new Mercadoria();
            mercadoria1.setNome("Filtro de Ar");
            mercadoria1.setFabricante("Bosch");
            mercadoria1.setQuantidade(50);
            mercadoria1.setValor(45.50);
            Calendar cal1 = Calendar.getInstance();
            cal1.set(2025, Calendar.DECEMBER, 31);
            mercadoria1.setValidade(cal1.getTime());
            mercadoria1.setDescricao("Filtro de ar para motores 1.6L");
            mercadoria1.setCadastro(new Date());
            mercadoria1 = mercadoriaRepositorio.save(mercadoria1);

            Mercadoria mercadoria2 = new Mercadoria();
            mercadoria2.setNome("Pastilha de Freio");
            mercadoria2.setFabricante("Fiat");
            mercadoria2.setQuantidade(100);
            mercadoria2.setValor(120.00);
            Calendar cal2 = Calendar.getInstance();
            cal2.set(2026, Calendar.MAY, 15);
            mercadoria2.setValidade(cal2.getTime());
            mercadoria2.setDescricao("Pastilha de freio dianteira");
            mercadoria2.setCadastro(new Date());
            mercadoria2 = mercadoriaRepositorio.save(mercadoria2);

            // Serviços (save independently)
            Servico servico1 = new Servico();
            servico1.setNome("Troca de Óleo");
            servico1.setValor(120.00);
            servico1.setDescricao("Troca de óleo e filtro do motor");
            servico1.setCadastro(new Date());
            servico1 = servicoRepositorio.save(servico1);

            Servico servico2 = new Servico();
            servico2.setNome("Balanceamento de Rodas");
            servico2.setValor(80.00);
            servico2.setDescricao("Balanceamento completo das 4 rodas");
            servico2.setCadastro(new Date());
            servico2 = servicoRepositorio.save(servico2);

            // Credenciais com roles de teste
            CredencialAcesso credAdmin = new CredencialAcesso();
            credAdmin.setNomeUsuario("admin");
            credAdmin.setSenha(passwordEncoder.encode("admin123"));
            credAdmin = credencialRepositorio.save(credAdmin);

            CredencialAcesso credGerente = new CredencialAcesso();
            credGerente.setNomeUsuario("gerente");
            credGerente.setSenha(passwordEncoder.encode("gerente123"));
            credGerente = credencialRepositorio.save(credGerente);

            CredencialAcesso credVendedor = new CredencialAcesso();
            credVendedor.setNomeUsuario("vendedor");
            credVendedor.setSenha(passwordEncoder.encode("vendedor123"));
            credVendedor = credencialRepositorio.save(credVendedor);

            CredencialAcesso credCliente = new CredencialAcesso();
            credCliente.setNomeUsuario("cliente");
            credCliente.setSenha(passwordEncoder.encode("cliente123"));
            credCliente = credencialRepositorio.save(credCliente);

            // Documentos
            Documento doc1 = new Documento();
            doc1.setTipo("CPF");
            doc1.setNumero("123.456.789-00");
            doc1 = documentoRepositorio.save(doc1);

            // Veículos
            Veiculo veiculo1 = new Veiculo();
            veiculo1.setModelo("Corolla");
            veiculo1.setFabricante("Toyota");
            veiculo1.setAnoFabricacao("2022");
            veiculo1.setPlaca("ABC-1234");
            veiculo1 = veiculoRepositorio.save(veiculo1);

            Veiculo veiculo2 = new Veiculo();
            veiculo2.setModelo("Golf");
            veiculo2.setFabricante("Volkswagen");
            veiculo2.setAnoFabricacao("2023");
            veiculo2.setPlaca("XYZ-5678");
            veiculo2 = veiculoRepositorio.save(veiculo2);

            // Usuários com novos roles
            Usuario usuarioAdmin = new Usuario();
            usuarioAdmin.setNome("Administrador System");
            usuarioAdmin.setNomeSocial("Admin");
            usuarioAdmin.setEmail("admin@autobots.com");
            usuarioAdmin.setDataNascimento(new Date());
            usuarioAdmin.setDataCadastro(new Date());
            usuarioAdmin.getPerfil().add(TipoUsuario.ADMINISTRADOR);
            usuarioAdmin.setCredencial(credAdmin);
            usuarioAdmin.setEndereco(endereco1);
            usuarioAdmin = usuarioRepositorio.save(usuarioAdmin);

            Usuario usuarioGerente = new Usuario();
            usuarioGerente.setNome("João Gerente");
            usuarioGerente.setNomeSocial("João");
            usuarioGerente.setEmail("joao@autobots.com");
            usuarioGerente.setDataNascimento(new Date());
            usuarioGerente.setDataCadastro(new Date());
            usuarioGerente.getPerfil().add(TipoUsuario.GERENTE);
            usuarioGerente.setCredencial(credGerente);
            usuarioGerente.setEndereco(endereco2);
            usuarioGerente.getTelefones().add(tel2);
            usuarioGerente = usuarioRepositorio.save(usuarioGerente);

            Usuario usuarioVendedor = new Usuario();
            usuarioVendedor.setNome("Maria Vendedor");
            usuarioVendedor.setNomeSocial("Maria");
            usuarioVendedor.setEmail("maria@autobots.com");
            usuarioVendedor.setDataNascimento(new Date());
            usuarioVendedor.setDataCadastro(new Date());
            usuarioVendedor.getPerfil().add(TipoUsuario.VENDEDOR);
            usuarioVendedor.setCredencial(credVendedor);
            usuarioVendedor.setEndereco(endereco2);
            usuarioVendedor.getVeiculos().add(veiculo1);
            usuarioVendedor = usuarioRepositorio.save(usuarioVendedor);

            Usuario usuarioCliente = new Usuario();
            usuarioCliente.setNome("Pedro Cliente");
            usuarioCliente.setNomeSocial("Pedro");
            usuarioCliente.setEmail("pedro@example.com");
            usuarioCliente.setDataNascimento(new Date());
            usuarioCliente.setDataCadastro(new Date());
            usuarioCliente.getPerfil().add(TipoUsuario.CLIENTE);
            usuarioCliente.setCredencial(credCliente);
            usuarioCliente.setEndereco(endereco2);
            usuarioCliente.getDocumentos().add(doc1);
            usuarioCliente.getTelefones().add(tel2);
            usuarioCliente.getVeiculos().add(veiculo2);
            usuarioCliente = usuarioRepositorio.save(usuarioCliente);

            // Empresa (with all relationships - use existing references, don't re-fetch)
            Empresa empresa = new Empresa();
            empresa.setRazaoSocial("AutoBots Manutenção e Peças LTDA");
            empresa.setNomeFantasia("AutoBots SP");
            empresa.setCnpj("12.345.678/0001-90");
            empresa.setEndereco(endereco1);
            empresa.getTelefones().add(tel1);
            empresa.getUsuarios().add(usuarioAdmin);
            empresa.getUsuarios().add(usuarioGerente);
            empresa.getUsuarios().add(usuarioVendedor);
            empresa.getUsuarios().add(usuarioCliente);
            empresa.getMercadorias().add(mercadoria1);
            empresa.getMercadorias().add(mercadoria2);
            empresa.getServicos().add(servico1);
            empresa.getServicos().add(servico2);
            empresa = empresaRepositorio.save(empresa);

            // Venda (with all relationships - use existing references, don't re-fetch)
            Venda venda1 = new Venda();
            venda1.setCadastro(new Date());
            venda1.setFuncionario(usuarioVendedor);
            venda1.setCliente(usuarioCliente);
            venda1.setVeiculo(veiculo1);
            venda1.getMercadorias().add(mercadoria1);
            venda1.getServicos().add(servico1);
            venda1 = vendaRepositorio.save(venda1);

            // Add venda to empresa
            empresa.getVendas().add(venda1);
            empresaRepositorio.save(empresa);

            System.out.println("✓ Dados iniciais carregados com sucesso!");
            System.out.println("  - 1 Empresa");
            System.out.println("  - 4 Usuários (Admin, Gerente, Vendedor, Cliente)");
            System.out.println("    Credenciais de teste:");
            System.out.println("    - Admin: admin / admin123");
            System.out.println("    - Gerente: gerente / gerente123");
            System.out.println("    - Vendedor: vendedor / vendedor123");
            System.out.println("    - Cliente: cliente / cliente123");
            System.out.println("  - 2 Veículos");
            System.out.println("  - 2 Mercadorias");
            System.out.println("  - 2 Serviços");
            System.out.println("  - 1 Venda");
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados iniciais: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
