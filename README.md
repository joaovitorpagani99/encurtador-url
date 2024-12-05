# Encurtador de URL com AWS Lambda e S3

Este projeto implementa um sistema de encurtamento de URLs utilizando **AWS Lambda** e **Amazon S3**, baseado na arquitetura serverless. Ele fornece dois serviços principais:

1. **Criação de URLs encurtadas**, com suporte para data de expiração.
2. **Redirecionamento** de URLs encurtadas, verificando a validade da URL.

---

## 📚 Descrição Geral

### Fluxo de Funcionamento
- O cliente faz uma solicitação para criar uma URL encurtada, informando a URL original e o tempo de expiração.
- A aplicação cria um código único para a URL encurtada e salva os dados no **Amazon S3**.
- Quando o cliente tenta acessar a URL encurtada, o sistema verifica o código no **S3**:
  - Redireciona para a URL original se a URL for válida.
  - Retorna uma mensagem de erro se a URL estiver expirada.

---

## 🏗️ Arquitetura

### Serviços Utilizados
- **Amazon API Gateway**: Exposição dos endpoints REST.
- **AWS Lambda**: Funções para criação e redirecionamento das URLs.
- **Amazon S3**: Armazenamento dos dados de URLs.

### Diagrama de Arquitetura
![Diagrama de Arquitetura](encurtador-de-url.jpg)

---

## 🌐 Endpoints

### 1. Criar URL Encurtada
- **Métoto:** POST  
- **Endpoint:** `/create`  

**Requisição:**
```json
{
  "originalUrl": "https://exemplo.com",
  "expirationTime": "3600"  // Tempo de expiração em segundos
}

Respostas: 

{
  "code": "abc12345"  // Código único da URL encurtada
}
````

### 2. Redirecionar URL

- **Método:** GET
- **Endpoint:** `/abc12345`
  
**Comportamento:**

- **Retorna um redirecionamento HTTP 302 para a URL original.**
- **Retorna HTTP 410 (Gone) se a URL estiver expirada.**

---

## ⚙️ Configuração

### Pré-requisitos
- Conta AWS com permissões para Lambda, S3 e API Gateway.
- Bucket S3 configurado (nome padrão: url-shortner-storage-pagani).
- Java 8 ou superior instalado para compilar o projeto.

#### Passos para Configuração

1. Configurar o Bucket S3:

- Crie um bucket no S3 com o nome url-shortner-storage-pagani.
- Garanta que o bucket permita leitura e escrita pelas funções Lambda.
  
2. Deploy das Funções Lambda:
   
- Compile o código-fonte:
  
````maven
mvn clean package
````
- Faça upload do arquivo .jar para duas funções Lambda:
  - CreateUrl: Responsável pela criação de URLs encurtadas.
  - RedirectUrl: Responsável pelo redirecionamento.
  
3. Configurar o API Gateway:

- Crie um API Gateway REST com os seguintes endpoints:
  - **POST /create** → Função CreateUrl.
  - **GET /{shortUrl}** → Função RedirectUrl.
---  

# 📜 Código-Fonte


## 1. Função Lambda: Criar URL (createUrlShortener)

**Resumo**

- Gera um identificador único de 8 caracteres.
- Salva os dados no S3 no formato JSON:
    - Chave: Código curto (ex.: abc12345.json).
    - Conteúdo:
        ````json
        {
        "originalUrl": "https://exemplo.com",
        "expirationTime": 1696473532
        }
        ````
**Principais Componentes**
- **Classe Main:** Responsável por gerar o código e salvar os dados.
- **Classe UrlData:** Representa os dados da URL (original e expiração).
  
**Fluxo:**
1. Recebe a URL original e o tempo de expiração.
2. Gera um código curto usando UUID.
3. Armazena os dados no S3.
   
## 🛠️ Testando a Aplicação

### 1. Criar URL Encurtada

Faça uma solicitação POST:

````bash
curl -X POST \
-H "Content-Type: application/json" \
-d '{"originalUrl": "https://exemplo.com", "expirationTime": "3600"}' \
https://<sua-api-gateway-url>/create
````

Resposta esperada:

````json
{
  "code": "abc12345"
}
````
1. Redirecionar URL

Acesse a URL gerada:

````arduino
https://<sua-api-gateway-url>/abc12345
Comportamento esperado:
Redireciona para https://exemplo.com (se a URL ainda for válida).
````

- Retorna HTTP 410 se a URL estiver expirada.

### 🚀 Melhorias Futuras
1. Implementar autenticação para limitar o uso da API.
2. Adicionar monitoramento com AWS CloudWatch.
3. Otimizar o algoritmo de geração de códigos para evitar colisões.
4. Permitir personalização de URLs encurtadas.
