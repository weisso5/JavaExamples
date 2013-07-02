/*
 Created by Michael Weissman on 07/01/13.
 Copyright (c) 2013 Michael Weissman. All rights reserved.

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package com.weisso.examples;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.log.MLevel;
import com.mysql.jdbc.Driver;

/**
 * @author mweissman

 	Super simple c3p0 implementation for MySQL or any JDBC driver

 	@see
 	{@link http://sourceforge.net/projects/c3p0/}

 	@version
 	1.0

 	<dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.20</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>com.mchange</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.2.1</version>
      <scope>compile</scope>
    </dependency>
 *
 */
public final class C3p0ConnectionManager {

	public static final String JDBC_URL = "jdbc:mysql://localhost:3306/testdb?zeroDateTimeBehavior=convertToNull";

	private static C3p0ConnectionManager _instance = null;
	private static ComboPooledDataSource _dsPool = null;

	private static final String JDBC_USER = "testuser";
	private static final String JDBC_PWD = "Welcome09";
	private static final String JDBC_TESTTABLE = "tst_c3p0";

	protected C3p0ConnectionManager() {

		com.mchange.v2.log.MLog.getLogger().setLevel(MLevel.INFO);
		_dsPool = new ComboPooledDataSource();

		try {
			_dsPool.setDriverClass(Driver.class.getCanonicalName());
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		_dsPool.setJdbcUrl(JDBC_URL);
		_dsPool.setUser(JDBC_USER);
		_dsPool.setPassword(JDBC_PWD);
		_dsPool.setMinPoolSize(5);
		_dsPool.setAcquireIncrement(5);
		_dsPool.setMaxPoolSize(20);
		_dsPool.setNumHelperThreads(5);
		_dsPool.setAutomaticTestTable(JDBC_TESTTABLE);
	}

	public static C3p0ConnectionManager getInstance()
	{
		if (_instance == null) {
			synchronized (C3p0ConnectionManager.class) {
				_instance = new C3p0ConnectionManager();
			}
		}
		return _instance;
	}

	public Connection getPooledConnection() throws SQLException
	{
		return _dsPool.getConnection();
	}

	public void finalize()
	{
		if (_dsPool != null) {
			_dsPool.close();
			_dsPool = null;
		}
	}
}
