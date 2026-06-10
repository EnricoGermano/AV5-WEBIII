# AV5-WEBIII

# AutoManager - AV5 - Sistema de Gerenciamento Automotivo

## Descrição

Atividade de avaliação individual 5 (AV5) da matéria de WEBIII
## Manual de execução

### Pré-requisitos
Java 25 instalado

### 1 - Clonar o repositorio
```bash
git clone https://github.com/EnricoGermano/AV5-WEBIII.git
cd AV5-WEBIII
```

### 2 - Iniciar os Servidores
```bash
run_all.bat
```
Ou, se preferir rodar manualmente, inicie a API principal na porta 8080 (`.\mvnw.cmd spring-boot:run`) e cada microsserviço separado.


## Endpoints da API

### Autenticacao
```
POST   /auth/login                 - Realizar login
POST   /auth/refresh               - Renovar token
```
    Credenciais de teste:
    - Admin: admin / admin123
    - Gerente: gerente / gerente123
    - Vendedor: vendedor / vendedor123
    - Cliente: cliente / cliente123

### Empresa
```
GET    /empresa                        - Listar todas as empresas
GET    /empresa/{id}                   - Obter empresa por ID
POST   /empresa/cadastro               - Cadastrar nova empresa
PUT    /empresa/atualizar/{id}         - Atualizar empresa
DELETE /empresa/excluir/{id}           - Excluir empresa
POST   /empresa/{empresaId}/usuario/{usuarioId}      - Vincular usuário à empresa
DELETE /empresa/{empresaId}/usuario/{usuarioId}      - Desvincular usuário da empresa
```

### Usuario
```
GET    /usuario                        - Listar todos os usuários
GET    /usuario/{id}                   - Obter usuário por ID
POST   /usuario/cadastro               - Cadastrar novo usuário
PUT    /usuario/atualizar/{id}         - Atualizar usuário
DELETE /usuario/excluir/{id}           - Excluir usuário
POST   /usuario/{usuarioId}/veiculo/{veiculoId}     - Vincular veículo ao usuário
DELETE /usuario/{usuarioId}/veiculo/{veiculoId}     - Desvincular veículo do usuário
```

### Veiculo
```
GET    /veiculo                        - Listar todos os veículos
GET    /veiculo/{id}                   - Obter veículo por ID
POST   /veiculo/cadastro               - Cadastrar novo veículo
PUT    /veiculo/atualizar/{id}         - Atualizar veículo
DELETE /veiculo/excluir/{id}           - Excluir veículo
```

### Mercadoria
```
GET    /mercadoria                     - Listar todas as mercadorias
GET    /mercadoria/{id}                - Obter mercadoria por ID
POST   /mercadoria/cadastro            - Cadastrar nova mercadoria
PUT    /mercadoria/atualizar/{id}      - Atualizar mercadoria
DELETE /mercadoria/excluir/{id}        - Excluir mercadoria
```

### Servico
```
GET    /servico                        - Listar todos os serviços
GET    /servico/{id}                   - Obter serviço por ID
POST   /servico/cadastro               - Cadastrar novo serviço
PUT    /servico/atualizar/{id}         - Atualizar serviço
DELETE /servico/excluir/{id}           - Excluir serviço
```

### Venda
```
GET    /venda                          - Listar todas as vendas
GET    /venda/{id}                     - Obter venda por ID
POST   /venda/cadastro                 - Cadastrar nova venda
PUT    /venda/atualizar/{id}           - Atualizar venda
DELETE /venda/excluir/{id}             - Excluir venda
POST   /venda/{vendaId}/funcionario/{funcionarioId}  - Vincular funcionário 
POST   /venda/{vendaId}/cliente/{clienteId}          - Vincular cliente
POST   /venda/{vendaId}/veiculo/{veiculoId}          - Vincular veículo 
POST   /venda/{vendaId}/mercadoria/{mercadoriaId}    - Adicionar mercadoria 
DELETE /venda/{vendaId}/mercadoria/{mercadoriaId}    - Remover mercadoria da venda
POST   /venda/{vendaId}/servico/{servicoId}          - Adicionar serviço 
DELETE /venda/{vendaId}/servico/{servicoId}          - Remover serviço da venda
```

### Microsserviços (Precisa do Token JWT da porta 8080)
```
GET    http://localhost:8081/api/clientes/{empresaId}                   - Listar clientes (MS Clientes)
GET    http://localhost:8082/api/funcionarios/{empresaId}               - Listar funcionários (MS Funcionarios)
GET    http://localhost:8083/api/catalogo/{empresaId}                   - Listar mercadorias e serviços (MS Catalogo)
GET    http://localhost:8084/api/vendas-periodo/{empresaId}?inicio=&fim= - Listar vendas por período (MS Vendas)
GET    http://localhost:8085/api/veiculos-atendidos/{empresaId}         - Listar veículos atendidos (MS Veiculos)
```

### Telefone, Documento, Endereco, CredencialAcesso
Seguem o padrão CRUD básico:
```
GET    /{recurso}                      - Listar
GET    /{recurso}/{id}                 - Obter por ID
POST   /{recurso}/cadastro             - Cadastrar
PUT    /{recurso}/atualizar/{id}       - Atualizar
DELETE /{recurso}/excluir/{id}         - Excluir

GET    /gerson                      - Retornar a imagem gerson
GET    /gerson2                     - Retornar a imagem gerson 2
```
