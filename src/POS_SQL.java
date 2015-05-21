import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class POS_SQL {
	private void showTable() throws SQLException {
		String specification = "";
		
		String sqlStr = "select count(column_name) num from cols where table_name = '"
				+ ((String) check_box.getSelectedItem()).toUpperCase() + "'";
		PreparedStatement stmt = dbTest.prepareStatement(sqlStr);
		ResultSet rs = stmt.executeQuery();
		
		rs.next();
		int number = rs.getInt("num");
		String[] tables = new String[number];
		
		sqlStr = "select column_name from cols where table_name = '"
				+ ((String) check_box.getSelectedItem()).toUpperCase() + "'";
		stmt = dbTest.prepareStatement(sqlStr);
		rs = stmt.executeQuery();
		
		for (number = 0; rs.next(); number ++) {
			tables[number] = rs.getString("column_name");
			specification += tables[number] + '\t';
		}
		
		for (specification += "\n"; number > 0; number--) {
			specification += "----------------------";
		}
		specification += "\n";
		
		sqlStr = "select * from " + (String) check_box.getSelectedItem();
		stmt = dbTest.prepareStatement(sqlStr);
		rs = stmt.executeQuery();
		
		for (number = 0; rs.next(); number++) {
			for (int i = 0; i < tables.length; i++) {
				specification += rs.getString(tables[i]) + '\t';
			}
			specification += "\n";
		}
		
		check_area.setText(specification);
		
		rs.close();
		stmt.close();
	}

}
