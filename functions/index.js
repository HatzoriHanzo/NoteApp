const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.sendReminderNotification = functions.firestore
    .document("notes/{noteId}")
    .onWrite(async (change, context) => {
      const note = change.after.data();

      const reminderDate = new Date(note.reminderDate);

      if (reminderDate && reminderDate > 0) {
        const oneDay = 24 * 60 * 60 * 1000;
        const now = Date.now();

        if ((reminderDate.getTime() - now) <= oneDay) {
          const token = note.token;

          const payload = {
            notification: {
              title: "Lembrete!",
              body: `Sua nota "${note.title}" vence amanhã!`,
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
      }
    });

exports.sendNewNoteNotification = functions.firestore
    .document("notes/{noteId}")
    .onCreate(async (snap, context) => {
      const note = snap.data();

      const token = note.token;

      const payload = {
        notification: {
          title: "Nova Nota Adicionada no Firebase :D",
          body: `A nota :"${note.title}" foi adicionada.`,
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
    });
