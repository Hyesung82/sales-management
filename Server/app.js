var createError = require('http-errors');
var express = require('express');
var path = require('path');

var mysql = require('mysql2');
var db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '0000',
    database: 'ot_sales_db',
    dateStrings: 'date'
});
db.connect();

var app = express();
var socketio = require('socket.io');
const { stat } = require('fs');

var server = app.listen(3001, () => {
    console.log('Listening at port number 3001')
})

// return socket.io server.
var io = socketio.listen(server)

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('error');
});

module.exports = app;


app.post('/myInfo', (req, res) => {
    console.log('who get in here post /myInfo');
    var inputData;
    var outputData = "";
    var user_id;

    req.on('data', (data) => {
        inputData = JSON.parse(data);
    });
    req.on('end', () => {
        user_id = inputData.user_id;
        console.log("user_id: " + user_id);

        db.query(`SELECT first_name, last_name, email, phone, hire_date, job_title FROM employees WHERE userid=?`, [user_id], function(error, results) {
            console.log(results);

            outputData += results[0].first_name + " " + results[0].last_name + "^" +
                        results[0].email + "^" +
                        results[0].phone + "^" +
                        (results[0].hire_date).split(' ') + "^" +
                        results[0].job_title;

            res.write(String(outputData))
            res.end();
        });
    });
});

app.get('/update', (req, res) => {
    console.log('who get in here post /update');
    var inputData;
    var user_id, email, phone;

    req.on('data', (data) => {
        inputData = JSON.parse(data);
    });
    req.on('end', () => {
        user_id = inputData.user_id;
        email = inputData.email;
        phone = inputData.phone;
        console.log("user_id: " + user_id);

        db.query(`UPDATE employees SET email=?, phone=? WHERE userid=?`, [email, phone, user_id], function(error, results) {
            console.log(results);
                        
            res.write(String(1))
            res.end();
        });
    });
});

app.post('/login', (req, res) => {
    console.log('who get in here post /login');
    var inputData;
    var user_id, user_pw
    req.on('data', (data) => {
        inputData = JSON.parse(data);
    });
    req.on('end', () => {
        user_id = inputData.user_id
        user_pw = inputData.user_pw
        console.log("user_id: " + user_id + ", user_pw: " + user_pw);

        db.query(`SELECT * FROM employees WHERE userid=? AND passwd=?`, [user_id, user_pw], function(error, results) {
            console.log(results);

            if (results.length == 0) {
                res.write("-1");
                res.end();
            } else {
                res.write("1");
                res.end();
            }
        });
    });   
})

app.post('/searchByCategory', (req, res) => {
    console.log('who get in here post /searchByCategory');
    var inputData;
    var outputData = "";
    var name;
    req.on('data', (data) => {
        inputData = JSON.parse(data);
    });
    req.on('end', () => {
        name = inputData.category
        console.log("category name: " + name);

        db.query(`SELECT * FROM products WHERE category_id=(SELECT category_id FROM product_categories WHERE category_name=?)`, 
        [name], function(error, results) {
            console.log(results);

            for (var i = 0; i < results.length; i++) {
                outputData += results[i].product_name + "^" +
                            results[i].description + "^" +
                            results[i].standard_cost + "^" +
                            results[i].list_price + "~";
            }
            res.write(String(outputData))
            res.end();
        });
    });   
})

app.post('/searchByName', (req, res) => {
    console.log('who get in here post /searchByName');
    var inputData;
    var outputData = "";
    var name;
    req.on('data', (data) => {
        inputData = JSON.parse(data);
    });
    req.on('end', () => {
        name = inputData.name
        console.log("product name: " + name);

        db.query(`SELECT * FROM products WHERE product_name LIKE ?`, ['%'+name+'%'], function(error, results) {
            console.log(results);

            for (var i = 0; i < results.length; i++) {
                outputData += results[i].product_name + "^" +
                            results[i].description + "^" +
                            results[i].standard_cost + "^" +
                            results[i].list_price + "~";
            }
            res.write(String(outputData))
            res.end();
        });
    });   
})

app.post('/allCustomers', (req, res) => {
    console.log('who get in here post /allCustomers');
    var inputData;
    var outputData = "";
    var name;
    req.on('data', (data) => {
        inputData = JSON.parse(data);
    });
    req.on('end', () => {
        name = inputData.name
        console.log("customer name: " + name);

        db.query(`SELECT name FROM customers WHERE name LIKE ?`, ['%'+name+'%'], function(error, results) {
            console.log(results);

            for (var i = 0; i < results.length; i++) {
                outputData += results[i].name + "~";
            }
            res.write(String(outputData))
            res.end();
        });
    });   
})

app.post('/customer', (req, res) => {
    console.log('who get in here post /customer');
    var inputData;
    var outputData = "";
    var name;
    req.on('data', (data) => {
        inputData = JSON.parse(data);
    });
    req.on('end', () => {
        name = inputData.name
        console.log("customer name: " + name);

        db.query(`SELECT customer_id, address, credit_limit, website FROM customers WHERE name=?`, [name], function(error, results) {
            console.log(results);

            outputData += results[0].customer_id + "^" +
                        results[0].address + "^" +
                        results[0].credit_limit + "^" +
                        results[0].website + "~";

            db.query(`SELECT orders.order_id, orders.status, employees.last_name, orders.order_date FROM orders LEFT JOIN employees ON orders.salesman_id=employees.employee_id WHERE customer_id=?`, 
            [results[0].customer_id], function(error, results2) {
                console.log(results2)
                var outputData2="";

                for (var i = 0; i < results2.length; i++) {
                    outputData2 += String(results2[i].order_id) + "^" +
                                results2[i].status + "^" +
                                results2[i].last_name + "^" +
                                (results2[i].order_date).split(' ')[0] + "!";
                    console.log(outputData2)
                }

                res.write(String(outputData + outputData2))
                res.end();
            })
        });
    });   
})

app.post('/warehouse', (req, res) => {
    console.log('who get in here post /warehouse');
    var inputData;
    var outputData = "";
    var name;
    req.on('data', (data) => {
        inputData = JSON.parse(data);
    });
    req.on('end', () => {
        name = inputData.name
        console.log("warehouse name: " + name);

        db.query(`SELECT p.product_name, i.quantity FROM inventories i LEFT JOIN products p on i.product_id=p.product_id WHERE warehouse_id=(SELECT warehouse_id FROM warehouses WHERE warehouse_name=?)`, [name], function(error, results) {
            console.log(results);

            for (var i = 0; i < results.length; i++) {
                outputData += results[i].product_name + "^" +
                        results[i].quantity + "~";
            }
            
            res.write(String(outputData));
            res.end();
        });
    });   
})
