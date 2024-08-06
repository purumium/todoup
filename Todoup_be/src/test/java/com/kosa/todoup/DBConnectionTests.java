package com.kosa.todoup;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootTest
class DBConnectionTests {

	static {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void TestConnection() {
		try {
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xepdb1",
					"todoup",
					"todoup");
			System.out.println("todoup DB 연결 성공 : " + con);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
