package com.voting;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class ResultServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM candidates ORDER BY votes DESC");

            out.println("<html><head><title>Election Results</title></head><body>");
            out.println("<h2>Election Results</h2>");
            out.println("<table border='1'><tr><th>Candidate</th><th>Votes</th></tr>");

            while (rs.next()) {
                String name = rs.getString("name");
                int votes = rs.getInt("votes");
                out.println("<tr><td>" + name + "</td><td>" + votes + "</td></tr>");
            }

            out.println("</table>");
            out.println("<br><a href='vote.jsp'>Back to Voting</a>");
            out.println("</body></html>");
        } catch (Exception e) {
            out.println("Error loading results: " + e.getMessage());
        }
    }
}
