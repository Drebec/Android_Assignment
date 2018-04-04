import threading, socket, time

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# get local machine name
#host = socket.gethostname()
host = "192.168.0.106"

port = 9999

# connection to hostname on the port.
s.connect((host, port))

def decode(message):
    createSendThread("Message Received")

def send(message):
    message = message + "\r\n"
    s.send(message.encode('ascii'))
    print("Sent: %s" % message)

def createSendThread(message):
    sendThread = threading.Thread(target=lambda msg=message: send(msg))
    sendThread.start()

def receive():
    while(True):
        input = s.recv(1024)
        input = input.decode('ascii')
        print(input)
        decode(input)

receiveThread = threading.Thread(target=receive)

receiveThread.start()
createSendThread("Hello World")

while(True):
    pass
