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

NotesScreen: Esta é a tela principal do aplicativo onde todas as anotações são exibidas. Cada nota pode ser clicada para navegar para a AddEditNoteScreen. A cor de fundo desta tela muda dinamicamente com um efeito de animação. Quando uma nota é excluída, uma snackbar é mostrada com a opção de desfazer a exclusão. A exclusão é tratada pelo NotesViewModel, que atualiza o banco de dados local e o Firebase.  

AddEditNoteScreen: Esta tela é usada para adicionar uma nova nota ou editar uma existente. Ela tem campos para o título e conteúdo da nota. Também tem um recurso de lembrete que pode ser ativado ou desativado. Quando uma nota é adicionada ou editada, o AddEditNoteViewModel atualiza o banco de dados local e o Firebase com a nova ou atualizada nota.  

BoxBackgroundImageComponent: Este componente é usado para exibir uma caixa com uma imagem de fundo. A cor de fundo desta caixa muda dinamicamente com um efeito de animação.  

OrderSection: Este componente é usado para ordenar as notas com base em diferentes critérios. A ordem é aplicada pelo NotesViewModel, que atualiza a ordem no banco de dados local e no Firebase.  

O aplicativo usa um ViewModel (NotesViewModel e AddEditNoteViewModel) para gerenciar o estado e lidar com eventos. O estado inclui a lista de notas, a ordem atual das notas e se a seção de ordem está visível. Os eventos incluem ações como alternar a seção de ordem, mudar a ordem das notas, excluir uma nota e restaurar uma nota excluída.  O aplicativo também usa navegação para alternar entre a NotesScreen e a AddEditNoteScreen. A rota AddEditNoteScreen inclui parâmetros para o ID da nota e a cor.  O aplicativo usa um Scaffold para fornecer uma estrutura de layout consistente, incluindo um botão de ação flutuante para adicionar novas notas. A cor de fundo do botão de ação flutuante é selecionada aleatoriamente de uma lista de cores de notas.  O banco de dados local é implementado usando Room, que fornece uma camada de abstração sobre o SQLite. A integração com o Firebase é tratada usando o SDK do Firebase, que fornece APIs para interagir com os serviços do Firebase como Firestore e Firebase Authentication. A sincronização entre o banco de dados local e o Firebase é tratada pelos ViewModels, que atualizam tanto o banco de dados local quanto o Firebase sempre que uma nota é adicionada, editada ou excluída.*



