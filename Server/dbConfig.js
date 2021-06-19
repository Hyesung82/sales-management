/* DB Info */
module.exports = { 
    user : process.env.NODE_ORACLEDB_USER || "root", 
    password : process.env.NODE_ORACLEDB_PASSWOR || "0000", 
    connectString : process.env.NODE_ORACLEDB_CONNECTIONSTRING || "localhost/orcl"
}
