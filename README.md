# [Projeto de PO em Java](https://web.tecnico.ulisboa.pt/~david.matos/w/pt/index.php/Programa%C3%A7%C3%A3o_com_Objectos/Projecto_de_Programa%C3%A7%C3%A3o_com_Objectos/Enunciado_do_Projecto_de_2022-2023)

## Como compilar:
  - Define o CLASSPATH estando na pasta [projeto](project)
    ```sh
    export CLASSPATH=$(pwd)/../po-uilib/po-uilib.jar:$(pwd)/prr-core/prr-core.jar:$(pwd)/prr-app/prr-app.jar 
    ``` 
  - Fazer `make` do [po_uilib](po-uilib) e do [projeto](project)
  
  ## Existem agora duas hipóteses:
  - Correr manualmente: 
  ```sh
  cd po_project/project 
  java prr.app.App
  ```
   e ir inserindo os comandos desejados
  - Correr automáticamente: 
  ```sh
  cd po_project/project/prr-tests-ef-eval-202211052226/
  sh runtests.sh
  ```
