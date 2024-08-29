const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();


exports.checkAndSendReminder = functions.firestore
    .document('notes/{noteId}')
    .onWrite(async (change, context) => {
        const note = change.after.exists ? change.after.data() : null;
        
        if (note) {
            const userId = note.userId;
            const reminderDate = note.reminderDate; 
            const currentTime = new Date();
            const oneDayInMillis = 24 * 60 * 60 * 1000;
            currentTime.setHours(0, 0, 0, 0);

            const tomorrowStart = currentTime.getTime() + oneDayInMillis;
            const dayAfterTomorrowStart = tomorrowStart + oneDayInMillis;

            if (reminderDate >= tomorrowStart && reminderDate <= dayAfterTomorrowStart) {
                const message = {
                    token: note.token,
                    notification: {
                        title: "Lembrete!",
                        body: `Sua nota:"${note.title}" vence amanhã!`,
                    },
                };

                try {
                    await admin.messaging().send(message);
                    console.log("Notification sent successfully");
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

exports.sendNewNoteIfDueDateTomorrow = functions.firestore
    .document("notes/{noteId}")
    .onCreate(async (snap, context) => {
        const note = snap.data();

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