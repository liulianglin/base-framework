package com.ldzhn.baseframework.utils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * 计算器工具类
 */
public class Counter {
  private static Counter counter = new Counter();
  private Counter() {
  }
  public static Counter getInstance(){
    return counter;
  }

  public synchronized long nextSerial(String strTable,Connection con){
    String strSQL = null;
    long serialno = 0;
    Statement stmt = null;
    try {
      strSQL = "select serialno from tblserial where tablename='" + strTable + "'";
      stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(strSQL);
      if (rs.next())
        serialno = rs.getLong(1);
      strSQL = "update tblserial set serialno = serialno + 1 where tablename='" + strTable + "'";
      stmt.execute(strSQL);
      rs.close();
      rs = null;
      serialno ++;
    }
    catch (Exception ex) {

    }finally{
      if (stmt != null)
        try {
          stmt.close();
          stmt = null;
        }
        catch (Exception ex) {}
    }
    return serialno;
  }
}
