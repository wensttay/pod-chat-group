package br.edu.ifpb.ads.pod.chat.project.client;

import br.edu.ifpb.ads.pod.chat.project.shared.dialog.DialogServer;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Message;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.Notification;
import br.edu.ifpb.ads.pod.chat.project.shared.entity.User;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 18/05/2017, 00:51:17
 */
public class Main {

    public static ChatAdministrator chatAdministrator;
    public static DialogServer dialogServer;
    public static User loggedUser;
    public static boolean isRunning = false;
    public static boolean isLogged = false;
    public static Timer tryUpdateChats = new Timer();
    public static Timer trySendOfflineMessages = new Timer();

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry(10999);
            dialogServer = (DialogServer) registry.lookup("DIALOG_SERVER");
            startApp();

        } catch (RemoteException | NotBoundException ex) {
            ex.printStackTrace();
            System.out.println("ERRO MEU: erro ao se connectar ao servidor.");
        }

    }

    public static void startApp() throws RemoteException {
        while (isRunning) {

            chatAdministrator = new ChatAdministrator(isLogged);
            chatAdministrator.setDialogServer(dialogServer);

            trySendOfflineMessages.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isRunning && isLogged) {
                        List<Message> offlineMessages = new ArrayList<>();
                        Collections.copy(offlineMessages, chatAdministrator.getOfflineMessages());
                        
                        if (!offlineMessages.isEmpty()) {
                            try {
                                for (Message m : offlineMessages) {
                                    dialogServer.publish(m);
                                    chatAdministrator.removeOfflineMessage(m);
                                }
                            } catch (RemoteException ex) {
                                System.out.println("Fail to try send a offline message");
                            }
                        }
                    }
                }
            }, 1000, 1000);

            tryUpdateChats.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isRunning && isLogged) {
                        chatAdministrator.reloadChatsFull();
                    }
                }
            }, 1000, 1000);

            int mainViewAnswer = startMainVeiw();
            switch (mainViewAnswer) {
                case 1:
                    startloginView();
                    break;
                case 2:
                    startCadastroView();
                    break;
                case 3:
                    System.out.println("Tchau, volte sempre!");
                    isLogged = false;
                    isRunning = false;
            }
        }
    }

    private static int startMainVeiw() {
        int answer = 0;
        Scanner s = new Scanner(System.in);

        while (true) {
            System.out.println("Selecione uma das opções: ");
            System.out.println("1 - Efetuar Login");
            System.out.println("2 - Se cadastrar");
            System.out.println("3 - Exit");
            System.out.print("> ");
            answer = s.nextInt();

            if (answer < 1 || answer > 3) {
                System.out.println("Opção Invalida, escolha uma opção ente 1 e 3;");
            } else {
                break;
            }
        }

        return answer;
    }

    private static void startloginView() {
        Scanner s = new Scanner(System.in);
        String login = "";
        String senha = "";

        while (true) {
            System.out.println("Login Screen:");
            System.out.print("Login: ");
            login = s.nextLine();
            System.out.print("Senha: ");
            senha = s.nextLine();

            try {
                loggedUser = dialogServer.login(login, senha, chatAdministrator);
                System.out.println("Logged With success!");

                chatAdministrator.setLoggedUser(loggedUser);
                isLogged = true;
                startLoggedView();
                break;
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("ERRO MEU: Erro ao efetuar login");
            }

        }
    }

    private static void startCadastroView() {
        Scanner s = new Scanner(System.in);
        String login = "";
        String senha = "";

        while (true) {
            System.out.println("Cadastro Screen:");
            System.out.print("Login: ");
            login = s.nextLine();
            System.out.print("Senha: ");
            senha = s.nextLine();

            try {
                dialogServer.register(new User(login, senha));
                System.out.println("Registred With success!");
                break;
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("ERRO MEU: Erro ao efetuar cadastro");
            }

        }
    }

    private static void startLoggedView() {

        Scanner s = new Scanner(System.in);

        while (isLogged) {
            try {
                System.out.println("Selecione uma das opções: ");
                System.out.println("1 - Entrar em um Chat");
                System.out.println("2 - Deletar conta");
                System.out.println("3 - Deslogar");
                System.out.print("> ");
                int answer = s.nextInt();
                switch (answer) {
                    case 1:
                        startChatListView();
                        break;
                    case 2:
                        startDeletarAccountView();
                        break;
                    case 3:
                        isLogged = false;
                        tryUpdateChats.cancel();
                        break;
                    default:
                        System.out.println("Opção inexistente porfavor escolha outra");
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("ERRO MEU: Erro na tela Logada de opções");
            }
        }
    }

    private static void startChatListView() {
        Scanner s = new Scanner(System.in);

        while (isLogged) {
            System.out.println("Lista de Chats:");

            for (ChatFull chatFull : chatAdministrator.getChatsFull()) {
                System.out.println(chatFull.toString());

            }
            System.out.println("Selecione um chat pelo seu nome para entrar no mesmo: ('/breck' para voltar ao menu anterior)");
            System.out.print("> ");
            String answer = s.nextLine();

            if (answer.equals("/breck")) {
                break;
            }

            ChatFull chat = chatAdministrator.findById(answer);

            if (chat != null) {
                startSelectedChat(chat);
            } else {
                System.out.println("Opção inexistente porfavor escolha outra");
            }
        }
    }

    private static void startSelectedChat(ChatFull chat) {

        System.out.println("Mensagens antigas: ");
        List<Message> messages = chat.getMessages();
        for (Message message : messages) {
            System.out.println(message.toString());
        }

        System.out.println("\nNovas Mensagens:");
        List<Notification> notifications = chat.getNotifications();
        for (Notification notification : notifications) {
            System.out.println(notification.getMessage().toString());
        }

        chatAdministrator.visualizeNotifications(chat.getName());

        System.out.println("Digite algo e precione enter para enviar"
                + " ou '/breck' para voltar ao menu anterior:");
        Scanner s = new Scanner(System.in);

        while (true) {
            String nextLine = s.nextLine();
            if (nextLine.equals("/break")) {
                break;
            }
            chatAdministrator.sendMessage(nextLine, chat.getName());
        }

    }

    private static void startDeletarAccountView(){
        Scanner s = new Scanner(System.in);

        while (isLogged) {
            System.out.println("Tela de confirmação de exclusão de conta: ");
            System.out.print("Senha: ");
            String senha = s.nextLine();

            if (loggedUser.getPassword().equals(senha)) {
                try {
                    dialogServer.unRegister(loggedUser.getLogin());
                    isLogged = false;
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                    System.out.println("ERRO MEU: ao tentar deletar uma conta");
                    break;
                }
            } else {
                System.out.println("Senha incorreta para o usuario: " + loggedUser.getLogin());
                break;
            }
        }
    }

}
