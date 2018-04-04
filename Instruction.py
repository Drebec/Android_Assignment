from maestro import Controller
import time
import threading, socket

c0 = Controller()

BODY            = 0
FORWARD_BACK    = 1
LEFT_RIGHT      = 2
HEAD_TURN       = 3
HEAD_TILT       = 4

# Enumerate positions
MIN    = 3000
MID    = 6000
MAX    = 9000

c0.setTarget(BODY, MID)

#Setting restrictions for all servos
c0.setRange(HEAD_TILT, MIN, MAX)
c0.setRange(HEAD_TURN, MIN, MAX)
c0.setRange(BODY, MIN, MAX)
c0.setRange(FORWARD_BACK, MIN, MAX)
c0.setRange(LEFT_RIGHT, MIN, MAX)

#Setting speeds for all servos
c0.setSpeed(HEAD_TILT, 60)
c0.setSpeed(HEAD_TURN, 60)
c0.setSpeed(LEFT_RIGHT, 60)
c0.setSpeed(FORWARD_BACK, 60)
c0.setSpeed(BODY, 60)

# Setting accelerations for all servos
c0.setAccel(HEAD_TILT, 30)
c0.setAccel(HEAD_TURN, 30)
c0.setAccel(BODY, 30)
c0.setAccel(FORWARD_BACK, 30)
c0.setAccel(LEFT_RIGHT, 30)

class Motor:
    def __init__(self, forward_back=0, left_right=0, delay=0, forward_back_target=0, left_right_target=0):
        self.delay = delay
        self.forward_back_target = forward_back_target
        self.left_right_target = left_right_target
        self.forward_back = forward_back
        self.left_right = left_right

    def execute(self):
        c0.setTarget(FORWARD_BACK, 6000 + -1*(self.forward_back * self.forward_back_target))
        c0.setTarget(LEFT_RIGHT, 6000 + -1*(self.left_right * self.left_right_target))
        # print("Executing Motor Movement")
        time.sleep(self.delay)

        c0.setTarget(FORWARD_BACK, 6000)
        c0.setTarget(LEFT_RIGHT, 6000)

class Body:
    def __init__(self, left_right=0, left_right_target=0):
        self.left_right = left_right
        self.left_right_target = left_right_target

    def execute(self):
        c0.setTarget(BODY, 6000 + -1*(self.left_right * self.left_right_target))
        # print("Executing Body Movement")

class Head: #lol
    def __init__(self, up_down=0, left_right=0, up_down_target=0, left_right_target=0):
        self.up_down_target = up_down_target
        self.left_right_target = left_right_target
        self.left_right = left_right
        self.up_down = up_down


    def execute(self):
        c0.setTarget(HEAD_TURN, 6000 + -1*(self.left_right * self.left_right_target))
        c0.setTarget(HEAD_TILT, 6000 + (self.up_down * self.up_down_target))
        # print("Executing Head Movement" + str(self.up_down_target))


class Wait:
    def __init__(self, delay=0):
        self.delay = delay

    def execute(self):
        print("Executing Wait" + str(self.delay))
        time.sleep(self.delay)

class TTS:
    def __init__(self, socket, phrase="Hello World"):
        self.phrase = phrase
        self.s = socket

    def execute(self):
        self.createSendThread(self.phrase)

    def send(self, message):
        message = message + "\r\n"
        self.s.send(message.encode('ascii'))
        print("Sent: %s" % message)

    def createSendThread(self, message):
        sendThread = threading.Thread(target=lambda msg=message: self.send(msg))
        sendThread.start()

    def receive(self):
        while(True):
            input = self.s.recv(1024)
            input = input.decode('ascii')
            print(input)
            decode(input)

    def decode(self, msg):
        pass
