package com.systex.sop.cvs.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.connection.ConnectionProviderFactory;

/**
 * 這是一個提供DBCP連接池的CONNECTION PROVIDER
 * <p>
 * hibernate.connection.provider_class org.hibernate.connection.DBCPConnectionProvider
 * <p>
 * 支持以下屬性： <br>
 *   hibernate.connection.driver_class <br>
 *   hibernate.connection.url <br>
 *   hibernate.connection.username <br>
 *   hibernate.connection.password <br>
 *   hibernate.connection.isolation <br>
 *   hibernate.connection.autocommit <br>
 *   hibernate.connection.pool_size <br>
 *   hibernate.connection (JDBC driver properties) <br>
 * <p>
 * 配置方式可能如下： <br>
 *   hibernate.connection.provider_class org.hibernate.connection.DBCPConnectionProvider <br>
 *   hibernate.connection.driver_class org.hsqldb.jdbcDriver <br>
 *   hibernate.connection.username sa <br>
 *   hibernate.connection.password <br>
 *   hibernate.connection.url jdbc:hsqldb:test <br>
 *   hibernate.connection.pool_size 20 <br>
 *   hibernate.dbcp.initialSize 10 <br>
 *   hibernate.dbcp.maxWait 3000 <br>
 *   hibernate.dbcp.validationQuery select 1 from dual <br>
 * <p>
 * @author 徐骏 (2009-06-06) [http://xujunprogrammer.blog.hexun.com/33571970_d.html]
 * 
 */
public class DBCPConnectionProvider implements ConnectionProvider {
	private static final Log log = LogFactory.getLog(DBCPConnectionProvider.class);
	private static final String PREFIX = "hibernate.dbcp.";
	private BasicDataSource ds;

	// Old Environment property for backward-compatibility (property removed in Hibernate3)
	private static final String DBCP_PS_MAXACTIVE = "hibernate.dbcp.ps.maxActive";

	// Property doesn't exists in Hibernate2
	private static final String AUTOCOMMIT = "hibernate.connection.autocommit";

	@Override
	public void close() throws HibernateException {
		log.debug("Close DBCPConnectionProvider");
		logStatistics();
		try {
			if (ds != null) {
				ds.close();
				ds = null;
			} else {
				log.warn("Cannot close DBCP pool (not initialized)");
			}
		} catch (Exception e) {
			throw new HibernateException("Could not close DBCP pool", e);
		}
		log.debug("Close DBCPConnectionProvider complete");
	}
	
	@Override
	public void closeConnection(Connection conn) throws SQLException {
		try {
			conn.close();
		} finally {
			logStatistics();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void configure(Properties props) throws HibernateException {
		try {
			log.debug("Configure DBCPConnectionProvider");

			// DBCP properties used to create the BasicDataSource
			Properties dbcpProperties = new Properties();

			// DriverClass & url
			String jdbcDriverClass = props.getProperty(Environment.DRIVER);
			String jdbcUrl = props.getProperty(Environment.URL);
			dbcpProperties.put("driverClassName", jdbcDriverClass);
			dbcpProperties.put("url", jdbcUrl);

			// Username / password
			String username = props.getProperty(Environment.USER);
			String password = props.getProperty(Environment.PASS);
			dbcpProperties.put("username", username);
			dbcpProperties.put("password", password);

			// Isolation level
			String isolationLevel = props.getProperty(Environment.ISOLATION);
			if ((isolationLevel != null) && (isolationLevel.trim().length() > 0)) {
				dbcpProperties.put("defaultTransactionIsolation", isolationLevel);
			}

			// Turn off autocommit (unless autocommit property is set)
			String autocommit = props.getProperty(AUTOCOMMIT);
			if ((autocommit != null) && (autocommit.trim().length() > 0)) {
				dbcpProperties.put("defaultAutoCommit", autocommit);
			} else {
				dbcpProperties.put("defaultAutoCommit", String.valueOf(Boolean.FALSE));
			}

			// Pool size
			String poolSize = props.getProperty(Environment.POOL_SIZE); // pool_size is same as maxActive
			if ((poolSize != null) && (poolSize.trim().length() > 0) && (Integer.parseInt(poolSize) > 0)) {
				dbcpProperties.put("maxActive", poolSize);
			}

			// Copy all "driver" properties into "connectionProperties"
			Properties driverProps = ConnectionProviderFactory.getConnectionProperties(props);
			if (driverProps.size() > 0) {
				StringBuffer connectionProperties = new StringBuffer();
				for (Iterator iter = driverProps.keySet().iterator(); iter.hasNext();) {
					String key = (String) iter.next();
					String value = driverProps.getProperty(key);
					connectionProperties.append(key).append('=').append(value);
					if (iter.hasNext()) {
						connectionProperties.append(';');
					}
				}
				dbcpProperties.put("connectionProperties", connectionProperties.toString());
			}

			// Copy all DBCP properties removing the prefix
			for (Iterator iter = props.keySet().iterator(); iter.hasNext();) {
				String key = String.valueOf(iter.next());
				if (key.startsWith(PREFIX)) {
					String property = key.substring(PREFIX.length());
					String value = props.getProperty(key);
					dbcpProperties.put(property, value);
				}
			}

			// Backward-compatibility
			if (props.getProperty(DBCP_PS_MAXACTIVE) != null) {
				dbcpProperties.put("poolPreparedStatements", String.valueOf(Boolean.TRUE));
				dbcpProperties.put("maxOpenPreparedStatements", props.getProperty(DBCP_PS_MAXACTIVE));
			}

			// Some debug info
//			if (log.isDebugEnabled()) {
//				log.debug("Creating a DBCP BasicDataSource with the following DBCP factory properties:");
//				StringWriter sw = new StringWriter();
//				dbcpProperties.list(new PrintWriter(sw, true));
//				log.debug(sw.toString());
//			}
			if (log.isInfoEnabled()) {
				StringBuffer propInfo = new StringBuffer("-- listing properties --");
				for (Object key : dbcpProperties.keySet()) {
					propInfo.append("\n").append(key).append(" = ").append(dbcpProperties.get(key));
				}
				log.info(propInfo);
			}

			// Let the factory create the pool
			ds = (BasicDataSource) BasicDataSourceFactory.createDataSource(dbcpProperties);

			// The BasicDataSource has lazy initialization
			// borrowing a connection will start the DataSource
			// and make sure it is configured correctly.
			Connection conn = ds.getConnection();
			conn.close();

			// Log pool statistics before continuing.
			logStatistics();
		} catch (Exception e) {
			String message = "Could not create a DBCP pool";
			log.fatal(message, e);
			if (ds != null) {
				try {
					ds.close();
				} catch (Exception e2) {
					// ignore
				}
				ds = null;
			}
			throw new HibernateException(message, e);
		}
		log.debug("Configure DBCPConnectionProvider complete");
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			conn = ds.getConnection();
//			System.out.println("这是一个DBCP连接池");
		} finally {
			logStatistics();
		}
		return conn;
	}

	protected void logStatistics() {
		if (log.isInfoEnabled()) {
//			log.info("active: " + ds.getNumActive() + " (max: " + ds.getMaxActive() + ")   " + "idle: "
//					+ ds.getNumIdle() + "(max: " + ds.getMaxIdle() + ")");
			log.info(StringUtil.concat("Active[", ds.getNumActive(), ", ", ds.getMaxActive(), "], Idle[", ds.getNumIdle(), ", ", ds.getMaxIdle(), "]") );
		}
	}
	
	@Override
	public boolean supportsAggressiveRelease() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
