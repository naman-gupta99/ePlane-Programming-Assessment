from math import perm
from lxml import etree
from signxml import XMLVerifier
import xmltodict
import time
import datetime as dt

def check_time(data_time, duration):
    data_time = list(map(int, data_time.split(':')))
    data_time_struct = dt.datetime(data_time[2], data_time[1], data_time[0], 
                    data_time[3], data_time[4]).timetuple()
    data_timestamp = time.mktime(data_time_struct)
    end_time = data_timestamp + float(duration)

    curr_time = time.time()

    if curr_time < data_timestamp:
        print('Be patient. There is still time left before activation of Mjölnir.')
        return False
    elif  curr_time > end_time:
        print('The permission has expired. Request Thor for a new permission.')
        return False
    
    return True
    

print('To use Mjölnir present the permission given by Thor.')
print('\n Copy your permission file to the current directory and enter the name of the file in the next line')

permission_filename = input("Enter name of the permission file (eg. permission-USuFixqrcd.xml): ")
permission_file = open(permission_filename, 'r')

permission_data = etree.fromstring(permission_file.read())

device_id = input('Enter the device id: ')
location = input('Enter the location of use: ')

cert = open("ds.pem").read()

try: 
    verified_data = XMLVerifier().verify(permission_data, x509_cert=cert).signed_xml
    signed_data = etree.tostring(verified_data).decode("utf-8")
    data = xmltodict.parse(signed_data)['root']
    data = {
        'device_id': data['device_id']['#text'],
        'location': data['location']['#text'],
        'time': data['time']['#text'],
        'duration': data['duration']['#text'],
    }
    
    if data['device_id'] != device_id:
        print('\nWrong Device-Id')
    elif data['location'] != location:
        print('\nWrong Location')
    elif check_time(data['time'], data['duration']):
        print('\nMjölnir is Activated. Thunder Strikes!!!')
    
except Exception as e:
    print('The permission has been tampered with. Your request has been denied.')

