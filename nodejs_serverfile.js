var app = require('express')();
var http = require('http').createServer(app);
var io = require('socket.io')(http)
io.on('connection', function (socket) {
  console.log('User has connected');
  socket.on('message', function (value) {
      socket.broadcast.emit('new message', value);
  });
  socket.on('disconnect', function () {
      console.log('User has disconnected');
  });
});
http.listen(3000,function()
{
  console.log('server is listening');
});
