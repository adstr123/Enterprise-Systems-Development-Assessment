package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CreationModel;

/**
 *
 * @author jacka
 */
public class Front extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CreationModel creationModel = new CreationModel();
        String id; // process Request URL
        String page = "/WEB-INF/docs/mainView.jsp"; // what page to forward to ...
        String include; // page to include into mainView.jsp

        // find last part of requested resource
        id = request.getRequestURI().substring(
                request.getContextPath().length());

        getServletContext().log("Front received a request for " + id);
        OUTER:
        switch (id) {
            case "/Front":
                include = "homePage.jsp";
                break;
            case "/docs/homePage":
                include = "homePage.jsp";
                break;
            case "/docs/loginPage":
                include = "loginPage.jsp";
                break;
            case "/docs/loginPage/registerNewUser":
                getServletContext().setAttribute("currentUser", creationModel.registerNewMember(request.getParameter("rName"), request.getParameter("rAddress"), request.getParameter("rDOB")));
                getServletContext().setAttribute("isNewUser", "true");
                include = "memberPage.jsp";
                break;
            case "/docs/memberPage":
                include = "memberPage.jsp";
                break;
            case "/docs/adminPage":
                include = "adminPage.jsp";
                break;
            case "/docs/tryLogin":
                String status = creationModel.verifyCredentials(request.getParameter("username"), request.getParameter("password"));
                switch (status) {
                    case "Invalid Credentials.":
                        include = "loginPage.jsp";
                        getServletContext().setAttribute("status", status);
                        break OUTER;
                    case "ADMIN":
                        include = "adminPage.jsp";
                        getServletContext().setAttribute("currentUser", request.getParameter("username").toLowerCase());
                        getServletContext().setAttribute("status", status);
                        getServletContext().setAttribute("isNewUser", "false");
                        break OUTER;
                    case "APPLIED":
                    case "APPROVED":
                    case "SUSPENDED":
                        include = "memberPage.jsp";
                        getServletContext().setAttribute("currentUser", request.getParameter("username").toLowerCase());
                        getServletContext().setAttribute("status", status);
                        getServletContext().setAttribute("isNewUser", "false");
                        break OUTER;
                    default:
                        include = "error.jsp";
                        break OUTER;
                }
            case "/docs/memberPage/makepayment":
                float paymentAmount = Float.parseFloat((String) request.getParameter("paymentAmount"));
                creationModel.createNewPayment((String) getServletContext().getAttribute("currentUser"), paymentAmount);
                include = "memberPage.jsp";
                break;
            case "/docs/memberPage/makeclaim":
                float claimAmount = Float.parseFloat((String) request.getParameter("claimAmount"));
                creationModel.createNewClaim((String) getServletContext().getAttribute("currentUser"), (String) request.getParameter("claimDescription"), claimAmount);
                include = "memberPage.jsp";
                break;
            default:
                include = "error.jsp";
        }

        request.setAttribute("includedView", include);
        request.getRequestDispatcher(page).forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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

}
