package com.voting;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("username", username);
                session.setAttribute("hasVoted", rs.getBoolean("has_voted"));
                res.sendRedirect("vote.jsp");
            } else {
                res.getWriter().println("Login Failed");
            }
        } catch (Exception e) {
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
