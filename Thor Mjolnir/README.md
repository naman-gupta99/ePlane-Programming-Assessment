# Full Stack Development Problem : Thor and the Mjölnir

## Problem

It’s not Christmas but Thor has decided to bestow us all with a gift, not any gift
but THE Mjölnir (Thor’s hammer). But there is a caveat - you have to personally
get approval from Thor himself before you can use it. But there’s something
even better for you guys. Marvel is looking for someone to develop the
authentication pipeline and the pay is significant too: A lifetime free pass to
access Mjölnir.
All you need to do is this:

-   Develop a dummy server (Thor) that gives permission to applicants after
    getting details like ID, Time, Place and duration of usage.
-   Develop a client that sends these information and gets the permission
    artifact (an xml file which is signed and authenticated using keys)
-   Develop a script that triggers the usage of The Mjolnir. Before usage, it
    has to check the ID of the device, and check if the permission is tampered
    with and if the details of time, place and duration are correct. The trigger
    has to work only if all the conditions are met.

Develop this based on Python/Django as a bonus. If not, use any framework of
your choice

# Instructions to run the program

1. Install dependencies
   `pip3 install -r requirements.txt`

2. Run thor_server

```
cd thor_server
python manage.py runserver
```

3. Request access from Thor

```
cd request_access_client
python request_access.py
```

4. Copy and paste the permission-\<id\>.xml file created into use_mjolnir directory

5. To use Mjölnir run

```
cd use_mjolnir
python use_mjolnir.py
```
