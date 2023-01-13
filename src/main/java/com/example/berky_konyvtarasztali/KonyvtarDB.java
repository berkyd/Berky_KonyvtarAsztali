package com.example.berky_konyvtarasztali;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KonyvtarDB {
    Connection conn;
    String DB_DRIVER = "mysql";
    String DB_HOST = "localhost";
    String DB_PORT = "3306";
    String DB_DATABASE = "books";
    String DB_USER = "root";
    String DB_PASS = "";

    public KonyvtarDB() throws SQLException {
        String url = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_HOST, DB_PORT, DB_DATABASE);
        conn = DriverManager.getConnection(url,DB_USER,DB_PASS);
    }

    public List<Konyv> getKonyvek() throws SQLException {
        List<Konyv> konyvek = new ArrayList<>();
        String sql = "SELECT * FROM books";
        Statement st = conn.createStatement();
        ResultSet resultSet = st.executeQuery(sql);
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            int publish_year = resultSet.getInt("publish_year");
            int page_count = resultSet.getInt("page_count");
            Konyv konyv = new Konyv(id, title, author, publish_year, page_count);
            konyvek.add(konyv);
        }
        return konyvek;
    }

    public boolean deleteKonyv(Konyv delete) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, delete.getId());
        return st.executeUpdate() > 0;
    }
}
