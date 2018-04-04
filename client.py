#!/usr/bin/python3           # This is client.py file

import socket
import time

# create a socket object
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# get local machine name
#host = socket.gethostname()
host = "192.168.0.106"

port = 9999

# connection to hostname on the port.
s.connect((host, port))

# Receive no more than 1024 bytes
#msg = s.recv(1024)

#print ("Received message: %s" % msg.decode('ascii'))

msg = """Hello World""" + "\r\n"
s.send(msg.encode('ascii'))
print("Sent msg: %s" % msg)
time.sleep(1)
s.send(msg.encode('ascii'))
print("Sent msg: %s" % msg)
while(True):
    rec = s.recv(1024)
    print(rec.decode('ascii'))
s.close()
