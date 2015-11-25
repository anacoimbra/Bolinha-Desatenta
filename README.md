# Bolinha-Desatenta
#####Bolinha Desatenta é um aplicativo para [Android](http://www.android.com/) conectado a uma [Sphero Robot](http://www.sphero.com/sphero) que tem como objetivo ser um jogo divertido e, ao mesmo tempo, ajude pessoas com [TDAH](http://www.tdah.org.br/).

##Configurações Iniciais
```java
public class MainActivity extends Activity implements DiscoveryAgentEventListener, RobotChangedStateListener
```
###Implementação dos métodos
Como a classe Activity implementa os Listeners da Sphero, é preciso implementar seus métodos abstratos:
####Buscar Robôs disponíveis
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
####Após a busca
Se for encontrado pelo menos um robô, ele estará alocado na variável **list**. Observe que é possível conectar a mais de uma Sphero mas o nosso sistema trabalha apenas com uma, portanto utilizamos apenas a primeira ocorrência encontrada do robô.

*_currentDiscoveryAgent instanceof DiscoveryAgentClassic* verifica se o agente de descoberta encontrou um robô do tipo Sphero (Existem tipo Sphero e tipo Ollie)

*connect()* faz a conexão da primeira instância de Sphero conectada.
```java
@Override
    public void handleRobotsAvailable(List<Robot> list) {
        if (_currentDiscoveryAgent instanceof DiscoveryAgentClassic) {
            _currentDiscoveryAgent.connect(list.get(0));
        }
    }
```
####Tratamento de mudança de estado do Robô
Sempre que o estado do robô for alterado, este método é executado. Por exemplo, assim que uma Sphero for conectada, seu estado muda para **Online**.
Como já foi encontrado pelo menos um robô, a descoberta é desligada, pois não tem mais necessidade. 

*_connectedRobot.setZeroHeading();* faz uma calibragem no robô para que ele tenha sua orientação voltada ao usuário.

Após conectado com a Sphero, podemos iniciar o jogo. Chamamos então *startNewGame();*

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
###Metodos Sphero
Implementações dos métodos do SDK Sphero para a interação.
####Mudança de Cor
*x,y,z* devem ser substituidos por valores em pontos flutuantes de 0 a 1 para *vermelho, verde e azul* respectivamente
```java
_connectedRobot.setLed(x,y,y);
```
####Movimentação
*a,b* correspondem, respectivamente, a rotação que a Sphero deve ter em relação a sua calibração feita anteriormente e sua velocidade.
```java
_connectedRobot.drive(a, b);
```
Se não for feito um tratamento, o robô deslocará por uma distância muito longa. Para resolver este problema, utilizamos um método do java *wait(s);* que faz uma espera de s milisegundos e depois paramos a Sphero com o seguinte comando:
```java
_connectedRobot.stop();
```

![ScreenShot](/device-2015-10-05-151431.png)
