/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import static chatserver.ChatClient.gui;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jannik
 */
public class ChatServlet extends HttpServlet implements ClientIF {

    private String messages = "";
    private String username;
    private ClientProxy handle;
    private ChatProxy session;
    private ChatServer server;
    private static String nickname;
    boolean init = false;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>\n"
                    + "    <head>\n"
                    + "        <title></title>\n"
                    + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "        <form name=\"form\" method=\"post\">\n"
                    + "            <p>Username eingeben: </p>"
                    + "            <input type=\"text\" name=\"name\"><br>\n"
                    + "            <input type=\"submit\" name=\"abschicken\" value=\"Abschicken\"><br>\n"
                    + "        </form>\n"
                    + "    </body>\n"
                    + "</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (init == false) {
            try {
                username = request.getParameter("name");
                server = (ChatServer) Naming.lookup("ChatServer");
                handle = new ClientProxyImpl(this);
                session = server.subscribeUser(username, handle);
            } catch (NotBoundException ex) {
                Logger.getLogger(ChatServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(ChatServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(ChatServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                init = true;
            }
        }

        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>\n"
                    + "    <head>\n"
                    + "        <title></title>\n"
                    + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                    + "    </head>\n"
                    + "    <body>\n"
                    + "     <p>Sie sind angemeldet als: " + username + "</p>\n"
                    + "        <form name=\"form2\" method=\"post\">\n"
                    + "            <input type=\"text\" name=\"eingabe\"><br>\n"
                    + "            <input type=\"submit\" name=\"go\" value=\"Abschicken\"><br>\n"
                    + "            <textarea name=\"ausgabe\" cols=\"50\" rows=\"10\" readonly>" + messages + "</textarea><br>\n"
                    + "            <input type=\"button\" name=\"beenden\" value=\"Beenden\" onclick=\"beenden();\"><br>\n"
                    + "        </form>\n"
                    + "    </body>\n"
                    + "</html>");
        } finally {
            out.close();
        }
        String eingabe = request.getParameter("eingabe");;
        if (eingabe != null && eingabe.trim().length() > 0) {
            sendMessage(eingabe);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    public void receiveMessage(String nickname, String message) throws RemoteException {
        messages += nickname + ": " + message + "\n";
    }

    @Override
    public void close() throws RemoteException {
        server.unsubscribeUser(session);
        System.exit(0);
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        session.sendMessage(message);
    }

    @Override
    public ChatProxy getSession() {
        return session;
    }
}
