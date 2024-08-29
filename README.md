# NoteApp

NoteApp é um aplicativo de anotações desenvolvido em Kotlin usando Jetpack Compose para a UI. O aplicativo permite aos usuários criar, editar e excluir anotações. As anotações são armazenadas em um banco de dados local e sincronizadas com o Firebase para armazenamento online e atualizações em tempo real.

## Tecnologias Usadas

- ![Kotlin](https://img.shields.io/badge/-Kotlin-0095D5?style=flat&logo=kotlin&logoColor=white) - Linguagem de programação principal usada para desenvolver o aplicativo.
- ![Java](https://img.shields.io/badge/-Java-007396?style=flat&logo=java&logoColor=white) - Usado em algumas partes do aplicativo.
- ![Gradle](https://img.shields.io/badge/-Gradle-02303A?style=flat&logo=gradle&logoColor=white) - Ferramenta de automação de construção usada para gerenciar dependências.
- ![JavaScript](https://img.shields.io/badge/-JavaScript-F7DF1E?style=flat&logo=javascript&logoColor=black) - Usado para escrever Firebase Cloud Functions.
- ![Node.js](https://img.shields.io/badge/-Node.js-339933?style=flat&logo=node.js&logoColor=white) - Ambiente de execução para executar código JavaScript no lado do servidor.
- ![Firebase](https://img.shields.io/badge/-Firebase-FFCA28?style=flat&logo=firebase&logoColor=black) - Usado para serviços de back-end como autenticação, banco de dados e notificações push.

## Notificações Push

As notificações push no aplicativo são realizadas através do Firebase Cloud Messaging (FCM). O arquivo `index.js` contém uma Firebase Cloud Function que dispara uma notificação push quando um determinado evento ocorre.

Aqui está uma versão simplificada de como funciona:

```javascript
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotification = functions.firestore
    .document('notes/{noteId}')
    .onCreate((snap, context) => {
        const data = snap.data();

        const payload = {
            notification: {
                title: 'Nova nota criada',
                body: `Uma nova nota intitulada ${data.title} foi criada.`,
            }
        };

        return admin.messaging().sendToTopic('notes', payload);
    });
```
*Neste código, uma Firebase Cloud Function é configurada para disparar quando um novo documento é criado na coleção 'notes' no Firestore. Quando isso acontece, uma notificação push é enviada para todos os dispositivos inscritos no tópico 'notes'.  
Descrição do Aplicativo
Este aplicativo é um aplicativo de anotações desenvolvido em Kotlin usando Jetpack Compose para a UI. Ele permite aos usuários criar, editar e excluir anotações. As anotações são armazenadas em um banco de dados local e sincronizadas com o Firebase para armazenamento online e atualizações em tempo real.  

## Notificações de Lembrete

O aplicativo possui um recurso de lembrete que pode ser ativado ou desativado ao adicionar ou editar uma nota. Quando um lembrete é definido para uma nota, uma notificação é enviada 24 horas antes do horário do lembrete.

Aqui está uma versão simplificada de como isso funciona no Firebase e Firestore:

```javascript
   const currentTime = new Date();
	currentTime.setHours(0, 0, 0, 0);
    const oneDayInMillis = 24 * 60 * 60 * 1000;

    const tomorrowStart = currentTime.getTime() + oneDayInMillis;
    const dayAfterTomorrowStart = tomorrowStart + oneDayInMillis;

    const reminderDateMillis = note.reminderDate;

    if (reminderDateMillis >= tomorrowStart && reminderDateMillis <= dayAfterTomorrowStart) {
        const token = note.token;

        const payload = {
            notification: {
                title: "Lembrete!",
                body: `Sua nota:"${note.title}" vence amanhã!`,
            },
        };

        const message = {
            token: token,
            notification: payload.notification,
        };

        try {
            const response = await admin.messaging().send(message);
            console.log("Notification sent successfully:", response);
        } catch (error) {
            console.error("Error sending notification:", error);
        }
    }
});
```

Essa função, chamada sendNewNoteIfDueDateTomorrow, é acionada sempre que um novo documento é criado na coleção 'notes' do Firestore. Ela verifica se a data de lembrete da nota está dentro do próximo dia.  Primeiro, a função obtém a data e hora atuais e define as horas, minutos, segundos e milissegundos para 0, o que dá o início do dia atual. Em seguida, adiciona 24 horas (em milissegundos) ao início do dia atual para obter o início do próximo dia e adiciona mais 24 horas ao início do próximo dia para obter o início do dia seguinte.  A função então verifica se a data de lembrete da nota (em milissegundos) é maior ou igual ao início do próximo dia e menor ou igual ao início do dia seguinte. Se for, a função prepara uma mensagem de notificação com o título "Lembrete!" e o corpo informando que a nota vence no dia seguinte.  Finalmente, a função tenta enviar a mensagem de notificação usando o método send do serviço de mensagens do Firebase Admin. Se a mensagem for enviada com sucesso, a função registra uma mensagem de sucesso no console. Se ocorrer um erro ao enviar a mensagem, a função registra o erro no console.


## Funcionalidades do app
NotesScreen: Esta é a tela principal do aplicativo onde todas as anotações são exibidas. Cada nota pode ser clicada para navegar para a AddEditNoteScreen. A cor de fundo desta tela muda dinamicamente com um efeito de animação. Quando uma nota é excluída, uma snackbar é mostrada com a opção de desfazer a exclusão. A exclusão é tratada pelo NotesViewModel, que atualiza o banco de dados local e o Firebase.  

- AddEditNoteScreen: Esta tela é usada para adicionar uma nova nota ou editar uma existente. Ela tem campos para o título e conteúdo da nota. Também tem um recurso de lembrete que pode ser ativado ou desativado. Quando uma nota é adicionada ou editada, o AddEditNoteViewModel atualiza o banco de dados local e o Firebase com a nova ou atualizada nota.  

- BoxBackgroundImageComponent: Este componente é usado para exibir uma caixa com uma imagem de fundo. A cor de fundo desta caixa muda dinamicamente com um efeito de animação.  

- OrderSection: Este componente é usado para ordenar as notas com base em diferentes critérios. A ordem é aplicada pelo NotesViewModel, que atualiza a ordem no banco de dados local e no Firebase.  

- O aplicativo usa um ViewModel (NotesViewModel e AddEditNoteViewModel) para gerenciar o estado e lidar com eventos. O estado inclui a lista de notas, a ordem atual das notas e se a seção de ordem está visível. Os eventos incluem ações como alternar a seção de ordem, mudar a ordem das notas, excluir uma nota e restaurar uma nota excluída.
- O aplicativo também usa navegação para alternar entre a NotesScreen e a AddEditNoteScreen.
- A rota AddEditNoteScreen inclui parâmetros para o ID da nota e a cor.
- O aplicativo usa um Scaffold para fornecer uma estrutura de layout consistente, incluindo um botão de ação flutuante para adicionar novas notas. A cor de fundo do botão de ação flutuante é selecionada aleatoriamente de uma lista de cores de notas.
- O banco de dados local é implementado usando Room, que fornece uma camada de abstração sobre o SQLite. 
- A integração com o Firebase é tratada usando o SDK do Firebase, que fornece APIs para interagir com os serviços do Firebase como Firestore e Firebase Authentication. A sincronização entre o banco de dados local e o Firebase é tratada pelos ViewModels, que atualizam tanto o banco de dados local quanto o Firebase sempre que uma nota é adicionada, editada ou excluída.*


## Requisitos 

- Adicionar Notas 

O usuário pode adicionar uma nova nota com título, descrição e data de vencimento. Isso é feito através da classe AddEditNoteViewModel com o evento AddEditNoteEvent.SaveNote, que adiciona ou edita notas no banco de dados ROOM e no Firestore.  

- Listar Notas
  
O aplicativo exibe uma lista de notas adicionadas. As notas são obtidas através da função getNotes() presente no NotesViewModel, que é responsável por listar todas as notas do banco de dados.  

- Editar Notas
  
O usuário pode editar o título, descrição e data de vencimento de uma nota. Isso é feito através da classe AddEditNoteViewModel com o evento AddEditNoteEvent.SaveNote, que adiciona ou edita notas no banco de dados ROOM e no Firestore.  

- Excluir Notas
  
O usuário pode excluir uma nota. Isso é feito através do NotesViewModel com o evento NotesEvent.DeleteNote, que deleta uma nota do banco de dados e na FireStore.  

- Persistência Local
  
As notas são armazenadas localmente usando Room. Isso é feito através da classe NoteDatabase.  

- Notificações Push Locais
  
O aplicativo faz uso de AlarmManager e Broadcast Receiver para enviar notificações locais e tocar alarme com alarmManager.setExactAndAllowWhileIdle().  

- Notificações Push Firebase
  
O aplicativo envia notificações push usando o Firebase Cloud Messaging (FCM). Isso é feito através das funções checkAndSendReminder, sendNewNoteNotification e sendNewNoteIfDueDateTomorrow. 

## Desafios Técnicos

- Arquitetura
  
O projeto utiliza a arquitetura MVVM (Model-View-ViewModel) e Clean Architecture. Isso é evidente pelo uso de ViewModel e LiveData na função AddEditNoteScreen, além do uso de MutableState e gerenciamento de estado.  

- Persistência de Dados
  
O projeto utiliza Room para persistência de dados. Isso é feito através da classe NoteDatabase.  

- Notificações Push
  
O projeto utiliza Firebase Cloud Messaging (FCM) para notificações push. Isso é feito através das funções checkAndSendReminder, sendNewNoteNotification e sendNewNoteIfDueDateTomorrow.  

- UI/UX
  
O projeto utiliza Material Design para a interface do usuário. Isso é evidente nos componentes AddEditNoteScreen e AddEditNoteScreen.kt, que são responsáveis pela interface do usuário para adicionar/editar notas. 
 
- Testes
  
O projeto inclui testes unitários e de UI. Isso é evidente nos arquivos NotesScreenTest e AddEditNoteViewModelTest, que contêm testes de UI e unitários, respectivamente.


