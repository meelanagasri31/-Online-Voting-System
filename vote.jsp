<%@ page import="java.sql.*" %>
<%
    String username = (String) session.getAttribute("username");
    Boolean hasVoted = (Boolean) session.getAttribute("hasVoted");

    if (username == null) {
        response.sendRedirect("login.jsp");
    } else if (hasVoted != null && hasVoted) {
        out.println("You have already voted.");
    } else {
        Connection conn = com.voting.DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM candidates");
%>
    <form action="VoteServlet" method="post">
        <% while (rs.next()) { %>
            <input type="radio" name="candidate" value="<%= rs.getInt("id") %>">
            <%= rs.getString("name") %><br>
        <% } %>
        <input type="submit" value="Vote">
    </form>
<%
    }
%>
