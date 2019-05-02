# Security Project

To run the application:

1.	Open eclipse.
2.	Import the zipped file project (SecurityProj.zip).
3.	Run "Server.java" then run "ClientMain.java".
4.	The GUI window appears.

![Start View](../master/resources/images/1.png)

5.	Register by entering your name then your password.
6.	Login with the name and password you registered with.
7.	The chatting interface appears.

![Registration View](../master/resources/images/2.png)

8.	To send a private message for certain user type '@' followed by the username then the message, ex: "@Amr , Hiii" 

![Private Message](../master/resources/images/3.png)

9.	To send a broadcast message, write the message then send.

•	To encrypt a message:
  1.	Upload a file (ex: test.png).

![Uploading View](../master/resources/images/4.png)

  2.	Click encrypt button after uploading the image file.
    	Write the desired name for the encrypted output file.
    	Write the message to be encrypted.
    	A success message appears and the message is encrypted and the output file saved to the current user's workspace.

•	To send the encrypted  message (image):
  1.	Type '@' followed by the username then the encrypted file name , ex: "@Amr , ency.png"
  2.	Click send
  3.	A message is sent to the receiver (and saved in his workspace) stating that he received an encrypted message in an image file.
  
• To decrypt a received message:
   1.	Click upload a file and select the needed one to be decrypted.
   2.	Click decrypt. A window is then opened with the decrypted message appearing.

•	To send a decrypted message as a broadcast message for all active users, type a message followed by the file's name. ex: "Hii , encry.png"

•	To exit the chat, click logout then the window is closed.

•	To delete your account, click deactivate.
