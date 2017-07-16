# PARANDO CONTAINERS
docker-compose down

# REMOVENDO IMAGENS
docker rmi -f natarajan/db-message
docker rmi -f natarajan/message-server


# REMOVENDO VOLUMES
docker volume remove questao05server_postgres-volume-message
