# Bolinha-Desatenta
##### Bolinha Desatenta é um aplicativo para [Android](http://www.android.com/) conectado a uma [Sphero Robot](http://www.sphero.com/sphero) que tem como objetivo ser um jogo divertido e, ao mesmo tempo, ajude pessoas com [TDAH](http://www.tdah.org.br/).

## Configurações Iniciais
```java
public class MainActivity extends Activity implements DiscoveryAgentEventListener, RobotChangedStateListener
```
### Implementação dos métodos
Como a classe Activity implementa os Listeners da Sphero, é preciso implementar seus métodos abstratos:
#### Buscar Robôs disponíveis
```java
private void startDiscovery() {
    try {
        _currentDiscoveryAgent.addDiscoveryListener(this);
        _currentDiscoveryAgent.addRobotStateListener(this);
        _currentDiscoveryAgent.startDiscovery(this);

        Log.d("ACHOU", "Encontrado sphero");
    } catch (DiscoveryException e) {
        Log.e("ERRO", "Could not start discovery. Reason: " + e.getMessage());
        e.printStackTrace();
    }
}
```
#### Após a busca
Se for encontrado pelo menos um robô, ele estará alocado na variável `list`. Observe que é possível conectar a mais de uma Sphero mas o nosso sistema trabalha apenas com uma, portanto utilizamos apenas a primeira ocorrência encontrada do robô.

`_currentDiscoveryAgent instanceof DiscoveryAgentClassic` verifica se o agente de descoberta encontrou um robô do tipo Sphero (Existem tipo Sphero e tipo Ollie)

`connect()` faz a conexão da primeira instância de Sphero conectada.
```java
@Override
    public void handleRobotsAvailable(List<Robot> list) {
        if (_currentDiscoveryAgent instanceof DiscoveryAgentClassic) {
            _currentDiscoveryAgent.connect(list.get(0));
        }
    }
```
#### Tratamento de mudança de estado do Robô
Sempre que o estado do robô for alterado, este método é executado. Por exemplo, assim que uma Sphero for conectada, seu estado muda para **Online**.
Como já foi encontrado pelo menos um robô, a descoberta é desligada, pois não tem mais necessidade. 

`_connectedRobot.setZeroHeading()` faz uma calibragem no robô para que ele tenha sua orientação voltada ao usuário.

Após conectado com a Sphero, podemos iniciar o jogo. Chamamos então `startNewGame()`

Se acontecer algum problema de conexão, o estado da Sphero será **Desconectado**.
```java
@Override
public void handleRobotChangedState(Robot robot, RobotChangedStateNotificationType robotChangedStateNotificationType) {
    switch (robotChangedStateNotificationType) {
        case Online:
            _currentDiscoveryAgent.stopDiscovery();
            _currentDiscoveryAgent.removeDiscoveryListener(this);
            _connectedRobot = new Sphero(robot);
            Log.d("OK", "Sphero conectado");

            _connectedRobot.setZeroHeading();

            startNewGame();

            break;
        case Disconnected:
            Log.d("DESCONECTADO", "Sphero foi desconectado");
            break;
        default:
            Log.v("ERRO", "Not handling state change notification: " + robotChangedStateNotificationType);
            break;
    }
}
```
### Metodos Sphero
Implementações dos métodos do SDK Sphero para a interação.
#### Mudança de Cor
`x`,`y`,`z` devem ser substituidos por valores em pontos flutuantes de 0 a 1 para *vermelho, verde e azul* respectivamente
```java
_connectedRobot.setLed(x,y,y);
```
#### Movimentação
`a`,`b` correspondem, respectivamente, a rotação que a Sphero deve ter em relação a sua calibração feita anteriormente e sua velocidade.
```java
_connectedRobot.drive(a, b);
```
Se não for feito um tratamento, o robô deslocará por uma distância muito longa. Para resolver este problema, utilizamos um método do java `wait(s)` que faz uma espera de s milisegundos e depois paramos a Sphero com o seguinte comando:
```java
_connectedRobot.stop();
```

![ScreenShot](https://github.com/anacoimbra/Bolinha-Desatenta/blob/master/device-2015-10-05-151431.png?raw=true)

## O Jogo
Para o jogo, foi criado uma classe chamada "Game", que possui a responsabilidade de gerar os movimentos aleatórios e calcular a pontuação. Para isso, a classe possui uma API de simples entendimento:

#### API
Para criar um novo jogo, utiliza-se o construtor, que recebe a dificuldade como parâmetro e um observer, que, no caso, é a MainActivity:
```java
public Game(int difficulty, Observer observer)
```

Para criar um novo turno no jogo utiliza-se o método `newTurn`. Este método possui a responsabilidade de zerar os movimentos da jogada anterior e salvar os movimentos aleatórios da nova jogada.
```java
public void newTurn()
```

Para guardar o movimento do usuário, é utilizado o método `addUserCommand` e é chamado nos eventos dos botões da interface.
```java
public void addUserCommand(Integer command)
```

O método `getExecution` retorna os movimentos da jogada atual. Com isto, é possível que a interface saiba os movimentos para passar para a Sphero Robot.
```java
public ArrayList<Integer> getExecution()
```
##### Métodos privados

Internamente, a classe Game possui os seguintes métodos:

O método `calculateMovements` calcula o número de movimentos do turno, que é dado por uma fórmula de dificuldade. 
```java
    private int calculateMovements() {
        return turn / difficulty + difficulty;
    }
```

O método `checkScore` faz o cálculo da pontuação e é chamado no método `addUserCommand` sempre que o número de entradas do usuário for igual ao número de tentativas do turno atual. Além disso, este método chama aciona os observers
```java
private void checkScore()
```

##### Variáveis privadas

A classe Game possui as seguintes variáveis:

A variável `turn` que é do tipo inteiro e inidica o turno do jogo e é utilizada no cálculo do método `calculateMovements()`.
```java
private int turn;
```

A variável `executedCommands` que é do tipo ArrayList de inteiro e inidica quais foram os comandos executados pela Sphero.
```java
private ArrayList<Integer> executedCommands;
```

A variável `userCommands` que é do tipo ArrayList de inteiro e inidica quais foram os comandos inseridos pelo usuário.
```java
private ArrayList<Integer> userCommands;
```

A variável `difficulty` que é do tipo inteiro e inidica o nível de dificuldade do jogo e é utilizada no cálculo do método `calculateMovements()`.
```java
private int difficulty;
```

##### Constantes privadas

A classe Game também possui as seguintes constantes:

A constante `COMMAND_LEFT` que associa o valor 0 ao comando que faz a Sphero ir para a esquerda.
```java
public static int COMMAND_LEFT = 0;
```

A constante `COMMAND_RIGHT` que associa o valor 1 ao comando que faz a Sphero ir para a direita.
```java
public static int COMMAND_RIGHT= 1;
```

A constante `COMMAND_UP` que associa o valor 2 ao comando que faz a Sphero ir para a frente.
```java
public static int COMMAND_UP = 2;
```

A constante `COMMAND_DOWN` que associa o valor 3 ao comando que faz a Sphero ir para trás.
```java
public static int COMMAND_DOWN = 3;
```

A constante `COMMAND_PINK` que associa o valor 4 ao comando que faz a Sphero mudar sua cor para rosa.
```java
public static int COMMAND_PINK = 4;
```

A constante `COMMAND_YELLOW` que associa o valor 4 ao comando que faz a Sphero mudar sua cor para amarelo.
```java
public static int COMMAND_YELLOW = 5;
```

A constante `COMMAND_BLACK` que associa o valor 4 ao comando que faz a Sphero mudar sua cor para preto.
```java
public static int COMMAND_BLACK = 6;
```

A constante `COMMAND_GREEN` que associa o valor 4 ao comando que faz a Sphero mudar sua cor para verde.
```java
public static int COMMAND_GREEN = 7;
```

A constante `COMMAND_BLUE` que associa o valor 4 ao comando que faz a Sphero mudar sua cor para azul.
```java
public static int COMMAND_BLUE = 8;
```

A constante `COMMAND_RED` que associa o valor 4 ao comando que faz a Sphero mudar sua cor para vermelho.
```java
public static int COMMAND_RED = 9;
```

A constante `DIFFICULTY_NORMAL` que estabelece o valor 3 como sendo o nível de dificuldade normal do jogo.
```java
public static int DIFFICULTY_NORMAL = 3;
```

A constante `MAX_COMMANDS` que estabelece o valor 9 como sendo o valor máximo de comandos possíveis no jogo.
```java
private static int MAX_COMMANDS = 9;
```
