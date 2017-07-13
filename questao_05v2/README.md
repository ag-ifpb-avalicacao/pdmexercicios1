**Instituto Federal de Educação, Ciência e Tecnologia da Paraíba**

**Campus Cajazeiras**

**Curso Superior de Tecnologia em Análise e Desenvolvimento de Sistemas**


<hr/>


#### ROTEIRO PARA IMPLANTAR O SERVIDOR DAS MENSAGENS


1. Certifique-se que o seu serviço **Docker** esteja iniciado.

2. No terminal de linha de comando, execute `docker network create cliente_default_ntw`. Este comando é necessário para criarmos a rede docker que será usada para comunicação entre os containers da aplicação e do banco de dados.

3. No seu terminal, navegue até a pasta raiz do projeto do servidor, `questao_05_webserver`.

4. Para iniciar os containers do projeto, execute `sh ./run.sh` (\*\*). A partir deste passo, a aplicação já deve estar disponível para uso, logo após os containers terem inicializado é claro :)

5. Se desejar parar todos os containers e remover os volumes de persistência de dados, pode ser executado `sh ./stop.sh`(\*\*).

  \*\* Os scripts `run.sh` e `stop.sh` são válidos para sistemas unix-like.

  Os passos acima devem ser suficientes para iniciar os containers com serviço restful, bem como aqueles responsáveis pela persistência.



**Observação**: o aplicativo Android foi gerado para rodar em Genymotion - cuja máquina virtual considera o ip do localhost como sendo 10.0.3.2.

Por isso as chamadas do IntentService no aplicativo são direcionadas para esse endereço.
