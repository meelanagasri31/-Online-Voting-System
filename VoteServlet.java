package com.voting;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class VoteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String candidateId = req.getParameter("candidate");
        String username = (String) req.getSession().getAttribute("username");

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement checkVote = conn.prepareStatement("SELECT has_voted FROM users WHERE username=?");
            checkVote.setString(1, username);
            ResultSet rs = checkVote.executeQuery();
            if (rs.next() && rs.getBoolean("has_voted")) {
                res.getWriter().println("You have already voted.");
                return;
            }

            PreparedStatement ps1 = conn.prepareStatement("UPDATE candidates SET votes = votes + 1 WHERE id=?");
            ps1.setInt(1, Integer.parseInt(candidateId));
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement("UPDATE users SET has_voted = TRUE WHERE username=?");
            ps2.setString(1, username);
            ps2.executeUpdate();

            req.getSession().setAttribute("hasVoted", true);
            res.sendRedirect("result.jsp");
        } catch (Exception e) {
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
