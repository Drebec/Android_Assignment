#!/usr/bin/python3           # This is server.py file
import socket

# create a socket object
serversocket = socket.socket(
	        socket.AF_INET, socket.SOCK_STREAM)

# get local machine name
host = socket.gethostname()

print("%s" % host)

port = 9999

# bind to the port
serversocket.bind((host, port))

# queue up to 5 requests
serversocket.listen(5)

while True:
   # establish a connection
   clientsocket,addr = serversocket.accept()

   print("Got a connection from %s" % str(addr))
   while True:
      msg = clientsocket.recv(1024)
      print("Received message: %s" % msg.decode('ascii'))

      msg = 'Message Received'+ "\r\n"
      clientsocket.send(msg.encode('ascii'))

   clientsocket.close()
