const app = require('./config/express');
const config = require('./config/config');

// initialize mongo 
require('./config/mongoose');

// listen to the port
app.listen(config.port, () => 
{
    console.log('server started on ' + config.port + ' ' + config.env);
});














/* const express = require('express');
const path = require('path');
const port = process.env.PORT || 4260;
const app = express();
const destinationDir = path.join(__dirname, '../dist');

//hosting from dist folder
app.use(express.static(destinationDir));
console.log('express hosting from ' + destinationDir);

//serving index.html
app.get('*', (req,res) => 
{
    res.sendFile(path.join(destinationDir, 'index.html'));
});
console.log('serving index.html');

//running/listening to port
app.listen(port, () => console.log('server is running from port ' + port)); */