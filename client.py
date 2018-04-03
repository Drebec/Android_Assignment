#!/usr/bin/python3           # This is client.py file

import socket

# create a socket object
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# get local machine name
#host = socket.gethostname()
host = "192.168.0.108"

port = 9999

# connection to hostname on the port.
s.connect((host, port))

# Receive no more than 1024 bytes
#msg = s.recv(1024)

#print ("Received message: %s" % msg.decode('ascii'))

msg = """Fuck the police! Comin' straight from the underground       A young nigga got it bad 'cause I'm brown       And not the other color, so police think        They have the authority to kill a minority        Fuck that shit, 'cause I ain't the one        For a punk motherfucker with a badge and a gun        To be beating on, and thrown in jail        We can go toe-to-toe in the middle of a cell        Fuckin' with me 'cause I'm a teenager        With a little bit of gold and a pager        Searchin' my car, lookin' for the product        Thinkin' every nigga is sellin' narcotics        You'd rather see me in the pen        Than me and Lorenzo rollin' in a Benz-o        Beat a police out of shape        And when I'm finished, bring the yellow tape        To tape off the scene of the slaughter        Still getting swoll off bread and water        I don't know if they fags or what        Search a nigga down, and grabbing his nuts        And on the other hand, without a gun, they can't get none        But don't let it be a black and a white one        'cause they'll slam ya down to the street top        Black police showing out for the white cop        Ice Cube will swarm        On any motherfucker in a blue uniform        Just 'cause I'm from the CPT        Punk police are afraid of me        Huh, a young nigga on the warpath        And when I'm finished, it's gonna be a bloodbath        Of cops, dying in L.A        Yo, Dre, I got something to sayFuck the police! Comin' straight from the underground        A young nigga got it bad 'cause I'm brown        And not the other color, so police think        They have the authority to kill a minority        Fuck that shit, 'cause I ain't the one        For a punk motherfucker with a badge and a gun        To be beating on, and thrown in jail        We can go toe-to-toe in the middle of a cell        Fuckin' with me 'cause I'm a teenager        With a little bit of gold and a pager        Searchin' my car, lookin' for the product        Thinkin' every nigga is sellin' narcotics        You'd rather see me in the pen        Than me and Lorenzo rollin' in a Benz-o        Beat a police out of shape        And when I'm finished, bring the yellow tape        To tape off the scene of the slaughter        Still getting swoll off bread and water        I don't know if they fags or what        Search a nigga down, and grabbing his nuts        And on the other hand, without a gun, they can't get none        But don't let it be a black and a white one        Cause they'll slam ya down to the street top        Black police showing out for the white cop        Ice Cube will swarm        On any motherfucker in a blue uniform        Just cause I'm from the CPT        Punk police are afraid of me        Huh, a young nigga on the warpath        And when I'm finished, it's gonna be a bloodbath        Of cops, dying in L.A        Yo, Dre, I got something to say""" + "\r\n"
s.send(msg.encode('ascii'))
print("Sent msg: %s" % msg)

rec = s.recv(1024)
print(rec.decode('ascii'))

s.close()
