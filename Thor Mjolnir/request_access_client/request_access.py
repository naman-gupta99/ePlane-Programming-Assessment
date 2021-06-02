import requests
import string
import random

print('Welcome to Mjölnir Request Portal\n')

print('TO REQUEST THE MIGHTY THOR FOR HIS MJOLNIR SUBMIT THE FOLLOWING DATA\n')

device_id = input('Enter the device id: ')
location = input('Enter the location of use: ')
time = input('Enter the time of use (format: DD:MM:YYYY:HH:MiMi): ')
duration = input('Enter the duration of use in seconds: ')

x = requests.get('http://127.0.0.1:8000/?device-id=' + device_id + '&time=' + time + '&location=' + location + '&duration=' + duration)

permission_filename = 'permission-' + ''.join(random.choice(string.ascii_letters) for i in range(10))  + '.xml'
signature = open(permission_filename, 'w')
signature.write(x.content.decode("utf-8")[1:-1].replace('\\"', '"').replace('\\n', '\n'))
signature.close()

print("\nAccess has been granted. Find the artifact (permission-<id>.xml) and use it to authorize your access.")
print("Copy permission-<id>.xml to the use_mjolnir directory. Run use_mjolnir/use_mjolnir.py")
print("Note: Do not change the name or content of permission.py to be able to use Mjölnir")