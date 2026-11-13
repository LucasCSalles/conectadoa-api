# Implantação no Azure — Azure SQL + Azure Container Apps

Este é o roteiro para colocar a API no ar no Azure, seguindo o modelo do repositório de referência `acnaweb/study-apir`. São usados dois serviços:

- **Azure SQL Database** (tier Basic) como SQL Server.
- **Azure Container Apps**, que roda a imagem publicada no Docker Hub pelo GitHub Actions.

Pré-requisitos:

- **Azure CLI** com login feito (`az login`).
- **Imagem já publicada** em `docker.io/lucascsalles/conectadoa-api:latest`. Ela é publicada pelo workflow a cada push na `main`.
- **Container Apps disponível.** Se ainda não usou, rode `az extension add --name containerapp --upgrade` e `az provider register --namespace Microsoft.App`.

> Os nomes já estão preenchidos com o RM 554789 e o usuário `lucascsalles` do Docker Hub. Troque apenas `<senha-forte>`, e **nunca** coloque a senha real neste arquivo ou no Git.

## 1. Variáveis

### Bash (Linux, macOS ou Git Bash)

```bash
export RESOURCE_GROUP=rg-554789-conectadoa
export LOCATION=brazilsouth
export SQL_SERVER=sql-554789-conectadoa
export DB_SERVER_URL=$SQL_SERVER.database.windows.net
export DB_SERVER_PORT=1433
export DB_SCHEMA=conectadoa
export DB_USER=sqladmin
export DB_PWD='<senha-forte>'
export CONTAINERAPPS_ENVIRONMENT=env-554789-conectadoa
export APP_CONTAINER=conectadoa-api-554789
export DOCKER_IMAGE=docker.io/lucascsalles/conectadoa-api:latest
```

### PowerShell

```powershell
$env:RESOURCE_GROUP="rg-554789-conectadoa"
$env:LOCATION="brazilsouth"
$env:SQL_SERVER="sql-554789-conectadoa"
$env:DB_SERVER_URL="$env:SQL_SERVER.database.windows.net"
$env:DB_SERVER_PORT="1433"
$env:DB_SCHEMA="conectadoa"
$env:DB_USER="sqladmin"
$env:DB_PWD="<senha-forte>"
$env:CONTAINERAPPS_ENVIRONMENT="env-554789-conectadoa"
$env:APP_CONTAINER="conectadoa-api-554789"
$env:DOCKER_IMAGE="docker.io/lucascsalles/conectadoa-api:latest"
```

> Se a assinatura (por exemplo, Azure for Students) não permitir `brazilsouth`, use outra região, como `eastus`.

## 2. Resource Group

```bash
az group create --name $RESOURCE_GROUP --location $LOCATION
```

## 3. SQL Server e banco de dados

```bash
az sql server create \
  --name $SQL_SERVER \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --admin-user $DB_USER \
  --admin-password "$DB_PWD"

az sql db create \
  --name $DB_SCHEMA \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --edition Basic \
  --capacity 5 \
  --max-size 2GB
```

> O banco usa o tier **Basic** (5 DTU, cerca de US$ 5/mês, coberto pelo crédito do Azure for Students). Ele fica sempre ligado, então a API responde normalmente durante a avaliação. A oferta gratuita *serverless* pausa o banco ou esgota a cota gratuita em poucos dias, porque o pool de conexões da aplicação mantém o banco ativo.

## 4. Firewall do SQL

Primeiro, libere o acesso a partir de serviços do Azure. A regra especial 0.0.0.0 permite que o Container App se conecte:

```bash
az sql server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0
```

Opcional: libere também o seu IP, para consultar o banco pelo Azure Data Studio ou SSMS durante a demonstração:

```bash
export MEU_IP=$(curl -s https://api.ipify.org)
az sql server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --name MeuIP \
  --start-ip-address $MEU_IP \
  --end-ip-address $MEU_IP
```

> Não é necessário criar tabelas. O Hibernate cria `usuarios`, `ongs`, `materiais` e `reservas` na primeira execução, e o `data.sql` cadastra as ONGs iniciais.

## 5. Container Apps Environment

```bash
az containerapp env create \
  --name $CONTAINERAPPS_ENVIRONMENT \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION
```

## 6. Container App

A senha vai como **secret** do Container App, nunca como texto nas variáveis.

```bash
az containerapp create \
  --name $APP_CONTAINER \
  --resource-group $RESOURCE_GROUP \
  --environment $CONTAINERAPPS_ENVIRONMENT \
  --image $DOCKER_IMAGE \
  --ingress external \
  --target-port 8080 \
  --min-replicas 1 \
  --max-replicas 1 \
  --secrets "db-pwd=$DB_PWD" \
  --env-vars \
    SPRING_PROFILES_ACTIVE=prd \
    DB_SERVER_URL=$DB_SERVER_URL \
    DB_SERVER_PORT=$DB_SERVER_PORT \
    DB_SCHEMA=$DB_SCHEMA \
    DB_USER=$DB_USER \
    DB_PWD=secretref:db-pwd
```

`--min-replicas 1` mantém a API acordada durante a avaliação, que exige a API acessível.

## 7. URL e status

```bash
az containerapp show --name $APP_CONTAINER --resource-group $RESOURCE_GROUP \
  --query properties.configuration.ingress.fqdn --output tsv

az containerapp show --name $APP_CONTAINER --resource-group $RESOURCE_GROUP \
  --query properties.runningStatus --output tsv
```

Abra `https://<fqdn>/` para ver o Swagger e testar os endpoints.

## 8. Atualizar para a imagem mais recente

Depois de um novo push na `main`, quando o workflow publicar a nova imagem:

```bash
az containerapp update \
  --name $APP_CONTAINER \
  --resource-group $RESOURCE_GROUP \
  --image $DOCKER_IMAGE \
  --revision-suffix r$(date +%Y%m%d%H%M%S)
```

## 9. Logs

```bash
az containerapp logs show --name $APP_CONTAINER --resource-group $RESOURCE_GROUP --type console --tail 100
```

Problemas comuns:

- **`Cannot open server` ou `Login failed`:** confira a regra de firewall do item 4 e as variáveis `DB_*`.
- **`Cannot open database`:** confira se o banco do item 3 tem o mesmo nome de `DB_SCHEMA`.
- **Aplicação reiniciando:** veja os logs e confira as variáveis `DB_*`.

## 10. Evidências para a entrega

- **URL do Swagger no Azure:** print do navegador.
- **Persistência:** cadastro feito pelo Swagger no Azure e consulta `SELECT * FROM materiais` no banco.
- **Portal:** print do Container App em execução e do Azure SQL Database.

## Estrutura

```text
Azure
└── Resource Group: rg-554789-conectadoa
    ├── SQL Server: sql-554789-conectadoa
    │   └── Database: conectadoa  (usuarios, ongs, materiais, reservas)
    └── Container Apps Environment: env-554789-conectadoa
        └── Container App: conectadoa-api-554789
            └── Imagem: docker.io/lucascsalles/conectadoa-api:latest

Internet → Container App (HTTPS) → sql-554789-conectadoa.database.windows.net:1433 → banco conectadoa
```
